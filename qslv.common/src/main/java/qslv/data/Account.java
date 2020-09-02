package qslv.data;

public class Account {

	private String accountNumber;
	private String accountLifeCycleStatus;
	private boolean protectAgainstOverdraft = true;
	
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getAccountLifeCycleStatus() {
		return accountLifeCycleStatus;
	}
	public void setAccountLifeCycleStatus(String accountLifeCycleStatus) {
		this.accountLifeCycleStatus = accountLifeCycleStatus;
	}
	public boolean isProtectAgainstOverdraft() {
		return protectAgainstOverdraft;
	}
	public void setProtectAgainstOverdraft(boolean protectAgainstOverdraft) {
		this.protectAgainstOverdraft = protectAgainstOverdraft;
	}
	
}
