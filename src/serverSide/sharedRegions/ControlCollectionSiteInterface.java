package serverSide.sharedRegions;

import serverSide.main.*;
import serverSide.entities.*;
import clientSide.entities.*;
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
            case MessageType.APPSIT:
            case MessageType.TAKARES:
            case MessageType.HANACAN:
            case MessageType.COLACAN:
            case MessageType.GETAVAASSPAR:
            case MessageType.GETAVAROO:
            case MessageType.SETTHITOPAR:
        }

        /* processing */

        switch (inMessage.getMsgType()) {
            case MessageType.STAOPE:
            case MessageType.APPSIT:
            case MessageType.TAKARES:
            case MessageType.HANACAN:
            case MessageType.COLACAN:
            case MessageType.GETAVAASSPAR:
            case MessageType.GETAVAROO:
            case MessageType.SETTHITOPAR:
        }

        return (outMessage);
    }
}
