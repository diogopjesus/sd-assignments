package serverSide.objects;

import java.rmi.*;
import interfaces.*;
import serverSide.main.*;
import clientSide.entities.*;
import commInfra.*;
import genclass.GenericIO;

/**
 * Control Collection Site.
 *
 * It is responsible to keep in count the number of canvas collected, the rooms
 * that are empty known by the master thief and is implemented as an implicit
 * monitor.
 * All public methods are executed in mutual exclusion.
 * There are two internal synchronization points: a single blocking point for
 * the master thief, where he waits for the arrival of an ordinary thief from
 * mission; an array of blocking points, one per each ordinary thief, where he
 * is waits to hand canvas.
 * Implementation of a client-server model of type 2 (server replication).
 * Communication is based on Java RMI.
 */
public class ControlCollectionSite implements ControlCollectionSiteInterface {
  /**
   * State of the ordinary thieves.
   */
  private final int[] ordStates;

  /**
   * State of the master thief.
   */
  private int masState;

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
  private GeneralRepositoryInterface reposStub;

  /**
   * Reference to the assault parties.
   */
  private AssaultPartyInterface[] assaultPartiesStub;

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
   * @param reposStub          general repository.
   * @param assaultPartiesStub assault parties.
   */
  public ControlCollectionSite(GeneralRepositoryInterface reposStub,
      AssaultPartyInterface[] assaultPartiesStub) {
    masState = -1;
    ordStates = new int[SimulPar.M - 1];
    for (int i = 0; i < SimulPar.M - 1; i++)
      ordStates[i] = -1;
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
  @Override
  public synchronized int startOperations() throws RemoteException {
    masState = MasterThiefStates.DECIDING_WHAT_TO_DO;
    return masState;
  }

  /**
   * Operation appraise situation.
   *
   * It is called by the master thief to appraise the situation.
   *
   * @return 'P' if we should prepare an assault party - 'R' if we should wait for
   *         the ordinary thieves - 'E' if we should end the operation.
   */
  @Override
  public synchronized char appraiseSit() throws RemoteException {
    // Wait arrival of ordinary thieves if:
    // - All assault parties are in mission;
    // - there is one assault party in mission, the number of empty rooms is equal
    // to N-1, and
    // the assault party in mission is targeting the non-empty room;
    // - There is one assault party in mission, and all rooms are empty.

    int targetRoom = NOT_A_ROOM; // Target room of the assault party in mission
    int assPartOnMisId = getAssaultPartyOnMissionId(); // Id of the ass part in mission (-1 if there is none)
    if (assPartOnMisId != NOT_A_PARTY) {
      try {
        targetRoom = assaultPartiesStub[assPartOnMisId].getTargetRoom();
      } catch (RemoteException e) {
        GenericIO.writelnString("MasterThief remote exception on appraiseSit - getTargetRoom: " + e.getMessage());
        System.exit(1);
      }
    }

    if ((availableAssaultParties() == 0)
        || ((availableAssaultParties() == ((SimulPar.M - 1) / SimulPar.K) - 1)
            && (availableRooms() == 1)
            && (targetRoom == getAvailableRoom()))
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
   * It is called by the master thief to wait until a new thief has arrived the
   * control collection
   * site.
   */
  @Override
  public synchronized int takeARest() throws RemoteException {
    int masState = MasterThiefStates.WAITING_FOR_GROUP_ARRIVAL;
    try {
      reposStub.setMasterThiefState(MasterThiefStates.WAITING_FOR_GROUP_ARRIVAL);
    } catch (RemoteException e) {
      GenericIO.writelnString("Remote exception on takeARest - setMasterThiefState: " + e.getMessage());
      System.exit(1);
    }

    while (readyToHandCanvas == 0) {
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    return masState;
  }

  /**
   * Operation hand a canvas.
   *
   * It is called by the ordinary thief to hand a canvas to the master thief. The
   * ordinary thief
   * blocks until he is called by the master thief, meaning his canvas was
   * collected.
   *
   * @param assaultPartyId assault party id.
   */
  @Override
  public synchronized void handACanvas(int assaultPartyId, int ordId) throws RemoteException {
    /* Add thief to the waiting queue */
    try {
      handCanvasQueue.write(ordId);
    } catch (MemException e) {
      GenericIO.writelnString("Instantiation of waiting FIFO failed: " + e.getMessage());
      System.exit(1);
    }
    readyToHandCanvas++;

    /* Signal if he is holding a canvas or not */
    try {
      holdingCanvas[ordId] = assaultPartiesStub[assaultPartyId].isHoldingCanvas(ordId);
    } catch (RemoteException e) {
      GenericIO.writelnString("OrdinaryThief " + ordId + " remote exception on handACanvas - isHoldingCanvas: "
          + e.getMessage());
      System.exit(1);
    }

    /* Notify master thief that is ready to hand a canvas */
    notifyAll();

    while (!readyToCollectCanvas[ordId]) {
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    readyToCollectCanvas[ordId] = false;
  }

  /**
   * Operation collect a canvas.
   *
   * It is called by the master thief to collect the canvas of the next ordinary
   * thief in queue.
   * After collecting the canvas, he wakes up the ordinary thief to proceed
   * operations.
   */
  @Override
  public synchronized int collectACanvas() throws RemoteException {
    int ordinaryThiefId = NOT_A_THIEF;
    try {
      ordinaryThiefId = handCanvasQueue.read();
    } catch (MemException e) {
      GenericIO.writelnString("Instantiation of waiting FIFO failed: " + e.getMessage());
      System.exit(1);
    }
    readyToHandCanvas--;

    int assaultPartyId = thievesParties[ordinaryThiefId];

    /* Collect canvas */
    if (!holdingCanvas[ordinaryThiefId]) {
      try {
        /* Set room empty */
        roomHasCanvas[assaultPartiesStub[assaultPartyId].getTargetRoom()] = false;
      } catch (RemoteException e) {
        GenericIO.writelnString(
            "MasterThief remote exception on collectACanvas - getTargetRoom: "
                + e.getMessage());
        System.exit(1);
      }
    }

    int element = -1;
    try {
      element = assaultPartiesStub[assaultPartyId].getThiefElement(ordinaryThiefId);
    } catch (RemoteException e) {
      GenericIO.writelnString(
          "MasterThief remote exception on collectACanvas - getThiefElement: "
              + e.getMessage());
      System.exit(1);
    }

    /* Disassociate thief to the assault party */
    try {
      assaultPartiesStub[assaultPartyId].quitAssaultParty(ordinaryThiefId);
    } catch (RemoteException e) {
      GenericIO.writelnString(
          "MasterThief remote exception on collectACanvas - quitAssaultParty: "
              + e.getMessage());
      System.exit(1);
    }
    setThiefToParty(ordinaryThiefId, NOT_A_PARTY);

    /* Notify thief that their canvas was colleted */
    readyToCollectCanvas[ordinaryThiefId] = true;
    notifyAll();

    masState = MasterThiefStates.DECIDING_WHAT_TO_DO;
    try {
      reposStub.endAssaultPartyElementMission(assaultPartyId, element);
    } catch (RemoteException e) {
      GenericIO.writelnString(
          "MasterThief remote exception on collectACanvas - endAssaultPartyElementMission: "
              + e.getMessage());
      System.exit(1);
    }

    return masState;
  }

  /**
   * Get the id of an available assault party.
   *
   * @return Id of an available assault party if there is one available, -1
   *         otherwise.
   */
  @Override
  public synchronized int getAvailableAssaultParty() throws RemoteException {
    int availableAssaultParty = NOT_A_PARTY;

    for (int i = 0; i < ((SimulPar.M - 1) / SimulPar.K); i++)
      try {
        if (assaultPartiesStub[i].isAvailable()) {
          availableAssaultParty = i;
          break;
        }
      } catch (RemoteException e) {
        GenericIO.writelnString(
            "MasterThief remote exception on getAvailableAssaultParty - isAvailable: " + e.getMessage());
        System.exit(1);
      }

    return availableAssaultParty;
  }

  /**
   * Get the id of an available assault party.
   *
   * @return Id of an available assault party if there is one available, -1
   *         otherwise.
   */
  @Override
  public synchronized int getAvailableRoom() throws RemoteException {
    int availableRoom = NOT_A_ROOM;

    for (int i = 0; i < SimulPar.N; i++)
      if (roomHasCanvas[i]) {
        /* Check if any assault party is targeting that room */
        int assPart = 0;
        for (assPart = 0; assPart < (SimulPar.M - 1) / SimulPar.K; assPart++)
          try {
            if (assaultPartiesStub[assPart].getTargetRoom() == i) {
              break;
            }
          } catch (RemoteException e) {
            GenericIO.writelnString(
                "MasterThief remote exception on getAvailableRoom - getTargetRoom: " + e.getMessage());
            System.exit(1);
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
   * Associate a thief to an assault party.
   *
   * @param thiefId        thief id.
   * @param assaultPartyId assault party id.
   */
  @Override
  public synchronized void setThiefToParty(int thiefId, int assaultPartyId) throws RemoteException {
    this.thievesParties[thiefId] = assaultPartyId;
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
      ServerHeistToTheMuseumControlCollectionSite.shutdown();
    notifyAll();
  }

  /**
   * Get the number of available assault parties.
   *
   * @return Number of available assault parties.
   */
  private int availableAssaultParties() {
    int availableAssaultParties = 0;

    for (int i = 0; i < ((SimulPar.M - 1) / SimulPar.K); i++)
      try {
        if (assaultPartiesStub[i].isAvailable())
          availableAssaultParties++;
      } catch (RemoteException e) {
        GenericIO.writelnString(
            "ControlCollectionSite remote exception on availableAssaultParties - isAvailable: "
                + e.getMessage());
        System.exit(1);
      }

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
   * Get the id of an assault party on mission.
   *
   * @return Id of an assault party on mission if there is one available, -1
   *         otherwise.
   */
  private int getAssaultPartyOnMissionId() {
    int assaultPartyOnMissionId = -1;

    for (int i = 0; i < ((SimulPar.M - 1) / SimulPar.K); i++)
      try {
        if (!assaultPartiesStub[i].isAvailable()) {
          assaultPartyOnMissionId = i;
          break;
        }
      } catch (RemoteException e) {
        GenericIO.writelnString(
            "ControlCollectionSite remote exception on getAssaultPartyOnMissionId - isAvailable: "
                + e.getMessage());
        System.exit(1);
      }

    return assaultPartyOnMissionId;
  }
}
