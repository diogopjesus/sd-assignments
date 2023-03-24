package interfaces;

public interface ControlSiteInterface {
	//CommonThief
	void handACanvas(int room_id, int canvas);
	
	// MasterThief
	void startOperations();
	void collectACanvas();
	void takeARest();
	
	int getRoomId();
	
}
