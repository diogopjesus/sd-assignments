package entities;
import sharedRegions.AssaultParty;
import sharedRegions.ConcentrationSite;
import sharedRegions.ControlSite;
import sharedRegions.GeneralRepository;

public class MasterThief extends Thread {
    
    /**
     * 
     */
    private int masterThiefId;
    
    /**
     * 
     */
    private int masterThiefState;
    
    /**
     * 
     */
    private final AssaultParty[] assPart;
    
    /**
     * 
     */
    private final ConcentrationSite concSite;
    
    /**
     * 
     */
    private final ControlSite contSite;
    
    /**
     * 
     */
    private final GeneralRepository genRep;

    /**
     * 
     * @param id
     * @param assPart
     * @param concSite
     * @param contSite
     * @param genRep
     */
    public MasterThief(int masterThiefId, AssaultParty[] assPart, ConcentrationSite concSite, ControlSite contSite, GeneralRepository genRep) {
        this.masterThiefId = masterThiefId;
        this.masterThiefState = 0;
        this.assPart = assPart;
        this.concSite = concSite;
        this.contSite = contSite;
        this.genRep = genRep;
    }


	/**
	 * @return the state
	 */
	public int getMasterThiefState() {
		return masterThiefState;
	}


	/**
	 * @param masterThiefState the state to set
	 */
	public void setMasterThiefState(int masterThiefState) {
		this.masterThiefState = masterThiefState;
	}


	/**
	 * @return the id
	 */
	public int getMasterThiefId() {
		return masterThiefId;
	}


	@Override
	public void run() {
		char oper;
		int assPartID, roomID;
		contSite.startOperations();
		
		while((oper = contSite.appraiseSit()) != 'E') {
			switch(oper) {
				case 'P':
					assPartID = contSite.getAssaultID();
					roomID = contSite.getRoomID();
					concSite.prepareAssaultParty(assPartID, roomID);
					assPart[assPartID].sendAssaultParty();
					break;
					
				case 'R':
					contSite.takeARest();
					contSite.collectACanvas();
					break;
			}
		}
		
	    concSite.sumUpResults(contSite.getNumberOfCanvas());
	}
}
