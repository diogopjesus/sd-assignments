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
            case MessageType.PREASSPAR:
            case MessageType.PREEXC:
            case MessageType.SUMUPRES:
        }

        /* processing */

        switch (inMessage.getMsgType()) {
            case MessageType.AMINEE:
            case MessageType.PREASSPAR:
            case MessageType.PREEXC:
            case MessageType.SUMUPRES:
        }

        return (outMessage);
    }
}
