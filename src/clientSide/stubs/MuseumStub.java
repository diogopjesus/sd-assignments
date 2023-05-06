package clientSide.stubs;

import commInfra.*;

/**
 * Stub to the museum.
 *
 * It instantiates a remote reference to the museum. Implementation of a client-server model of type
 * 2 (server replication). Communication is based on a communication channel under the TCP protocol.
 */
public class MuseumStub {
    /**
     * Name of the platform where is located the museum site server.
     */
    private String serverHostName;

    /**
     * Port number for listening to service requests.
     */
    private int serverPortNumb;

    /**
     * Instantiation of a stub to the museum site.
     *
     * @param serverHostName name of the platform where is located the museum server.
     * @param serverPortNumb port number for listening to service requests.
     */

    public MuseumStub(String serverHostName, int serverPortNumb) {
        this.serverHostName = serverHostName;
        this.serverPortNumb = serverPortNumb;
    }

    /**
     * Operation roll a canvas.
     *
     * It is called by an ordinary thief to roll a canvas from a room.
     *
     * @param assaultPartyId assault party id.
     */
    public void rollACanvas(int assaultPartyId) {
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
