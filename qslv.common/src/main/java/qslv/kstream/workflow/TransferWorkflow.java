package qslv.kstream.workflow;

import qslv.data.Account;
import qslv.kstream.TransferRequest;

public class TransferWorkflow extends Workflow {
	//--------------------------------------------------
	public final static int JOIN_ACCOUNTS = 1;
	public final static int START_TRANSFER_FUNDS = 2;
		// [no funds available] --> FAILURE
		// [funds available] --> TRANSFER_FUNDS
	public final static int TRANSFER_FUNDS = 3;
		// --> SUCCESS
	public final static int SUCCESS = 4;
		// respond --> COMPLETE
	public final static int INSUFFICIENT_FUNDS = 5;
	// respond --> COMPLETE
	//--------------------------------------------------
	
	private TransferRequest request = null;
	
	// --Enriched Data -----------------------------------------
	private Account transferFromAccount = null;  // holds status and other account attributed
	private Account transferToAccount = null;  // holds status and other account attributed

	public TransferWorkflow() {}
	public TransferWorkflow(int state, TransferRequest request) {
		super.state = state;
		this.request = request;
	}
	public TransferRequest getRequest() {
		return request;
	}

	public void setRequest(TransferRequest request) {
		this.request = request;
	}

	public Account getTransferFromAccount() {
		return transferFromAccount;
	}

	public void setTransferFromAccount(Account transferFromAccount) {
		this.transferFromAccount = transferFromAccount;
	}

	public Account getTransferToAccount() {
		return transferToAccount;
	}

	public void setTransferToAccount(Account transferToAccount) {
		this.transferToAccount = transferToAccount;
	}

}
