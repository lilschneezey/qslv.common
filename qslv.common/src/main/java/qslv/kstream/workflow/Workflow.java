package qslv.kstream.workflow;

import java.util.ArrayList;

import org.apache.avro.reflect.Union;

import qslv.kstream.LoggedTransaction;

@Union({
	CancelReservationWorkflow.class,
	CommitReservationWorkflow.class,
	ReservationWorkflow.class,
	TransactionWorkflow.class,
	TransferWorkflow.class
})
public abstract class Workflow {
	public static final int INITIAL = 0;
	public static final int COMPLETE = -1;
	public static final int FAILURE = -2;
	public static final int VALIDATION_FAILURE = -3;
	public static final int CONFLICT_FAILURE = -4;
	
	protected int state = INITIAL;

	private String processingAccountNumber = null;
	private String errorMessage = null;	
	private ArrayList<LoggedTransaction> accumulatedResults = null;
	private ArrayList<LoggedTransaction> results = null;
	
	public String getProcessingAccountNumber() {
		return processingAccountNumber;
	}
	public void setProcessingAccountNumber(String processingAccountNumber) {
		this.processingAccountNumber = processingAccountNumber;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public ArrayList<LoggedTransaction> getAccumulatedResults() {
		return accumulatedResults;
	}
	public void setAccumulatedResults(ArrayList<LoggedTransaction> accumulatedResults) {
		this.accumulatedResults = accumulatedResults;
	}
	public ArrayList<LoggedTransaction> getResults() {
		return results;
	}
	public void setResults(ArrayList<LoggedTransaction> results) {
		this.results = results;
	}
	
}
