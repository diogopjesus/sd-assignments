package serverSide.objects;

import java.rmi.*;
import interfaces.*;
import serverSide.main.*;
import genclass.GenericIO;

/**
 * Museum.
 *
 * It is responsible to keep in count the number of paintings in each room and
 * is implemented as an implicit monitor.
 * All public methods are executed in mutual exclusion.
 * There are no internal synchronization points.
 * Implementation of a client-server model of type 2 (server replication).
 * Communication is based on Java RMI.
 */
public class Museum implements MuseumInterface {
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
  private final Thread[] ord;

  /**
   * Reference to the general repository.
   */
  private final GeneralRepositoryInterface reposStub;

  /**
   * Reference to the assault parties.
   */
  private final AssaultPartyInterface[] assaultPartiesStub;

  /**
   * Museum constructor.
   *
   * @param reposStub          general repository.
   * @param assaultPartiesStub assault parties.
   */
  public Museum(GeneralRepositoryInterface reposStub, AssaultPartyInterface[] assaultPartiesStub) {
    ord = new Thread[SimulPar.M - 1];
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
   * @param ordId          identification of the ordinary thief.
   * @throws RemoteException if either the invocation of the remote method, or the
   *                         communication with the registry service fails
   */
  @Override
  public synchronized void rollACanvas(int assaultPartyId, int ordId) throws RemoteException {
    ord[ordId] = (Thread) Thread.currentThread();

    /* Get target room from assault party */
    int targetRoom = Integer.MIN_VALUE;
    try {
      targetRoom = assaultPartiesStub[assaultPartyId].getTargetRoom();
    } catch (RemoteException e) {
      GenericIO.writelnString(
          "Ordinary thief " + ordId + " remote exception on rollACanvas - getTargetRoom: " + e.getMessage());
      System.exit(1);
    }

    /* Define if thief is holding a canvas */
    try {
      assaultPartiesStub[assaultPartyId].setHoldingCanvas(ordId, canvasInRoom[targetRoom] > 0);
    } catch (RemoteException e) {
      GenericIO.writelnString("Ordinary thief " + ordId + " remote exception on rollACanvas - setHoldingCanvas: "
          + e.getMessage());
    }

    if (canvasInRoom[targetRoom] > 0) {
      canvasInRoom[targetRoom]--;

      /* Get ordinary thief element (position inside the assault party) */
      int elementId = Integer.MIN_VALUE;

      try {
        elementId = assaultPartiesStub[assaultPartyId].getThiefElement(ordId);
      } catch (RemoteException e) {
        GenericIO.writelnString(
            "Ordinary thief " + ordId + " remote exception on rollACanvas - getThiefElement: "
                + e.getMessage());
        System.exit(1);
      }

      /* Set thief holding a canvas */
      try {
        reposStub.setAssaultPartyElementCanvas(assaultPartyId, elementId, true);
      } catch (RemoteException e) {
        GenericIO.writelnString(
            "Ordinary thief " + ordId + " remote exception on rollACanvas - setAssaultPartyElementCanvas: "
                + e.getMessage());
        System.exit(1);
      }
    }
  }

  /**
   * Get the distance to a room.
   *
   * @param roomId room id.
   * @return distance to a room.
   */
  @Override
  public synchronized int getRoomDistance(int roomId) throws RemoteException {
    return roomDistances[roomId];
  }

  /**
   * Operation set room information (number of canvas and distance to each room).
   *
   * It is called by the ordinary thieves server before the simulation starts.
   *
   * @param canvasInRoom  number of canvas in each room.
   * @param roomDistances distance to each room.
   */
  @Override
  public synchronized void setRoomInfo(int[] canvasInRoom, int[] roomDistances) throws RemoteException {
    this.canvasInRoom = canvasInRoom;
    this.roomDistances = roomDistances;
  }

  /**
   * Operation server shutdown.
   *
   * New operation.
   */
  @Override
  public synchronized void shutdown() throws RemoteException {
    nEntities += 1;
    if (nEntities >= SimulPar.E)
      ServerHeistToTheMuseumMuseum.shutdown();
    notifyAll();
  }
}
