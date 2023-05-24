package clientSide.entities;

import java.rmi.*;
import interfaces.*;
import genclass.GenericIO;

/**
 * Master thief thread.
 *
 * It simulates the master thief life cycle.
 * Implementation of a client-server model of type 2 (server replication).
 * Communication is based on remote calls under Java RMI.
 */
public class MasterThief extends Thread {
  /**
   * Master thief state.
   */
  private int masterThiefState;

  /**
   * Reference to the control collection site.
   */
  private final ControlCollectionSiteInterface contColSiteStub;

  /**
   * Reference to the concentration site.
   */
  private final ConcentrationSiteInterface concentSiteStub;

  /**
   * Reference to the assault parties.
   */
  private final AssaultPartyInterface[] assPartStub;

  /**
   * Master thief constructor.
   *
   * @param name            thread name.
   * @param contColSiteStub remote reference to the control collection site.
   * @param concentSiteStub remote reference to the concentration site.
   * @param assPartStub     remote reference to the assault parties.
   */
  public MasterThief(String name, ControlCollectionSiteInterface contColSiteStub,
      ConcentrationSiteInterface concentSiteStub, AssaultPartyInterface[] assPartStub) {
    super(name);
    this.masterThiefState = MasterThiefStates.PLANNING_THE_HEIST;
    this.contColSiteStub = contColSiteStub;
    this.concentSiteStub = concentSiteStub;
    this.assPartStub = assPartStub;
  }

  /**
   * Master thief life cycle.
   */
  @Override
  public void run() {
    char oper;
    int assaultPartyId, roomId;

    startOperations();

    while ((oper = appraiseSit()) != 'E') {
      switch (oper) {
        case 'P':
          assaultPartyId = getAvailableAssaultParty();
          roomId = getAvailableRoom();

          prepareAssaultParty(assaultPartyId, roomId);

          sendAssaultParty(assaultPartyId);

          break;

        case 'R':
          takeARest();

          collectACanvas();

          break;
      }
    }

    sumUpResults();
  }

  /**
   * Master thief start operations.
   *
   * Remote operation.
   */
  private void startOperations() {
    int state = -1;

    try {
      state = contColSiteStub.startOperations();
    } catch (RemoteException e) {
      GenericIO.writelnString("MasterThief remote exception on startOperations: " + e.getMessage());
      System.exit(1);
    }

    masterThiefState = state;
  }

  /**
   * Master thief appraise situation.
   *
   * Remote operation.
   *
   * @return 'P' if we should prepare an assault party - 'R' if we should wait for
   *         the ordinary thieves - 'E' if we should end the operation.
   */
  private char appraiseSit() {
    char oper = 0x00; // return value

    try {
      oper = contColSiteStub.appraiseSit();
    } catch (RemoteException e) {
      GenericIO.writelnString("MasterThief remote exception on appraiseSit: " + e.getMessage());
      System.exit(1);
    }

    return oper;
  }

  /**
   * Master thief get available assault party.
   *
   * Remote operation.
   *
   * @return Assault party id.
   */
  private int getAvailableAssaultParty() {
    int assaultPartyId = -1;

    try {
      assaultPartyId = contColSiteStub.getAvailableAssaultParty();
    } catch (RemoteException e) {
      GenericIO.writelnString("MasterThief remote exception on getAvailableAssaultParty: " + e.getMessage());
      System.exit(1);
    }

    return assaultPartyId;
  }

  /**
   * Master thief get available room.
   *
   * Remote operation.
   *
   * @return Room id.
   */
  private int getAvailableRoom() {
    int roomId = -1;

    try {
      roomId = contColSiteStub.getAvailableRoom();
    } catch (RemoteException e) {
      GenericIO.writelnString("MasterThief remote exception on getAvailableRoom: " + e.getMessage());
      System.exit(1);
    }

    return roomId;
  }

  /**
   * Master thief prepare assault party.
   *
   * Remote operation.
   *
   * @param assaultPartyId Assault party id.
   * @param roomId         Room id.
   */
  private void prepareAssaultParty(int assaultPartyId, int roomId) {
    int state = -1;

    try {
      state = concentSiteStub.prepareAssaultParty(assaultPartyId, roomId);
    } catch (RemoteException e) {
      GenericIO.writelnString("MasterThief remote exception on prepareAssaultParty: " + e.getMessage());
      System.exit(1);
    }

    masterThiefState = state;
  }

  /**
   * Master thief send assault party.
   *
   * Remote operation.
   *
   * @param assaultPartyId Assault party id.
   */
  private void sendAssaultParty(int assaultPartyId) {
    int state = -1;

    try {
      state = assPartStub[assaultPartyId].sendAssaultParty();
    } catch (RemoteException e) {
      GenericIO.writelnString("MasterThief remote exception on sendAssaultParty: " + e.getMessage());
      System.exit(1);
    }

    masterThiefState = state;
  }

  /**
   * Master thief take a rest.
   *
   * Remote operation.
   */
  private void takeARest() {
    int state = -1;

    try {
      state = contColSiteStub.takeARest();
    } catch (RemoteException e) {
      GenericIO.writelnString("MasterThief remote exception on takeARest: " + e.getMessage());
      System.exit(1);
    }

    masterThiefState = state;
  }

  /**
   * Master thief collect a canvas.
   *
   * Remote operation.
   */
  private void collectACanvas() {
    int state = -1;

    try {
      state = contColSiteStub.collectACanvas();
    } catch (RemoteException e) {
      GenericIO.writelnString("MasterThief remote exception on collectACanvas: " + e.getMessage());
      System.exit(1);
    }

    masterThiefState = state;
  }

  /**
   * Master thief sum up results.
   *
   * Remote operation.
   */
  private void sumUpResults() {
    int state = -1;

    try {
      state = concentSiteStub.sumUpResults();
    } catch (RemoteException e) {
      GenericIO.writelnString("MasterThief remote exception on sumUpResults: " + e.getMessage());
      System.exit(1);
    }

    masterThiefState = state;
  }
}
