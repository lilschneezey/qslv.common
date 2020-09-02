package save;

import java.util.ArrayList;

import qslv.data.Account;
import qslv.data.OverdraftInstruction;
import qslv.kstream.LoggedTransaction;
import qslv.kstream.PostingRequest;

public class EnhancedPostingMessage {
	// --Input -----------------------------------------
	private PostingRequest postingRequest;  // should not be altered.

	// --Working Data-----------------------------------------
	private String processingAccountNumber; // for managing overdraft processing. 
	private int nextOverdraftIndex=0; // for managing overdraft processing. 
	private boolean complete = false; // for managing overdraft processing.

	// --Enriched Data -----------------------------------------
	private Account account = null;  // holds status and other account attributed
	private ArrayList<OverdraftInstruction> overdraftInstructions = null;  // the list of overdraft with associated Accounts

	// --Enriched Data for Commit and Cancel. The original reservation. -----------------------------------------
	private LoggedTransaction reservation = null;
	
	// --Reply -----------------------------------------
	private int status = 0;
	private String errorMessage = null;	
	private ArrayList<LoggedTransaction> results = null; // usually one, but for overdraft will be many
	
	public EnhancedPostingMessage() {}
	public EnhancedPostingMessage(String accountNumber, PostingRequest postingRequest) {
		this.processingAccountNumber = accountNumber;
		this.postingRequest = postingRequest;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public PostingRequest getPostingRequest() {
		return postingRequest;
	}
	public void setPostingRequest(PostingRequest postingRequest) {
		this.postingRequest = postingRequest;
	}
	public String getProcessingAccountNumber() {
		return processingAccountNumber;
	}
	public void setProcessingAccountNumber(String processingAccountNumber) {
		this.processingAccountNumber = processingAccountNumber;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public ArrayList<OverdraftInstruction> getOverdraftInstructions() {
		return overdraftInstructions;
	}
	public void setOverdraftInstructions(ArrayList<OverdraftInstruction> overdraftInstructions) {
		this.overdraftInstructions = overdraftInstructions;
	}
	public int getNextOverdraftIndex() {
		return nextOverdraftIndex;
	}
	public void setNextOverdraftIndex(int nextOverdraftIndex) {
		this.nextOverdraftIndex = nextOverdraftIndex;
	}
	public LoggedTransaction getReservation() {
		return reservation;
	}
	public void setReservation(LoggedTransaction reservation) {
		this.reservation = reservation;
	}
	public ArrayList<LoggedTransaction> getResults() {
		return results;
	}
	public void setResults(ArrayList<LoggedTransaction> results) {
		this.results = results;
	}
	public boolean isComplete() {
		return complete;
	}
	public void setComplete(boolean complete) {
		this.complete = complete;
	}
}
