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
            case MessageType.AM_I_NEEDED:
                if ((inMessage.getOtId() < 0) || (inMessage.getOtId() >= SimulPar.M - 1))
                    throw new MessageException("Invalid Ordinary Thief Id!", inMessage);
                if ((inMessage.getOtState() < OrdinaryThiefStates.CONCENTRATION_SITE)
                        || (inMessage.getOtState() > OrdinaryThiefStates.COLLECTION_SITE))
                    throw new MessageException("Invalid Ordinary Thief State!", inMessage);
                break;

            case MessageType.PREPARE_ASSAULT_PARTY:
                if ((inMessage.getAssPartId() < 0) || (inMessage.getAssPartId() >= SimulPar.K))
                    throw new MessageException("Invalid Assault Party Id!", inMessage);
                if ((inMessage.getRoomId() < 0) || (inMessage.getRoomId() >= SimulPar.N))
                    throw new MessageException("Invalid Room Id!", inMessage);
                break;

            case MessageType.PREPARE_EXCURSION:
                if ((inMessage.getOtId() < 0) || (inMessage.getOtId() >= SimulPar.M - 1))
                    throw new MessageException("Invalid Ordinary Thief Id!", inMessage);
                break;

            case MessageType.SUM_UP_RESULTS:
                break;

            case MessageType.SHUTDOWN:
                break;

            default:
                throw new MessageException("Invalid message type!", inMessage);
        }

        /* processing */

        switch (inMessage.getMsgType()) {
            case MessageType.AM_I_NEEDED:
                ((ConcentrationSiteClientProxy) Thread.currentThread())
                        .setOrdinaryThiefId(inMessage.getOtId());
                ((ConcentrationSiteClientProxy) Thread.currentThread())
                        .setOrdinaryThiefState(inMessage.getOtState());
                boolean isNeeded = concentrationSite.amINeeded();
                outMessage = new Message(MessageType.AM_I_NEEDED_DONE,
                        ((ConcentrationSiteClientProxy) Thread.currentThread())
                                .getOrdinaryThiefId(),
                        ((ConcentrationSiteClientProxy) Thread.currentThread())
                                .getOrdinaryThiefState(),
                        isNeeded);
                break;

            case MessageType.PREPARE_ASSAULT_PARTY:
                concentrationSite.prepareAssaultParty(inMessage.getAssPartId(),
                        inMessage.getRoomId());
                outMessage = new Message(MessageType.PREPARE_ASSAULT_PARTY_DONE,
                        ((ConcentrationSiteClientProxy) Thread.currentThread())
                                .getMasterThiefState());
                break;

            case MessageType.PREPARE_EXCURSION:
                ((ConcentrationSiteClientProxy) Thread.currentThread())
                        .setOrdinaryThiefId(inMessage.getOtId());
                int assPartId = concentrationSite.prepareExcursion();
                outMessage = new Message(MessageType.PREPARE_EXCURSION_DONE,
                        ((ConcentrationSiteClientProxy) Thread.currentThread())
                                .getOrdinaryThiefId(),
                        ((ConcentrationSiteClientProxy) Thread.currentThread())
                                .getOrdinaryThiefState(),
                        assPartId);
                break;

            case MessageType.SUM_UP_RESULTS:
                concentrationSite.sumUpResults();
                outMessage = new Message(MessageType.SUM_UP_RESULTS_DONE,
                        ((ConcentrationSiteClientProxy) Thread.currentThread())
                                .getMasterThiefState());
                break;

            case MessageType.SHUTDOWN:
                concentrationSite.shutdown();
                outMessage = new Message(MessageType.SHUTDOWN_DONE);
                break;
        }

        return (outMessage);
    }
}
