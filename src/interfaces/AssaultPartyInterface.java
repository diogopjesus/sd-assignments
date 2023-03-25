package interfaces;

public interface AssaultPartyInterface {

	// int assignMember (int ap);
	// int getRoom();
	// void sendAssaultParty(int room);
	public int crawlIn(int thief_id) throws Exception;
	public int crawlOut(int thief_id) throws Exception;
	public int getPartyID();
	//void reverseDirection(thief_id);
}
