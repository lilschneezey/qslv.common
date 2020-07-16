package qslv.util;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;

import qslv.common.TimedResponse;

/**
 * Standard logging for splunk timing instrumentation SLIs
 * Measures execution time in nanoseconds (10e-9), as defined by System.nanoTime(); 
 * @author SMS
 *
 */
public class RestClientElapsedTimeSLILogger {
	private Logger log;
	private String aitNumber;
	private String remoteAitNumber;
	private String name;
	private List<Class<? extends RuntimeException>> resourceFailures;
	
	public interface TimedOperation<T> {
		T execute();
	}
	public interface TimedVoidOperation {
		void execute();
	}
	public class ElapsedTimerException extends RuntimeException {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private long elapsedTime;
		public ElapsedTimerException(long elapsedTime, Throwable cause) {
			super(cause);
			this.elapsedTime = elapsedTime;
		}
		public long getElapsedTime() {
			return elapsedTime;
		}
	}
	
	public RestClientElapsedTimeSLILogger(Logger log, String aitNumber, String name, String remoteAitNumber) {
		this.log = log;
		this.aitNumber = aitNumber;
		this.name = name;
		this.remoteAitNumber = remoteAitNumber;
		this.resourceFailures = Collections.emptyList();
	}
	
	public RestClientElapsedTimeSLILogger(Logger log, String aitNumber, String name, String remoteAitNumber, List<Class<? extends RuntimeException>> logScope) {
		this.log = log;
		this.aitNumber = aitNumber;
		this.name = name;
		this.remoteAitNumber = remoteAitNumber;
		this.resourceFailures = logScope;
	}

	public <T> T logElapsedTime(TimedOperation<T> operation) {
		Duration serverTime = null;
		long start = System.nanoTime();
		T value;
		try {
			value = operation.execute();
			serverTime = analyzeOutput(value);
		} catch(RuntimeException thrown) {
			for (Class<?> element : resourceFailures) {
				if (thrown.getClass().isAssignableFrom(element) ) {
					ServiceLevelIndicator.logServiceFailureElapsedTime(log, name, aitNumber, Duration.ofNanos(System.nanoTime() - start));
					break;
				} // else ignore - internal error we shouldn't log
			}
			throw thrown;
		}
		if ( serverTime == null ) {
			ServiceLevelIndicator.logServiceElapsedTime(log, name, aitNumber, Duration.ofNanos(System.nanoTime() - start));
		} else {
			ServiceLevelIndicator.logServiceVsServerElapsedTime(log, name, aitNumber, remoteAitNumber, Duration.ofNanos(System.nanoTime() - start), serverTime);
		}
		return value;
	}

	private Duration analyzeOutput(Object object) {
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

}
