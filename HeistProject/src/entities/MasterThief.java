package entities;

public class MasterThief extends Thread {
	private int masterThiefState;
	
	
	public int getMasterThiefState() {
        return masterThiefState;
    }
	
	public void setMasterThiefState(int masterThiefState) {
        this.masterThiefState = masterThiefState;
    }
}
