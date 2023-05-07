package serverSide.sharedRegions;

import serverSide.entities.ControlCollectionSiteClientProxy;
import serverSide.main.*;
import clientSide.entities.*;
import clientSide.stubs.*;
import commInfra.*;
import genclass.GenericIO;

/**
 * Control Collection Site.
 *
 * It is responsible to keep in count the number of canvas collected, the rooms that are empty known
 * by the master thief and is implemented as an implicit monitor. All public methods are executed in
 * mutual exclusion. There are two internal synchronization points: a single blocking point for the
 * master thief, where he waits for the arrival of an ordinary thief from mission; an array of
 * blocking points, one per each ordinary thief, where he is waits to hand canvas.
 */
public class ControlCollectionSite {
    /**
     * Reference to barber threads.
     */
    private ControlCollectionSiteClientProxy mas;

    /**
     * Reference to customer threads.
     */
    private final ControlCollectionSiteClientProxy[] ord;

    /**
     * Number of entity groups requesting the shutdown.
     */
    private int nEntities;

    /**
     * Flag to indicate that there is not a party.
     */
    private static final int NOT_A_PARTY = -1;

    /**
     * Flag to indicate that there is not a room.
     */
    private static final int NOT_A_ROOM = -1;

    /**
     * Flag to indicate that there is not a thief.
     */
    private static final int NOT_A_THIEF = -1;

    /**
     * Reference to the general repository.
     */
    private GeneralRepositoryStub reposStub;

    /**
     * Reference to the assault parties.
     */
    private AssaultPartyStub[] assaultPartiesStub;

    /**
     * Indicates the party where a thief belongs.
     */
    private int[] thievesParties;

    /**
     * Queue of thieves ready to hand canvas.
     */
    private MemFIFO<Integer> handCanvasQueue;

    /**
     * Indicates how many thieves are ready to hand canvas.
     */
    private int readyToHandCanvas;

    /**
     * Indicates the thief id chosen to collect canvas.
     */
    private boolean[] readyToCollectCanvas;

    /**
     * Indicates if a room still has canvas inside.
     */
    private boolean[] roomHasCanvas;

    /**
     * Indication of a thief holding a canvas to be collected.
     */
    private boolean[] holdingCanvas;

    /**
     * Control collection site constructor.
     *
     * @param reposStub general repository.
     * @param assaultPartiesStub assault parties.
     */
    public ControlCollectionSite(GeneralRepositoryStub reposStub,
            AssaultPartyStub[] assaultPartiesStub) {
        mas = null;
        ord = new ControlCollectionSiteClientProxy[SimulPar.M - 1];
        for (int i = 0; i < SimulPar.M - 1; i++)
            ord[i] = null;
        this.nEntities = 0;
        this.reposStub = reposStub;
        this.assaultPartiesStub = assaultPartiesStub;
        this.thievesParties = new int[SimulPar.M - 1];
        for (int i = 0; i < SimulPar.M - 1; i++)
            this.thievesParties[i] = NOT_A_PARTY;
        try {
            this.handCanvasQueue = new MemFIFO<>(new Integer[SimulPar.M - 1]);
        } catch (MemException e) {
            GenericIO.writelnString("Instance of waiting  FIFO failed: " + e.getMessage());
            System.exit(1);
        }
        this.readyToHandCanvas = 0;
        this.readyToCollectCanvas = new boolean[SimulPar.M - 1];
        for (int i = 0; i < SimulPar.M - 1; i++)
            this.readyToCollectCanvas[i] = false;
        this.roomHasCanvas = new boolean[SimulPar.N];
        for (int i = 0; i < SimulPar.N; i++)
            this.roomHasCanvas[i] = true;
        this.holdingCanvas = new boolean[SimulPar.M - 1];
        for (int i = 0; i < SimulPar.M - 1; i++)
            this.holdingCanvas[i] = false;
    }

    /**
     * Operation start operations.
     *
     * It is called by the master thief to start the operations.
     */
    public synchronized void startOperations() {
        mas = (ControlCollectionSiteClientProxy) Thread.currentThread();

        mas.setMasterThiefState(MasterThiefStates.DECIDING_WHAT_TO_DO);
        reposStub.setMasterThiefState(MasterThiefStates.DECIDING_WHAT_TO_DO);
    }

