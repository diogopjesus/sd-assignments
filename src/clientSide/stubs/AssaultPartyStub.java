package clientSide.stubs;

import serverSide.main.*;
import clientSide.entities.*;
import commInfra.*;
import genclass.GenericIO;

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

        outMessage = new Message(MessageType.SENASSPAR);
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if ((inMessage.getMsgType() != MessageType.SENASSPARDONE)) {
            GenericIO.writelnString(
                    "Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        if ((inMessage.getMasterThiefState() < MasterThiefStates.PLANNING_THE_HEIST
                || inMessage.getMasterThiefState() > MasterThiefStates.PRESENTING_THE_REPORT)) {
            GenericIO.writelnString(
                    "Thread " + Thread.currentThread().getName() + ": Invalid Master Thief State!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();

        ((MasterThief) Thread.currentThread()).setMasterThiefState(inMessage.getMasterThiefState());
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

        outMessage = new Message(MessageType.CRAWIN,
                ((OrdinaryThief) Thread.currentThread()).getOrdinaryThiefId(),
                ((OrdinaryThief) Thread.currentThread()).getMaximumDisplacement());
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if ((inMessage.getMsgType() != MessageType.CRAWINDONE)) {
            GenericIO.writelnString(
                    "Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        if (inMessage.getOrdinaryThiefId() != ((OrdinaryThief) Thread.currentThread())
                .getOrdinaryThiefId()) {
            GenericIO.writelnString(
                    "Thread " + Thread.currentThread().getName() + ": Invalid ordinary thief id!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        if ((inMessage.getOrdinaryThiefState() < OrdinaryThiefStates.CONCENTRATION_SITE)
                || (inMessage.getOrdinaryThiefState() > OrdinaryThiefStates.COLLECTION_SITE)) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName()
                    + ": Invalid ordinary thief state!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();

        ((OrdinaryThief) Thread.currentThread())
                .setOrdinaryThiefState(inMessage.getOrdinaryThiefState());

        return inMessage.isContCrawl();
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

        outMessage = new Message(MessageType.REVDIR);
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if ((inMessage.getMsgType() != MessageType.REVDIRDONE)) {
            GenericIO.writelnString(
                    "Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        if (inMessage.getOrdinaryThiefId() != ((OrdinaryThief) Thread.currentThread())
                .getOrdinaryThiefId()) {
            GenericIO.writelnString(
                    "Thread " + Thread.currentThread().getName() + ": Invalid ordinary thief id!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        if ((inMessage.getOrdinaryThiefState() < OrdinaryThiefStates.CONCENTRATION_SITE)
                || (inMessage.getOrdinaryThiefState() > OrdinaryThiefStates.COLLECTION_SITE)) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName()
                    + ": Invalid ordinary thief state!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();

        ((OrdinaryThief) Thread.currentThread())
                .setOrdinaryThiefState(inMessage.getOrdinaryThiefState());
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

        outMessage = new Message(MessageType.CRAWOUT,
                ((OrdinaryThief) Thread.currentThread()).getOrdinaryThiefId(),
                ((OrdinaryThief) Thread.currentThread()).getMaximumDisplacement());
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if ((inMessage.getMsgType() != MessageType.CRAWOUTDONE)) {
            GenericIO.writelnString(
                    "Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        if (inMessage.getOrdinaryThiefId() != ((OrdinaryThief) Thread.currentThread())
                .getOrdinaryThiefId()) {
            GenericIO.writelnString(
                    "Thread " + Thread.currentThread().getName() + ": Invalid ordinary thief id!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        if ((inMessage.getOrdinaryThiefState() < OrdinaryThiefStates.CONCENTRATION_SITE)
                || (inMessage.getOrdinaryThiefState() > OrdinaryThiefStates.COLLECTION_SITE)) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName()
                    + ": Invalid ordinary thief state!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();

        ((OrdinaryThief) Thread.currentThread())
                .setOrdinaryThiefState(inMessage.getOrdinaryThiefState());

        return inMessage.isContCrawl();
    }

    /**
     * Set the assault party target room for mission.
     *
     * @param targetRoom target room id.
     */
    public void setTargetRoom(int targetRoom) {
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

        outMessage = new Message(MessageType.SETASSPARTARROO, targetRoom);
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if ((inMessage.getMsgType() != MessageType.SETASSPARTARROODONE)) {
            GenericIO.writelnString(
                    "Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
    }

    /**
     * Get the assault party target room for mission.
     *
     * @return Target room.
     */
    public int getTargetRoom() {
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

        outMessage = new Message(MessageType.GETASSPARTARROO);
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if ((inMessage.getMsgType() != MessageType.GETASSPARTARROODONE)) {
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
     * Set the assault party target room distance for mission.
     *
     * @param targetRoomDistance target room distance.
     */
    public void setTargetRoomDistance(int targetRoomDistance) {
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

        outMessage = new Message(MessageType.SETASSPARTARROODIS, targetRoomDistance);
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if ((inMessage.getMsgType() != MessageType.SETASSPARTARROODISDONE)) {
            GenericIO.writelnString(
                    "Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
    }

    /**
     * Check if the assault party is available.
     *
     * @return true if the assault party is available, false otherwise.
     */
    public boolean isAvailable() {
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

        outMessage = new Message(MessageType.ASSPARISAVA);
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if ((inMessage.getMsgType() != MessageType.ASSPARISAVADONE)) {
            GenericIO.writelnString(
                    "Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();

        return inMessage.isAvailable();
    }

    /**
     * Check if the assault party is full.
     *
     * @return true if the assault party is full, false otherwise.
     */
    public synchronized boolean isFull() {
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

        outMessage = new Message(MessageType.ASSPARISFUL);
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if ((inMessage.getMsgType() != MessageType.ASSPARISFULDONE)) {
            GenericIO.writelnString(
                    "Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();

        return inMessage.isFull();
    }

    /**
     * Add new thief to the assault party.
     *
     * @param thiefId thief id.
     */
    public synchronized void joinAssaultParty(int thiefId) {
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

        outMessage = new Message(MessageType.JOIASSPAR, thiefId);
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if ((inMessage.getMsgType() != MessageType.JOIASSPARDONE)) {
            GenericIO.writelnString(
                    "Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
    }

    /**
     * Remove thief from the assault party.
     *
     * @param thiefId thief id.
     */
    public synchronized void quitAssaultParty(int thiefId) {
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

        outMessage = new Message(MessageType.QUIASSPAR, thiefId);
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if ((inMessage.getMsgType() != MessageType.QUIASSPARDONE)) {
            GenericIO.writelnString(
                    "Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
    }

    /**
     * Set thief canvas state.
     *
     * @param thiefId thief id.
     * @param canvas true if is holding a canvas - false, otherwise.
     */
    public synchronized void setHoldingCanvas(int thiefId, boolean canvas) {
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

        outMessage = new Message(MessageType.SETHOLCAN, thiefId, canvas);
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if ((inMessage.getMsgType() != MessageType.SETHOLCANDONE)) {
            GenericIO.writelnString(
                    "Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();
    }

    /**
     * Check wether a thief is holding a canvas.
     *
     * @param thiefId thief id.
     * @return True if is holding a canvas, false otherwise.
     */
    public synchronized boolean isHoldingCanvas(int thiefId) {
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

        outMessage = new Message(MessageType.ISHOLCAN, thiefId);
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if ((inMessage.getMsgType() != MessageType.ISHOLCANDONE)) {
            GenericIO.writelnString(
                    "Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();

        return inMessage.isCanvas();
    }

    /**
     * Get the element (position inside the assault party) of the thief with the given id.
     *
     * @param thiefId thief id.
     * @return thief element.
     */
    public synchronized int getThiefElement(int thiefId) {
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

        outMessage = new Message(MessageType.GETTHIELEINASSPAR, thiefId);
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if ((inMessage.getMsgType() != MessageType.GETTHIELEINASSPARDONE)) {
            GenericIO.writelnString(
                    "Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        if ((inMessage.getElementId() < 0)
                || (inMessage.getElementId() > ((SimulPar.M - 1) / SimulPar.K))) {
            GenericIO.writelnString(
                    "Thread " + Thread.currentThread().getName() + ": Invalid element id!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();

        return inMessage.getElementId();
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
        outMessage = new Message(MessageType.SHUT);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.SHUTDONE) {
            GenericIO.writelnString(
                    "Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        com.close();
    }
}
