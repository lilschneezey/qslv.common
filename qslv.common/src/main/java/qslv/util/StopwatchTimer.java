package qslv.util;

import java.time.Duration;

public class StopwatchTimer {
	private long elapsedTime = 0L;

	public interface TimedOperation<T> {
		T execute();
	}
	public interface TimedVoidOperation {
		void execute();
	}

	public static class StopwatchOperationException extends RuntimeException {
		private static final long serialVersionUID = 1L;
		private long elapsedTime;

		public StopwatchOperationException(long elapsedTime, RuntimeException cause) {
			super(cause);
			this.elapsedTime = elapsedTime;
		}
		public long getElapsedTime() {
			return elapsedTime;
		}
		public Duration getDuration() {
			return Duration.ofNanos(elapsedTime);
		}
	}

	public static long measureTime(TimedVoidOperation operation) {
		long start = System.nanoTime();
		try {
			operation.execute();
		} catch (RuntimeException ex) {
			throw new StopwatchOperationException((System.nanoTime() - start), ex);
		}
		return (System.nanoTime() - start);
	}

	public <T> T measureTime(TimedOperation<T> operation) {
		long start = System.nanoTime();
		T value;
		try {
			value = operation.execute();
		} catch (RuntimeException ex) {
			elapsedTime = (System.nanoTime() - start);
			throw ex;
		}
		elapsedTime = (System.nanoTime() - start);
		return value; 
	}
	public long getElapsedTime() {
		return elapsedTime;
	}
	public Duration getDuration() {
		return Duration.ofNanos(elapsedTime);
	}
}
