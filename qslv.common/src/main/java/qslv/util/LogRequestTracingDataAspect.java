package qslv.util;

import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import qslv.common.TraceableRequest;

@Aspect
@Component
public class LogRequestTracingDataAspect {
	Logger log = null;
	
	@Before("@annotation(logRequestTracingData)")
	public void logTracingData(JoinPoint joinPoint, LogRequestTracingData logRequestTracingData) throws Throwable {
		if ( null == log ) {
			if (logRequestTracingData.logScope().equals(LogRequestTracingData.class)) {
				log = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
			} else {
				log = LoggerFactory.getLogger(logRequestTracingData.logScope());
			}
		}
		for (Object object : joinPoint.getArgs()) {
			if (TraceableRequest.class.isAssignableFrom(object.getClass()) ) {
				TraceableRequest<?> request = TraceableRequest.class.cast(object);
				ServiceLevelIndicator.logServiceTraceData(log, logRequestTracingData.value(), logRequestTracingData.ait(), 
					null == request.getAitId() ? "null" : request.getAitId(), 
					null == request.getBusinessTaxonomyId() ? "null" : request.getBusinessTaxonomyId(), 
					null == request.getCorrelationId() ? "null" : request.getCorrelationId() );
				break;
			} else if ( Map.class.isAssignableFrom(object.getClass()) ) {
				try {
					@SuppressWarnings("unchecked")
					Map<String,String> map = (Map<String,String>)(object);
					ServiceLevelIndicator.logServiceTraceData(log, logRequestTracingData.value(), logRequestTracingData.ait(), 
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

}
