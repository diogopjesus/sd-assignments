package serverSide.objects;

import serverSide.entities.ConcentrationSiteClientProxy;
import serverSide.main.*;
import clientSide.entities.*;
import clientSide.stubs.*;
import commInfra.*;
import genclass.GenericIO;

/**
 * Concentration Site.
 *
 * It is responsible to keep in count the number of thieves waiting to be assigned to an assault and
 * is implemented as an implicit monitor. All public methods are executed in mutual exclusion. There
 * are 3 synchronization points: an array of blocking points, one per each ordinary thief, where
 * they wait to be called to join an assault party; a blocking point where the master thief waits
 * for all ordinary thieves to join an assault party; and a blocking point where the master thief
 * waits for all ordinary thieves to be present at the concentration site to end the operations.
 */
public class ConcentrationSite {
    /**
     * Reference to barber threads.
     */
    private ConcentrationSiteClientProxy mas;

    /**
     * Reference to customer threads.
     */
    private final ConcentrationSiteClientProxy[] ord;

    /**
     * Number of entity groups requesting the shutdown.
     */
    private int nEntities;

    /**
     * Flag to indicate that there is not a thief to be called.
     */
    private final int NOT_A_THIEF = -1;

    /**
     * Reference to the general repository.
     */
    private final GeneralRepositoryStub reposStub;

    /**
     * Reference to the control collection site.
     */
    private final ControlCollectionSiteStub contColSiteStub;

    /**
     * Reference to the assault parties.
     */
    private final AssaultPartyStub[] assaultPartiesStub;

    /**
     * Reference to the museum.
     */
    private final MuseumStub museumStub;

    /**
     * Queue of thieves waiting to be assigned to an assault party.
     */
    private MemFIFO<Integer> waitingThieves;

    /**
     * Number of thieves waiting to be assigned to an assault party.
     */
    private int numberOfWaitingThieves;

    /**
     * Id of the thief that was called to join an assault party.
     */
    private int calledThiefId;

    /**
     * Id of the assault party that is available for thieves to join.
     */
    private int availableAssaultPartyId;

    /**
     * Flag to indicate if the operation ended or not.
     */
    private boolean endOfOperations;

    /**
     * Concentration site constructor.
     *
     * @param reposStub general repository.
     * @param contColSiteStub control collection site.
     * @param assaultPartiesStub assault parties.
     * @param museumStub museum.
     */
    public ConcentrationSite(GeneralRepositoryStub reposStub,
            ControlCollectionSiteStub contColSiteStub, AssaultPartyStub[] assaultPartiesStub,
            MuseumStub museumStub) {
        mas = null;
        ord = new ConcentrationSiteClientProxy[SimulPar.M - 1];
        for (int i = 0; i < SimulPar.M - 1; i++)
            ord[i] = null;
        this.nEntities = 0;
        this.reposStub = reposStub;
        this.contColSiteStub = contColSiteStub;
        this.assaultPartiesStub = assaultPartiesStub;
        this.museumStub = museumStub;
        try {
            this.waitingThieves = new MemFIFO<>(new Integer[SimulPar.M - 1]);
        } catch (MemException e) {
            GenericIO.writelnString("Instance of waiting  FIFO failed: " + e.getMessage());
            System.exit(1);
        }
        this.numberOfWaitingThieves = 0;
        this.calledThiefId = NOT_A_THIEF;
        this.endOfOperations = false;
    }

