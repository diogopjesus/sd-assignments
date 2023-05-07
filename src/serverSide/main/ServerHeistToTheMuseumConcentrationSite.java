package serverSide.main;

import serverSide.entities.*;
import serverSide.sharedRegions.*;
import clientSide.stubs.*;
import commInfra.*;
import genclass.GenericIO;
import java.net.*;

public class ServerHeistToTheMuseumConcentrationSite {
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
     *        <li>args[3] - name of the platform where is located the server for the control
     *        collection site</li>
     *        <li>args[4] - port number where the server for the control collection site is
     *        listening to service requests</li>
     *        <li>args[5] - name of the platform where is located the server for the first assault
     *        party</li>
     *        <li>args[6] - port number where the server for the first assault party is listening to
     *        service requests</li>
     *        <li>args[7] - name of the platform where is located the server for the second assault
     *        party</li>
     *        <li>args[8] - port number where the server for the second assault party is listening
     *        to service requests</li>
     *        <li>args[9] - name of the platform where is located the server for the museum</li>
     *        <li>args[10] - port number where the server for the museum is listening to service
     *        requests</li>
     *        </ul>
     */
    public static void main(String[] args) {
        ConcentrationSite concSite; // ConcentrationSite (service to be rendered)
        ConcentrationSiteInterface concSiteInter; // interface to the concentration site
        GeneralRepositoryStub reposStub; // stub to the general repository
        ControlCollectionSiteStub contColSiteStub; // stub to the control collection site
        AssaultPartyStub[] assPartStub; // array of stubs to the assault parties
        MuseumStub museumStub; // stub to the museum
        ServerCom scon, sconi; // communication channels
        int portNumb = -1; // port number for listening to service requests
        String reposServerName; // name of the platform where is located the server for the general
                                // repository
        int reposPortNumb = -1; // port number where the server for the general repository is
                                // listening to service requests
        String contColSiteServerName; // name of the platform where is located the server for the
                                      // control collection site
        int contColSitePortNumb = -1; // port number where the server for the control collection
                                      // site is listening to service requests
        String assPart1ServerName; // name of the platform where is located the server for the first
                                   // assault party
        int assPart1PortNumb = -1; // port number where the server for the first assault party is
                                   // listening to service requests
        String assPart2ServerName; // name of the platform where is located the server for the
                                   // second assault party
        int assPart2PortNumb = -1; // port number where the server for the second assault party is
                                   // listening to service requests
        String museumServerName; // name of the platform where is located the server for the museum
        int museumPortNumb = -1; // port number where the server for the museum is listening to
                                 // service requests

        if (args.length != 11) {
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

        contColSiteServerName = args[3];

        try {
            contColSitePortNumb = Integer.parseInt(args[4]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString("args[4] is not a number!");
            System.exit(1);
        }
        if ((contColSitePortNumb < 4000) || (contColSitePortNumb >= 65536)) {
            GenericIO.writelnString("args[4] is not a valid port number!");
            System.exit(1);
        }

        assPart1ServerName = args[5];

        try {
            assPart1PortNumb = Integer.parseInt(args[6]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString("args[6] is not a number!");
            System.exit(1);
        }
        if ((assPart1PortNumb < 4000) || (assPart1PortNumb >= 65536)) {
            GenericIO.writelnString("args[6] is not a valid port number!");
            System.exit(1);
        }

        assPart2ServerName = args[7];

        try {
            assPart2PortNumb = Integer.parseInt(args[8]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString("args[8] is not a number!");
            System.exit(1);
        }
        if ((assPart2PortNumb < 4000) || (assPart2PortNumb >= 65536)) {
            GenericIO.writelnString("args[8] is not a valid port number!");
            System.exit(1);
        }

        museumServerName = args[9];

        try {
            museumPortNumb = Integer.parseInt(args[10]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString("args[10] is not a number!");
            System.exit(1);
        }
        if ((museumPortNumb < 4000) || (museumPortNumb >= 65536)) {
            GenericIO.writelnString("args[10] is not a valid port number!");
            System.exit(1);
        }

        /* service is established */

        // communication to the general repository is instantiated
        reposStub = new GeneralRepositoryStub(reposServerName, reposPortNumb);
        // communication to the control collection site is instantiated
        contColSiteStub = new ControlCollectionSiteStub(contColSiteServerName, contColSitePortNumb);
        assPartStub = new AssaultPartyStub[2];
        // communication to the first assault party is instantiated
        assPartStub[0] = new AssaultPartyStub(assPart1ServerName, assPart1PortNumb);
        // communication to the second assault party is instantiated
        assPartStub[1] = new AssaultPartyStub(assPart2ServerName, assPart2PortNumb);
        // communication to the museum is instantiated
        museumStub = new MuseumStub(museumServerName, museumPortNumb);
        // service is instantiated
        concSite = new ConcentrationSite(reposStub, contColSiteStub, assPartStub, museumStub);
        // interface to the service is instantiated
        concSiteInter = new ConcentrationSiteInterface(concSite);
        // listening channel at the public port is established
        scon = new ServerCom(portNumb);
        scon.start();
        GenericIO.writelnString("Service is established!");
        GenericIO.writelnString("Server is listening for service requests.");

        /* service request processing */

        ConcentrationSiteClientProxy cliProxy; // service provider agent

        waitConnection = true;
        while (waitConnection) {
            try {
                sconi = scon.accept(); // enter listening procedure
                // start a service provider agent to address
                cliProxy = new ConcentrationSiteClientProxy(sconi, concSiteInter);
                cliProxy.start(); // the request of service
            } catch (SocketTimeoutException e) {
            }
        }
        scon.end(); // operations termination
        GenericIO.writelnString("Server was shutdown.");
    }
}
