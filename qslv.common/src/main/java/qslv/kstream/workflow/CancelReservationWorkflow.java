package qslv.kstream.workflow;

import qslv.data.Account;
import qslv.kstream.CancelReservationRequest;
import qslv.kstream.LoggedTransaction;

public class CancelReservationWorkflow extends Workflow {
	//--------------------------------------------------
	public final static int MATCH_RESERVATION = 1;
		// [match] --> CANCEL_RESERVATION
		// [no match] -> NO_MATCH
	public final static int CANCEL_RESERVATION = 1;
		//   --> SUCCESS
	public final static int SUCCESS = 2;
		//  --> COMPLETE
	public final static int NO_MATCH = 3;
		//  --> COMPLETE
	//--------------------------------------------------
	
	private CancelReservationRequest request = null;
	private LoggedTransaction reservation = null;

	// Enriched Data
	private Account account = null;  // holds status and other account attributed

	public CancelReservationWorkflow() {}
	public CancelReservationWorkflow(int state, CancelReservationRequest request) {
		super.state = state;
		this.request = request;
	}
	public CancelReservationRequest getRequest() {
		return request;
	}
	public void setRequest(CancelReservationRequest request) {
		this.request = request;
	}
	public LoggedTransaction getReservation() {
		return reservation;
	}
	public void setReservation(LoggedTransaction reservation) {
		this.reservation = reservation;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}

}
