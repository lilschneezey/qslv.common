package qslv.common;

//import org.springframework.http.HttpHeaders;

/**
 * There are two options for reporting Server's percieved elapsed time. HTTP Header or payload.
 * Both require coding changes on the client, but HTTP header can be introduced without affecting
 * all clients at the same time, and can be rolled out to existing service versions without requireing
 * client change.
 * 
 * @author SMS
 *
 * @param <R> payload
 */
public class TimedResponse <R> {
	// Option 1 HTTP Header
	public final static String ELAPSED_TIME = "elapsed-time";

	/*
	public static void addElapsedTimeHeader(HttpHeaders returnHeader, long elapsedTime) {
		returnHeader.add(TimedResponse.ELAPSED_TIME, Long.toString(elapsedTime));
	}
	*/
	
	private long serviceTimeElapsed = 0;
	private R payload = null;
	
	public TimedResponse() {}
	public TimedResponse(R resource) {
		this.payload = resource;
	}
	public TimedResponse(long serviceTimeElapsed, R resource) {
		this.serviceTimeElapsed = serviceTimeElapsed;
		this.payload = resource;
	}
	public long getServiceTimeElapsed() {
		return serviceTimeElapsed;
	}
	public void setServiceTimeElapsed(long serviceTimeElapsed) {
		this.serviceTimeElapsed = serviceTimeElapsed;
	}
	public R getPayload() {
		return payload;
	}
	public void setPayload(R payload) {
		this.payload = payload;
	}
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("TimedResponse:");
		buf.append(" serviceTimeElapsed: "); buf.append(serviceTimeElapsed);
		buf.append(" resource: "); buf.append(payload == null ? "null" : payload.toString());
		return buf.toString();
	}
}
