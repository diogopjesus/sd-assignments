package serverSide.entities;

import serverSide.sharedRegions.*;
import clientSide.entities.*;
import commInfra.*;
import genclass.GenericIO;

/**
 * Service provider agent for access to the Concentration Site.
 *
 * Implementation of a client-server model of type 2 (server replication). Communication is based on
 * a communication channel under the TCP protocol.
 */
public class ConcentrationSiteClientProxy extends Thread
        implements MasterThiefCloning, OrdinaryThiefCloning {
    /**
     * Number of instantiated threads.
     */
    private static int nProxy = 0;

    /**
     * Communication channel.
     */
    private ServerCom sconi;

    /**
     * Interface to the concentration site.
     */
    private ConcentrationSiteInterface concentSiteInter;

    /**
     * Master thief state.
     */
    private int masterThiefState;

    /**
     * Ordinary thief identification.
     */
    private int ordinaryThiefId;

    /**
     * Ordinary thief maximum displacement.
     */
    private int ordinaryThiefMaximumDisplacement;

    /**
     * Ordinary thief state.
     */
    private int ordinaryThiefState;

    /**
     * Instantiation of a client proxy.
     *
     * @param sconi communication channel
     * @param concentSiteInter interface to the concentration site
     */
    public ConcentrationSiteClientProxy(ServerCom sconi,
            ConcentrationSiteInterface concentSiteInter) {
        super("ConcentrationSiteProxy_" + ConcentrationSiteClientProxy.getProxyId());
        this.sconi = sconi;
        this.concentSiteInter = concentSiteInter;
    }

    /**
     * Generation of the instantiation identifier.
     *
     * @return instantiation identifier
     */
    private static int getProxyId() {
        Class<?> cl = null; // representation of the ConcentrationSiteClientProxy object in JVM
        int proxyId; // instantiation identifier

        try {
            cl = Class.forName("serverSide.entities.ConcentrationSiteClientProxy");
        } catch (ClassNotFoundException e) {
            GenericIO.writelnString("Data type ConcentrationSiteClientProxy was not found!");
            e.printStackTrace();
            System.exit(1);
        }
        synchronized (cl) {
            proxyId = nProxy;
            nProxy += 1;
        }
        return proxyId;
    }

    /**
     * Get master thief state.
     */
    public int getMasterThiefState() {
        return masterThiefState;
    }

    /**
     * Set master thief state.
     */
    public void setMasterThiefState(int masterThiefState) {
        this.masterThiefState = masterThiefState;
    }

    /**
     * Get ordinary thief identification.
     */
    public int getOrdinaryThiefId() {
        return ordinaryThiefId;
    }

    /**
     * Set ordinary thief identification.
     */
    public void setOrdinaryThiefId(int ordinaryThiefId) {
        this.ordinaryThiefId = ordinaryThiefId;
    }

    /**
     * Get ordinary thief maximum displacement.
     */
    public int getMaximumDisplacement() {
        return ordinaryThiefMaximumDisplacement;
    }

    /**
     * Set ordinary thief maximum displacement.
     */
    public void setMaximumDisplacement(int maximumDisplacement) {
        this.ordinaryThiefMaximumDisplacement = maximumDisplacement;
    }

    /**
     * Get ordinary thief state.
     */
    public int getOrdinaryThiefState() {
        return ordinaryThiefState;
    }

    /**
     * Set ordinary thief state.
     */
    public void setOrdinaryThiefState(int ordinaryThiefState) {
        this.ordinaryThiefState = ordinaryThiefState;
    }

    /**
     * Life cycle of the service provider agent.
     */
    @Override
    public void run() {
        Message inMessage = null, // service request
                outMessage = null; // service reply

        /* service providing */

        inMessage = (Message) sconi.readObject(); // get service request
        try {
            outMessage = concentSiteInter.processAndReply(inMessage); // process it
        } catch (MessageException e) {
            GenericIO.writelnString("Thread " + getName() + ": " + e.getMessage() + "!");
            GenericIO.writelnString(e.getMessageVal().toString());
            System.exit(1);
        }
        sconi.writeObject(outMessage); // send service reply
        sconi.close(); // close the communication channel
    }
}
