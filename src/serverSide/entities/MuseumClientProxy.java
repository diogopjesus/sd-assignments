package serverSide.entities;

import serverSide.sharedRegions.*;
import clientSide.entities.*;
import commInfra.*;
import genclass.GenericIO;

public class MuseumClientProxy extends Thread implements OrdinaryThiefCloning {
    /**
     * Number of instantiated threads.
     */
    private static int nProxy = 0;

    /**
     * Communication channel.
     */
    private ServerCom sconi;

    /**
     * Interface to the control collection site.
     */
    private MuseumInterface museumInter;

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
     * @param contColSiteInter interface to the control collection site
     */
    public MuseumClientProxy(ServerCom sconi, MuseumInterface museumInter) {
        super("ControlCollectionSiteProxy_" + MuseumClientProxy.getProxyId());
        this.sconi = sconi;
        this.museumInter = museumInter;
    }

    /**
     * Generation of the instantiation identifier.
     *
     * @return instantiation identifier
     */
    private static int getProxyId() {
        Class<?> cl = null; // representation of the MuseumClientProxy object in JVM
        int proxyId; // instantiation identifier

        try {
            cl = Class.forName("serverSide.entities.MuseumClientProxy");
        } catch (ClassNotFoundException e) {
            GenericIO.writelnString("Data type MuseumClientProxy was not found!");
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
            outMessage = museumInter.processAndReply(inMessage); // process it
        } catch (MessageException e) {
            GenericIO.writelnString("Thread " + getName() + ": " + e.getMessage() + "!");
            GenericIO.writelnString(e.getMessageVal().toString());
            System.exit(1);
        }
        sconi.writeObject(outMessage); // send service reply
        sconi.close(); // close the communication channel
    }
}
