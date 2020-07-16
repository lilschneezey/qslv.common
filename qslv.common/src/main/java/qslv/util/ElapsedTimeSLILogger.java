package qslv.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

/**
 * Standard logging for splunk timing instrumentation SLIs
 * Measures execution time in nanoseconds (10e-9), as defined by System.nanoTime(); 
 * @author SMS
 *
 */
public class ElapsedTimeSLILogger {
	private Logger log;
	private String aitNumber;
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
	
	public ElapsedTimeSLILogger(Logger log, String aitNumber, String name) {
		this.log = log;
		this.aitNumber = aitNumber;
		this.name = name;
		resourceFailures = new ArrayList<Class<? extends RuntimeException>>();
	}
	
	public ElapsedTimeSLILogger(Logger log, String aitNumber, String name, List<Class<? extends RuntimeException>> logScope) {
		this.log = log;
		this.aitNumber = aitNumber;
		this.name = name;
		this.resourceFailures = logScope;
	}

	public <T> T logElapsedTime(TimedOperation<T> operation) {
		long start = System.nanoTime();
		T value;
		try { 
			value = operation.execute();
		} catch(RuntimeException thrown) {
			for (Class<?> element : resourceFailures) {
				if (thrown.getClass().isAssignableFrom(element) ) {
					ServiceLevelIndicator.logServiceFailureElapsedTime(log, name, aitNumber, (System.nanoTime() - start));
					break;
				} // else ignore - internal error we shouldn't log
			}
			throw thrown;
		}
		ServiceLevelIndicator.logServiceElapsedTime(log, name, aitNumber, (System.nanoTime() - start));
		return value;
	}
	public void logElapsedTime(TimedVoidOperation operation) {
		long start = System.nanoTime();
		try { 
			operation.execute();
		} catch(RuntimeException thrown) {
			for (Class<?> element : resourceFailures) {
				if (thrown.getClass().isAssignableFrom(element) ) {
					ServiceLevelIndicator.logServiceFailureElapsedTime(log, name, aitNumber, (System.nanoTime() - start));
					break;
				} // else ignore - internal error we shouldn't log
			}
			throw thrown;
		}
		ServiceLevelIndicator.logServiceElapsedTime(log, name, aitNumber, (System.nanoTime() - start));
	}
}
