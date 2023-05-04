package clientSide.entities;

import serverSide.sharedRegions.*;

/**
 * Master thief thread.
 *
 * It simulates the master thief life cycle.
 */
public class MasterThief extends Thread {
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
     * Master thief state.
     */
    private int masterThiefState;


    /**
     * Master thief constructor.
     *
     * @param contColSite control collection site.
     * @param concentSite concentration site.
     * @param assaultParties assault parties.
     */
    public MasterThief(ControlCollectionSite contColSite, ConcentrationSite concentSite,
            AssaultParty[] assaultParties) {
        this.contColSite = contColSite;
        this.concentSite = concentSite;
        this.assaultParties = assaultParties;
        this.masterThiefState = MasterThiefStates.PLANNING_THE_HEIST;
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

        contColSite.startOperations();

        while ((oper = contColSite.appraiseSit()) != 'E') {
            switch (oper) {
                case 'P':
                    assaultPartyId = contColSite.getAvailableAssaultParty();
                    roomId = contColSite.getAvailableRoom();

                    concentSite.prepareAssaultParty(assaultPartyId, roomId);

                    assaultParties[assaultPartyId].sendAssaultParty();

                    break;

                case 'R':
                    contColSite.takeARest();

                    contColSite.collectACanvas();

                    break;
            }
        }

        concentSite.sumUpResults();
    }
}
