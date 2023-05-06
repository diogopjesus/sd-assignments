package serverSide.sharedRegions;

import serverSide.main.*;
import serverSide.entities.*;
import clientSide.entities.*;
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
            case MessageType.CRAWIN:
            case MessageType.REVDIR:
            case MessageType.CRAWOUT:
            case MessageType.SETASSPARTARROO:
            case MessageType.GETASSPARTARROO:
            case MessageType.SETASSPARTARROODIS:
            case MessageType.ASSPARISAVA:
            case MessageType.ASSPARISFUL:
            case MessageType.JOIASSPAR:
            case MessageType.QUIASSPAR:
            case MessageType.SETHOLCAN:
            case MessageType.ISHOLCAN:
            case MessageType.GETTHIELEINASSPAR:
            case MessageType.SHUT:
        }

        /* processing */

        switch (inMessage.getMsgType()) {
            case MessageType.SENASSPAR:
            case MessageType.CRAWIN:
            case MessageType.REVDIR:
            case MessageType.CRAWOUT:
            case MessageType.SETASSPARTARROO:
            case MessageType.GETASSPARTARROO:
            case MessageType.SETASSPARTARROODIS:
            case MessageType.ASSPARISAVA:
            case MessageType.ASSPARISFUL:
            case MessageType.JOIASSPAR:
            case MessageType.QUIASSPAR:
            case MessageType.SETHOLCAN:
            case MessageType.ISHOLCAN:
            case MessageType.GETTHIELEINASSPAR:
            case MessageType.SHUT:
        }

        return (outMessage);
    }
}
