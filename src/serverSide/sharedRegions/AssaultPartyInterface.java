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
            case MessageType.SEND_ASSAULT_PARTY:
                break;

            case MessageType.CRAWL_IN:
                if ((inMessage.getOtId() < 0) || (inMessage.getOtId() >= (SimulPar.M - 1)))
                    throw new MessageException("Invalid ordinary thief id!", inMessage);
                if ((inMessage.getMaxDis() < SimulPar.md) || (inMessage.getMaxDis() > SimulPar.MD))
                    throw new MessageException("Invalid maximum displacement!", inMessage);
                break;

            case MessageType.REVERSE_DIRECTION:
                if ((inMessage.getOtId() < 0) || (inMessage.getOtId() >= (SimulPar.M - 1)))
                    throw new MessageException("Invalid ordinary thief id!", inMessage);
                break;

            case MessageType.CRAWL_OUT:
                if ((inMessage.getOtId() < 0) || (inMessage.getOtId() >= (SimulPar.M - 1)))
                    throw new MessageException("Invalid ordinary thief id!", inMessage);
                if ((inMessage.getMaxDis() < SimulPar.md) || (inMessage.getMaxDis() > SimulPar.MD))
                    throw new MessageException("Invalid maximum displacement!", inMessage);
                break;

            case MessageType.SET_TARGET_ROOM:
                if ((inMessage.getRoomId() < 0) || (inMessage.getRoomId() >= SimulPar.N))
                    throw new MessageException("Invalid room id!", inMessage);
                break;

            case MessageType.GET_TARGET_ROOM:
                break;

            case MessageType.SET_TARGET_ROOM_DISTANCE:
                if ((inMessage.getDistance() < SimulPar.d)
                        || (inMessage.getDistance() > SimulPar.D)) {
                    throw new MessageException("Invalid distance!", inMessage);
                }
                break;

            case MessageType.IS_AVAILABLE:
                break;

            case MessageType.IS_FULL:
                break;

            case MessageType.JOIN_ASSAULT_PARTY:
                if ((inMessage.getOtId() < 0) || (inMessage.getOtId() >= (SimulPar.M - 1)))
                    throw new MessageException("Invalid ordinary thief id!", inMessage);
                break;

            case MessageType.QUIT_ASSAULT_PARTY:
                if ((inMessage.getOtId() < 0) || (inMessage.getOtId() >= (SimulPar.M - 1)))
                    throw new MessageException("Invalid ordinary thief id!", inMessage);
                break;

            case MessageType.SET_HOLDING_CANVAS:
                if ((inMessage.getOtId() < 0) || (inMessage.getOtId() >= (SimulPar.M - 1)))
                    throw new MessageException("Invalid ordinary thief id!", inMessage);
                break;

            case MessageType.IS_HOLDING_CANVAS:
                if ((inMessage.getOtId() < 0) || (inMessage.getOtId() >= (SimulPar.M - 1)))
                    throw new MessageException("Invalid ordinary thief id!", inMessage);
                break;

            case MessageType.GET_THIEF_ELEMENT:
                if ((inMessage.getOtId() < 0) || (inMessage.getOtId() >= (SimulPar.M - 1)))
                    throw new MessageException("Invalid ordinary thief id!", inMessage);
                break;

            case MessageType.SHUTDOWN:
                break;

            default:
                throw new MessageException("Invalid message type!", inMessage);
        }

        /* processing */
        int thiefId;

        switch (inMessage.getMsgType()) {
            case MessageType.SEND_ASSAULT_PARTY:
                assaultParty.sendAssaultParty();
                outMessage = new Message(MessageType.SEND_ASSAULT_PARTY_DONE,
                        ((AssaultPartyClientProxy) Thread.currentThread()).getMasterThiefState());
                break;

            case MessageType.CRAWL_IN:
                ((AssaultPartyClientProxy) Thread.currentThread())
                        .setOrdinaryThiefId(inMessage.getOtId());
                ((AssaultPartyClientProxy) Thread.currentThread())
                        .setMaximumDisplacement(inMessage.getMaxDis());
                if (assaultParty.crawlIn()) {
                    outMessage = new Message(MessageType.CRAWL_IN_DONE,
                            ((AssaultPartyClientProxy) Thread.currentThread()).getOrdinaryThiefId(),
                            ((AssaultPartyClientProxy) Thread.currentThread())
                                    .getOrdinaryThiefState(),
                            true);
                } else {
                    outMessage = new Message(MessageType.CRAWL_IN_DONE,
                            ((AssaultPartyClientProxy) Thread.currentThread()).getOrdinaryThiefId(),
                            ((AssaultPartyClientProxy) Thread.currentThread())
                                    .getOrdinaryThiefState(),
                            false);
                }
                break;

            case MessageType.REVERSE_DIRECTION:
                ((AssaultPartyClientProxy) Thread.currentThread())
                        .setOrdinaryThiefId(inMessage.getOtId());
                assaultParty.reverseDirection();
                outMessage = new Message(MessageType.REVERSE_DIRECTION_DONE,
                        ((AssaultPartyClientProxy) Thread.currentThread()).getOrdinaryThiefId(),
                        ((AssaultPartyClientProxy) Thread.currentThread()).getOrdinaryThiefState());
                break;

            case MessageType.CRAWL_OUT:
                ((AssaultPartyClientProxy) Thread.currentThread())
                        .setOrdinaryThiefId(inMessage.getOtId());
                ((AssaultPartyClientProxy) Thread.currentThread())
                        .setMaximumDisplacement(inMessage.getMaxDis());
                if (assaultParty.crawlOut()) {
                    outMessage = new Message(MessageType.CRAWL_OUT_DONE,
                            ((AssaultPartyClientProxy) Thread.currentThread()).getOrdinaryThiefId(),
                            ((AssaultPartyClientProxy) Thread.currentThread())
                                    .getOrdinaryThiefState(),
                            true);
                } else {
                    outMessage = new Message(MessageType.CRAWL_OUT_DONE,
                            ((AssaultPartyClientProxy) Thread.currentThread()).getOrdinaryThiefId(),
                            ((AssaultPartyClientProxy) Thread.currentThread())
                                    .getOrdinaryThiefState(),
                            false);
                }
                break;

            case MessageType.SET_TARGET_ROOM:
                assaultParty.setTargetRoom(inMessage.getRoomId());
                outMessage = new Message(MessageType.SET_TARGET_ROOM_DONE);
                break;

            case MessageType.GET_TARGET_ROOM:
                int roomId = assaultParty.getTargetRoom();
                outMessage = new Message(MessageType.GET_TARGET_ROOM_DONE, roomId);
                break;

            case MessageType.SET_TARGET_ROOM_DISTANCE:
                assaultParty.setTargetRoomDistance(inMessage.getDistance());
                outMessage = new Message(MessageType.SET_TARGET_ROOM_DISTANCE_DONE);
                break;

            case MessageType.IS_AVAILABLE:
                boolean isAvailable = assaultParty.isAvailable();
                outMessage = new Message(MessageType.IS_AVAILABLE_DONE, isAvailable);
                break;

            case MessageType.IS_FULL:
                boolean isFull = assaultParty.isFull();
                outMessage = new Message(MessageType.IS_FULL_DONE, isFull);
                break;

            case MessageType.JOIN_ASSAULT_PARTY:
                thiefId = inMessage.getOtId();
                assaultParty.joinAssaultParty(thiefId);
                outMessage = new Message(MessageType.JOIN_ASSAULT_PARTY_DONE);
                break;

            case MessageType.QUIT_ASSAULT_PARTY:
                thiefId = inMessage.getOtId();
                assaultParty.quitAssaultParty(thiefId);
                outMessage = new Message(MessageType.QUIT_ASSAULT_PARTY_DONE);
                break;

            case MessageType.SET_HOLDING_CANVAS:
                thiefId = inMessage.getOtId();
                assaultParty.setHoldingCanvas(thiefId, inMessage.isCanvas());
                outMessage = new Message(MessageType.SET_HOLDING_CANVAS_DONE);
                break;

            case MessageType.IS_HOLDING_CANVAS:
                thiefId = inMessage.getOtId();
                boolean holdingCanvas = assaultParty.isHoldingCanvas(thiefId);
                outMessage = new Message(MessageType.IS_HOLDING_CANVAS_DONE, holdingCanvas);
                break;

            case MessageType.GET_THIEF_ELEMENT:
                thiefId = inMessage.getOtId();
                int element = assaultParty.getThiefElement(thiefId);
                outMessage = new Message(MessageType.GET_THIEF_ELEMENT_DONE, element);
                break;

            case MessageType.SHUTDOWN:
                assaultParty.shutdown();
                outMessage = new Message(MessageType.SHUTDOWN_DONE);
                break;
        }

        return (outMessage);
    }
}
