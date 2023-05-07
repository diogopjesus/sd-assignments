package serverSide.sharedRegions;

import serverSide.main.*;
import clientSide.entities.*;
import commInfra.*;

/**
 * Interface to the General Repository of Information.
 *
 * It is responsible to validate and process the incoming message, execute the corresponding method
 * on the General Repository and generate the outgoing message. Implementation of a client-server
 * model of type 2 (server replication). Communication is based on a communication channel under the
 * TCP protocol.
 */

public class GeneralRepositoryInterface {
    /**
     * Reference to the general repository.
     */

    private final GeneralRepository repos;

    /**
     * Instantiation of an interface to the general repository.
     *
     * @param repos reference to the general repository
     */

    public GeneralRepositoryInterface(GeneralRepository repos) {
        this.repos = repos;
    }

    /**
     * Processing of the incoming messages.
     *
     * Validation, execution of the corresponding method and generation of the outgoing message.
     *
     * @param inMessage service request
     * @return service reply
     * @throws MessageException if the incoming message is not valid
     */

    public Message processAndReply(Message inMessage) throws MessageException {
        Message outMessage = null; // outgoing message

        /* validation of the incoming message */
        switch (inMessage.getMsgType()) {
            case MessageType.SET_MASTER_THIEF_STATE:
                if ((inMessage.getMtState() < MasterThiefStates.PLANNING_THE_HEIST)
                        || (inMessage.getMtState() > MasterThiefStates.PRESENTING_THE_REPORT))
                    throw new MessageException("Invalid Master Thief State!", inMessage);
                break;

            case MessageType.SET_ORDINARY_THIEF_STATE:
                if ((inMessage.getOtId() < 0) || (inMessage.getOtId() >= SimulPar.M - 1))
                    throw new MessageException("Invalid Ordinary Thief Id!", inMessage);
                if ((inMessage.getOtState() < OrdinaryThiefStates.CONCENTRATION_SITE)
                        || (inMessage.getOtState() > OrdinaryThiefStates.COLLECTION_SITE))
                    throw new MessageException("Invalid Ordinary Thief State!", inMessage);
                break;

            case MessageType.SET_ASSAULT_PARTY_ROOM_ID:
                if ((inMessage.getAssPartId() < 0)
                        || (inMessage.getAssPartId() >= ((SimulPar.M - 1) / SimulPar.K)))
                    throw new MessageException("Invalid Assault Party Id!", inMessage);
                if ((inMessage.getRoomId() < 0) || (inMessage.getRoomId() >= SimulPar.N))
                    throw new MessageException("Invalid Room Id!", inMessage);
                break;

            case MessageType.SET_ASSAULT_PARTY_ELEMENT_ID:
                if ((inMessage.getAssPartId() < 0)
                        || (inMessage.getAssPartId() >= ((SimulPar.M - 1) / SimulPar.K)))
                    throw new MessageException("Invalid Assault Party Id!", inMessage);
                if ((inMessage.getElemId() < 0) || (inMessage.getElemId() >= SimulPar.K))
                    throw new MessageException("Invalid Element Id!", inMessage);
                if ((inMessage.getOtId() < 0) || (inMessage.getOtId() >= SimulPar.M - 1))
                    throw new MessageException("Invalid Ordinary Thief Id!", inMessage);
                break;

            case MessageType.SET_ASSAULT_PARTY_ELEMENT_POSITION:
                if ((inMessage.getAssPartId() < 0)
                        || (inMessage.getAssPartId() >= ((SimulPar.M - 1) / SimulPar.K)))
                    throw new MessageException("Invalid Assault Party Id!", inMessage);
                if ((inMessage.getElemId() < 0) || (inMessage.getElemId() >= SimulPar.K))
                    throw new MessageException("Invalid Element Id!", inMessage);
                if ((inMessage.getPos() < 0) || (inMessage.getPos() > SimulPar.D))
                    throw new MessageException("Invalid Position!", inMessage);
                break;

            case MessageType.SET_ASSAULT_PARTY_ELEMENT_CANVAS:
                if ((inMessage.getAssPartId() < 0)
                        || (inMessage.getAssPartId() >= ((SimulPar.M - 1) / SimulPar.K)))
                    throw new MessageException("Invalid Assault Party Id!", inMessage);
                if ((inMessage.getElemId() < 0) || (inMessage.getElemId() >= SimulPar.K))
                    throw new MessageException("Invalid Element Id!", inMessage);
                break;

            case MessageType.END_ASSAULT_PARTY_ELEMENT_MISSION:
                if ((inMessage.getAssPartId() < 0)
                        || (inMessage.getAssPartId() >= ((SimulPar.M - 1) / SimulPar.K)))
                    throw new MessageException("Invalid Assault Party Id!", inMessage);
                if ((inMessage.getElemId() < 0) || (inMessage.getElemId() >= SimulPar.K))
                    throw new MessageException("Invalid Element Id!", inMessage);
                break;

            case MessageType.INIT_SIMULATION:
                String logFileName = inMessage.getfName();
                if ((logFileName == null))
                    throw new MessageException("Invalid Log File Name!", inMessage);
                int[] numPaint = inMessage.getNumPaint();
                int[] roomDist = inMessage.getRoomDist();
                for (int i = 0; i < SimulPar.N; i++) {
                    if ((numPaint[i] < SimulPar.p) || (numPaint[i] > SimulPar.P))
                        throw new MessageException("Invalid Number of Paintings!", inMessage);
                    if ((roomDist[i] < SimulPar.d) || (roomDist[i] > SimulPar.D))
                        throw new MessageException("Invalid Room Distance!", inMessage);
                }
                break;

            case MessageType.SET_ORDINARY_THIEF:
                if ((inMessage.getOtId() < 0) || (inMessage.getOtId() >= SimulPar.M - 1))
                    throw new MessageException("Invalid Ordinary Thief Id!", inMessage);
                if ((inMessage.getMaxDis() < SimulPar.md) || (inMessage.getMaxDis() > SimulPar.MD))
                    throw new MessageException("Invalid Maximum Displacement!", inMessage);
                break;

            case MessageType.SHUTDOWN:
                break;

            default:
                throw new MessageException("Invalid message type!", inMessage);
        }

        /* processing */
        switch (inMessage.getMsgType()) {
            case MessageType.SET_MASTER_THIEF_STATE:
                repos.setMasterThiefState(inMessage.getMtState());
                outMessage = new Message(MessageType.SET_ACKNOWLEDGE);
                break;

            case MessageType.SET_ORDINARY_THIEF_STATE:
                repos.setOrdinaryThiefState(inMessage.getOtId(), inMessage.getOtState());
                outMessage = new Message(MessageType.SET_ACKNOWLEDGE);
                break;

            case MessageType.SET_ASSAULT_PARTY_ROOM_ID:
                repos.setAssaultPartyRoomId(inMessage.getAssPartId(), inMessage.getRoomId());
                outMessage = new Message(MessageType.SET_ACKNOWLEDGE);
                break;

            case MessageType.SET_ASSAULT_PARTY_ELEMENT_ID:
                repos.setAssaultPartyElementId(inMessage.getAssPartId(), inMessage.getElemId(),
                        inMessage.getOtId());
                outMessage = new Message(MessageType.SET_ACKNOWLEDGE);
                break;

            case MessageType.SET_ASSAULT_PARTY_ELEMENT_POSITION:
                repos.setAssaultPartyElementPosition(inMessage.getAssPartId(),
                        inMessage.getElemId(), inMessage.getPos());
                outMessage = new Message(MessageType.SET_ACKNOWLEDGE);
                break;

            case MessageType.SET_ASSAULT_PARTY_ELEMENT_CANVAS:
                repos.setAssaultPartyElementCanvas(inMessage.getAssPartId(), inMessage.getElemId(),
                        inMessage.isCanvas());
                outMessage = new Message(MessageType.SET_ACKNOWLEDGE);
                break;

            case MessageType.END_ASSAULT_PARTY_ELEMENT_MISSION:
                repos.endAssaultPartyElementMission(inMessage.getAssPartId(),
                        inMessage.getElemId());
                outMessage = new Message(MessageType.SET_ACKNOWLEDGE);
                break;

            case MessageType.INIT_SIMULATION:
                repos.initSimul(inMessage.getfName(), inMessage.getNumPaint(),
                        inMessage.getRoomDist());
                outMessage = new Message(MessageType.INIT_SIMULATION_DONE);
                break;

            case MessageType.SET_ORDINARY_THIEF:
                repos.setOrdinaryThief(inMessage.getOtId(), inMessage.getMaxDis());
                outMessage = new Message(MessageType.SET_ACKNOWLEDGE);
                break;

            case MessageType.SHUTDOWN:
                repos.shutdown();
                outMessage = new Message(MessageType.SHUTDOWN_DONE);
                break;
        }

        return (outMessage);
    }
}
