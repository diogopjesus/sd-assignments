package interfaces;

import java.rmi.*;

/**
 * Operational interface of a remote object of type AssaultParty.
 *
 * It provides the functionality to access the Assault Party.
 */
public interface AssaultPartyInterface extends Remote {

  /**
   * Operation send assault party.
   *
   * It is called by the master thief to send the assault party on mission
   *
   * @throws RemoteException if either the invocation of the remote method, or the
   *                         communication with the registry service fails
   */
  public int sendAssaultParty() throws RemoteException;

  /**
   * Operation crawl in.
   *
   * It is called by the ordinary thief to crawl into the museum.
   *
   * @return true if can continue to crawl in - false otherwise.
   * @throws RemoteException if either the invocation of the remote method, or the
   *                         communication with the registry service fails
   */
  public ReturnBoolean crawlIn(int ordId, int maxDis) throws RemoteException;

  /**
   * Operation reverse direction.
   *
   * It is called by the ordinary thief to reverse the crawling direction from in
   * to out.
   *
   * @throws RemoteException if either the invocation of the remote method, or the
   *                         communication with the registry service fails
   */
  public int reverseDirection(int ordId) throws RemoteException;

  /**
   * Operation crawl out.
   *
   * It is called by the ordinary thief to crawl out of the museum.
   *
   * @return true if can continue to crawl out - false otherwise.
   * @throws RemoteException if either the invocation of the remote method, or the
   *                         communication with the registry service fails
   */
  public ReturnBoolean crawlOut(int ordId, int maxDis) throws RemoteException;

  /**
   * Set the assault party target room for mission.
   *
   * @param targetRoom target room id.
   * @throws RemoteException if either the invocation of the remote method, or the
   *                         communication with the registry service fails
   */
  public void setTargetRoom(int targetRoom) throws RemoteException;

  /**
   * Get the assault party target room for mission.
   *
   * @return Target room.
   * @throws RemoteException if either the invocation of the remote method, or the
   *                         communication with the registry service fails
   */
  public int getTargetRoom() throws RemoteException;

  /**
   * Set the assault party target room distance for mission.
   *
   * @param targetRoomDistance target room distance.
   * @throws RemoteException if either the invocation of the remote method, or the
   *                         communication with the registry service fails
   */
  public void setTargetRoomDistance(int targetRoomDistance) throws RemoteException;

  /**
   * Check if the assault party is available.
   *
   * @return true if the assault party is available, false otherwise.
   * @throws RemoteException if either the invocation of the remote method, or the
   *                         communication with the registry service fails
   */
  public boolean isAvailable() throws RemoteException;

  /**
   * Check if the assault party is full.
   *
   * @return true if the assault party is full, false otherwise.
   * @throws RemoteException if either the invocation of the remote method, or the
   *                         communication with the registry service fails
   */
  public boolean isFull() throws RemoteException;

  /**
   * Add new thief to the assault party.
   *
   * @param thiefId thief id.
   * @throws RemoteException if either the invocation of the remote method, or the
   *                         communication with the registry service fails
   */
  public void joinAssaultParty(int thiefId) throws RemoteException;

  /**
   * Remove thief from the assault party.
   *
   * @param thiefId thief id.
   * @throws RemoteException if either the invocation of the remote method, or the
   *                         communication with the registry service fails
   */
  public void quitAssaultParty(int thiefId) throws RemoteException;

  /**
   * Set thief canvas state.
   *
   * @param thiefId thief id.
   * @param canvas  true if is holding a canvas - false, otherwise.
   * @throws RemoteException if either the invocation of the remote method, or the
   *                         communication with the registry service fails
   */
  public void setHoldingCanvas(int thiefId, boolean canvas) throws RemoteException;

  /**
   * Check wether a thief is holding a canvas.
   *
   * @param thiefId thief id.
   * @return True if is holding a canvas, false otherwise.
   * @throws RemoteException if either the invocation of the remote method, or the
   *                         communication with the registry service fails
   */
  public boolean isHoldingCanvas(int thiefId) throws RemoteException;

  /**
   * Get the element (position inside the assault party) of the thief with the
   * given id.
   *
   * @param thiefId thief id.
   * @return thief element.
   * @throws RemoteException if either the invocation of the remote method, or the
   *                         communication with the registry service fails
   */
  public int getThiefElement(int thiefId) throws RemoteException;

  /**
   * Operation server shutdown.
   *
   * @throws RemoteException if either the invocation of the remote method, or the
   *                         communication with the registry service fails
   */
  public void shutdown() throws RemoteException;
}
