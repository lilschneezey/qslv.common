package qslv.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExternalResourceSLIAspect {
	Logger log = null;
	
	@Around("@annotation(externalResourceSLI)")
	public Object remoteService(ProceedingJoinPoint joinPoint, ExternalResourceSLI externalResourceSLI) throws Throwable {
		if ( null == log ) {
			if (externalResourceSLI.logScope().equals(ExternalResourceSLI.class)) {
				log = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
			} else {
				log = LoggerFactory.getLogger(externalResourceSLI.logScope());
			}
		}
		long start = System.nanoTime();
		Object proceed=null;
		try {
			proceed = joinPoint.proceed();
		} catch (Throwable thrown) {
			for( Class<? extends Throwable> element : externalResourceSLI.remoteFailures() ) {
				if (thrown.getClass().isAssignableFrom(element) ) {
					ServiceLevelIndicator.logServiceFailureElapsedTime(log, externalResourceSLI.value(), externalResourceSLI.ait(), (System.nanoTime() - start) );
				}
			}
			throw thrown;
		}
		ServiceLevelIndicator.logServiceElapsedTime(log, externalResourceSLI.value(), externalResourceSLI.ait(), (System.nanoTime() - start) );
		return proceed;
	}

}
