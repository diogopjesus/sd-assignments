package sharedRegions;

import commInfra.*;
import genclass.*;
import entities.*;
import main.*;

/**
 * Ordinary thieves concentration site.
 *
 * It is responsible to (...) All public methods are executed in mutual exclusion. There are three
 * internal synchronization points:
 * <ul>
 * <li>a single blocking point for the master thief, where he waits for sufficient ordinary thieves
 * to form a assault party.</li>
 * <li>a single blocking point for the master thief, where he waits for every ordinary thieves to
 * sum up results.</li>
 * <li>an array of blocking points, one per each ordinary thief, where he waits to be called to an
 * assault party or to terminate operations.</li>
 * </ul>
 */
public class ConcentrationSite {
    /**
     * Reference to the general repository.
     */
    private final GeneralRepository repos;

    /**
     * Assault parties ID's 
     */
    private final AssaultParty[] assaultParties;

    /**
     * Fifo of waiting thieves 
     */
    private MemFIFO<Integer> waitingThieves;

    /**
     * Number of waiting thieves
     */
    private int numberOfWaitingThieves;

    /**
     *  Thieves that have been summoned
     */
    private int[] summonedThieves;

    /**
     * Available assault party
     */
    private int availableAssaultParty;

    /**
     * Indicates the end of the operation
     */
    private boolean endOfOps;

    /**
     * Instantiation of the concentration site monitor.
     * 
     * @param repos  Reference to GeneralRepository 
     * @param assaultParties id's of the assault parties 
     */
    public ConcentrationSite(GeneralRepository repos, AssaultParty[] assaultParties) {
        this.repos = repos;
        this.assaultParties = assaultParties;

        try {
            this.waitingThieves = new MemFIFO<>(new Integer[SimulPar.M - 1]);
        } catch (MemException e) {
            GenericIO.writelnString("Instance of waiting  FIFO failed: " + e.getMessage());
            System.exit(1);
        }
        this.numberOfWaitingThieves = 0;

        this.endOfOps = false;

        this.summonedThieves = new int[SimulPar.K];
        summonedThieves = new int[] {-1, -1, -1};
    }

    /**
     * Indicates whether the ordinary thief is needed
     * 
     * @return the master thief decision 
     */
    public synchronized char amINeeded() {
        OrdinaryThief ot = (OrdinaryThief) Thread.currentThread();

        /* Check if thief returns from ControlSite (or started running) */
        if (ot.getOrdinaryThiefState() == OrdinaryThiefStates.COLLECTION_SITE) {
            ot.setOrdinaryThiefState(OrdinaryThiefStates.CONCENTRATION_SITE);

            try {
                waitingThieves.write(ot.getOrdinaryThiefId());
            } catch (MemException e) {
                GenericIO.writelnString("Instantiation of waiting FIFO failed: " + e.getMessage());
                System.exit(1);
            }
            numberOfWaitingThieves++;

            notifyAll();
        }

        while (!endOfOps && summonedThieves[0] != ot.getOrdinaryThiefId()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (endOfOps) {
            for (int i = 0; i < SimulPar.K - 1; i++)
                summonedThieves[i] = summonedThieves[i + 1];
            notifyAll();
        }

        return endOfOps ? 'E' : 'P';
    }

    /**
     * Prepares the assault party that is going to be sent to the museum
     * 
     * @param assaultPartyId assault party ID
     * @param roomId room ID
     * @param roomDistance distance to the rooom
     */
    public synchronized void prepareAssaultParty(int assaultPartyId, int roomId, int roomDistance) {
        MasterThief mt = (MasterThief) Thread.currentThread();

        while (numberOfWaitingThieves < SimulPar.K) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        assaultParties[assaultPartyId].setTargetRoom(roomId, roomDistance);
        assaultParties[assaultPartyId].startOperation();

        repos.setAssaultPartyRoomId(assaultPartyId, roomId);

        for (int i = 0; i < SimulPar.K; i++) {
            try {
                summonedThieves[i] = waitingThieves.read();
                numberOfWaitingThieves--;
            } catch (MemException e) {
                GenericIO.writelnString(
                        "Retrieval of customer id from waiting FIFO failed: " + e.getMessage());
                System.exit(1);
            }
        }

        mt.setMasterThiefState(MasterThiefStates.ASSEMBLING_A_GROUP);

        availableAssaultParty = assaultPartyId;
        notifyAll();

        while (assaultParties[assaultPartyId].getNumberOfThievesInParty() < SimulPar.K) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Clear summonedThieves array
        summonedThieves = new int[] {-1, -1, -1};
    }

    /**
     * Prepares the excursion 
     * 
     * @return available assault party 
     */
    public synchronized int prepareExcursion() {
        OrdinaryThief ot = (OrdinaryThief) Thread.currentThread();

        assaultParties[availableAssaultParty].assignNewThief(ot.getOrdinaryThiefId());
        repos.addAssaultPartyElement(availableAssaultParty, ot.getOrdinaryThiefId());

        ot.setOrdinaryThiefState(OrdinaryThiefStates.CRAWLING_INWARDS);

        for (int i = 0; i < SimulPar.K - 1; i++)
            summonedThieves[i] = summonedThieves[i + 1];

        notifyAll();

        return availableAssaultParty;
    }

    /**
     * After the assault, the master thief sums up the results of the heist
     * 
     * @param numberOfCanvas number of canvas
     */
    public synchronized void sumUpResults(int numberOfCanvas) {
        MasterThief mt = (MasterThief) Thread.currentThread();

        while (numberOfWaitingThieves < SimulPar.M - 1) {
            try {
                wait();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }

        endOfOps = true;
        notifyAll();

        mt.setMasterThiefState(MasterThiefStates.PRESENTING_THE_REPORT);
    }
}
