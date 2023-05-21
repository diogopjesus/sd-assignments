package serverSide.objects;

import java.rmi.*;
import interfaces.*;
import serverSide.main.*;
import clientSide.entities.*;
import commInfra.*;
import genclass.GenericIO;

/**
 * Concentration Site.
 *
 * It is responsible to keep in count the number of thieves waiting to be
 * assigned to an assault and is implemented as an implicit monitor.
 * All public methods are executed in mutual exclusion.
 * There are 3 synchronization points: an array of blocking points, one per each
 * ordinary thief, where they wait to be called to join an assault party; a
 * blocking point where the master thief waits for all ordinary thieves to join
 * an assault party; and a blocking point where the master thief waits for all
 * ordinary thieves to be present at the concentration site to end the
 * operations.
 * Implementation of a client-server model of type 2 (server replication).
 * Communication is based on Java RMI.
 */
public class ConcentrationSite implements ConcentrationSiteInterface {
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
   * Flag to indicate that there is not a thief to be called.
   */
  private final int NOT_A_THIEF = -1;

  /**
   * Reference to the general repository.
   */
  private final GeneralRepositoryInterface reposStub;

  /**
   * Reference to the control collection site.
   */
  private final ControlCollectionSiteInterface contColSiteStub;

  /**
   * Reference to the assault parties.
   */
  private final AssaultPartyInterface[] assaultPartiesStub;

