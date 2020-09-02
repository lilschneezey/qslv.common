package qslv.util;

import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import qslv.common.TraceableRequest;

@Aspect
@Component
public class LogRequestTracingDataAspect implements BeanFactoryAware {
	Logger log = null;
	StandardEvaluationContext context;
	private ExpressionParser parser = new SpelExpressionParser();
	
	@Before("@annotation(logRequestTracingData)")
	public void logTracingData(JoinPoint joinPoint, LogRequestTracingData logRequestTracingData) throws Throwable {
		if ( null == log ) {
			if (logRequestTracingData.logScope().equals(LogRequestTracingData.class)) {
				log = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
			} else {
				log = LoggerFactory.getLogger(logRequestTracingData.logScope());
			}
		}
		String ait;
		try { 
			ait = parser.parseExpression(logRequestTracingData.ait()).getValue(context, String.class);
		} catch (Throwable thrown) {
			log.error("Parse error for \"{}\" {}", logRequestTracingData.ait(), thrown.getMessage());
			ait = logRequestTracingData.ait();
		}
		for (Object object : joinPoint.getArgs()) {
			if (TraceableRequest.class.isAssignableFrom(object.getClass()) ) {
				TraceableRequest<?> request = TraceableRequest.class.cast(object);
				ServiceLevelIndicator.logServiceTraceData(log, logRequestTracingData.value(), ait, 
					null == request.getAitId() ? "null" : request.getAitId(), 
					null == request.getBusinessTaxonomyId() ? "null" : request.getBusinessTaxonomyId(), 
					null == request.getCorrelationId() ? "null" : request.getCorrelationId() );
				break;
			} else if ( Map.class.isAssignableFrom(object.getClass()) ) {
				try {
					@SuppressWarnings("unchecked")
					Map<String,String> map = (Map<String,String>)(object);
					ServiceLevelIndicator.logServiceTraceData(log, logRequestTracingData.value(), ait, 
							String.class.cast( map.getOrDefault(TraceableRequest.AIT_ID, "null") ),
							String.class.cast( map.getOrDefault(TraceableRequest.BUSINESS_TAXONOMY_ID, "null") ),
							String.class.cast( map.getOrDefault(TraceableRequest.CORRELATION_ID, "null") ) );
				} catch (Throwable ex) {
					// nope. didn't work. wrong type of map.
					continue;
				}
				break;
			}
 		}
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		context = new StandardEvaluationContext(beanFactory);
		context.setBeanResolver(new BeanFactoryResolver(beanFactory));
	}
}
