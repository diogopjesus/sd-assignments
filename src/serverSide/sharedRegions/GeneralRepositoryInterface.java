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
            case MessageType.SETMASTHISTA:
                if ((inMessage.getMasterThiefState() < MasterThiefStates.PLANNING_THE_HEIST)
                        || (inMessage
                                .getMasterThiefState() >= MasterThiefStates.PRESENTING_THE_REPORT))
                    throw new MessageException("Invalid Master Thief State!", inMessage);
                break;

            case MessageType.SETORDTHISTA:
                if ((inMessage.getOrdinaryThiefId() < 0)
                        || (inMessage.getOrdinaryThiefId() >= SimulPar.M - 1))
                    throw new MessageException("Invalid Ordinary Thief Id!", inMessage);
                if ((inMessage.getOrdinaryThiefState() < OrdinaryThiefStates.CONCENTRATION_SITE)
                        || (inMessage
                                .getOrdinaryThiefState() > OrdinaryThiefStates.COLLECTION_SITE))
                    throw new MessageException("Invalid Ordinary Thief State!", inMessage);
                break;

            case MessageType.SETASSPARROOID:
                if ((inMessage.getAssaultPartyId() < 0)
                        || (inMessage.getAssaultPartyId() >= ((SimulPar.M - 1) / SimulPar.K)))
                    throw new MessageException("Invalid Assault Party Id!", inMessage);
                if ((inMessage.getRoomId() < 0) || (inMessage.getRoomId() >= SimulPar.N))
                    throw new MessageException("Invalid Room Id!", inMessage);
                break;

            case MessageType.SETASSPARELEID:
                if ((inMessage.getAssaultPartyId() < 0)
                        || (inMessage.getAssaultPartyId() >= ((SimulPar.M - 1) / SimulPar.K)))
                    throw new MessageException("Invalid Assault Party Id!", inMessage);
                if ((inMessage.getElementId() < 0) || (inMessage.getElementId() >= SimulPar.K))
                    throw new MessageException("Invalid Element Id!", inMessage);
                if ((inMessage.getOrdinaryThiefId() < 0)
                        || (inMessage.getOrdinaryThiefId() >= SimulPar.M - 1))
                    throw new MessageException("Invalid Ordinary Thief Id!", inMessage);
                break;

            case MessageType.SETASSPARELEPOS:
                if ((inMessage.getAssaultPartyId() < 0)
                        || (inMessage.getAssaultPartyId() >= ((SimulPar.M - 1) / SimulPar.K)))
                    throw new MessageException("Invalid Assault Party Id!", inMessage);
                if ((inMessage.getElementId() < 0) || (inMessage.getElementId() >= SimulPar.K))
                    throw new MessageException("Invalid Element Id!", inMessage);
                if ((inMessage.getPosition() < 0) || (inMessage.getPosition() > SimulPar.D))
                    throw new MessageException("Invalid Position!", inMessage);
                break;

            case MessageType.SETASSPARELECAN:
                if ((inMessage.getAssaultPartyId() < 0)
                        || (inMessage.getAssaultPartyId() >= ((SimulPar.M - 1) / SimulPar.K)))
                    throw new MessageException("Invalid Assault Party Id!", inMessage);
                if ((inMessage.getElementId() < 0) || (inMessage.getElementId() >= SimulPar.K))
                    throw new MessageException("Invalid Element Id!", inMessage);
                break;

            case MessageType.ENDASSPARELEMIS:
                if ((inMessage.getAssaultPartyId() < 0)
                        || (inMessage.getAssaultPartyId() >= ((SimulPar.M - 1) / SimulPar.K)))
                    throw new MessageException("Invalid Assault Party Id!", inMessage);
                if ((inMessage.getElementId() < 0) || (inMessage.getElementId() >= SimulPar.K))
                    throw new MessageException("Invalid Element Id!", inMessage);
                break;

            case MessageType.SETROOINF:
                int[] numPaint = inMessage.getNumPaint();
                int[] roomDist = inMessage.getRoomDist();
                for (int i = 0; i < SimulPar.N; i++) {
                    if ((numPaint[i] < SimulPar.p) || (numPaint[i] > SimulPar.P))
                        throw new MessageException("Invalid Number of Paintings!", inMessage);
                    if ((roomDist[i] < SimulPar.d) || (roomDist[i] > SimulPar.D))
                        throw new MessageException("Invalid Room Distance!", inMessage);
                }
                break;

            case MessageType.SHUT:
                break;
        }

        /* processing */
        switch (inMessage.getMsgType()) {
            case MessageType.SETMASTHISTA:
                repos.setMasterThiefState(inMessage.getMasterThiefState());
                outMessage = new Message(MessageType.SACK);
                break;

            case MessageType.SETORDTHISTA:
                repos.setOrdinaryThiefState(inMessage.getOrdinaryThiefId(),
                        inMessage.getOrdinaryThiefState());
                outMessage = new Message(MessageType.SACK);
                break;

            case MessageType.SETASSPARROOID:
                repos.setAssaultPartyRoomId(inMessage.getAssaultPartyId(), inMessage.getRoomId());
                outMessage = new Message(MessageType.SACK);
                break;

            case MessageType.SETASSPARELEID:
                repos.setAssaultPartyElementId(inMessage.getAssaultPartyId(),
                        inMessage.getElementId(), inMessage.getOrdinaryThiefId());
                outMessage = new Message(MessageType.SACK);
                break;

            case MessageType.SETASSPARELEPOS:
                repos.setAssaultPartyElementPosition(inMessage.getAssaultPartyId(),
                        inMessage.getElementId(), inMessage.getPosition());
                outMessage = new Message(MessageType.SACK);
                break;

            case MessageType.SETASSPARELECAN:
                repos.setAssaultPartyElementCanvas(inMessage.getAssaultPartyId(),
                        inMessage.getElementId(), inMessage.isCanvas());
                outMessage = new Message(MessageType.SACK);
                break;

            case MessageType.ENDASSPARELEMIS:
                repos.endAssaultPartyElementMission(inMessage.getAssaultPartyId(),
                        inMessage.getElementId());
                outMessage = new Message(MessageType.SACK);
                break;

            case MessageType.SETROOINF:
                repos.setRoomInfo(inMessage.getNumPaint(), inMessage.getRoomDist());
                outMessage = new Message(MessageType.SACK);
                break;

            case MessageType.SHUT:
                repos.shutdown();
                outMessage = new Message(MessageType.SHUTDONE);
                break;
        }

        return (outMessage);
    }
}
