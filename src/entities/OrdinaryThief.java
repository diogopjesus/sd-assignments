package entities;

import sharedRegions.*;

/**
 * Ordinary thief thread.
 *
 * It simulates the ordinary thief life cycle.
 */
public class OrdinaryThief extends Thread {
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
     * Ordinary Thief id. 
     */
    private int ordinaryThiefId;

    /**
     * Ordinary Thief state. 
     */
    private int ordinaryThiefState;

    /**
     * Assault Party id. 
     */
    private int assaultPartyId;

    /**
     * Ordinary Thief maximum displacement. 
     */
    private int maximumDisplacement;

    /**
     *
     */
    private boolean withCanvas;

    /**
     * Instantiation of a ordinary thief thread.
     * 
     * @param repos  Reference to GeneralRepository 
     * @param contColSite  Reference to Control collection site 
     * @param concentSite  Reference to concentration site
     * @param assaultParties Reference to Assault party
     * @param museum  Reference to Museum
     * @param ordinaryThiefId ordinary thief id
     * @param maxDis ordinary thief max displacement
     */
    public OrdinaryThief(GeneralRepository repos, ControlCollectionSite contColSite,
            ConcentrationSite concentSite, AssaultParty[] assaultParties, Museum museum,
            int ordinaryThiefId, int maxDis) {
        this.repos = repos;
        this.contColSite = contColSite;
        this.concentSite = concentSite;
        this.assaultParties = assaultParties;
        this.museum = museum;

        this.ordinaryThiefId = ordinaryThiefId;
        this.ordinaryThiefState = OrdinaryThiefStates.COLLECTION_SITE;
        this.maximumDisplacement = maxDis;
        this.withCanvas = false;
    }

    /**
     * Get Ordinary Thief Id
     * @return ordinary thief id
     */
    public int getOrdinaryThiefId() {
        return ordinaryThiefId;
    }

    /**
     * Get Ordinary Thief State
     * @return ordinary thief state
     */
    public int getOrdinaryThiefState() {
        return ordinaryThiefState;
    }

    /**
     * Set Ordinary Thief State
     * @param ordinaryThiefState ordinary thief state
     */
    public void setOrdinaryThiefState(int ordinaryThiefState) {
        this.ordinaryThiefState = ordinaryThiefState;
        repos.setOrdinaryThiefState(ordinaryThiefId, ordinaryThiefState);
    }

    /**
     * Ordinary Thief Maximum Displacement
     * @return ordinary thief maximum displacement
     */
    public int getMaximumDisplacement() {
        return maximumDisplacement;
    }

    /**
     *
     */
    public boolean isHoldingCanvas() {
        return withCanvas;
    }

    /**
     *
     */
    public void holdCanvas() {
        withCanvas = true;
    }

    /**
     * 
     */
    public void dropCanvas() {
        withCanvas = false;
    }

    /**
     *  Life cycle of the ordinary thieves.
     */
    @Override
    public void run() {
        int roomId;

        while (concentSite.amINeeded() != 'E') {
            assaultPartyId = concentSite.prepareExcursion();

            roomId = assaultParties[assaultPartyId].getTargetRoom();

            while (assaultParties[assaultPartyId].crawlIn());

            museum.rollACanvas(assaultPartyId, roomId);

            assaultParties[assaultPartyId].reverseDirection();

            while (assaultParties[assaultPartyId].crawlOut());

            contColSite.handACanvas(assaultPartyId, roomId);
        }
    }
}
