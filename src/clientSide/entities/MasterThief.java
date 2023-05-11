package clientSide.entities;

import clientSide.stubs.*;

/**
 * Master thief thread.
 *
 * It simulates the master thief life cycle.
 */
public class MasterThief extends Thread {
    /**
     * Master thief state.
     */
    private int masterThiefState;

    /**
     * Reference to the control collection site.
     */
    private final ControlCollectionSiteStub contColSiteStub;

    /**
     * Reference to the concentration site.
     */
    private final ConcentrationSiteStub concentSiteStub;

    /**
     * Reference to the assault parties.
     */
    private final AssaultPartyStub[] assPartStub;

    /**
     * Master thief constructor.
     *
     * @param name thread name.
     * @param contColSiteStub reference to the control collection site.
     * @param concentSiteStub reference to the concentration site.
     * @param assPartStub reference to the assault parties.
     */
    public MasterThief(String name, ControlCollectionSiteStub contColSiteStub,
            ConcentrationSiteStub concentSiteStub, AssaultPartyStub[] assPartStub) {
        super(name);
        this.masterThiefState = MasterThiefStates.PLANNING_THE_HEIST;
        this.contColSiteStub = contColSiteStub;
        this.concentSiteStub = concentSiteStub;
        this.assPartStub = assPartStub;
    }


    /**
     * Get master thief state.
     *
     * @return Master thief state.
     */
    public int getMasterThiefState() {
        return masterThiefState;
    }

    /**
     * Set master thief state.
     *
     * @param masterThiefState Master thief state.
     */
    public void setMasterThiefState(int masterThiefState) {
        this.masterThiefState = masterThiefState;
    }

    /**
     * Master thief life cycle.
     */
    @Override
    public void run() {
        char oper;
        int assaultPartyId, roomId;

        contColSiteStub.startOperations();

        while ((oper = contColSiteStub.appraiseSit()) != 'E') {
            switch (oper) {
                case 'P':
                    assaultPartyId = contColSiteStub.getAvailableAssaultParty();
                    roomId = contColSiteStub.getAvailableRoom();

                    concentSiteStub.prepareAssaultParty(assaultPartyId, roomId);

                    assPartStub[assaultPartyId].sendAssaultParty();

                    break;

                case 'R':
                    contColSiteStub.takeARest();

                    contColSiteStub.collectACanvas();

                    break;
            }
        }

        concentSiteStub.sumUpResults();
    }
}
