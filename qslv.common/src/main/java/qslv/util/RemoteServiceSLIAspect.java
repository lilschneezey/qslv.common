package qslv.util;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;

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
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import qslv.common.TimedResponse;

@Aspect
@Component
public class RemoteServiceSLIAspect implements BeanFactoryAware {
	Logger log = null;
	StandardEvaluationContext context;
	private ExpressionParser parser = new SpelExpressionParser();
	private TemplateParserContext tcontext = new TemplateParserContext();
	HashMap<String, String> resolvedLiterals = new HashMap<>();
	
	@Around("@annotation(remoteServiceSLI)")
	public Object remoteService(ProceedingJoinPoint joinPoint, RemoteServiceSLI remoteServiceSLI) throws Throwable {
		if ( null == log ) {
			if (remoteServiceSLI.logScope().equals(RemoteServiceSLI.class)) {
				log = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
			} else {
				log = LoggerFactory.getLogger(remoteServiceSLI.logScope());
			}
		}
		String remoteAit = resolvedLiterals.computeIfAbsent(remoteServiceSLI.remoteAit(), this::resolveLiteral);
		String ait = resolvedLiterals.computeIfAbsent(remoteServiceSLI.ait(), this::resolveLiteral);
		long start = System.nanoTime();
		Object proceed=null;
		try {
			proceed = joinPoint.proceed();
		} catch (Throwable thrown) {
			for( Class<? extends Throwable> element : remoteServiceSLI.remoteFailures() ) {
				if (thrown.getClass().isAssignableFrom(element) ) {
					ServiceLevelIndicator.logRemoteServiceFailureElapsedTime(log, remoteServiceSLI.value(), ait, 
							remoteAit, (System.nanoTime() - start) );
				}
			}
			throw thrown;
		}
		Duration serverElapsedTime = analyzeResponse(proceed);
		if (serverElapsedTime == null ) {
			ServiceLevelIndicator.logRemoteServiceElapsedTime(log, remoteServiceSLI.value(), ait, 
					remoteAit, (System.nanoTime() - start) );
		} else {
			ServiceLevelIndicator.logServiceVsServerElapsedTime(log, remoteServiceSLI.value(), ait, 
					remoteAit, Duration.ofNanos(System.nanoTime() - start), serverElapsedTime );
		}
		return proceed;
	}
	
	private Duration analyzeResponse(Object object) {
		try {
			if (TimedResponse.class.isAssignableFrom(object.getClass())) {
				TimedResponse<?> response = TimedResponse.class.cast(object);
				return Duration.ofNanos(response.getServiceTimeElapsed());
			} else if (ResponseEntity.class.isAssignableFrom(object.getClass())) {
				ResponseEntity<?> entity = ResponseEntity.class.cast(object);
				List<String> values = entity.getHeaders().get(TimedResponse.ELAPSED_TIME);
				if ( values != null && values.size() == 1 ) {
					return Duration.ofNanos(Long.parseLong(values.get(0)));
				}
				if ( entity.hasBody() && entity.getBody().getClass().isAssignableFrom(TimedResponse.class) ) {
					TimedResponse<?> response = TimedResponse.class.cast(entity.getBody());
					return Duration.ofNanos(response.getServiceTimeElapsed());
				}
			}
		} catch( Throwable thrown) {
			log.error(thrown.getLocalizedMessage());
		}
		return null;
	}
	
	private String resolveLiteral( String literal ) {
		String resolved;
		try {
			resolved = parser.parseExpression(literal,tcontext).getValue(context, String.class);
		} catch (Throwable thrown ) {
			resolved = literal;
			log.error("Parse error for \"{}\" {}", literal, thrown.getMessage());
		}
		return resolved;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		context = new StandardEvaluationContext(beanFactory);
		context.setBeanResolver(new BeanFactoryResolver(beanFactory));
	}
}
