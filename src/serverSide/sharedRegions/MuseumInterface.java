package serverSide.sharedRegions;

import serverSide.main.*;
import serverSide.entities.*;
import commInfra.*;

/**
 * Interface to the museum.
 *
 * It is responsible to validate and process the incoming message, execute the corresponding method
 * on the museum and generate the outgoing message. Implementation of a client-server model of type
 * 2 (server replication). Communication is based on a communication channel under the TCP protocol.
 */
public class MuseumInterface {
    /**
     * Reference to the museum.
     */
    private final Museum museum;

    /**
     * Instantiation of an interface to the museum.
     *
     * @param museum reference to the museum.
     */
    public MuseumInterface(Museum museum) {
        this.museum = museum;
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
            case MessageType.ROLACAN:
                if ((inMessage.getOrdinaryThiefId() < 0)
                        || (inMessage.getOrdinaryThiefId() >= SimulPar.M - 1))
                    throw new MessageException("Invalid Ordinary Thief Id!", inMessage);
                if ((inMessage.getAssaultPartyId() < 0)
                        || (inMessage.getAssaultPartyId() >= SimulPar.N))
                    throw new MessageException("Invalid Assault Party Id!", inMessage);
                break;

            case MessageType.GETROODIS:
                if (inMessage.getRoomId() < 0 || inMessage.getRoomId() >= SimulPar.N)
                    throw new MessageException("Invalid Room Id!", inMessage);
                break;

            case MessageType.SHUT:
                break;

            default:
                throw new MessageException("Invalid message type!", inMessage);
        }

        /* processing */

        switch (inMessage.getMsgType()) {
            case MessageType.ROLACAN:
                ((MuseumClientProxy) Thread.currentThread())
                        .setOrdinaryThiefId(inMessage.getOrdinaryThiefId());
                museum.rollACanvas(inMessage.getAssaultPartyId());
                outMessage = new Message(MessageType.ROLACANDONE,
                        ((MuseumClientProxy) Thread.currentThread()).getOrdinaryThiefId());
                break;

            case MessageType.GETROODIS:
                int roomDis = museum.getRoomDistance(inMessage.getRoomId());
                outMessage = new Message(MessageType.GETROODISDONE, roomDis);
                break;

            case MessageType.SHUT:
                museum.shutdown();
                outMessage = new Message(MessageType.SHUTDONE);
                break;
        }

        return (outMessage);
    }
}