  /**
   * Reference to the museum.
   */
  private final MuseumInterface museumStub;

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
   * @param reposStub          general repository.
   * @param contColSiteStub    control collection site.
   * @param assaultPartiesStub assault parties.
   * @param museumStub         museum.
   * @throws RemoteException if either the invocation of the remote method, or the
   *                         communication with the registry service fails
   */
  public ConcentrationSite(GeneralRepositoryInterface reposStub,
      ControlCollectionSiteInterface contColSiteStub, AssaultPartyInterface[] assaultPartiesStub,
      MuseumInterface museumStub) {
    masState = -1;
    ordStates = new int[SimulPar.M - 1];
    for (int i = 0; i < SimulPar.M - 1; i++)
      ordStates[i] = -1;
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
   * @throws RemoteException if either the invocation of the remote method, or the
   *                         communication with the registry service fails
   */
  @Override
  public synchronized ReturnBoolean amINeeded(int ordId, int ordState) throws RemoteException {
    /* Set ordinary thief state to concentration site */
    if (ordState != OrdinaryThiefStates.CONCENTRATION_SITE) {
      ordStates[ordId] = OrdinaryThiefStates.CONCENTRATION_SITE;
      reposStub.setOrdinaryThiefState(ordId, OrdinaryThiefStates.CONCENTRATION_SITE);
    }

    /* Add thief to the waiting queue */
    try {
      waitingThieves.write(ordId);
    } catch (MemException e) {
      GenericIO.writelnString("Instantiation of waiting FIFO failed: " + e.getMessage());
      System.exit(1);
    }
    numberOfWaitingThieves++;

    /* Notify master thief that a new thief has entered the waiting queue */
    notifyAll();

    /* Wait until thief is called to join a party or to end operations */
    while (calledThiefId != ordId && !endOfOperations) {
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    return new ReturnBoolean(!endOfOperations, ordStates[ordId]);
  }

  /**
   * Operation prepare assault party.
   *
   * It is called by the master thief to prepare an assault party.
   *
   * @param assaultPartyId assault party id.
   * @param roomId         room id.
   * @throws RemoteException if either the invocation of the remote method, or the
   *                         communication with the registry service fails
   */
  @Override
  public synchronized int prepareAssaultParty(int assaultPartyId, int roomId) throws RemoteException {
    /* Block until there is a sufficient number of ordinary thieves available */
    while (numberOfWaitingThieves < SimulPar.K) {
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    /* Update master thief state to assembling a group */
    masState = MasterThiefStates.ASSEMBLING_A_GROUP;
    reposStub.setMasterThiefState(MasterThiefStates.ASSEMBLING_A_GROUP);

    /* Update available assault party id */
    availableAssaultPartyId = assaultPartyId;

    /* Update assault party target room */
    assaultPartiesStub[assaultPartyId].setTargetRoom(roomId);
    assaultPartiesStub[assaultPartyId]
        .setTargetRoomDistance(museumStub.getRoomDistance(roomId));
    reposStub.setAssaultPartyRoomId(assaultPartyId, roomId);

    /*
     * In this implementation, the master thief calls an ordinary thief to join a
     * party. Then,
     * the ordinary thief calls the next ordinary thief to join the party. The last
     * ordinary
     * thief calls the master thief to prepare the next assault party.
     */

    /* Get the id of the first ordinary thief to join the assault party */
    try {
      calledThiefId = waitingThieves.read();
      numberOfWaitingThieves--;
    } catch (MemException e) {
      GenericIO.writelnString(
          "Retrieval of customer id from waiting FIFO failed: " + e.getMessage());
      System.exit(1);
    }

    /* Notify ordinary thief that he was called to join a party */
    notifyAll();

    /* Wait until all ordinary thieves have entered the assault party */
    while (calledThiefId != NOT_A_THIEF) {
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    return masState;
  }

  /**
   * Operation prepare excursion.
   *
   * It is called by an ordinary thief to prepare excursion.
   *
   * @return id of the assault party that the thief joined.
   * @throws RemoteException if either the invocation of the remote method, or the
   *                         communication with the registry service fails
   */
  @Override
  public synchronized ReturnInt prepareExcursion(int ordId) throws RemoteException {
    /* Store info on Control site to associate a thief to an assault party */
    contColSiteStub.setThiefToParty(ordId, availableAssaultPartyId);

    /* Join the available assault party */
    assaultPartiesStub[availableAssaultPartyId].joinAssaultParty(ordId);

    /* Check if assault party is full */
    if (assaultPartiesStub[availableAssaultPartyId].isFull())
      /* Clear called thief */
      calledThiefId = NOT_A_THIEF;
    else
      /* Get the id of a new ordinary thief */
      try {
        calledThiefId = waitingThieves.read();
        numberOfWaitingThieves--;
      } catch (MemException e) {
        GenericIO.writelnString(
            "Retrieval of customer id from waiting FIFO failed: " + e.getMessage());
        System.exit(1);
      }

    /* Update ordinary thief state */
    ordStates[ordId] = OrdinaryThiefStates.CRAWLING_INWARDS;
    reposStub.setAssaultPartyElementId(
        availableAssaultPartyId, assaultPartiesStub[availableAssaultPartyId].getThiefElement(ordId),
        ordId);

    /*
     * Notify a new thief to join the assault party, or the master thief the party
     * is full.
     */
    notifyAll();

    return new ReturnInt(availableAssaultPartyId, ordStates[ordId]);
  }

  /**
   * Operation sum up results.
   *
   * It is called by the master thief to sum up the results of the heist.
   *
   * @throws RemoteException if either the invocation of the remote method, or the
   *                         communication with the registry service fails
   */
  @Override
  public synchronized int sumUpResults() throws RemoteException {
    /* Block until there is a sufficient number of ordinary thieves available */
    while (numberOfWaitingThieves < SimulPar.M - 1) {
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    masState = MasterThiefStates.PRESENTING_THE_REPORT;
    reposStub.setMasterThiefState(MasterThiefStates.PRESENTING_THE_REPORT);

    /* Signal end of operations */
    endOfOperations = true;

    /* Notify all ordinary thieves that the heist has ended */
    notifyAll();

    return masState;
  }

  /**
   * Operation server shutdown.
   *
   * @throws RemoteException if either the invocation of the remote method, or the
   *                         communication with the registry service fails
   */
  @Override
  public synchronized void shutdown() throws RemoteException {
    nEntities += 1;
    if (nEntities >= SimulPar.E)
      ServerHeistToTheMuseumConcentrationSite.shutdown();
    notifyAll();
  }
}
