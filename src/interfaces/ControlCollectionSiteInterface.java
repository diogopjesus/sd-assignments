package interfaces;

import java.rmi.*;

/**
 * Operational interface of a remote object of type ControlCollectionSite.
 *
 * It provides the functionality to access the Control Collection Site.
 */
public interface ControlCollectionSiteInterface extends Remote {
    /**
     * Operation start operations.
     *
     * It is called by the master thief to start the operations.
     * 
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry service fails
     */
    public int startOperations() throws RemoteException;

    /**
     * Operation appraise situation.
     *
     * It is called by the master thief to appraise the situation.
     *
     * @return 'P' if we should prepare an assault party - 'R' if we should wait for
     *         the ordinary thieves - 'E' if we should end the operation.
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry service fails
     */
    public char appraiseSit() throws RemoteException;

    /**
     * Operation take a rest.
     *
     * It is called by the master thief to wait until a new thief has arrived the
     * control collection site.
     * 
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry service fails
     */
    public int takeARest() throws RemoteException;

    /**
     * Operation hand a canvas.
     *
     * It is called by the ordinary thief to hand a canvas to the master thief. The
     * ordinary thief blocks until he is called by the master thief, meaning his
     * canvas was collected.
     *
     * @param assaultPartyId assault party id.
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry service fails
     */
    public void handACanvas(int assaultPartyId, int ordId) throws RemoteException;

    /**
     * Operation collect a canvas.
     *
     * It is called by the master thief to collect the canvas of the next ordinary
     * thief in queue. After collecting the canvas, he wakes up the ordinary thief
     * to proceed operations.
     * 
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry service fails
     */
    public int collectACanvas() throws RemoteException;

    /**
     * Get the id of an available assault party.
     *
     * @return Id of an available assault party if there is one available, -1
     *         otherwise.
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry service fails
     */
    public int getAvailableAssaultParty() throws RemoteException;

    /**
     * Get the id of an available assault party.
     *
     * @return Id of an available assault party if there is one available, -1
     *         otherwise.
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry service fails
     */
    public int getAvailableRoom() throws RemoteException;

    /**
     * Associate a thief to an assault party.
     *
     * @param thiefId        thief id.
     * @param assaultPartyId assault party id.
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry service fails
     */
    public void setThiefToParty(int thiefId, int assaultPartyId) throws RemoteException;

    /**
     * Operation server shutdown.
     *
     * New operation.
     *
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry service fails
     */
    public void shutdown() throws RemoteException;
}