package interfaces;

import java.rmi.*;

/**
 * Operational interface of a remote object of type Museum.
 *
 * It provides the functionality to access the Museum.
 */
public interface MuseumInterface extends Remote {
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
  public void rollACanvas(int assaultPartyId, int ordId) throws RemoteException;

  /**
   * Get the distance to a room.
   *
   * @param roomId room id.
   * @return distance to a room.
   * @throws RemoteException if either the invocation of the remote method, or the
   *                         communication with the registry service fails
   */
  public int getRoomDistance(int roomId) throws RemoteException;

  /**
   * Operation set room information (number of canvas and distance to each room).
   *
   * It is called by the ordinary thieves server before the simulation starts.
   *
   * @param canvasInRoom  number of canvas in each room.
   * @param roomDistances distance to each room.
   * @throws RemoteException if either the invocation of the remote method, or the
   *                         communication with the registry service fails
   */
  public void setRoomInfo(int[] canvasInRoom, int[] roomDistances) throws RemoteException;

  /**
   * Operation server shutdown.
   *
   * @throws RemoteException if either the invocation of the remote method, or the
   *                         communication with the registry service fails
   */
  public void shutdown() throws RemoteException;
}
