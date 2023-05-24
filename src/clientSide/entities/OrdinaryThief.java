package clientSide.entities;

import java.rmi.*;
import interfaces.*;
import genclass.GenericIO;

/**
 * Ordinary thief thread.
 *
 * It simulates the ordinary thief life cycle.
 * Implementation of a client-server model of type 2 (server replication).
 * Communication is based on remote calls under Java RMI.
 */
public class OrdinaryThief extends Thread {
  /**
   * Ordinary thief identification.
   */
  private int ordinaryThiefId;

  /**
   * Maximum displacement.
   */
  private int maximumDisplacement;

  /**
   * Ordinary thief state.
   */
  private int ordinaryThiefState;

  /**
   * Reference to the control collection site.
   */
  private ControlCollectionSiteInterface contColSiteStub;

  /**
   * Reference to the concentration site.
   */
  private ConcentrationSiteInterface concentSiteStub;

  /**
   * Reference to the assault parties.
   */
  private AssaultPartyInterface[] assPartStub;

  /**
   * Reference to the museum.
   */
  private MuseumInterface museumStub;

  /**
   * Ordinary thief constructor.
   *
   * @param name                thread name.
   * @param ordinaryThiefId     ordinary thief identification.
   * @param maximumDisplacement maximum displacement.
   * @param contColSiteStub     remote reference to the control collection site.
   * @param concentSiteStub     remote reference to the concentration site.
   * @param assPartStub         remote reference to the assault parties.
   * @param museumStub          remote reference to the museum.
   */
  public OrdinaryThief(String name, int ordinaryThiefId, int maximumDisplacement,
      ControlCollectionSiteInterface contColSiteStub, ConcentrationSiteInterface concentSiteStub,
      AssaultPartyInterface[] assPartStub, MuseumInterface museumStub) {
    super(name);
    this.ordinaryThiefId = ordinaryThiefId;
    this.maximumDisplacement = maximumDisplacement;
    this.ordinaryThiefState = OrdinaryThiefStates.CONCENTRATION_SITE;
    this.contColSiteStub = contColSiteStub;
    this.concentSiteStub = concentSiteStub;
    this.assPartStub = assPartStub;
    this.museumStub = museumStub;
  }

  /**
   * Ordinary thief life cycle.
   */
  @Override
  public void run() {
    int assaultPartyId;

    while (amINeeded()) {
      assaultPartyId = prepareExcursion();

      while (crawlIn(assaultPartyId))
        ;

      rollACanvas(assaultPartyId);

      reverseDirection(assaultPartyId);

      while (crawlOut(assaultPartyId))
        ;

      handACanvas(assaultPartyId);
    }
  }

  /**
   * Ordinary thief is needed.
   *
   * Remote operation.
   *
   * @return true, if the ordinary thief is needed - false, otherwise.
   */
  private boolean amINeeded() {
    ReturnBoolean ret = null;

    try {
      ret = concentSiteStub.amINeeded(ordinaryThiefId, ordinaryThiefState);
    } catch (RemoteException e) {
      GenericIO.writelnString(
          "Ordinary thief " + ordinaryThiefId + " remote exception on amINeeded: " + e.getMessage());
      System.exit(1);
    }

    ordinaryThiefState = ret.getIntStateVal();

    return ret.getBooleanVal();
  }

  /**
   * Ordinary thief prepares the excursion.
   *
   * Remote operation.
   *
   * @return assault party id.
   */
  private int prepareExcursion() {
    ReturnInt ret = null;

    try {
      ret = concentSiteStub.prepareExcursion(ordinaryThiefId);
    } catch (RemoteException e) {
      GenericIO.writelnString(
          "Ordinary thief " + ordinaryThiefId + " remote exception on prepareExcursion: " + e.getMessage());
      System.exit(1);
    }

    ordinaryThiefState = ret.getIntStateVal();

    return ret.getIntVal();
  }

  /**
   * Ordinary thief crawls in.
   *
   * Remote operation.
   *
   * @param assaultPartyId assault party identification.
   * @return true, if the ordinary thief can crawl in - false, otherwise.
   */
  private boolean crawlIn(int assaultPartyId) {
    ReturnBoolean ret = null;

    try {
      ret = assPartStub[assaultPartyId].crawlIn(ordinaryThiefId, maximumDisplacement);
    } catch (RemoteException e) {
      GenericIO.writelnString(
          "Ordinary thief " + ordinaryThiefId + " remote exception on crawlIn: " + e.getMessage());
      System.exit(1);
    }

    ordinaryThiefState = ret.getIntStateVal();

    return ret.getBooleanVal();
  }

  /**
   * Ordinary thief rolls a canvas.
   *
   * Remote operation.
   *
   * @param assaultPartyId assault party identification.
   */
  private void rollACanvas(int assaultPartyId) {
    try {
      museumStub.rollACanvas(assaultPartyId, ordinaryThiefId);
    } catch (RemoteException e) {
      GenericIO.writelnString(
          "Ordinary thief " + ordinaryThiefId + " remote exception on rollACanvas: " + e.getMessage());
      System.exit(1);
    }
  }

  /**
   * Ordinary thief reverses direction.
   *
   * Remote operation.
   *
   * @param assaultPartyId assault party identification.
   */
  private void reverseDirection(int assaultPartyId) {
    int state = -1;

    try {
      state = assPartStub[assaultPartyId].reverseDirection(ordinaryThiefId);
    } catch (RemoteException e) {
      GenericIO.writelnString(
          "Ordinary thief " + ordinaryThiefId + " remote exception on reverseDirection: " + e.getMessage());
      System.exit(1);
    }

    ordinaryThiefState = state;
  }

  /**
   * Ordinary thief crawls out.
   *
   * Remote operation.
   *
   * @param assaultPartyId assault party identification.
   * @return true, if the ordinary thief can crawl out - false, otherwise.
   */
  private boolean crawlOut(int assaultPartyId) {
    ReturnBoolean ret = null;

    try {
      ret = assPartStub[assaultPartyId].crawlOut(ordinaryThiefId, maximumDisplacement);
    } catch (RemoteException e) {
      GenericIO.writelnString(
          "Ordinary thief " + ordinaryThiefId + " remote exception on crawlOut: " + e.getMessage());
      System.exit(1);
    }

    ordinaryThiefState = ret.getIntStateVal();

    return ret.getBooleanVal();
  }

  /**
   * Ordinary thief hands a canvas.
   *
   * Remote operation.
   *
   * @param assaultPartyId assault party identification.
   */
  private void handACanvas(int assaultPartyId) {
    try {
      contColSiteStub.handACanvas(assaultPartyId, ordinaryThiefId);
    } catch (RemoteException e) {
      GenericIO.writelnString(
          "Ordinary thief " + ordinaryThiefId + " remote exception on handACanvas: " + e.getMessage());
      System.exit(1);
    }
  }
}
