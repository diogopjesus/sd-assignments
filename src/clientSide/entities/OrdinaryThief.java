package clientSide.entities;

import serverSide.sharedRegions.*;

/**
 * Ordinary thief thread.
 *
 * It simulates the ordinary thief life cycle.
 */
public class OrdinaryThief extends Thread {
    /**
     * Reference to the control collection site.
     */
    private ControlCollectionSite contColSite;

    /**
     * Reference to the concentration site.
     */
    private ConcentrationSite concentSite;

    /**
     * Reference to the assault parties.
     */
    private AssaultParty[] assaultParties;

    /**
     * Reference to the museum.
     */
    private Museum museum;

    /**
     * Ordinary thief state.
     */
    private int ordinaryThiefState;

    /**
     * Ordinary thief id.
     */
    private int ordinaryThiefId;

    /**
     * Maximum displacement.
     */
    private int maximumDisplacement;

    /**
     * Ordinary thief constructor.
     *
     * @param contColSite control collection site.
     * @param concentSite concentration site.
     * @param assaultParties assault parties.
     * @param museum museum.
     * @param ordinaryThiefId ordinary thief id.
     * @param maximumDisplacement maximum displacement.
     */
    public OrdinaryThief(ControlCollectionSite contColSite, ConcentrationSite concentSite,
            AssaultParty[] assaultParties, Museum museum, int ordinaryThiefId,
            int maximumDisplacement) {
        this.contColSite = contColSite;
        this.concentSite = concentSite;
        this.assaultParties = assaultParties;
        this.museum = museum;
        this.ordinaryThiefState = OrdinaryThiefStates.CONCENTRATION_SITE;
        this.ordinaryThiefId = ordinaryThiefId;
        this.maximumDisplacement = maximumDisplacement;
    }

    /**
     * Get the ordinary thief state.
     *
     * @return ordinary thief state.
     */
    public int getOrdinaryThiefState() {
        return ordinaryThiefState;
    }

    /**
     * Set the ordinary thief state.
     *
     * @param ordinaryThiefState ordinary thief state.
     */
    public void setOrdinaryThiefState(int ordinaryThiefState) {
        this.ordinaryThiefState = ordinaryThiefState;
    }

    /**
     * Get the ordinary thief id.
     *
     * @return ordinary thief id.
     */
    public int getOrdinaryThiefId() {
        return ordinaryThiefId;
    }

    /**
     * Set the ordinary thief id.
     *
     * @param ordinaryThiefId ordinary thief id.
     */
    public void setOrdinaryThiefId(int ordinaryThiefId) {
        this.ordinaryThiefId = ordinaryThiefId;
    }

    /**
     * Get the maximum displacement.
     *
     * @return maximum displacement.
     */
    public int getMaximumDisplacement() {
        return maximumDisplacement;
    }

    /**
     * Set the maximum displacement.
     *
     * @param maximumDisplacement maximum displacement.
     */
    public void setMaximumDisplacement(int maximumDisplacement) {
        this.maximumDisplacement = maximumDisplacement;
    }

    /**
     * Ordinary thief life cycle.
     */
    @Override
    public void run() {
        int assaultPartyId;

        while (concentSite.amINeeded()) {
            assaultPartyId = concentSite.prepareExcursion();

            while (assaultParties[assaultPartyId].crawlIn());

            museum.rollACanvas(assaultPartyId);

            assaultParties[assaultPartyId].reverseDirection();

            while (assaultParties[assaultPartyId].crawlOut());

            contColSite.handACanvas(assaultPartyId);
        }
    }
}
