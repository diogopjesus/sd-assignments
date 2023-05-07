package clientSide.stubs;

import commInfra.*;
import genclass.GenericIO;

/**
 * Stub to the general repository.
 *
 * It instantiates a remote reference to the general repository. Implementation of a client-server
 * model of type 2 (server replication). Communication is based on a communication channel under the
 * TCP protocol.
 */
public class GeneralRepositoryStub {
    /**
     * Name of the platform where is located the general repository server.
     */
    private String serverHostName;

    /**
     * Port number for listening to service requests.
     */
    private int serverPortNumb;

    /**
     * Instantiation of a stub to the general repository.
     *
     * @param serverHostName name of the platform where is located the barber shop server
     * @param serverPortNumb port number for listening to service requests
     */
    public GeneralRepositoryStub(String serverHostName, int serverPortNumb) {
        this.serverHostName = serverHostName;
        this.serverPortNumb = serverPortNumb;
    }

    /**
     * Set the current state of the master thief.
     *
     * @param masterThiefState the current state of the master thief.
     */
    public void setMasterThiefState(int masterThiefState) {
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

        outMessage = new Message(MessageType.SET_MASTER_THIEF_STATE, masterThiefState);
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if (inMessage.getMsgType() != MessageType.SET_ACKNOWLEDGE) {
            GenericIO.writelnString(
                    "Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
    }

    /**
     * Set the current state of an ordinary thief.
     *
     * @param thiefId the thief id.
     * @param ordinaryThiefState the ordinary thief state.
     */
    public void setOrdinaryThiefState(int thiefId, int ordinaryThiefState) {
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

        outMessage = new Message(MessageType.SET_ORDINARY_THIEF_STATE, thiefId, ordinaryThiefState);
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if (inMessage.getMsgType() != MessageType.SET_ACKNOWLEDGE) {
            GenericIO.writelnString(
                    "Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
    }


    /**
     * Set the current room id of an assault party.
     *
     * @param assaultPartyId the assault party id.
     * @param assaultPartyRoomId the target room id of the assault party.
     */
    public void setAssaultPartyRoomId(int assaultPartyId, int assaultPartyRoomId) {
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

        outMessage = new Message(MessageType.SET_ASSAULT_PARTY_ROOM_ID, assaultPartyId,
                assaultPartyRoomId);
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if (inMessage.getMsgType() != MessageType.SET_ACKNOWLEDGE) {
            GenericIO.writelnString(
                    "Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
    }

    /**
     * Set the current element id of an assault party.
     *
     * @param assaultPartyId the assault party id.
     * @param elementId the element id.
     * @param thiefId thief id.
     */
    public void setAssaultPartyElementId(int assaultPartyId, int elementId, int thiefId) {
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

        outMessage = new Message(MessageType.SET_ASSAULT_PARTY_ELEMENT_ID, assaultPartyId,
                elementId, thiefId);
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if (inMessage.getMsgType() != MessageType.SET_ACKNOWLEDGE) {
            GenericIO.writelnString(
                    "Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
    }

    /**
     * Set the current element position of an assault party.
     *
     * @param assaultPartyId the assault party id.
     * @param elementId the element id.
     * @param assaultPartyElementPosition the target element position of the assault party.
     */
    public void setAssaultPartyElementPosition(int assaultPartyId, int elementId,
            int assaultPartyElementPosition) {
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

        outMessage = new Message(MessageType.SET_ASSAULT_PARTY_ELEMENT_POSITION, assaultPartyId,
                elementId, assaultPartyElementPosition);
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if (inMessage.getMsgType() != MessageType.SET_ACKNOWLEDGE) {
            GenericIO.writelnString(
                    "Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
    }

    /**
     * Set the current element canvas of an assault party.
     *
     * @param assaultPartyId the assault party id.
     * @param elementId the element id.
     * @param assaultPartyElementCanvas the target element canvas of the assault party.
     */
    public void setAssaultPartyElementCanvas(int assaultPartyId, int elementId,
            boolean assaultPartyElementCanvas) {
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

        outMessage = new Message(MessageType.SET_ASSAULT_PARTY_ELEMENT_CANVAS, assaultPartyId,
                elementId, assaultPartyElementCanvas);
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if (inMessage.getMsgType() != MessageType.SET_ACKNOWLEDGE) {
            GenericIO.writelnString(
                    "Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
    }

    /**
     * Increment canvas stolen by a thief, if he has one and remove the thief from the assault
     * party.
     *
     * @param assaultPartyId assault party id.
     * @param elementId element id (position of the thief in the party).
     */
    public void endAssaultPartyElementMission(int assaultPartyId, int elementId) {
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

        outMessage = new Message(MessageType.END_ASSAULT_PARTY_ELEMENT_MISSION, assaultPartyId,
                elementId);
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if (inMessage.getMsgType() != MessageType.SET_ACKNOWLEDGE) {
            GenericIO.writelnString(
                    "Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
    }

    /**
     * Operation initialization of simulation.
     *
     * @param logFileName name of the logging file.
     * @param numPaint number of paintings in the museum.
     * @param roomDist distance between each room and the outside.
     */
    public void initSimul(String logFileName, int[] maxDis, int[] numPaint, int[] roomDist) {
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

        outMessage =
                new Message(MessageType.INIT_SIMULATION, logFileName, maxDis, numPaint, roomDist);
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if (inMessage.getMsgType() != MessageType.INIT_SIMULATION_DONE) {
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
}
