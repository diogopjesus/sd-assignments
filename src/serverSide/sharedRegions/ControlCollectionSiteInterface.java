package serverSide.sharedRegions;

import serverSide.main.*;
import serverSide.entities.*;
import commInfra.*;

/**
 * Interface to the control collection site.
 *
 * It is responsible to validate and process the incoming message, execute the corresponding method
 * on the control collection site and generate the outgoing message. Implementation of a
 * client-server model of type 2 (server replication). Communication is based on a communication
 * channel under the TCP protocol.
 */
public class ControlCollectionSiteInterface {
    /**
     * Reference to the control collection site.
     */
    private final ControlCollectionSite controlCollectionSite;

    /**
     * Instantiation of an interface to the control collection site.
     *
     * @param controlCollectionSite reference to the control collection site
     */
    public ControlCollectionSiteInterface(ControlCollectionSite controlCollectionSite) {
        this.controlCollectionSite = controlCollectionSite;
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
            case MessageType.START_OPERATIONS:
                break;

            case MessageType.APPRAISE_SIT:
                break;

            case MessageType.TAKE_A_REST:
                break;

            case MessageType.HAND_A_CANVAS:
                if ((inMessage.getOtId() < 0) || (inMessage.getOtId() >= SimulPar.M - 1))
                    throw new MessageException("Invalid Ordinary Thief Id!", inMessage);
                if ((inMessage.getAssPartId() < 0)
                        || (inMessage.getAssPartId() >= ((SimulPar.M - 1) / SimulPar.K)))
                    throw new MessageException("Invalid assault party id!", inMessage);
                break;

            case MessageType.COLLECT_A_CANVAS:
                break;

            case MessageType.GET_AVAILABLE_ASSAULT_PARTY:
                break;

            case MessageType.GET_AVAILABLE_ROOM:
                break;

            case MessageType.SET_THIEF_TO_PARTY:
                if ((inMessage.getOtId() < 0) || (inMessage.getOtId() >= SimulPar.M - 1))
                    throw new MessageException("Invalid Ordinary Thief Id!", inMessage);
                if ((inMessage.getAssPartId() < 0)
                        || (inMessage.getAssPartId() >= ((SimulPar.M - 1) / SimulPar.K)))
                    throw new MessageException("Invalid assault party id!", inMessage);
                break;

            case MessageType.SHUTDOWN:
                break;

            default:
                throw new MessageException("Invalid message type!", inMessage);
        }

        /* processing */

        switch (inMessage.getMsgType()) {
            case MessageType.START_OPERATIONS:
                controlCollectionSite.startOperations();
                outMessage = new Message(MessageType.START_OPERATIONS_DONE,
                        ((ControlCollectionSiteClientProxy) Thread.currentThread())
                                .getMasterThiefState());
                break;

            case MessageType.APPRAISE_SIT:
                char oper = controlCollectionSite.appraiseSit();
                outMessage = new Message(MessageType.APPRAISE_SIT_DONE, oper);
                break;

            case MessageType.TAKE_A_REST:
                controlCollectionSite.takeARest();
                outMessage = new Message(MessageType.TAKE_A_REST_DONE,
                        ((ControlCollectionSiteClientProxy) Thread.currentThread())
                                .getMasterThiefState());
                break;

            case MessageType.HAND_A_CANVAS:
                ((ControlCollectionSiteClientProxy) Thread.currentThread())
                        .setOrdinaryThiefId(inMessage.getOtId());
                controlCollectionSite.handACanvas(inMessage.getAssPartId());
                outMessage = new Message(MessageType.HAND_A_CANVAS_DONE,
                        ((ControlCollectionSiteClientProxy) Thread.currentThread())
                                .getOrdinaryThiefId());
                break;

            case MessageType.COLLECT_A_CANVAS:
                controlCollectionSite.collectACanvas();
                outMessage = new Message(MessageType.COLLECT_A_CANVAS_DONE,
                        ((ControlCollectionSiteClientProxy) Thread.currentThread())
                                .getMasterThiefState());
                break;

            case MessageType.GET_AVAILABLE_ASSAULT_PARTY:
                int assPartId = controlCollectionSite.getAvailableAssaultParty();
                outMessage = new Message(MessageType.GET_AVAILABLE_ASSAULT_PARTY_DONE, assPartId);
                break;

            case MessageType.GET_AVAILABLE_ROOM:
                int roomId = controlCollectionSite.getAvailableRoom();
                outMessage = new Message(MessageType.GET_AVAILABLE_ROOM_DONE, roomId);
                break;

            case MessageType.SET_THIEF_TO_PARTY:
                controlCollectionSite.setThiefToParty(inMessage.getOtId(),
                        inMessage.getAssPartId());
                outMessage = new Message(MessageType.SET_THIEF_TO_PARTY_DONE);
                break;

            case MessageType.SHUTDOWN:
                controlCollectionSite.shutdown();
                outMessage = new Message(MessageType.SHUTDOWN_DONE);
                break;
        }

        return (outMessage);
    }
}
