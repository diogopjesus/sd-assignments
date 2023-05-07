package serverSide.sharedRegions;

import serverSide.main.*;
import serverSide.entities.*;
import commInfra.*;

/**
 * Interface to the assault party.
 *
 * It is responsible to validate and process the incoming message, execute the corresponding method
 * on the assault party and generate the outgoing message. Implementation of a client-server model
 * of type 2 (server replication). Communication is based on a communication channel under the TCP
 * protocol.
 */
public class AssaultPartyInterface {
    /**
     * Reference to the assault party.
     */
    private final AssaultParty assaultParty;

    /**
     * Instantiation of an interface to the assault party.
     *
     * @param assaultParty reference to the assault party.
     */
    public AssaultPartyInterface(AssaultParty assaultParty) {
        this.assaultParty = assaultParty;
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
            case MessageType.SENASSPAR:
                break;

            case MessageType.CRAWIN:
                if ((inMessage.getOrdinaryThiefId() < 0)
                        || (inMessage.getOrdinaryThiefId() >= (SimulPar.M - 1)))
                    throw new MessageException("Invalid ordinary thief id!", inMessage);
                if ((inMessage.getMaximumDisplacement() < SimulPar.md)
                        || (inMessage.getMaximumDisplacement() > SimulPar.MD))
                    throw new MessageException("Invalid maximum displacement!", inMessage);
                break;

            case MessageType.REVDIR:
                if ((inMessage.getOrdinaryThiefId() < 0)
                        || (inMessage.getOrdinaryThiefId() >= (SimulPar.M - 1)))
                    throw new MessageException("Invalid ordinary thief id!", inMessage);
                break;

            case MessageType.CRAWOUT:
                if ((inMessage.getOrdinaryThiefId() < 0)
                        || (inMessage.getOrdinaryThiefId() >= (SimulPar.M - 1)))
                    throw new MessageException("Invalid ordinary thief id!", inMessage);
                if ((inMessage.getMaximumDisplacement() < SimulPar.md)
                        || (inMessage.getMaximumDisplacement() > SimulPar.MD))
                    throw new MessageException("Invalid maximum displacement!", inMessage);
                break;

            case MessageType.SETASSPARTARROO:
                if ((inMessage.getRoomId() < 0) || (inMessage.getRoomId() >= SimulPar.N))
                    throw new MessageException("Invalid room id!", inMessage);
                break;

            case MessageType.GETASSPARTARROO:
                break;

            case MessageType.SETASSPARTARROODIS:
                if ((inMessage.getDistance() < SimulPar.d)
                        || (inMessage.getDistance() > SimulPar.D)) {
                    throw new MessageException("Invalid distance!", inMessage);
                }
                break;

            case MessageType.ASSPARISAVA:
                break;

            case MessageType.ASSPARISFUL:
                break;

            case MessageType.JOIASSPAR:
                if ((inMessage.getOrdinaryThiefId() < 0)
                        || (inMessage.getOrdinaryThiefId() >= (SimulPar.M - 1)))
                    throw new MessageException("Invalid ordinary thief id!", inMessage);
                break;

            case MessageType.QUIASSPAR:
                if ((inMessage.getOrdinaryThiefId() < 0)
                        || (inMessage.getOrdinaryThiefId() >= (SimulPar.M - 1)))
                    throw new MessageException("Invalid ordinary thief id!", inMessage);
                break;

            case MessageType.SETHOLCAN:
                if ((inMessage.getOrdinaryThiefId() < 0)
                        || (inMessage.getOrdinaryThiefId() >= (SimulPar.M - 1)))
                    throw new MessageException("Invalid ordinary thief id!", inMessage);
                break;

            case MessageType.ISHOLCAN:
                if ((inMessage.getOrdinaryThiefId() < 0)
                        || (inMessage.getOrdinaryThiefId() >= (SimulPar.M - 1)))
                    throw new MessageException("Invalid ordinary thief id!", inMessage);
                break;

            case MessageType.GETTHIELEINASSPAR:
                if ((inMessage.getOrdinaryThiefId() < 0)
                        || (inMessage.getOrdinaryThiefId() >= (SimulPar.M - 1)))
                    throw new MessageException("Invalid ordinary thief id!", inMessage);
                break;

            case MessageType.SHUT:
                break;

            default:
                throw new MessageException("Invalid message type!", inMessage);
        }

        /* processing */
        int thiefId;

