package qslv.data;

import java.util.UUID;

public class BalanceLog {
	private String accountNumber = null;
	private UUID lastTransaction = null;
	private long balance = 0L;
	
	public BalanceLog() {}
	public BalanceLog(String accountNumber, UUID lastTransaction, long balance) {
		this.accountNumber = accountNumber;
		this.balance = balance;
		this.lastTransaction = lastTransaction;
	}
	
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public UUID getLastTransaction() {
		return lastTransaction;
	}
	public void setLastTransaction(UUID lastTransaction) {
		this.lastTransaction = lastTransaction;
	}
	public long getBalance() {
		return balance;
	}
	public void setBalance(long balance) {
		this.balance = balance;
	}
	
}
