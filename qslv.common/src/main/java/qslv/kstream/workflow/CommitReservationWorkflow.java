package qslv.kstream.workflow;

import java.util.ArrayList;

import qslv.data.Account;
import qslv.data.OverdraftInstruction;
import qslv.kstream.CommitReservationRequest;
import qslv.kstream.LoggedTransaction;

public class CommitReservationWorkflow extends Workflow {
	//--------------------------------------------------
	public final static int MATCH_RESERVATION = 1;
	// [ reservation not found ] --> NO_MATCH
	// --> COMMIT_START
	public final static int COMMIT_START= 2;
		// [funds available] --> SUCCESS
		// [no funds available && OD allowed && OD accounts] use OD account --> TRANSFER_FROM_OVERDRAFT_PROTECTION
		// [OD not allowed || no OD accounts] --> SUCCESS
	public final static int TRANSFER_FROM_OVERDRAFT_PROTECTION = 3;
		// [no funds available && more OD accounts] use next OD account --> TRANSFER_FROM_OVERDRAFT_PROTECTION
		// [no more OD accounts] --> OVERDRAFT_ACCOUNT
		// [funds available] --> TRANSFER_AND_COMMIT
	public final static int TRANSFER_AND_COMMIT = 4;
		// --> SUCCESS
	public final static int OVERDRAFT_ACCOUNT = 5;
		// --> SUCCESS
	public final static int SUCCESS = 6;
		//  --> COMPLETE
	public final static int NO_MATCH = 7;
		// --> COMPLETE
	//--------------------------------------------------
	
	private CommitReservationRequest request = null;
	
	// --Enriched Data -----------------------------------------
	private Account account = null;  // holds status and other account attributed
	private ArrayList<OverdraftInstruction> processedInstructions = null;  // the list of overdraft with associated Accounts
	private ArrayList<OverdraftInstruction> unprocessedInstructions = null;  // the list of overdraft with associated Accounts
	private LoggedTransaction reservation = null;

	public CommitReservationWorkflow() {}
	public CommitReservationWorkflow(int state, CommitReservationRequest request) {
		super.state=state;
		this.request=request;
	}
	public CommitReservationRequest getRequest() {
		return request;
	}

	public void setRequest(CommitReservationRequest request) {
		this.request = request;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public ArrayList<OverdraftInstruction> getProcessedInstructions() {
		return processedInstructions;
	}

	public void setProcessedInstructions(ArrayList<OverdraftInstruction> processedInstructions) {
		this.processedInstructions = processedInstructions;
	}

	public ArrayList<OverdraftInstruction> getUnprocessedInstructions() {
		return unprocessedInstructions;
	}

	public void setUnprocessedInstructions(ArrayList<OverdraftInstruction> unprocessedInstructions) {
		this.unprocessedInstructions = unprocessedInstructions;
	}

	public LoggedTransaction getReservation() {
		return reservation;
	}

	public void setReservation(LoggedTransaction reservation) {
		this.reservation = reservation;
	}
}
