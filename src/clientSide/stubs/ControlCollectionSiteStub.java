package clientSide.stubs;

import serverSide.main.*;
import clientSide.entities.*;
import commInfra.*;
import genclass.GenericIO;

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

        outMessage = new Message(MessageType.START_OPERATIONS);
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if (inMessage.getMsgType() != MessageType.START_OPERATIONS_DONE) {
            GenericIO.writelnString(
                    "Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        if ((inMessage.getMtState() < MasterThiefStates.PLANNING_THE_HEIST
                || inMessage.getMtState() > MasterThiefStates.PRESENTING_THE_REPORT)) {
            GenericIO.writelnString(
                    "Thread " + Thread.currentThread().getName() + ": Invalid Master Thief State!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();

        ((MasterThief) Thread.currentThread()).setMasterThiefState(inMessage.getMtState());
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

        outMessage = new Message(MessageType.APPRAISE_SIT);
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if (inMessage.getMsgType() != MessageType.APPRAISE_SIT_DONE) {
            GenericIO.writelnString(
                    "Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        if ((inMessage.getOper() != 'P' && inMessage.getOper() != 'R'
                && inMessage.getOper() != 'E')) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName()
                    + ": Invalid appraise sit operation!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();

        return inMessage.getOper();
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

        outMessage = new Message(MessageType.TAKE_A_REST);
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if (inMessage.getMsgType() != MessageType.TAKE_A_REST_DONE) {
            GenericIO.writelnString(
                    "Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        if ((inMessage.getMtState() < MasterThiefStates.PLANNING_THE_HEIST
                || inMessage.getMtState() > MasterThiefStates.PRESENTING_THE_REPORT)) {
            GenericIO.writelnString(
                    "Thread " + Thread.currentThread().getName() + ": Invalid Master Thief State!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();

        ((MasterThief) Thread.currentThread()).setMasterThiefState(inMessage.getMtState());
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

        outMessage = new Message(MessageType.HAND_A_CANVAS,
                ((OrdinaryThief) Thread.currentThread()).getOrdinaryThiefId(), assaultPartyId);
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if (inMessage.getMsgType() != MessageType.HAND_A_CANVAS_DONE) {
            GenericIO.writelnString(
                    "Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        if (inMessage.getOtId() != ((OrdinaryThief) Thread.currentThread()).getOrdinaryThiefId()) {
            GenericIO.writelnString(
                    "Thread " + Thread.currentThread().getName() + ": Invalid ordinary thief id!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
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

        outMessage = new Message(MessageType.COLLECT_A_CANVAS);
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if (inMessage.getMsgType() != MessageType.COLLECT_A_CANVAS_DONE) {
            GenericIO.writelnString(
                    "Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        if ((inMessage.getMtState() < MasterThiefStates.PLANNING_THE_HEIST
                || inMessage.getMtState() > MasterThiefStates.PRESENTING_THE_REPORT)) {
            GenericIO.writelnString(
                    "Thread " + Thread.currentThread().getName() + ": Invalid Master Thief State!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();

        ((MasterThief) Thread.currentThread()).setMasterThiefState(inMessage.getMtState());
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

        outMessage = new Message(MessageType.GET_AVAILABLE_ASSAULT_PARTY);
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if (inMessage.getMsgType() != MessageType.GET_AVAILABLE_ASSAULT_PARTY_DONE) {
            GenericIO.writelnString(
                    "Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        if ((inMessage.getAssPartId() < 0)
                || (inMessage.getAssPartId() > ((SimulPar.M - 1) / SimulPar.K))) {
            GenericIO.writelnString(
                    "Thread " + Thread.currentThread().getName() + ": Invalid assault party id!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();

        return inMessage.getAssPartId();
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

        outMessage = new Message(MessageType.GET_AVAILABLE_ROOM);
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if (inMessage.getMsgType() != MessageType.GET_AVAILABLE_ROOM_DONE) {
            GenericIO.writelnString(
                    "Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        if ((inMessage.getRoomId() < 0) || (inMessage.getRoomId() >= SimulPar.N)) {
            GenericIO.writelnString(
                    "Thread " + Thread.currentThread().getName() + ": Invalid room id!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();

        return inMessage.getRoomId();
    }

    /**
     * Associate a thief to an assault party.
     *
     * @param thiefId thief id.
     * @param assaultPartyId assault party id.
     */
    public void setThiefToParty(int thiefId, int assaultPartyId) {
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

        outMessage = new Message(MessageType.SET_THIEF_TO_PARTY, thiefId, assaultPartyId);
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if (inMessage.getMsgType() != MessageType.SET_THIEF_TO_PARTY_DONE) {
            GenericIO.writelnString(
                    "Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
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
        outMessage = new Message(MessageType.SHUTDOWN);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.SHUTDOWN_DONE) {
            GenericIO.writelnString(
                    "Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        com.close();
    }


    /**
     * Operation end of work.
     */
    public void endOperation() {
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

        outMessage = new Message(MessageType.END_OPERATION);
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if (inMessage.getMsgType() != MessageType.END_OPERATION_DONE) {
            GenericIO.writelnString(
                    "Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
    }
}
