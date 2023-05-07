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
            case MessageType.STAOPE:
                break;

            case MessageType.APPSIT:
                break;

            case MessageType.TAKARES:
                break;

            case MessageType.HANACAN:
                if ((inMessage.getOrdinaryThiefId() < 0)
                        || (inMessage.getOrdinaryThiefId() >= SimulPar.M - 1))
                    throw new MessageException("Invalid Ordinary Thief Id!", inMessage);
                if ((inMessage.getAssaultPartyId() < 0)
                        || (inMessage.getAssaultPartyId() >= ((SimulPar.M - 1) / SimulPar.K)))
                    throw new MessageException("Invalid assault party id!", inMessage);
                break;

            case MessageType.COLACAN:
                break;

            case MessageType.GETAVAASSPAR:
                break;

            case MessageType.GETAVAROO:
                break;

            case MessageType.SETTHITOPAR:
                if ((inMessage.getOrdinaryThiefId() < 0)
                        || (inMessage.getOrdinaryThiefId() >= SimulPar.M - 1))
                    throw new MessageException("Invalid Ordinary Thief Id!", inMessage);
                if ((inMessage.getAssaultPartyId() < 0)
                        || (inMessage.getAssaultPartyId() >= ((SimulPar.M - 1) / SimulPar.K)))
                    throw new MessageException("Invalid assault party id!", inMessage);
                break;

            case MessageType.SHUT:
                break;

            default:
                throw new MessageException("Invalid message type!", inMessage);
        }

        /* processing */

        switch (inMessage.getMsgType()) {
            case MessageType.STAOPE:
                controlCollectionSite.startOperations();
                outMessage = new Message(MessageType.STAOPEDONE,
                        ((ControlCollectionSiteClientProxy) Thread.currentThread())
                                .getMasterThiefState());
                break;

            case MessageType.APPSIT:
                char oper = controlCollectionSite.appraiseSit();
                outMessage = new Message(MessageType.APPSITDONE, oper);
                break;

            case MessageType.TAKARES:
                controlCollectionSite.takeARest();
                outMessage = new Message(MessageType.TAKARESDONE,
                        ((ControlCollectionSiteClientProxy) Thread.currentThread())
                                .getMasterThiefState());
                break;

            case MessageType.HANACAN:
                ((ControlCollectionSiteClientProxy) Thread.currentThread())
                        .setOrdinaryThiefId(inMessage.getOrdinaryThiefId());
                controlCollectionSite.handACanvas(inMessage.getAssaultPartyId());
                outMessage = new Message(MessageType.HANACANDONE,
                        ((ControlCollectionSiteClientProxy) Thread.currentThread())
                                .getOrdinaryThiefId());
                break;

            case MessageType.COLACAN:
                controlCollectionSite.collectACanvas();
                outMessage = new Message(MessageType.COLACANDONE,
                        ((ControlCollectionSiteClientProxy) Thread.currentThread())
                                .getMasterThiefState());
                break;

            case MessageType.GETAVAASSPAR:
                int assPartId = controlCollectionSite.getAvailableAssaultParty();
                outMessage = new Message(MessageType.GETAVAASSPARDONE, assPartId);
                break;

            case MessageType.GETAVAROO:
                int roomId = controlCollectionSite.getAvailableRoom();
                outMessage = new Message(MessageType.GETAVAROODONE, roomId);
                break;

            case MessageType.SETTHITOPAR:
                controlCollectionSite.setThiefToParty(inMessage.getOrdinaryThiefId(),
                        inMessage.getAssaultPartyId());
                outMessage = new Message(MessageType.SETTHITOPARDONE);

            case MessageType.SHUT:
                controlCollectionSite.shutdown();
                outMessage = new Message(MessageType.SHUTDONE);
                break;
        }

        return (outMessage);
    }
}
