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
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExternalResourceSLIAspect implements BeanFactoryAware {
	private Logger log = null;
	StandardEvaluationContext context;
	private ExpressionParser parser = new SpelExpressionParser();

	@Around("@annotation(externalResourceSLI)")
	public Object remoteService(ProceedingJoinPoint joinPoint, ExternalResourceSLI externalResourceSLI) throws Throwable {
		if ( null == log ) {
			if (externalResourceSLI.logScope().equals(ExternalResourceSLI.class)) {
				log = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
			} else {
				log = LoggerFactory.getLogger(externalResourceSLI.logScope());
			}
		}
		String ait;
		try { 
			ait = parser.parseExpression(externalResourceSLI.ait()).getValue(context, String.class);
		} catch (Throwable thrown) {
			log.error("Parse error for \"{}\" {}", externalResourceSLI.ait(), thrown.getMessage());
			ait = externalResourceSLI.ait();
		}
		long start = System.nanoTime();
		Object proceed=null;
		try {
			proceed = joinPoint.proceed();
		} catch (Throwable thrown) {
			for( Class<? extends Throwable> element : externalResourceSLI.remoteFailures() ) {
				if (thrown.getClass().isAssignableFrom(element) ) {
					ServiceLevelIndicator.logServiceFailureElapsedTime(log, externalResourceSLI.value(), ait, (System.nanoTime() - start) );
				}
			}
			throw thrown;
		}
		ServiceLevelIndicator.logServiceElapsedTime(log, externalResourceSLI.value(), ait, (System.nanoTime() - start) );
		return proceed;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		context = new StandardEvaluationContext(beanFactory);
		context.setBeanResolver(new BeanFactoryResolver(beanFactory));
	}

}
