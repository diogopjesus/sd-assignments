package interfaces;

import java.rmi.*;

/**
 * Operational interface of a remote object of type ConcentrationSite.
 *
 * It provides the functionality to access the Concentration Site.
 */
public interface ConcentrationSiteInterface extends Remote {
  /**
   * Operation am i needed.
   *
   * It is called by the ordinary thief to check if he is needed.
   *
   * @param ordId    id of the ordinary thief
   * @param ordState state of the ordinary thief
   * @return true if he is needed - false otherwise (to end operations).
   * @throws RemoteException if either the invocation of the remote method, or the
   *                         communication with the registry service fails
   */
  public ReturnBoolean amINeeded(int ordId, int ordState) throws RemoteException;

  /**
   * Operation prepare assault party.
   *
   * It is called by the master thief to prepare an assault party.
   *
   * @param assaultPartyId assault party id.
   * @param roomId         room id.
   * @return master thief state.
   * @throws RemoteException if either the invocation of the remote method, or the
   *                         communication with the registry service fails
   */
  public int prepareAssaultParty(int assaultPartyId, int roomId) throws RemoteException;

  /**
   * Operation prepare excursion.
   *
   * It is called by an ordinary thief to prepare excursion.
   *
   * @param ordId id of the ordinary thief
   * @return id of the assault party that the thief joined and ordinary thief
   *         state.
   * @throws RemoteException if either the invocation of the remote method, or the
   *                         communication with the registry service fails
   */
  public ReturnInt prepareExcursion(int ordId) throws RemoteException;

  /**
   * Operation sum up results.
   *
   * It is called by the master thief to sum up the results of the heist.
   *
   * @return master thief state.
   * @throws RemoteException if either the invocation of the remote method, or the
   *                         communication with the registry service fails
   */
  public int sumUpResults() throws RemoteException;

  /**
   * Operation server shutdown.
   *
   * @throws RemoteException if either the invocation of the remote method, or the
   *                         communication with the registry service fails
   */
  public void shutdown() throws RemoteException;
}
