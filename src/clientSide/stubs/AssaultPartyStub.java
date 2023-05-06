package clientSide.stubs;

import commInfra.*;

/**
 * Stub to the assault party.
 *
 * It instantiates a remote reference to the assault party. Implementation of a client-server model
 * of type 2 (server replication). Communication is based on a communication channel under the TCP
 * protocol.
 */
public class AssaultPartyStub {
    /**
     * Name of the platform where is located the assault party server.
     */
    private String serverHostName;

    /**
     * Port number for listening to service requests.
     */
    private int serverPortNumb;

    /**
     * Instantiation of a stub to the assault party.
     *
     * @param serverHostName name of the platform where is located the assault party server.
     * @param serverPortNumb port number for listening to service requests.
     */

    public AssaultPartyStub(String serverHostName, int serverPortNumb) {
        this.serverHostName = serverHostName;
        this.serverPortNumb = serverPortNumb;
    }

    /**
     * Operation send assault party.
     *
     * It is called by the master thief to send the assault party on mission
     */
    public void sendAssaultParty() {
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
     * Operation crawl in.
     *
     * It is called by the ordinary thief to crawl into the museum.
     *
     * @return true if can continue to crawl in - false otherwise.
     */
    public boolean crawlIn() {
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
     * Operation reverse direction.
     *
     * It is called by the ordinary thief to reverse the crawling direction from in to out.
     */
    public void reverseDirection() {
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
     * Operation crawl out.
     *
     * It is called by the ordinary thief to crawl out of the museum.
     *
     * @return true if can continue to crawl out - false otherwise.
     */
    public boolean crawlOut() {
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
