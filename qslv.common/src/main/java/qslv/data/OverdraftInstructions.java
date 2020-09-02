package qslv.data;

import java.util.ArrayList;

public class OverdraftInstructions {
	private ArrayList<OverdraftInstruction> overdrafts;

	public OverdraftInstructions() {}
	public OverdraftInstructions(ArrayList<OverdraftInstruction> overdrafts) {
		this.overdrafts = overdrafts;
	}
	public ArrayList<OverdraftInstruction> getOverdrafts() {
		return overdrafts;
	}

	public void setOverdrafts(ArrayList<OverdraftInstruction> overdrafts) {
		this.overdrafts = overdrafts;
	}
	
}
