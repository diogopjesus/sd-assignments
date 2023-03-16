package Interfaces;

public interface ConcentrationSiteInterface {
	public void prepareAssaultParty();
    public int amINeeded(int thiefID);
    public int prepareExcursion(); // Returns assault party id (??)
    public void heistOver();
}
