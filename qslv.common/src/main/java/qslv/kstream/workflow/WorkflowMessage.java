package qslv.kstream.workflow;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class WorkflowMessage {
	private Workflow workflow = null;
	private String responseKey = null;

	public WorkflowMessage() {}
	public WorkflowMessage(Workflow workflow) {
		this.workflow = workflow;
	}
	public WorkflowMessage(Workflow workflow, String responseKey) {
		this.workflow = workflow;
		this.responseKey = responseKey;
	}
	public Workflow getWorkflow() {
		return workflow;
	}
	public void setWorkflow(Workflow workflow) {
		this.workflow = workflow;
	}
	public String getResponseKey() {
		return responseKey;
	}
	public void setResponseKey(String responseKey) {
		this.responseKey = responseKey;
	}
	
	@JsonIgnore public boolean hasReservationWorkflow() {
		return workflow instanceof ReservationWorkflow;
	}
	@JsonIgnore public boolean hasCancelReservationWorkflow() {
		return workflow instanceof CancelReservationWorkflow;
	}
	@JsonIgnore public boolean hasCommitReservationWorkflow() {
		return workflow instanceof CommitReservationWorkflow;
	}
	@JsonIgnore public boolean hasTransactionWorkflow() {
		return workflow instanceof TransactionWorkflow;
	}
	@JsonIgnore public boolean hasTransferWorkflow() {
		return workflow instanceof TransferWorkflow;
	}
	
	@JsonIgnore public ReservationWorkflow getReservationWorkflow() {
		return workflow instanceof ReservationWorkflow ? ReservationWorkflow.class.cast(workflow) : null;
	}
	@JsonIgnore public CancelReservationWorkflow getCancelReservationWorkflow() {
		return workflow instanceof CancelReservationWorkflow ? CancelReservationWorkflow.class.cast(workflow) : null;
	}
	@JsonIgnore public CommitReservationWorkflow getCommitReservationWorkflow() {
		return workflow instanceof CommitReservationWorkflow ? CommitReservationWorkflow.class.cast(workflow) : null;
	}
	@JsonIgnore public TransactionWorkflow getTransactionWorkflow() {
		return workflow instanceof TransactionWorkflow ? TransactionWorkflow.class.cast(workflow) : null;
	}
	@JsonIgnore public TransferWorkflow getTransferWorkflow() {
		return workflow instanceof TransferWorkflow ? TransferWorkflow.class.cast(workflow) : null;
	}

}
