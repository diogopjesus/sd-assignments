package sharedRegions;

import commInfra.*;
import entities.*;
import genclass.*;
import main.*;

/**
 * Master thief control and collection site.
 *
 * It is responsible to (...) All public methods are executed in mutual exclusion. There are X
 * internal synchronization points: (...)
 */

public class ControlCollectionSite {
    /**
     * Reference to the general repository.
     */
    private final GeneralRepository repos;

    /**
     * Assault parties ID's 
     */
    private AssaultParty[] assaultParties;

    /**
     * Number of canvas 
     */
    private int numberOfCanvas;

    /**
     * Indicates what rooms are empty
     */
    private boolean[] emptyRooms;

    /**
     * Fifo of waiting thieves 
     */
    private MemFIFO<Integer> waitingThieves;

    /**
     * Number of waiting thieves
     */
    private int numberOfWaitingThieves;

    /**
     * ID of the next thief waiting in the line 
     */
    private int nextThiefInLine;

    /**
     * Number of ordinary thieves that have been cleared 
     */
    private int clearedOrdinaryThieves;

    /**
     * Number of active assault parties 
     */
    private int activeAssaultParties;

    /**
     * Indicates if the information was updated
     */
    private boolean infoUpdated;

    /**
     * Instantiation of the control and collection site monitor.
     * 
     * @param repos  Reference to GeneralRepository 
     * @param assaultParties id's of the assault parties 
     */
    public ControlCollectionSite(GeneralRepository repos, AssaultParty[] assaultParties) {
        this.repos = repos;
        this.assaultParties = assaultParties;

        this.numberOfCanvas = 0;
        this.emptyRooms = new boolean[SimulPar.N];
        for (int i = 0; i < SimulPar.N; i++)
            this.emptyRooms[i] = false;

        try {
            this.waitingThieves = new MemFIFO<>(new Integer[SimulPar.M - 1]);
        } catch (MemException e) {
            GenericIO.writelnString("Instance of waiting  FIFO failed: " + e.getMessage());
            System.exit(1);
        }
        this.numberOfWaitingThieves = 0;

        this.nextThiefInLine = -1;

        this.clearedOrdinaryThieves = 6;
        this.activeAssaultParties = 0;

        this.infoUpdated = false;
    }

    /**
     * Get the number of canvas
     * 
     * @return number of canvas 
     */
    public int getNumberOfCanvas() {
        return numberOfCanvas;
    }

    /**
     * Get an available assault party
     * 
     * @return ID of the assault party or -1 if no assault party is available
     */
    public int getAvailableAssaultParty() {
        for (int i = 0; i < (SimulPar.M - 1) / SimulPar.K; i++)
            if (!assaultParties[i].operationStatus())
                return i;

        return -1;
    }

    /**
     * Get an available room 
     * 
     * @return available room or -1 if no room is available
     */
    public int getAvailableRoom() {
        for (int i = 0; i < SimulPar.N; i++)
            if (!emptyRooms[i]) {
                int j;
                for (j = 0; j < (SimulPar.M - 1) / SimulPar.K; j++)
                    if (assaultParties[j].operationStatus()
                            && (assaultParties[j].getTargetRoom() == i))
                        break;
                if (j == (SimulPar.M - 1) / SimulPar.K)
                    return i;
            }
        return -1;
    }

    /**
     * Indicates if all rooms have been cleared
     * 
     * @return true if all rooms have been cleared
     */
    private boolean allRoomsCleared() {
        for (int i = 0; i < SimulPar.N; i++)
            if (!emptyRooms[i])
                return false;

        return true;
    }

    /**
     * Starts the operations
     */
    public synchronized void startOperations() {
        MasterThief mt = (MasterThief) Thread.currentThread();

        mt.setMasterThiefState(MasterThiefStates.DECIDING_WHAT_TO_DO);
    }

