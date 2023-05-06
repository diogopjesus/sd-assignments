package serverSide.entities;

import serverSide.sharedRegions.*;
import clientSide.entities.*;
import commInfra.*;
import genclass.GenericIO;

/**
 * Service provider agent for access to the assault party.
 *
 * Implementation of a client-server model of type 2 (server replication). Communication is based on
 * a communication channel under the TCP protocol.
 */
public class AssaultPartyClientProxy extends Thread
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
     * Interface to the assault party.
     */
    private AssaultPartyInterface assPartInter;

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
     * @param assPartInter interface to the assault party
     */
    public AssaultPartyClientProxy(ServerCom sconi, AssaultPartyInterface assPartInter) {
        super("AssaultPartyProxy_" + AssaultPartyClientProxy.getProxyId());
        this.sconi = sconi;
        this.assPartInter = assPartInter;
    }

    /**
     * Generation of the instantiation identifier.
     *
     * @return instantiation identifier
     */
    private static int getProxyId() {
        Class<?> cl = null; // representation of the AssaultPartyClientProxy object in JVM
        int proxyId; // instantiation identifier

        try {
            cl = Class.forName("serverSide.entities.AssaultPartyClientProxy");
        } catch (ClassNotFoundException e) {
            GenericIO.writelnString("Data type AssaultPartyClientProxy was not found!");
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
            outMessage = assPartInter.processAndReply(inMessage); // process it
        } catch (MessageException e) {
            GenericIO.writelnString("Thread " + getName() + ": " + e.getMessage() + "!");
            GenericIO.writelnString(e.getMessageVal().toString());
            System.exit(1);
        }
        sconi.writeObject(outMessage); // send service reply
        sconi.close(); // close the communication channel
    }
}
