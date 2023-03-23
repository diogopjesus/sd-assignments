package interfaces;

public interface ConcentrationSiteInterface {
	public void prepareAssaultParty(int assParty_id, int room_id);
	public char appraiseSit(boolean roomsEmpty); // returns 'P' if it's preparing assP, 'R' if takes a rest or 'E' if the heist it's over
    public boolean amINeeded(int assParty_id);
    public int prepareExcursion(); // Returns assault party id (??)
    public void sumUpResults();
    public int getAssPartyRoom(int assParty_id);
    public int getAssParty();
}