    /**
     * MasterThief appraises sit 
     * 
     * @return decision of the master thief
     */
    public synchronized char appraiseSit() {
        // MasterThief mt = (MasterThief)Thread.currentThread();

        /* Get Number of Empty Rooms and ID of a not empty room */
        int numberOfEmptyRooms = 0;
        int roomNotEmptyId = 0;
        for (int i = 0; i < SimulPar.N; i++) {
            if (emptyRooms[i])
                numberOfEmptyRooms++;
            else
                roomNotEmptyId = i;
        }

        /* Get id of an assault party in operation */
        int assaultPartyId = -1;
        for (int i = 0; i < (SimulPar.M - 1) / SimulPar.K; i++)
            if (assaultParties[i].operationStatus()) {
                assaultPartyId = i;
                break;
            }

        // Wait arrival of ordinary thieves
        if ((activeAssaultParties == ((SimulPar.M - 1) / SimulPar.K))
                || ((activeAssaultParties == 1) && (numberOfEmptyRooms == SimulPar.N - 1)
                        && (assaultParties[assaultPartyId].getTargetRoom() == roomNotEmptyId))
                || ((activeAssaultParties == 1) && (numberOfEmptyRooms == SimulPar.N)))

        {
            return 'R';
        }

        if (allRoomsCleared()) {
            while (clearedOrdinaryThieves < SimulPar.M - 1) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return 'E';
        }

        while (clearedOrdinaryThieves < SimulPar.K || activeAssaultParties == 2) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        activeAssaultParties++;
        clearedOrdinaryThieves -= 3;

        return 'P';
    }

    /**
     * Master thief takes a rest
     */
    public synchronized void takeARest() {
        MasterThief mt = (MasterThief) Thread.currentThread();

        mt.setMasterThiefState(MasterThiefStates.WAITING_FOR_GROUP_ARRIVAL);

        while (numberOfWaitingThieves == 0) {
            try {
                wait();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }

    /**
     * An ordinary thief hands a canvas to the master thief
     * 
     * @param assaultPartyId id of the assault party of the ordinary thief
     * @param roomId id of the room that has been assaulted
     */
    public synchronized void handACanvas(int assaultPartyId, int roomId) {
        OrdinaryThief ot = (OrdinaryThief) Thread.currentThread();

        ot.setOrdinaryThiefState(OrdinaryThiefStates.COLLECTION_SITE);

        // if(ot.isHoldingCanvas() || !emptyRooms[roomId])
        // {
        try {
            waitingThieves.write(ot.getOrdinaryThiefId());
            numberOfWaitingThieves++;
        } catch (MemException e) {
            GenericIO.writelnString(
                    "Retrieval of customer id from waiting FIFO failed: " + e.getMessage());
            System.exit(1);
        }

        notifyAll();

        while (nextThiefInLine != ot.getOrdinaryThiefId()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (ot.isHoldingCanvas()) {
            numberOfCanvas++;
            repos.yieldAssaultPartyElementCanvas(assaultPartyId, ot.getOrdinaryThiefId());
        } else
            emptyRooms[roomId] = true;

        infoUpdated = true;
        // }

        assaultParties[assaultPartyId].removeThief(ot.getOrdinaryThiefId());

        repos.removeAssaultPartyElement(assaultPartyId, ot.getOrdinaryThiefId());

        if (!assaultParties[assaultPartyId].operationStatus())
            activeAssaultParties--;

        clearedOrdinaryThieves++;

        notifyAll();
    }

    /**
     * The master thief collects the canvas that has been handed by an ordinary 
     */
    public synchronized void collectACanvas() {
        MasterThief mt = (MasterThief) Thread.currentThread();

        try {
            nextThiefInLine = waitingThieves.read();
            numberOfWaitingThieves--;
        } catch (MemException e) {
            GenericIO.writelnString("Instance of waiting  FIFO failed: " + e.getMessage());
            System.exit(1);
        }

        infoUpdated = false;

        notifyAll();

        while (!infoUpdated) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        nextThiefInLine = -1;

        mt.setMasterThiefState(MasterThiefStates.DECIDING_WHAT_TO_DO);
    }
}