    /**
     * Operation am i needed.
     *
     * It is called by the ordinary thief to check if he is needed.
     *
     * @return true if he is needed - false otherwise (to end operations).
     */
    public synchronized boolean amINeeded() {
        int ordId;
        ordId = ((ConcentrationSiteClientProxy) Thread.currentThread()).getOrdinaryThiefId();
        ord[ordId] = (ConcentrationSiteClientProxy) Thread.currentThread();

        /* Set ordinary thief state to concentration site */
        if (ord[ordId].getOrdinaryThiefState() != OrdinaryThiefStates.CONCENTRATION_SITE) {
            ord[ordId].setOrdinaryThiefState(OrdinaryThiefStates.CONCENTRATION_SITE);
            reposStub.setOrdinaryThiefState(ord[ordId].getOrdinaryThiefId(),
                    OrdinaryThiefStates.CONCENTRATION_SITE);
        }

        /* Add thief to the waiting queue */
        try {
            waitingThieves.write(ord[ordId].getOrdinaryThiefId());
        } catch (MemException e) {
            GenericIO.writelnString("Instantiation of waiting FIFO failed: " + e.getMessage());
            System.exit(1);
        }
        numberOfWaitingThieves++;

        /* Notify master thief that a new thief has entered the waiting queue */
        notifyAll();

        /* Wait until thief is called to join a party or to end operations */
        while (getCalledThiefId() != ord[ordId].getOrdinaryThiefId() && !getEndOfOperations()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return !getEndOfOperations();
    }

    /**
     * Operation prepare assault party.
     *
     * It is called by the master thief to prepare an assault party.
     *
     * @param assaultPartyId assault party id.
     * @param roomId room id.
     */
    public synchronized void prepareAssaultParty(int assaultPartyId, int roomId) {
        mas = (ConcentrationSiteClientProxy) Thread.currentThread();

        /* Block until there is a sufficient number of ordinary thieves available */
        while (numberOfWaitingThieves < SimulPar.K) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /* Update master thief state to assembling a group */
        mas.setMasterThiefState(MasterThiefStates.ASSEMBLING_A_GROUP);
        reposStub.setMasterThiefState(MasterThiefStates.ASSEMBLING_A_GROUP);

        /* Update available assault party id */
        setAvailableAssaultPartyId(assaultPartyId);

        /* Update assault party target room */
        assaultPartiesStub[assaultPartyId].setTargetRoom(roomId);
        assaultPartiesStub[assaultPartyId]
                .setTargetRoomDistance(museumStub.getRoomDistance(roomId));
        reposStub.setAssaultPartyRoomId(assaultPartyId, roomId);

        /*
         * In this implementation, the master thief calls an ordinary thief to join a party. Then,
         * the ordinary thief calls the next ordinary thief to join the party. The last ordinary
         * thief calls the master thief to prepare the next assault party.
         */

        /* Get the id of the first ordinary thief to join the assault party */
        try {
            setCalledThiefId(waitingThieves.read());
            numberOfWaitingThieves--;
        } catch (MemException e) {
            GenericIO.writelnString(
                    "Retrieval of customer id from waiting FIFO failed: " + e.getMessage());
            System.exit(1);
        }

        /* Notify ordinary thief that he was called to join a party */
        notifyAll();

        /* Wait until all ordinary thieves have entered the assault party */
        while (getCalledThiefId() != NOT_A_THIEF) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Operation prepare excursion.
     *
     * It is called by an ordinary thief to prepare excursion.
     *
     * @return id of the assault party that the thief joined.
     */
    public synchronized int prepareExcursion() {
        int ordId;
        ordId = ((ConcentrationSiteClientProxy) Thread.currentThread()).getOrdinaryThiefId();
        ord[ordId] = (ConcentrationSiteClientProxy) Thread.currentThread();

        /* Store info on Control site to associate a thief to an assault party */
        contColSiteStub.setThiefToParty(ord[ordId].getOrdinaryThiefId(),
                getAvailableAssaultPartyId());

        /* Join the available assault party */
        assaultPartiesStub[getAvailableAssaultPartyId()]
                .joinAssaultParty(ord[ordId].getOrdinaryThiefId());

        /* Check if assault party is full */
        if (assaultPartiesStub[getAvailableAssaultPartyId()].isFull())
            /* Clear called thief */
            setCalledThiefId(NOT_A_THIEF);
        else
            /* Get the id of a new ordinary thief */
            try {
                setCalledThiefId(waitingThieves.read());
                numberOfWaitingThieves--;
            } catch (MemException e) {
                GenericIO.writelnString(
                        "Retrieval of customer id from waiting FIFO failed: " + e.getMessage());
                System.exit(1);
            }

        /* Update ordinary thief state */
        ord[ordId].setOrdinaryThiefState(OrdinaryThiefStates.CRAWLING_INWARDS);
        reposStub.setAssaultPartyElementId(
                getAvailableAssaultPartyId(), assaultPartiesStub[getAvailableAssaultPartyId()]
                        .getThiefElement(ord[ordId].getOrdinaryThiefId()),
                ord[ordId].getOrdinaryThiefId());

        /*
         * Notify a new thief to join the assault party, or the master thief the party is full.
         */
        notifyAll();

        return getAvailableAssaultPartyId();
    }

    /**
     * Operation sum up results.
     *
     * It is called by the master thief to sum up the results of the heist.
     */
    public synchronized void sumUpResults() {
        mas = (ConcentrationSiteClientProxy) Thread.currentThread();

        /* Block until there is a sufficient number of ordinary thieves available */
        while (numberOfWaitingThieves < SimulPar.M - 1) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        mas.setMasterThiefState(MasterThiefStates.PRESENTING_THE_REPORT);
        reposStub.setMasterThiefState(MasterThiefStates.PRESENTING_THE_REPORT);

        /* Signal end of operations */
        setEndOfOperations();

        /* Notify all ordinary thieves that the heist has ended */
        notifyAll();
    }

    /**
     * Operation server shutdown.
     *
     * New operation.
     */
    public synchronized void shutdown() {
        nEntities += 1;
        if (nEntities >= SimulPar.E)
            ServerHeistToTheMuseumConcentrationSite.waitConnection = false;
        notifyAll();
    }

    /**
     * Get the id of the thief that was called to join an assault party.
     *
     * @return called thief id.
     */
    private int getCalledThiefId() {
        return calledThiefId;
    }

    /**
     * Set the id of the thief that was called to join an assault party.
     *
     * @param calledThiefId called thief id.
     */
    private void setCalledThiefId(int calledThiefId) {
        this.calledThiefId = calledThiefId;
    }

    /**
     * Get the id of the available assault party.
     *
     * @return available assault party id.
     */
    private int getAvailableAssaultPartyId() {
        return availableAssaultPartyId;
    }

    /**
     * Set the id of the available assault party.
     *
     * @param availableAssaultPartyId available assault party id.
     */
    private void setAvailableAssaultPartyId(int availableAssaultPartyId) {
        this.availableAssaultPartyId = availableAssaultPartyId;
    }

    /**
     * Check the flag that indicates if the operation ended or not.
     *
     * @return true if operation has ended - false otherwise.
     */
    private boolean getEndOfOperations() {
        return endOfOperations;
    }

    /**
     * Set the flag to indicate the operation ended.
     */
    private void setEndOfOperations() {
        this.endOfOperations = true;
    }
}