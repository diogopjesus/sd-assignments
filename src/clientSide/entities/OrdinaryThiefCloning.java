package clientSide.entities;

/**
 * Ordinary thief cloning.
 *
 * It specifies his own attributes. Implementation of a client-server model of type 2 (server
 * replication). Communication is based on a communication channel under the TCP protocol.
 */
public interface OrdinaryThiefCloning {
    /**
     * Get the ordinary thief id.
     *
     * @return ordinary thief id.
     */
    public int getOrdinaryThiefId();

    /**
     * Set the ordinary thief id.
     *
     * @param ordinaryThiefId ordinary thief id.
     */
    public void setOrdinaryThiefId(int ordinaryThiefId);

    /**
     * Get the maximum displacement.
     *
     * @return maximum displacement.
     */
    public int getMaximumDisplacement();

    /**
     * Set the maximum displacement.
     *
     * @param maximumDisplacement maximum displacement.
     */
    public void setMaximumDisplacement(int maximumDisplacement);

    /**
     * Get the ordinary thief state.
     *
     * @return ordinary thief state.
     */
    public int getOrdinaryThiefState();

    /**
     * Set the ordinary thief state.
     *
     * @param ordinaryThiefState ordinary thief state.
     */
    public void setOrdinaryThiefState(int ordinaryThiefState);
}
