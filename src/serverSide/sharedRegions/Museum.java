package serverSide.sharedRegions;

import clientSide.entities.*;
import serverSide.main.SimulPar;

/**
 * Museum.
 *
 * It is responsible to keep in count the number of paintings in each room and is implemented as an
 * implicit monitor. All public methods are executed in mutual exclusion. There are no internal
 * synchronization points.
 */
public class Museum {
    /**
     * Number of canvas in each room.
     */
    private int[] canvasInRoom = new int[SimulPar.N];

    /**
     * Distance to each room.
     */
    private int[] roomDistances = new int[SimulPar.N];

    /**
     * Reference to the general repository.
     */
    private final GeneralRepository repos;

    /**
     * Reference to the assault parties.
     */
    private final AssaultParty[] assaultParties;

    /**
     * Museum constructor.
     *
     * @param repos general repository.
     * @param assaultParties assault parties.
     * @param canvasInRoom number of canvas in each room.
     * @param roomDistances distance to each room.
     */
    public Museum(GeneralRepository repos, AssaultParty[] assaultParties, int[] canvasInRoom,
            int[] roomDistances) {
        this.repos = repos;
        this.assaultParties = assaultParties;
        this.canvasInRoom = canvasInRoom;
        this.roomDistances = roomDistances;
    }

    /**
     * Operation roll a canvas.
     *
     * It is called by an ordinary thief to roll a canvas from a room.
     *
     * @param assaultPartyId assault party id.
     */
    public synchronized void rollACanvas(int assaultPartyId) {
        OrdinaryThief ot = (OrdinaryThief) Thread.currentThread();

        /* Get target room from assault party */
        int targetRoom = assaultParties[assaultPartyId].getTargetRoom();

        /* Define if thief is holding a canvas */
        assaultParties[assaultPartyId].setHoldingCanvas(ot.getOrdinaryThiefId(),
                canvasInRoom[targetRoom] > 0);

        if (canvasInRoom[targetRoom] > 0) {
            canvasInRoom[targetRoom]--;

            /* Get ordinary thief element (position inside the assault party) */
            int elementId = assaultParties[assaultPartyId].getThiefElement(ot.getOrdinaryThiefId());
            /* Set thief holding a canvas */
            repos.setAssaultPartyElementCanvas(assaultPartyId, elementId, true);
        }
    }

    /**
     * Get the distance to a room.
     *
     * @param roomId room id.
     * @return distance to a room.
     */
    protected int getRoomDistance(int roomId) {
        return roomDistances[roomId];
    }
}
