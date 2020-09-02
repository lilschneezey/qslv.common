package qslv.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import qslv.common.TimedResponse;

@Aspect
@Component
public class ServiceElapsedTimeSLIAspect implements BeanFactoryAware {
	private Logger log = null;
	StandardEvaluationContext context;
	private ExpressionParser parser = new SpelExpressionParser();
	
	@Around("@annotation(serviceElapsedTimeSLI)")
	public Object logServiceElapsedTimeSLI(ProceedingJoinPoint joinPoint, ServiceElapsedTimeSLI serviceElapsedTimeSLI) throws Throwable {
		if ( null == log ) {
			if (serviceElapsedTimeSLI.logScope().equals(RemoteServiceSLI.class)) {
				log = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
			} else {
				log = LoggerFactory.getLogger(serviceElapsedTimeSLI.logScope());
			}
		}
		String ait;
		try { 
			ait = parser.parseExpression(serviceElapsedTimeSLI.ait()).getValue(context, String.class);
		} catch (Throwable thrown) {
			log.error("Parse error for \"{}\" {}", serviceElapsedTimeSLI.ait(), thrown.getMessage());
			ait = serviceElapsedTimeSLI.ait();
		}
		long start = System.nanoTime();
		Object proceed = joinPoint.proceed();
		long elapsedTime = System.nanoTime() - start;
		
		ServiceLevelIndicator.logServiceElapsedTime(log, serviceElapsedTimeSLI.value(), ait,  elapsedTime );
		if (serviceElapsedTimeSLI.injectResponse()) {
			proceed = injectElapsedTime (proceed, elapsedTime);
		}
		return proceed;
	}

	private Object injectElapsedTime(Object object, long elapsedTime) {
		if ( ResponseEntity.class.isAssignableFrom(object.getClass()) ) {
			ResponseEntity<?> entity = ResponseEntity.class.cast(object);
			
			if ( TimedResponse.class.isAssignableFrom( entity.getBody().getClass() ) ) {
				TimedResponse<?> response = TimedResponse.class.cast( entity.getBody() );
				response.setServiceTimeElapsed(elapsedTime);
			}
			return ResponseEntity
				.status(entity.getStatusCode())
				.header(TimedResponse.ELAPSED_TIME, Long.toString(elapsedTime))
				.body(entity.getBody());
		} else if ( TimedResponse.class.isAssignableFrom( object.getClass() )) {
			TimedResponse<?> response = TimedResponse.class.cast( object );
			response.setServiceTimeElapsed(elapsedTime);
		}
		return object;
	}
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		context = new StandardEvaluationContext(beanFactory);
		context.setBeanResolver(new BeanFactoryResolver(beanFactory));
	}

}
