package clientSide.entities;

import clientSide.stubs.*;

/**
 * Ordinary thief thread.
 *
 * It simulates the ordinary thief life cycle.
 */
public class OrdinaryThief extends Thread {
    /**
     * Ordinary thief id.
     */
    private int ordinaryThiefId;

    /**
     * Maximum displacement.
     */
    private int maximumDisplacement;

    /**
     * Ordinary thief state.
     */
    private int ordinaryThiefState;

    /**
     * Reference to the control collection site.
     */
    private ControlCollectionSiteStub contColSiteStub;

    /**
     * Reference to the concentration site.
     */
    private ConcentrationSiteStub concentSiteStub;

    /**
     * Reference to the assault parties.
     */
    private AssaultPartyStub[] assPartStub;

    /**
     * Reference to the museum.
     */
    private MuseumStub museumStub;

    /**
     * Ordinary thief constructor.
     *
     * @param name thread name.
     * @param ordinaryThiefId ordinary thief id.
     * @param maximumDisplacement maximum displacement.
     * @param contColSiteStub reference to the control collection site.
     * @param concentSiteStub reference to the concentration site.
     * @param assPartStub reference to the assault parties.
     * @param museumStub reference to the museum.
     */
    public OrdinaryThief(String name, int ordinaryThiefId, int maximumDisplacement,
            ControlCollectionSiteStub contColSiteStub, ConcentrationSiteStub concentSiteStub,
            AssaultPartyStub[] assPartStub, MuseumStub museumStub) {
        super(name);
        this.ordinaryThiefId = ordinaryThiefId;
        this.maximumDisplacement = maximumDisplacement;
        this.ordinaryThiefState = OrdinaryThiefStates.CONCENTRATION_SITE;
        this.contColSiteStub = contColSiteStub;
        this.concentSiteStub = concentSiteStub;
        this.assPartStub = assPartStub;
        this.museumStub = museumStub;
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
     * Ordinary thief life cycle.
     */
    @Override
    public void run() {
        int assaultPartyId;

        while (concentSiteStub.amINeeded()) {
            assaultPartyId = concentSiteStub.prepareExcursion();

            while (assPartStub[assaultPartyId].crawlIn());

            museumStub.rollACanvas(assaultPartyId);

            assPartStub[assaultPartyId].reverseDirection();

            while (assPartStub[assaultPartyId].crawlOut());

            contColSiteStub.handACanvas(assaultPartyId);
        }
    }
}
