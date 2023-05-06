package clientSide.stubs;

import commInfra.*;

/**
 * Stub to the concentration site.
 *
 * It instantiates a remote reference to the concentration site. Implementation of a client-server
 * model of type 2 (server replication). Communication is based on a communication channel under the
 * TCP protocol.
 */
public class ConcentrationSiteStub {
    /**
     * Name of the platform where is located the concentration site server.
     */
    private String serverHostName;

    /**
     * Port number for listening to service requests.
     */
    private int serverPortNumb;

    /**
     * Instantiation of a stub to the concentration site.
     *
     * @param serverHostName name of the platform where is located the concentration site server.
     * @param serverPortNumb port number for listening to service requests.
     */

    public ConcentrationSiteStub(String serverHostName, int serverPortNumb) {
        this.serverHostName = serverHostName;
        this.serverPortNumb = serverPortNumb;
    }

    /**
     * Operation am i needed.
     *
     * It is called by the ordinary thief to check if he is needed.
     *
     * @return true if he is needed - false otherwise (to end operations).
     */
    public boolean amINeeded() {
        ClientCom com; // communication channel
        Message outMessage, // outgoing message
                inMessage; // incoming message

        com = new ClientCom(serverHostName, serverPortNumb);
        while (!com.open()) {
            try {
                Thread.sleep((long) (1000));
            } catch (InterruptedException e) {
            }
        }

        return false;
    }

    /**
     * Operation prepare assault party.
     *
     * It is called by the master thief to prepare an assault party.
     *
     * @param assaultPartyId assault party id.
     * @param roomId room id.
     */
    public void prepareAssaultParty(int assaultPartyId, int roomId) {
        ClientCom com; // communication channel
        Message outMessage, // outgoing message
                inMessage; // incoming message

        com = new ClientCom(serverHostName, serverPortNumb);
        while (!com.open()) {
            try {
                Thread.sleep((long) (1000));
            } catch (InterruptedException e) {
            }
        }
    }

    /**
     * Operation prepare excursion.
     *
     * It is called by an ordinary thief to prepare excursion.
     *
     * @return id of the assault party that the thief joined.
     */
    public int prepareExcursion() {
        ClientCom com; // communication channel
        Message outMessage, // outgoing message
                inMessage; // incoming message

        com = new ClientCom(serverHostName, serverPortNumb);
        while (!com.open()) {
            try {
                Thread.sleep((long) (1000));
            } catch (InterruptedException e) {
            }
        }

        return 0;
    }

    /**
     * Operation sum up results.
     *
     * It is called by the master thief to sum up the results of the heist.
     */
    public void sumUpResults() {
        ClientCom com; // communication channel
        Message outMessage, // outgoing message
                inMessage; // incoming message

        com = new ClientCom(serverHostName, serverPortNumb);
        while (!com.open()) {
            try {
                Thread.sleep((long) (1000));
            } catch (InterruptedException e) {
            }
        }
    }

    /**
     * Operation server shutdown.
     */
    public void shutdown() {
        ClientCom com; // communication channel
        Message outMessage, // outgoing message
                inMessage; // incoming message

        com = new ClientCom(serverHostName, serverPortNumb);
        while (!com.open()) {
            try {
                Thread.sleep((long) (1000));
            } catch (InterruptedException e) {
            }
        }
    }
}
