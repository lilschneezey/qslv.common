package qslv.kstream.workflow;

import java.util.ArrayList;

import qslv.data.Account;
import qslv.data.OverdraftInstruction;
import qslv.kstream.ReservationRequest;

public class ReservationWorkflow extends Workflow {
	//--------------------------------------------------
	public final static int ACQUIRE_RESERVATION = 1;
		// [funds available] --> SUCCESS
		// [no funds available] use next OD account --> ACQUIRE_RESERVATION
		// [no more OD accounts] --> INSUFFICIENT_FUNDS
	public final static int SUCCESS = 2;
		//  --> COMPLETE
	public final static int INSUFFICIENT_FUNDS = 3;
		//  --> COMPLETE
	//--------------------------------------------------
	
	private ReservationRequest request = null;
	
	// --Enriched Data -----------------------------------------
	private Account account = null;  // holds status and other account attributed
	private ArrayList<OverdraftInstruction> processedInstructions = null;  // the list of overdraft with associated Accounts
	private ArrayList<OverdraftInstruction> unprocessedInstructions = null;  // the list of overdraft with associated Accounts

	public ReservationWorkflow() {}
	public ReservationWorkflow(int state, ReservationRequest request) {
		super.state = state;
		this.request = request;
	}
	
	public ReservationRequest getRequest() {
		return request;
	}

	public void setRequest(ReservationRequest request) {
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
}
