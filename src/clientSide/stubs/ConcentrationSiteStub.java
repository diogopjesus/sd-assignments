package clientSide.stubs;

import serverSide.main.*;
import clientSide.entities.*;
import commInfra.*;
import genclass.GenericIO;

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

        outMessage = new Message(MessageType.AM_I_NEEDED,
                ((OrdinaryThief) Thread.currentThread()).getOrdinaryThiefId(),
                ((OrdinaryThief) Thread.currentThread()).getOrdinaryThiefState());
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if ((inMessage.getMsgType() != MessageType.AM_I_NEEDED_DONE)) {
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
        if ((inMessage.getOtState() < OrdinaryThiefStates.CONCENTRATION_SITE)
                || (inMessage.getOtState() > OrdinaryThiefStates.COLLECTION_SITE)) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName()
                    + ": Invalid ordinary thief state!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();

        ((OrdinaryThief) Thread.currentThread()).setOrdinaryThiefState(inMessage.getOtState());

        return inMessage.isNeeded();
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

        outMessage = new Message(MessageType.PREPARE_ASSAULT_PARTY, assaultPartyId, roomId);
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if ((inMessage.getMsgType() != MessageType.PREPARE_ASSAULT_PARTY_DONE)) {
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

        outMessage = new Message(MessageType.PREPARE_EXCURSION,
                ((OrdinaryThief) Thread.currentThread()).getOrdinaryThiefId());
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if ((inMessage.getMsgType() != MessageType.PREPARE_EXCURSION_DONE)) {
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
        if ((inMessage.getOtState() < OrdinaryThiefStates.CONCENTRATION_SITE)
                || (inMessage.getOtState() > OrdinaryThiefStates.COLLECTION_SITE)) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName()
                    + ": Invalid ordinary thief state!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        if ((inMessage.getAssPartId() < 0)
                || (inMessage.getAssPartId() >= ((SimulPar.M - 1) / SimulPar.K))) {
            GenericIO.writelnString(
                    "Thread " + Thread.currentThread().getName() + ": Invalid assault party id!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        com.close();

        ((OrdinaryThief) Thread.currentThread()).setOrdinaryThiefState(inMessage.getOtState());

        return inMessage.getAssPartId();
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

        outMessage = new Message(MessageType.SUM_UP_RESULTS);
        com.writeObject(outMessage);

        inMessage = (Message) com.readObject();

        if ((inMessage.getMsgType() != MessageType.SUM_UP_RESULTS_DONE)) {
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