        switch (inMessage.getMsgType()) {
            case MessageType.SENASSPAR:
                assaultParty.sendAssaultParty();
                outMessage = new Message(MessageType.SENASSPARDONE,
                        ((AssaultPartyClientProxy) Thread.currentThread()).getMasterThiefState());
                break;

            case MessageType.CRAWIN:
                ((AssaultPartyClientProxy) Thread.currentThread())
                        .setOrdinaryThiefId(inMessage.getOrdinaryThiefId());
                ((AssaultPartyClientProxy) Thread.currentThread())
                        .setMaximumDisplacement(inMessage.getMaximumDisplacement());
                if (assaultParty.crawlIn()) {
                    outMessage = new Message(MessageType.CRAWINDONE,
                            ((AssaultPartyClientProxy) Thread.currentThread()).getOrdinaryThiefId(),
                            ((AssaultPartyClientProxy) Thread.currentThread())
                                    .getOrdinaryThiefState(),
                            true);
                } else {
                    outMessage = new Message(MessageType.CRAWINDONE,
                            ((AssaultPartyClientProxy) Thread.currentThread()).getOrdinaryThiefId(),
                            ((AssaultPartyClientProxy) Thread.currentThread())
                                    .getOrdinaryThiefState(),
                            false);
                }
                break;

            case MessageType.REVDIR:
                ((AssaultPartyClientProxy) Thread.currentThread())
                        .setOrdinaryThiefId(inMessage.getOrdinaryThiefId());
                assaultParty.reverseDirection();
                outMessage = new Message(MessageType.REVDIRDONE,
                        ((AssaultPartyClientProxy) Thread.currentThread()).getOrdinaryThiefId(),
                        ((AssaultPartyClientProxy) Thread.currentThread()).getOrdinaryThiefState());
                break;

            case MessageType.CRAWOUT:
                ((AssaultPartyClientProxy) Thread.currentThread())
                        .setOrdinaryThiefId(inMessage.getOrdinaryThiefId());
                ((AssaultPartyClientProxy) Thread.currentThread())
                        .setMaximumDisplacement(inMessage.getMaximumDisplacement());
                if (assaultParty.crawlOut()) {
                    outMessage = new Message(MessageType.CRAWOUTDONE,
                            ((AssaultPartyClientProxy) Thread.currentThread()).getOrdinaryThiefId(),
                            ((AssaultPartyClientProxy) Thread.currentThread())
                                    .getOrdinaryThiefState(),
                            true);
                } else {
                    outMessage = new Message(MessageType.CRAWOUTDONE,
                            ((AssaultPartyClientProxy) Thread.currentThread()).getOrdinaryThiefId(),
                            ((AssaultPartyClientProxy) Thread.currentThread())
                                    .getOrdinaryThiefState(),
                            false);
                }
                break;

            case MessageType.SETASSPARTARROO:
                assaultParty.setTargetRoom(inMessage.getRoomId());
                outMessage = new Message(MessageType.SETASSPARTARROODONE);
                break;

            case MessageType.GETASSPARTARROO:
                int roomId = assaultParty.getTargetRoom();
                outMessage = new Message(MessageType.GETASSPARTARROODONE, roomId);
                break;

            case MessageType.SETASSPARTARROODIS:
                assaultParty.setTargetRoomDistance(inMessage.getDistance());
                outMessage = new Message(MessageType.SETASSPARTARROODISDONE);
                break;

            case MessageType.ASSPARISAVA:
                boolean isAvailable = assaultParty.isAvailable();
                outMessage = new Message(MessageType.ASSPARISAVADONE, isAvailable);
                break;

            case MessageType.ASSPARISFUL:
                boolean isFull = assaultParty.isFull();
                outMessage = new Message(MessageType.ASSPARISFULDONE, isFull);
                break;

            case MessageType.JOIASSPAR:
                thiefId = inMessage.getOrdinaryThiefId();
                assaultParty.joinAssaultParty(thiefId);
                outMessage = new Message(MessageType.JOIASSPARDONE);
                break;

            case MessageType.QUIASSPAR:
                thiefId = inMessage.getOrdinaryThiefId();
                assaultParty.quitAssaultParty(thiefId);
                outMessage = new Message(MessageType.QUIASSPARDONE);
                break;

            case MessageType.SETHOLCAN:
                thiefId = inMessage.getOrdinaryThiefId();
                assaultParty.setHoldingCanvas(thiefId, inMessage.isCanvas());
                outMessage = new Message(MessageType.SETHOLCANDONE);
                break;

            case MessageType.ISHOLCAN:
                thiefId = inMessage.getOrdinaryThiefId();
                boolean holdingCanvas = assaultParty.isHoldingCanvas(thiefId);
                outMessage = new Message(MessageType.ISHOLCANDONE, holdingCanvas);
                break;

            case MessageType.GETTHIELEINASSPAR:
                thiefId = inMessage.getOrdinaryThiefId();
                int element = assaultParty.getThiefElement(thiefId);
                outMessage = new Message(MessageType.GETTHIELEINASSPARDONE, element);

            case MessageType.SHUT:
                assaultParty.shutdown();
                outMessage = new Message(MessageType.SHUTDONE);
                break;
        }

        return (outMessage);
    }
}
