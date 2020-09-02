package qslv.kstream.workflow;

import java.util.ArrayList;

import qslv.data.Account;
import qslv.data.OverdraftInstruction;
import qslv.kstream.TransactionRequest;

public class TransactionWorkflow extends Workflow {
	//--------------------------------------------------
	public final static int TRANSACT_START = 1;
		// [funds available] --> SUCCESS
		// [no funds available && (OD not allowed || no OD accounts) ] --> INSUFFICIENT_FUNDS
		// [OD allowed && no funds available] use OD account --> START_TRANSFER_FUNDS
	public final static int TRANSFER_FROM_OVERDRAFT_PROTECTION = 2;
		// [no funds available && more OD accounts] use next OD account --> START_TRANSFER_FUNDS
		// [no more OD accounts && not authorize-only ] --> OVERDRAFT_ACCOUNT
		// [no more OD accounts && authorize-only ] --> INSUFFICIENT_FUNDS
		// [funds available] complete transfer --> TRANSFER_AND_TRANSACT
	public final static int TRANSFER_AND_TRANSACT = 3;
		// --> SUCCESS
	public final static int OVERDRAFT_ACCOUNT = 5;
		// --> SUCCESS
	public final static int SUCCESS = 6;
		//  --> COMPLETE
	public final static int INSUFFICIENT_FUNDS = 7;
		//  --> COMPLETE
	//--------------------------------------------------
	
	private TransactionRequest request = null;
	
	// --Enriched Data -----------------------------------------
	private Account account = null;  // holds status and other account attributed
	private ArrayList<OverdraftInstruction> processedInstructions = null;  // the list of overdraft with associated Accounts
	private ArrayList<OverdraftInstruction> unprocessedInstructions = null;  // the list of overdraft with associated Accounts

	public TransactionWorkflow() {}
	public TransactionWorkflow(int state, TransactionRequest request) {
		super.state = state;
		this.request = request;
	}
	public TransactionRequest getRequest() {
		return request;
	}

	public void setRequest(TransactionRequest request) {
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
