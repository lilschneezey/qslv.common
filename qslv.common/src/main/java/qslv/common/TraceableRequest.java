package qslv.common;

/**
 * There are two ways to enable transaction and business process tracing: headers or payload.
 * If the interface is not yet defined, payload is a good first choice, as it is clear and obvious.
 * If the interface is already defined and introduction of these additional items may cause problems
 * using headers to exchange the tracing and timing data is a good alternative.
 * 
 * use of the standard Accept-version is also required
 * @author SMS
 *
 * @param <T> payload
 */
public class TraceableRequest<T> {
	
	// Option 1: use custom Http Headers 
	public final static String AIT_ID = "ait-id";
	public final static String BUSINESS_TAXONOMY_ID = "business-taxonomy-id";
	public final static String CORRELATION_ID = "correlation-id";
	
	// Http Header to support versioning. Don't know where else this is defined
	public final static String ACCEPT_VERSION = "Accept-version";
	
	// Option 2: provide in payload
	private String aitId;
	private String businessTaxonomyId;
	private String correlationId;
	private T payload;
	
	public String getAitId() {
		return aitId;
	}
	public void setAitId(String aitId) {
		this.aitId = aitId;
	}
	public String getBusinessTaxonomyId() {
		return businessTaxonomyId;
	}
	public void setBusinessTaxonomyId(String businessTaxonomyId) {
		this.businessTaxonomyId = businessTaxonomyId;
	}
	public String getCorrelationId() {
		return correlationId;
	}
	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}
	public T getPayload() {
		return payload;
	}
	public void setPayload(T payload) {
		this.payload = payload;
	}
	
	public String toString() {
		return new StringBuffer()
		.append("TraceableRequest<").append(payload==null ? "?" : payload.getClass().getSimpleName()).append("> ")
		.append(" AIT: ").append(aitId==null ? "null" : correlationId)
		.append(" Business Taxonomy Id: ").append(businessTaxonomyId==null ? "null" : correlationId)
		.append(" Correlation Id:").append(correlationId==null ? "null" : correlationId)
		.append(" Payload: ").append(payload==null ? "null" : payload.toString())
		.toString();
	}
}
