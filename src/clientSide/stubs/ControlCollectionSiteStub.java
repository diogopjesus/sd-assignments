package clientSide.stubs;

import commInfra.*;

/**
 * Stub to the control collection site.
 *
 * It instantiates a remote reference to the control collection site. Implementation of a
 * client-server model of type 2 (server replication). Communication is based on a communication
 * channel under the TCP protocol.
 */
public class ControlCollectionSiteStub {
    /**
     * Name of the platform where is located the control collection site server.
     */
    private String serverHostName;

    /**
     * Port number for listening to service requests.
     */
    private int serverPortNumb;

    /**
     * Instantiation of a stub to the control collection site.
     *
     * @param serverHostName name of the platform where is located the control collection site
     *        server.
     * @param serverPortNumb port number for listening to service requests.
     */

    public ControlCollectionSiteStub(String serverHostName, int serverPortNumb) {
        this.serverHostName = serverHostName;
        this.serverPortNumb = serverPortNumb;
    }

    /**
     * Operation start operations.
     *
     * It is called by the master thief to start the operations.
     */
    public void startOperations() {
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

        outMessage = new Message(MessageType.STAOPE);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

    }

    /**
     * Operation appraise situation.
     *
     * It is called by the master thief to appraise the situation.
     *
     * @return 'P' if we should prepare an assault party - 'R' if we should wait for the ordinary
     *         thieves - 'E' if we should end the operation.
     */
    public char appraiseSit() {
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
     * Operation take a rest.
     *
     * It is called by the master thief to wait until a new thief has arrived the control collection
     * site.
     */
    public void takeARest() {
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
     * Operation hand a canvas.
     *
     * It is called by the ordinary thief to hand a canvas to the master thief. The ordinary thief
     * blocks until he is called by the master thief, meaning his canvas was collected.
     *
     * @param assaultPartyId assault party id.
     */
    public void handACanvas(int assaultPartyId) {
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
     * Operation collect a canvas.
     *
     * It is called by the master thief to collect the canvas of the next ordinary thief in queue.
     * After collecting the canvas, he wakes up the ordinary thief to proceed operations.
     */
    public void collectACanvas() {
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
     * Get the id of an available assault party.
     *
     * @return Id of an available assault party if there is one available, -1 otherwise.
     */
    public int getAvailableAssaultParty() {
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
     * Get the id of an available assault party.
     *
     * @return Id of an available assault party if there is one available, -1 otherwise.
     */
    public int getAvailableRoom() {
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
