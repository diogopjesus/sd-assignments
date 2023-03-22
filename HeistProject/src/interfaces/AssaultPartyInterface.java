package interfaces;

public interface AssaultPartyInterface {
	int crawlIn(int thief_id) throws Exception;
	int crawlOut(int thief_id) throws Exception;
	int getPartyID();
	//void reverseDirection(thief_id);
}
