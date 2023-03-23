package entities;

public class CommonThief extends Thread{
	private int commonThiefId;
	private int commonThiefState;
	
	public int getCommonThiefId() {
		return commonThiefId;
	}
	public void setCommonThiefId(int commonThiefId) {
		this.commonThiefId = commonThiefId;
	}
	public int getCommonThiefState() {
		return commonThiefState;
	}
	public void setCommonThiefState(int comonThiefState) {
		this.commonThiefState = comonThiefState;
	}
	
}
