package interfaces;

public interface ControlSiteInterface {
	//CommonThief
	void handACanvas(int thief_id, boolean hasCanvas);
	//int	prepareExcursion(int thief_id);
	
	// MasterThief
	void startOperations();
	int appraiseSit(boolean end);
	void sendAssaultParty();
	boolean[] collectACanvas();
	void takeARest();
	int sumUpResults();
}