    /**
     * Operation appraise situation.
     *
     * It is called by the master thief to appraise the situation.
     *
     * @return 'P' if we should prepare an assault party - 'R' if we should wait for the ordinary
     *         thieves - 'E' if we should end the operation.
     */
    public synchronized char appraiseSit() {
        // Wait arrival of ordinary thieves if:
        // - All assault parties are in mission;
        // - there is one assault party in mission, the number of empty rooms is equal
        // to N-1, and
        // the assault party in mission is targeting the non-empty room;
        // - There is one assault party in mission, and all rooms are empty.
        if ((availableAssaultParties() == 0)
                || ((availableAssaultParties() == ((SimulPar.M - 1) / SimulPar.K) - 1)
                        && (availableRooms() == 1)
                        && (assaultPartiesStub[getAssaultPartyOnMissionId()]
                                .getTargetRoom() == getAvailableRoom()))
                || ((availableAssaultParties() == ((SimulPar.M - 1) / SimulPar.K) - 1)
                        && (availableRooms() == 0)))
            return 'R';

        // End if all rooms are already cleared
        if (availableRooms() == 0)
            return 'E';

        // Prepare an assault party
        return 'P';
    }

    /**
     * Operation take a rest.
     *
     * It is called by the master thief to wait until a new thief has arrived the control collection
     * site.
     */
    public synchronized void takeARest() {
        mas = (ControlCollectionSiteClientProxy) Thread.currentThread();

        mas.setMasterThiefState(MasterThiefStates.WAITING_FOR_GROUP_ARRIVAL);
        reposStub.setMasterThiefState(MasterThiefStates.WAITING_FOR_GROUP_ARRIVAL);

        while (readyToHandCanvas == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Operation hand a canvas.
     *
     * It is called by the ordinary thief to hand a canvas to the master thief. The ordinary thief
     * blocks until he is called by the master thief, meaning his canvas was collected.
     *
     * @param assaultPartyId assault party id.
     */
    public synchronized void handACanvas(int assaultPartyId) {
        int ordId;
        ordId = ((ControlCollectionSiteClientProxy) Thread.currentThread()).getOrdinaryThiefId();
        ord[ordId] = (ControlCollectionSiteClientProxy) Thread.currentThread();

        /* Add thief to the waiting queue */
        try {
            handCanvasQueue.write(ord[ordId].getOrdinaryThiefId());
        } catch (MemException e) {
            GenericIO.writelnString("Instantiation of waiting FIFO failed: " + e.getMessage());
            System.exit(1);
        }
        readyToHandCanvas++;

        /* Signal if he is holding a canvas or not */
        boolean canvas =
                assaultPartiesStub[assaultPartyId].isHoldingCanvas(ord[ordId].getOrdinaryThiefId());
        setHoldingCanvas(ord[ordId].getOrdinaryThiefId(), canvas);

        /* Notify master thief that is ready to hand a canvas */
        notifyAll();

        while (!readyToCollectCanvas[ord[ordId].getOrdinaryThiefId()]) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        readyToCollectCanvas[ord[ordId].getOrdinaryThiefId()] = false;
    }

    /**
     * Operation collect a canvas.
     *
     * It is called by the master thief to collect the canvas of the next ordinary thief in queue.
     * After collecting the canvas, he wakes up the ordinary thief to proceed operations.
     */
    public synchronized void collectACanvas() {
        mas = (ControlCollectionSiteClientProxy) Thread.currentThread();

        int ordinaryThiefId = NOT_A_THIEF;
        try {
            ordinaryThiefId = handCanvasQueue.read();
        } catch (MemException e) {
            GenericIO.writelnString("Instantiation of waiting FIFO failed: " + e.getMessage());
            System.exit(1);
        }
        readyToHandCanvas--;

        int assaultPartyId = getThiefParty(ordinaryThiefId);

        /* Collect canvas */
        if (!isHoldingCanvas(ordinaryThiefId)) {
            setRoomEmpty(assaultPartiesStub[assaultPartyId].getTargetRoom());
        }

        int element = assaultPartiesStub[assaultPartyId].getThiefElement(ordinaryThiefId);

        /* Disassociate thief to the assault party */
        assaultPartiesStub[assaultPartyId].quitAssaultParty(ordinaryThiefId);
        setThiefToParty(ordinaryThiefId, NOT_A_PARTY);

        /* Notify thief that their canvas was colleted */
        readyToCollectCanvas[ordinaryThiefId] = true;
        notifyAll();

        mas.setMasterThiefState(MasterThiefStates.DECIDING_WHAT_TO_DO);
        reposStub.endAssaultPartyElementMission(assaultPartyId, element);
    }

    /**
     * Get the id of an available assault party.
     *
     * @return Id of an available assault party if there is one available, -1 otherwise.
     */
    public synchronized int getAvailableAssaultParty() {
        int availableAssaultParty = NOT_A_PARTY;

        for (int i = 0; i < ((SimulPar.M - 1) / SimulPar.K); i++)
            if (assaultPartiesStub[i].isAvailable()) {
                availableAssaultParty = i;
                break;
            }

        return availableAssaultParty;
    }

    /**
     * Get the id of an available assault party.
     *
     * @return Id of an available assault party if there is one available, -1 otherwise.
     */
    public synchronized int getAvailableRoom() {
        int availableRoom = NOT_A_ROOM;

        for (int i = 0; i < SimulPar.N; i++)
            if (roomHasCanvas[i]) {
                /* Check if any assault party is targeting that room */
                int assPart = 0;
                for (assPart = 0; assPart < (SimulPar.M - 1) / SimulPar.K; assPart++) {
                    if (assaultPartiesStub[assPart].getTargetRoom() == i) {
                        break;
                    }
                }

                /* If no assault party is targeting that room, return it */
                if (assPart == (SimulPar.M - 1) / SimulPar.K) {
                    availableRoom = i;
                    break;
                }
            }

        /* if there is no other available room. WARNING: Should not happen! */
        if (availableRoom == NOT_A_ROOM) {
            /* Get the same room the other assault party is targeting */
            for (int i = 0; i < SimulPar.N; i++)
                if (roomHasCanvas[i]) {
                    availableRoom = i;
                    break;
                }
        }

        return availableRoom;
    }

    /**
     * Get the number of available assault parties.
     *
     * @return Number of available assault parties.
     */
    private int availableAssaultParties() {
        int availableAssaultParties = 0;

        for (int i = 0; i < ((SimulPar.M - 1) / SimulPar.K); i++)
            if (assaultPartiesStub[i].isAvailable())
                availableAssaultParties++;

        return availableAssaultParties;
    }

    /**
     * Get the number of available rooms.
     *
     * @return Number of available rooms.
     */
    private int availableRooms() {
        int availableRooms = 0;

        for (int i = 0; i < SimulPar.N; i++)
            if (roomHasCanvas[i])
                availableRooms++;

        return availableRooms;
    }

    /**
     * Set a room as empty.
     *
     * @param roomId room id.
     */
    private void setRoomEmpty(int roomId) {
        roomHasCanvas[roomId] = false;
    }

    /**
     * Get the id of an assault party on mission.
     *
     * @return Id of an assault party on mission if there is one available, -1 otherwise.
     */
    private int getAssaultPartyOnMissionId() {
        int assaultPartyOnMissionId = -1;

        for (int i = 0; i < ((SimulPar.M - 1) / SimulPar.K); i++)
            if (!assaultPartiesStub[i].isAvailable()) {
                assaultPartyOnMissionId = i;
                break;
            }

        return assaultPartyOnMissionId;
    }

    /**
     * Associate a thief to an assault party.
     *
     * @param thiefId thief id.
     * @param assaultPartyId assault party id.
     */
    public synchronized void setThiefToParty(int thiefId, int assaultPartyId) {
        this.thievesParties[thiefId] = assaultPartyId;
    }

    /**
     * Operation server shutdown.
     *
     * New operation.
     */
    public synchronized void shutdown() {
        nEntities += 1;
        if (nEntities >= SimulPar.E)
            ServerHeistToTheMuseumControlCollectionSite.waitConnection = false;
        notifyAll();
    }

    /**
     * Get the assault party of a thief.
     *
     * @param thiefId thief id.
     * @return Assault party id.
     */
    private int getThiefParty(int thiefId) {
        return this.thievesParties[thiefId];
    }

    /**
     * Set if a thief is holding a canvas or not.
     *
     * @param thiefId thief id.
     * @param canvas true if he is holding a canvas - false, otherwise.
     */
    private void setHoldingCanvas(int thiefId, boolean canvas) {
        this.holdingCanvas[thiefId] = canvas;
    }

    /**
     * Get if a thief is holding a canvas or not.
     *
     * @param thiefId thief id.
     * @return True if the thief is holding a canvas, false otherwise.
     */
    private boolean isHoldingCanvas(int thiefId) {
        return this.holdingCanvas[thiefId];
    }
}
