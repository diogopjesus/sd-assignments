package clientSide.entities;

/**
 * Master thief cloning.
 *
 * It specifies his own attributes. Implementation of a client-server model of type 2 (server
 * replication). Communication is based on a communication channel under the TCP protocol.
 */
public interface MasterThiefCloning {
    /**
     * Get master thief state.
     *
     * @return Master thief state.
     */
    public int getMasterThiefState();

    /**
     * Set master thief state.
     *
     * @param masterThiefState Master thief state.
     */
    public void setMasterThiefState(int masterThiefState);
}
