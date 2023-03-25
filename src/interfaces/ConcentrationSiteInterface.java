package interfaces;

public interface ConcentrationSiteInterface {
	public void prepareAssaultParty();
    public int amINeeded(int thiefID);
    public int prepareExcursion(); // Returns assault party id (??)
    // int getRoom(int ap);
    // int getAssaultParty();
    // int appraiseSit(boolean roomState);
    public void sumUpResults();

	public char appraiseSit(boolean roomsEmpty); // returns 'P' if it's preparing assP, 'R' if takes a rest or 'E' if the heist it's over

    public int getAssPartyRoom(int assParty_id);
    public int getAssParty();
}
