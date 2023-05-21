package interfaces;

import java.rmi.*;

/**
 * Operational interface of a remote object of type GeneralRepository.
 *
 * It provides the functionality to access the General Repository of
 * Information.
 */
public interface GeneralRepositoryInterface extends Remote {
  /**
   * Operation initialization of simulation.
   *
   * @param logFileName name of the logging file.
   * @param maxDis      maximum displacement of the ordinary thieves.
   * @param numPaint    number of paintings in each room.
   * @param roomDist    distance between each room and the outside gathering site.
   * @throws RemoteException if either the invocation of the remote method, or the
   *                         communication with the registry service fails
   */
  public void initSimul(String logFileName, int[] maxDis, int[] numPaint, int[] roomDist) throws RemoteException;

  /**
   * Set the current state of the master thief.
   *
   * @param masterThiefState the current state of the master thief.
   * @throws RemoteException if either the invocation of the remote method, or the
   *                         communication with the registry service fails
   */
  public void setMasterThiefState(int masterThiefState) throws RemoteException;

  /**
   * Set the current state of an ordinary thief.
   *
   * @param thiefId            the thief id.
   * @param ordinaryThiefState the ordinary thief state.
   * @throws RemoteException if either the invocation of the remote method, or the
   *                         communication with the registry service fails
   */
  public void setOrdinaryThiefState(int thiefId, int ordinaryThiefState) throws RemoteException;

  /**
   * Set the current room id of an assault party.
   *
   * @param assaultPartyId     the assault party id.
   * @param assaultPartyRoomId the target room id of the assault party.
   * @throws RemoteException if either the invocation of the remote method, or the
   *                         communication with the registry service fails
   */
  public void setAssaultPartyRoomId(int assaultPartyId, int assaultPartyRoomId) throws RemoteException;

  /**
   * Set the current element id of an assault party.
   *
   * @param assaultPartyId the assault party id.
   * @param elementId      the element id.
   * @param thiefId        thief id.
   * @throws RemoteException if either the invocation of the remote method, or the
   *                         communication with the registry service fails
   */
  public void setAssaultPartyElementId(int assaultPartyId, int elementId, int thiefId) throws RemoteException;

  /**
   * Set the current element position of an assault party.
   *
   * @param assaultPartyId              the assault party id.
   * @param elementId                   the element id.
   * @param assaultPartyElementPosition the target element position of the assault
   *                                    party.
   * @throws RemoteException if either the invocation of the remote method, or the
   *                         communication with the registry service fails
   */
  public void setAssaultPartyElementPosition(int assaultPartyId, int elementId, int assaultPartyElementPosition)
      throws RemoteException;

  /**
   * Set the current element canvas of an assault party.
   *
   * @param assaultPartyId            the assault party id.
   * @param elementId                 the element id.
   * @param assaultPartyElementCanvas the target element canvas of the assault
   *                                  party.
   * @throws RemoteException if either the invocation of the remote method, or the
   *                         communication with the registry service fails
   */
  public void setAssaultPartyElementCanvas(int assaultPartyId, int elementId, boolean assaultPartyElementCanvas)
      throws RemoteException;

  /**
   * Increment canvas stolen by a thief, if he has one and remove the thief from
   * the assault
   * party.
   *
   * @param assaultPartyId assault party id.
   * @param elementId      element id (position of the thief in the party).
   * @throws RemoteException if either the invocation of the remote method, or the
   *                         communication with the registry service fails
   */
  public void endAssaultPartyElementMission(int assaultPartyId, int elementId) throws RemoteException;

  /**
   * Operation server shutdown.
   *
   * @throws RemoteException if either the invocation of the remote method, or the
   *                         communication with the registry service fails
   */
  public void shutdown() throws RemoteException;

}
