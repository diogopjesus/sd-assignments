package serverSide.sharedRegions;

import serverSide.main.*;
import serverSide.entities.*;
import clientSide.entities.*;
import commInfra.*;

/**
 * Interface to the concentration site.
 *
 * It is responsible to validate and process the incoming message, execute the corresponding method
 * on the concentration site and generate the outgoing message. Implementation of a client-server
 * model of type 2 (server replication). Communication is based on a communication channel under the
 * TCP protocol.
 */
public class ConcentrationSiteInterface {
    /**
     * Reference to the concentration site.
     */
    private final ConcentrationSite concentrationSite;

    /**
     * Instantiation of an interface to the concentration site.
     *
     * @param concentrationSite reference to the concentration site.
     */
    public ConcentrationSiteInterface(ConcentrationSite concentrationSite) {
        this.concentrationSite = concentrationSite;
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
            case MessageType.AMINEE:
                if ((inMessage.getOrdinaryThiefId() < 0)
                        || (inMessage.getOrdinaryThiefId() >= SimulPar.M - 1))
                    throw new MessageException("Invalid Ordinary Thief Id!", inMessage);
                if ((inMessage.getOrdinaryThiefState() < OrdinaryThiefStates.CONCENTRATION_SITE)
                        || (inMessage
                                .getOrdinaryThiefState() > OrdinaryThiefStates.COLLECTION_SITE))
                    throw new MessageException("Invalid Ordinary Thief State!", inMessage);
                break;

            case MessageType.PREASSPAR:
                if ((inMessage.getAssaultPartyId() < 0)
                        || (inMessage.getAssaultPartyId() >= SimulPar.K))
                    throw new MessageException("Invalid Assault Party Id!", inMessage);
                if ((inMessage.getRoomId() < 0) || (inMessage.getRoomId() >= SimulPar.N))
                    throw new MessageException("Invalid Room Id!", inMessage);
                break;

            case MessageType.PREEXC:
                if ((inMessage.getOrdinaryThiefId() < 0)
                        || (inMessage.getOrdinaryThiefId() >= SimulPar.M - 1))
                    throw new MessageException("Invalid Ordinary Thief Id!", inMessage);

            case MessageType.SUMUPRES:
                break;

            case MessageType.SHUT:
                break;

            default:
                throw new MessageException("Invalid message type!", inMessage);
        }

        /* processing */

        switch (inMessage.getMsgType()) {
            case MessageType.AMINEE:
                ((ConcentrationSiteClientProxy) Thread.currentThread())
                        .setOrdinaryThiefId(inMessage.getOrdinaryThiefId());
                ((ConcentrationSiteClientProxy) Thread.currentThread())
                        .setOrdinaryThiefState(inMessage.getOrdinaryThiefState());
                boolean isNeeded = concentrationSite.amINeeded();
                outMessage = new Message(MessageType.AMINEEDONE,
                        ((ConcentrationSiteClientProxy) Thread.currentThread())
                                .getOrdinaryThiefId(),
                        ((ConcentrationSiteClientProxy) Thread.currentThread())
                                .getOrdinaryThiefState(),
                        isNeeded);
                break;

            case MessageType.PREASSPAR:
                concentrationSite.prepareAssaultParty(inMessage.getAssaultPartyId(),
                        inMessage.getRoomId());
                outMessage = new Message(MessageType.PREASSPARDONE,
                        ((ConcentrationSiteClientProxy) Thread.currentThread())
                                .getMasterThiefState());
                break;

            case MessageType.PREEXC:
                ((ConcentrationSiteClientProxy) Thread.currentThread())
                        .setOrdinaryThiefId(inMessage.getOrdinaryThiefId());
                int assPartId = concentrationSite.prepareExcursion();
                outMessage = new Message(MessageType.PREEXCDONE,
                        ((ConcentrationSiteClientProxy) Thread.currentThread())
                                .getOrdinaryThiefId(),
                        ((ConcentrationSiteClientProxy) Thread.currentThread())
                                .getOrdinaryThiefState(),
                        assPartId);
                break;

            case MessageType.SUMUPRES:
                concentrationSite.sumUpResults();
                outMessage = new Message(MessageType.SUMUPRESDONE,
                        ((ConcentrationSiteClientProxy) Thread.currentThread())
                                .getMasterThiefState());
                break;

            case MessageType.SHUT:
                concentrationSite.shutdown();
                outMessage = new Message(MessageType.SHUTDONE);
                break;
        }

        return (outMessage);
    }
}
