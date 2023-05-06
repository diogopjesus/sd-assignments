package serverSide.sharedRegions;

import serverSide.main.*;
import serverSide.entities.*;
import clientSide.entities.*;
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
            case MessageType.GETROODIS:
        }

        /* processing */

        switch (inMessage.getMsgType()) {
            case MessageType.ROLACAN:
            case MessageType.GETROODIS:
        }

        return (outMessage);
    }
}
