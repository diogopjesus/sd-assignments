package clientSide.entities;

import java.rmi.*;
import interfaces.*;
import genclass.GenericIO;

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
    private ControlCollectionSiteInterface contColSiteStub;

    /**
     * Reference to the concentration site.
     */
    private ConcentrationSiteInterface concentSiteStub;

    /**
     * Reference to the assault parties.
     */
    private AssaultPartyInterface[] assPartStub;

    /**
     * Reference to the museum.
     */
    private MuseumInterface museumStub;

    /**
     * Ordinary thief constructor.
     *
     * @param name                thread name.
     * @param ordinaryThiefId     ordinary thief id.
     * @param maximumDisplacement maximum displacement.
     * @param contColSiteStub     reference to the control collection site.
     * @param concentSiteStub     reference to the concentration site.
     * @param assPartStub         reference to the assault parties.
     * @param museumStub          reference to the museum.
     */
    public OrdinaryThief(String name, int ordinaryThiefId, int maximumDisplacement,
            ControlCollectionSiteInterface contColSiteStub, ConcentrationSiteInterface concentSiteStub,
            AssaultPartyInterface[] assPartStub, MuseumInterface museumStub) {
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

        while (amINeeded()) {
            assaultPartyId = prepareExcursion();

            while (crawlIn(assaultPartyId))
                ;

            rollACanvas(assaultPartyId);

            reverseDirection(assaultPartyId);

            while (crawlOut(assaultPartyId))
                ;

            handACanvas(assaultPartyId);
        }
    }

    /**
     * Ordinary thief is needed.
     * 
     * Remote operation.
     * 
     * @return true, if the ordinary thief is needed - false, otherwise.
     */
    private boolean amINeeded() {
        ReturnBoolean ret = null;

        try {
            ret = concentSiteStub.amINeeded(ordinaryThiefId, ordinaryThiefState);
        } catch (RemoteException e) {
            GenericIO.writelnString(
                    "Ordinary thief " + ordinaryThiefId + " remote exception on amINeeded: " + e.getMessage());
            System.exit(1);
        }

        ordinaryThiefState = ret.getIntStateVal();

        return ret.getBooleanVal();
    }

    /**
     * Ordinary thief prepares the excursion.
     * 
     * Remote operation.
     * 
     * @return assault party id.
     */
    private int prepareExcursion() {
        ReturnInt ret = null;

        try {
            ret = concentSiteStub.prepareExcursion(ordinaryThiefId);
        } catch (RemoteException e) {
            GenericIO.writelnString(
                    "Ordinary thief " + ordinaryThiefId + " remote exception on prepareExcursion: " + e.getMessage());
            System.exit(1);
        }

        ordinaryThiefState = ret.getIntStateVal();

        return ret.getIntVal();
    }

    /**
     * Ordinary thief crawls in.
     * 
     * Remote operation.
     * 
     * @return true, if the ordinary thief can crawl in - false, otherwise.
     */
    private boolean crawlIn(int assaultPartyId) {
        ReturnBoolean ret = null;

        try {
            ret = assPartStub[assaultPartyId].crawlIn(ordinaryThiefId, ordinaryThiefState);
        } catch (RemoteException e) {
            GenericIO.writelnString(
                    "Ordinary thief " + ordinaryThiefId + " remote exception on crawlIn: " + e.getMessage());
            System.exit(1);
        }

        ordinaryThiefState = ret.getIntStateVal();

        return ret.getBooleanVal();
    }

    /**
     * Ordinary thief rolls a canvas.
     * 
     * Remote operation.
     */
    private void rollACanvas(int assaultPartyId) {
        try {
            museumStub.rollACanvas(assaultPartyId, ordinaryThiefId);
        } catch (RemoteException e) {
            GenericIO.writelnString(
                    "Ordinary thief " + ordinaryThiefId + " remote exception on rollACanvas: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Ordinary thief reverses direction.
     * 
     * Remote operation.
     */
    private void reverseDirection(int assaultPartyId) {
        int state = -1;

        try {
            state = assPartStub[assaultPartyId].reverseDirection(ordinaryThiefId);
        } catch (RemoteException e) {
            GenericIO.writelnString(
                    "Ordinary thief " + ordinaryThiefId + " remote exception on reverseDirection: " + e.getMessage());
            System.exit(1);
        }

        ordinaryThiefState = state;
    }

    /**
     * Ordinary thief crawls out.
     * 
     * Remote operation.
     * 
     * @return true, if the ordinary thief can crawl out - false, otherwise.
     */
    private boolean crawlOut(int assaultPartyId) {
        ReturnBoolean ret = null;

        try {
            ret = assPartStub[assaultPartyId].crawlOut(ordinaryThiefId, ordinaryThiefState);
        } catch (RemoteException e) {
            GenericIO.writelnString(
                    "Ordinary thief " + ordinaryThiefId + " remote exception on crawlOut: " + e.getMessage());
            System.exit(1);
        }

        ordinaryThiefState = ret.getIntStateVal();

        return ret.getBooleanVal();
    }

    /**
     * Ordinary thief hands a canvas.
     * 
     * Remote operation.
     */
    private void handACanvas(int assaultPartyId) {
        try {
            contColSiteStub.handACanvas(assaultPartyId, ordinaryThiefId);
        } catch (RemoteException e) {
            GenericIO.writelnString(
                    "Ordinary thief " + ordinaryThiefId + " remote exception on handACanvas: " + e.getMessage());
            System.exit(1);
        }
    }
}
