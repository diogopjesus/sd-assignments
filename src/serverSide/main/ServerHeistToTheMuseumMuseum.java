package serverSide.main;

import serverSide.entities.*;
import serverSide.sharedRegions.*;
import clientSide.stubs.*;
import commInfra.*;
import genclass.GenericIO;
import java.net.*;

/**
 * Server side of the Museum.
 *
 * Implementation of a client-server model of type 2 (server replication). Communication is based on
 * a communication channel under the TCP protocol.
 */
public class ServerHeistToTheMuseumMuseum {
    /**
     * Flag signaling the service is active.
     */
    public static boolean waitConnection;

    /**
     * Main method.
     *
     * @param args runtime arguments
     *        <ul>
     *        <li>args[0] - port number for listening to service requests</li>
     *        <li>args[1] - name of the platform where is located the server for the general
     *        repository</li>
     *        <li>args[2] - port number where the server for the general repository is listening to
     *        service requests</li>
     *        <li>args[3] - name of the platform where is located the server for the first assault
     *        party</li>
     *        <li>args[4] - port number where the server for the first assault party is listening to
     *        service requests</li>
     *        <li>args[5] - name of the platform where is located the server for the second assault
     *        party</li>
     *        <li>args[6] - port number where the server for the second assault party is listening
     *        to service requests</li>
     *        </ul>
     */
    public static void main(String[] args) {
        Museum museum; // Museum (service to be rendered)
        MuseumInterface museumInter; // interface to the museum
        GeneralRepositoryStub reposStub; // stub to the general repository
        AssaultPartyStub[] assPartStub; // array of stubs to the assault parties
        ServerCom scon, sconi; // communication channels
        int portNumb = -1; // port number for listening to service requests
        String reposServerName; // name of the platform where is located the server for the general
                                // repository
        int reposPortNumb = -1; // port number where the server for the general repository is
                                // listening to service requests
        String assPart1ServerName; // name of the platform where is located the server for the first
                                   // assault party
        int assPart1PortNumb = -1; // port number where the server for the first assault party is
                                   // listening to service requests
        String assPart2ServerName; // name of the platform where is located the server for the
                                   // second assault party
        int assPart2PortNumb = -1; // port number where the server for the second assault party is
                                   // listening to service requests

        if (args.length != 7) {
            GenericIO.writelnString("Wrong number of parameters!");
            System.exit(1);
        }

        try {
            portNumb = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString("args[0] is not a number!");
            System.exit(1);
        }
        if ((portNumb < 4000) || (portNumb >= 65536)) {
            GenericIO.writelnString("args[0] is not a valid port number!");
            System.exit(1);
        }

        reposServerName = args[1];

        try {
            reposPortNumb = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString("args[2] is not a number!");
            System.exit(1);
        }
        if ((reposPortNumb < 4000) || (reposPortNumb >= 65536)) {
            GenericIO.writelnString("args[2] is not a valid port number!");
            System.exit(1);
        }

        assPart1ServerName = args[3];

        try {
            assPart1PortNumb = Integer.parseInt(args[4]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString("args[4] is not a number!");
            System.exit(1);
        }
        if ((assPart1PortNumb < 4000) || (assPart1PortNumb >= 65536)) {
            GenericIO.writelnString("args[4] is not a valid port number!");
            System.exit(1);
        }

        assPart2ServerName = args[5];

        try {
            assPart2PortNumb = Integer.parseInt(args[6]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString("args[6] is not a number!");
            System.exit(1);
        }
        if ((assPart2PortNumb < 4000) || (assPart2PortNumb >= 65536)) {
            GenericIO.writelnString("args[6] is not a valid port number!");
            System.exit(1);
        }

        /* service is established */

        // communication to the general repository is instantiated
        reposStub = new GeneralRepositoryStub(reposServerName, reposPortNumb);
        assPartStub = new AssaultPartyStub[2];
        // communication to the first assault party is instantiated
        assPartStub[0] = new AssaultPartyStub(assPart1ServerName, assPart1PortNumb);
        // communication to the second assault party is instantiated
        assPartStub[1] = new AssaultPartyStub(assPart2ServerName, assPart2PortNumb);
        // service is instantiated
        museum = new Museum(reposStub, assPartStub);
        // interface to the service is instantiated
        museumInter = new MuseumInterface(museum);
        // listening channel at the public port is established
        scon = new ServerCom(portNumb);
        scon.start();
        GenericIO.writelnString("Service is established!");
        GenericIO.writelnString("Server is listening for service requests.");

        /* service request processing */

        MuseumClientProxy cliProxy; // service provider agent

        waitConnection = true;
        while (waitConnection) {
            try {
                sconi = scon.accept(); // enter listening procedure
                // start a service provider agent to address
                cliProxy = new MuseumClientProxy(sconi, museumInter);
                cliProxy.start(); // the request of service
            } catch (SocketTimeoutException e) {
            }
        }
        scon.end(); // operations termination
        GenericIO.writelnString("Server was shutdown.");
    }
}
