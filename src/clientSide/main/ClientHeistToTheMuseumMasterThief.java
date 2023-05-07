package clientSide.main;

import clientSide.entities.*;
import clientSide.stubs.*;
import genclass.GenericIO;

/**
 * Client side of the Heist to the Museum (master thief).
 *
 * Implementation of a client-server model of type 2 (server replication). Communication is based on
 * a communication channel under the TCP protocol.
 */
public class ClientHeistToTheMuseumMasterThief {
    /**
     * Main method.
     *
     * @param args runtime arguments
     *        <ul>
     *        <li>args[0] - name of the platform where is located the first assault party
     *        server</li>
     *        <li>args[1] - port number for listening to service requests</li>
     *        <li>args[2] - name of the platform where is located the second assault party
     *        server</li>
     *        <li>args[3] - port number for listening to service requests</li>
     *        <li>args[4] - name of the platform where is located the concentration site server</li>
     *        <li>args[5] - port number for listening to service requests</li>
     *        <li>args[6] - name of the platform where is located the control collection site
     *        server</li>
     *        <li>args[7] - port number for listening to service requests</li>
     *        <li>args[8] - name of the platform where is located the museum server</li>
     *        <li>args[9] - port number for listening to service requests</li>
     *        <li>args[10] - name of the platform where is located the general repository
     *        server</li>
     *        <li>args[11] - port number for listening to service requests</li>
     *        </ul>
     */
    public static void main(String[] args) {
        String assaultParty1ServerHostName; // name of the platform where is located the first
                                            // assault party server
        int assaultParty1ServerPortNumb = -1; // port number for listening to service requests
        String assaultParty2ServerHostName; // name of the platform where is located the second
                                            // assault party server
        int assaultParty2ServerPortNumb = -1; // port number for listening to service requests
        String concSiteServerHostName; // name of the platform where is located the concentration
                                       // site server
        int concSiteServerPortNumb = -1; // port number for listening to service requests
        String contColSiteServerHostName; // name of the platform where is located the control
                                          // collection site server
        int contColSiteServerPortNumb = -1; // port number for listening to service requests
        String museumServerHostName; // name of the platform where is located the museum server
        int museumServerPortNumb = -1; // port number for listening to service requests
        String reposServerHostName; // name of the platform where is located the general
                                    // repository server
        int reposServerPortNumb = -1; // port number for listening to service requests
        MasterThief masterThief; // master thief thread
        AssaultPartyStub[] assPartStub; // remote reference to the barber shop
        ConcentrationSiteStub concSiteStub; // remote reference to the concentration site
        ControlCollectionSiteStub contColSiteStub; // remote reference to the control collection
                                                   // site
        MuseumStub museumStub; // remote reference to the museum
        GeneralRepositoryStub reposStub; // remote reference to the general repository

        /* getting problem runtime parameters */

        if (args.length != 12) {
            GenericIO.writelnString("Wrong number of parameters!");
            System.exit(1);
        }

        assaultParty1ServerHostName = args[0];
        try {
            assaultParty1ServerPortNumb = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString("args[1] is not a number!");
            System.exit(1);
        }
        if ((assaultParty1ServerPortNumb < 4000) || (assaultParty1ServerPortNumb >= 65536)) {
            GenericIO.writelnString("args[1] is not a valid port number!");
            System.exit(1);
        }

        assaultParty2ServerHostName = args[2];
        try {
            assaultParty2ServerPortNumb = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString("args[3] is not a number!");
            System.exit(1);
        }
        if ((assaultParty2ServerPortNumb < 4000) || (assaultParty2ServerPortNumb >= 65536)) {
            GenericIO.writelnString("args[3] is not a valid port number!");
            System.exit(1);
        }

        concSiteServerHostName = args[4];
        try {
            concSiteServerPortNumb = Integer.parseInt(args[5]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString("args[5] is not a number!");
            System.exit(1);
        }
        if ((concSiteServerPortNumb < 4000) || (concSiteServerPortNumb >= 65536)) {
            GenericIO.writelnString("args[5] is not a valid port number!");
            System.exit(1);
        }

        contColSiteServerHostName = args[6];
        try {
            contColSiteServerPortNumb = Integer.parseInt(args[7]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString("args[7] is not a number!");
            System.exit(1);
        }
        if ((contColSiteServerPortNumb < 4000) || (contColSiteServerPortNumb >= 65536)) {
            GenericIO.writelnString("args[7] is not a valid port number!");
            System.exit(1);
        }

        museumServerHostName = args[8];
        try {
            museumServerPortNumb = Integer.parseInt(args[9]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString("args[9] is not a number!");
            System.exit(1);
        }
        if ((museumServerPortNumb < 4000) || (museumServerPortNumb >= 65536)) {
            GenericIO.writelnString("args[9] is not a valid port number!");
            System.exit(1);
        }

        reposServerHostName = args[10];
        try {
            reposServerPortNumb = Integer.parseInt(args[11]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString("args[11] is not a number!");
            System.exit(1);
        }
        if ((reposServerPortNumb < 4000) || (reposServerPortNumb >= 65536)) {
            GenericIO.writelnString("args[11] is not a valid port number!");
            System.exit(1);
        }

        /* problem initialization */

        assPartStub = new AssaultPartyStub[2];
        assPartStub[0] =
                new AssaultPartyStub(assaultParty1ServerHostName, assaultParty1ServerPortNumb);
        assPartStub[1] =
                new AssaultPartyStub(assaultParty2ServerHostName, assaultParty2ServerPortNumb);
        concSiteStub = new ConcentrationSiteStub(concSiteServerHostName, concSiteServerPortNumb);
        contColSiteStub =
                new ControlCollectionSiteStub(contColSiteServerHostName, contColSiteServerPortNumb);
        museumStub = new MuseumStub(museumServerHostName, museumServerPortNumb);
        reposStub = new GeneralRepositoryStub(reposServerHostName, reposServerPortNumb);
        masterThief = new MasterThief("mas_0", contColSiteStub, concSiteStub, assPartStub);

        /* start of the simulation */

        masterThief.start();

        /* waiting for the end of the simulation */

        GenericIO.writelnString();
        while (masterThief.isAlive()) {
            contColSiteStub.endOperation();
            Thread.yield();
        }
        try {
            masterThief.join();
        } catch (InterruptedException e) {
        }
        GenericIO.writelnString("The master thief has terminated.");
        GenericIO.writelnString();

        assPartStub[0].shutdown();
        assPartStub[1].shutdown();
        concSiteStub.shutdown();
        contColSiteStub.shutdown();
        museumStub.shutdown();
        reposStub.shutdown();
    }
}
