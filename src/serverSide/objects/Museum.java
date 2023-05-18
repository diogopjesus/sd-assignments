package serverSide.objects;

import serverSide.entities.*;
import serverSide.main.*;
import clientSide.stubs.*;

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
     * Number of entity groups requesting the shutdown.
     */
    private int nEntities;

    /**
     * Reference to customer threads.
     */

    private final MuseumClientProxy[] ord;

    /**
     * Reference to the general repository.
     */
    private final GeneralRepositoryStub reposStub;

    /**
     * Reference to the assault parties.
     */
    private final AssaultPartyStub[] assaultPartiesStub;

    /**
     * Museum constructor.
     *
     * @param reposStub general repository.
     * @param assaultPartiesStub assault parties.
     */
    public Museum(GeneralRepositoryStub reposStub, AssaultPartyStub[] assaultPartiesStub) {
        ord = new MuseumClientProxy[SimulPar.M - 1];
        for (int i = 0; i < SimulPar.M - 1; i++)
            ord[i] = null;
        this.nEntities = 0;
        this.reposStub = reposStub;
        this.assaultPartiesStub = assaultPartiesStub;
        canvasInRoom = new int[SimulPar.N];
        for (int i = 0; i < SimulPar.N; i++)
            canvasInRoom[i] = -1;
        roomDistances = new int[SimulPar.N];
        for (int i = 0; i < SimulPar.N; i++)
            roomDistances[i] = -1;
    }

    /**
     * Operation roll a canvas.
     *
     * It is called by an ordinary thief to roll a canvas from a room.
     *
     * @param assaultPartyId assault party id.
     */
    public synchronized void rollACanvas(int assaultPartyId) {
        int ordId;
        ordId = ((MuseumClientProxy) Thread.currentThread()).getOrdinaryThiefId();
        ord[ordId] = (MuseumClientProxy) Thread.currentThread();

        /* Get target room from assault party */
        int targetRoom = assaultPartiesStub[assaultPartyId].getTargetRoom();

        /* Define if thief is holding a canvas */
        assaultPartiesStub[assaultPartyId].setHoldingCanvas(ord[ordId].getOrdinaryThiefId(),
                canvasInRoom[targetRoom] > 0);

        if (canvasInRoom[targetRoom] > 0) {
            canvasInRoom[targetRoom]--;

            /* Get ordinary thief element (position inside the assault party) */
            int elementId = assaultPartiesStub[assaultPartyId]
                    .getThiefElement(ord[ordId].getOrdinaryThiefId());
            /* Set thief holding a canvas */
            reposStub.setAssaultPartyElementCanvas(assaultPartyId, elementId, true);
        }
    }

    /**
     * Get the distance to a room.
     *
     * @param roomId room id.
     * @return distance to a room.
     */
    public synchronized int getRoomDistance(int roomId) {
        return roomDistances[roomId];
    }

    /**
     * Operation set room information (number of canvas and distance to each room).
     *
     * It is called by the ordinary thieves server before the simulation starts.
     *
     * @param canvasInRoom number of canvas in each room.
     * @param roomDistances distance to each room.
     */
    public synchronized void setRoomInfo(int[] canvasInRoom, int[] roomDistances) {
        this.canvasInRoom = canvasInRoom;
        this.roomDistances = roomDistances;
    }

    /**
     * Operation server shutdown.
     *
     * New operation.
     */
    public synchronized void shutdown() {
        nEntities += 1;
        if (nEntities >= SimulPar.E)
            ServerHeistToTheMuseumMuseum.waitConnection = false;
        notifyAll();
    }
}
