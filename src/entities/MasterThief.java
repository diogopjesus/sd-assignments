package entities;

import sharedRegions.*;

/**
 * Master thief thread.
 *
 * It simulates the master thief life cycle.
 */
public class MasterThief extends Thread {
    /**
     *  Reference to the general repository.
     */
    private GeneralRepository repos;

    /**
     * Reference to the control and collection site.
     */
    private ControlCollectionSite contColSite;

    /**
     * Reference to the concentration site.
     */
    private ConcentrationSite concentSite;

    /**
     * Reference to the Assault Party.
    */
    private AssaultParty[] assaultParties;

    /**
     * Reference to the Museum.
    */
    private Museum museum;


    /**
     * Master Thief id. 
    */
    private int masterThiefId;

    /**
     *  Master Thief state. 
    */
    private int masterThiefState;

    /**
     * Instantiation of a ordinary thief thread.
     * 
     * @param repos  Reference to GeneralRepository 
     * @param contColSite  Reference to Control collection site 
     * @param concentSite  Reference to concentration site
     * @param assaultParties Reference to Assault party
     * @param museum  Reference to Museum
     * @param masterThiefId Master thief id
     */
    public MasterThief(GeneralRepository repos, ControlCollectionSite contColSite,
            ConcentrationSite concentSite, AssaultParty[] assaultParties, Museum museum,
            int masterThiefId) {
        this.repos = repos;
        this.contColSite = contColSite;
        this.concentSite = concentSite;
        this.assaultParties = assaultParties;
        this.museum = museum;

        this.masterThiefId = masterThiefId;
        this.masterThiefState = MasterThiefStates.PLANNING_THE_HEIST;
    }

   /**
     * Get Master Thief State
     * @return master thief state
     */
    public int getMasterThiefState() {
        return masterThiefState;
    }

    /**
     * Set Master Thief State
     * @param masterThiefState master thief state
     */
    public void setMasterThiefState(int masterThiefState) {
        this.masterThiefState = masterThiefState;
        repos.setMasterThiefState(masterThiefState);
    }

    /**
     *  Life cycle of the master thief.
     */
    @Override
    public void run() {
        char oper;
        int assaultPartyId, roomId, roomDistance;
        int numberOfCanvas;

        contColSite.startOperations();

        while ((oper = contColSite.appraiseSit()) != 'E') {
            switch (oper) {
                case 'P':
                    assaultPartyId = contColSite.getAvailableAssaultParty();
                    roomId = contColSite.getAvailableRoom();
                    roomDistance = museum.getRoomDistance(roomId);

                    concentSite.prepareAssaultParty(assaultPartyId, roomId, roomDistance);

                    assaultParties[assaultPartyId].sendAssaultParty();

                    break;

                case 'R':
                    contColSite.takeARest();

                    contColSite.collectACanvas();

                    break;
            }
        }

        numberOfCanvas = contColSite.getNumberOfCanvas();

        concentSite.sumUpResults(numberOfCanvas);
        repos.endAssault(numberOfCanvas);
    }
}
