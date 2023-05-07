package serverSide.main;

import serverSide.entities.*;
import serverSide.sharedRegions.*;
import clientSide.stubs.*;
import commInfra.*;
import genclass.GenericIO;
import java.net.*;

/**
 * Server side of the Assault Party.
 *
 * Implementation of a client-server model of type 2 (server replication). Communication is based on
 * a communication channel under the TCP protocol.
 */
public class ServerHeistToTheMuseumAssaultParty {
    /**
     * Flag signaling the service is active.
     */
    public static boolean waitConnection;

    /**
     * Main method.
     *
     * @param args runtime arguments
     *        <ul>
     *        <li>args[0] - identification of the assault party</li>
     *        <li>args[1] - port number for listening to service requests</li>
     *        <li>args[2] - name of the platform where is located the server for the general
     *        repository</li>
     *        <li>args[3] - port number where the server for the general repository is listening to
     *        service requests</li>
     *        </ul>
     */
    public static void main(String[] args) {
        AssaultParty assPart; // AssaultParty (service to be rendered)
        AssaultPartyInterface assPartInter; // interface to the assault party
        GeneralRepositoryStub reposStub; // stub to the general repository
        ServerCom scon, sconi; // communication channels
        int assPartId = -1; // identification of the assault party
        int portNumb = -1; // port number for listening to service requests
        String reposServerName; // name of the platform where is located the server for the general
                                // repository
        int reposPortNumb = -1; // port number where the server for the general repository is
                                // listening to service requests

        if (args.length != 4) {
            GenericIO.writelnString("Wrong number of parameters!");
            System.exit(1);
        }

        try {
            assPartId = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString("args[0] is not a number!");
            System.exit(1);
        }

        try {
            portNumb = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString("args[1] is not a number!");
            System.exit(1);
        }
        if ((portNumb < 4000) || (portNumb >= 65536)) {
            GenericIO.writelnString("args[1] is not a valid port number!");
            System.exit(1);
        }

        reposServerName = args[2];

        try {
            reposPortNumb = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString("args[3] is not a number!");
            System.exit(1);
        }
        if ((reposPortNumb < 4000) || (reposPortNumb >= 65536)) {
            GenericIO.writelnString("args[3] is not a valid port number!");
            System.exit(1);
        }

        /* service is established */

        // communication to the general repository is instantiated
        reposStub = new GeneralRepositoryStub(reposServerName, reposPortNumb);
        // service is instantiated
        assPart = new AssaultParty(reposStub, assPartId);
        // interface to the service is instantiated
        assPartInter = new AssaultPartyInterface(assPart);
        // listening channel at the public port is established
        scon = new ServerCom(portNumb);
        scon.start();
        GenericIO.writelnString("Service is established!");
        GenericIO.writelnString("Server is listening for service requests.");

        /* service request processing */

        AssaultPartyClientProxy cliProxy; // service provider agent

        waitConnection = true;
        while (waitConnection) {
            try {
                sconi = scon.accept(); // enter listening procedure
                cliProxy = new AssaultPartyClientProxy(sconi, assPartInter); // start a service
                                                                             // provider
                                                                             // agent to address
                cliProxy.start(); // the request of service
            } catch (SocketTimeoutException e) {
            }
        }
        scon.end(); // operations termination
        GenericIO.writelnString("Server was shutdown.");
    }
}
