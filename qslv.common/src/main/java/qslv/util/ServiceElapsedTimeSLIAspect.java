package qslv.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import qslv.common.TimedResponse;

@Aspect
@Component
public class ServiceElapsedTimeSLIAspect {
	private Logger log = null;
	
	@Around("@annotation(serviceElapsedTimeSLI)")
	public Object logServiceElapsedTimeSLI(ProceedingJoinPoint joinPoint, ServiceElapsedTimeSLI serviceElapsedTimeSLI) throws Throwable {
		if ( null == log ) {
			if (serviceElapsedTimeSLI.logScope().equals(RemoteServiceSLI.class)) {
				log = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
			} else {
				log = LoggerFactory.getLogger(serviceElapsedTimeSLI.logScope());
			}
		}
		long start = System.nanoTime();
		Object proceed = joinPoint.proceed();
		long elapsedTime = System.nanoTime() - start;
		
		ServiceLevelIndicator.logServiceElapsedTime(log, serviceElapsedTimeSLI.value(), serviceElapsedTimeSLI.ait(),  elapsedTime );
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

}
