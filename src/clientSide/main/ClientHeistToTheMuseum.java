package clientSide.main;

import java.rmi.registry.*;
import java.rmi.*;
import clientSide.entities.*;
import serverSide.main.*;
import interfaces.*;
import genclass.GenericIO;

/**
 * Client side of the Heist to the Museum.
 *
 * Implementation of a client-server model of type 2 (server replication).
 * Communication is based on Java RMI.
 */
public class ClientHeistToTheMuseum {
  /**
   * Main method.
   *
   * @param args runtime arguments
   *             args[0] - name of the platform where is located the RMI
   *             registering service
   *             args[1] - port number where the registering service is listening
   *             to service requests
   *             args[2] - name of the logging file
   */
  public static void main(String[] args) {
    String rmiRegHostName; // name of the platform where is located the RMI registering service
    int rmiRegPortNumb = -1; // port number where the registering service is listening to service requests
    String fileName; // name of the logging file

    /* getting problem runtime parameters */

    if (args.length != 3) {
      GenericIO.writelnString("Wrong number of parameters!");
      System.exit(1);
    }
    rmiRegHostName = args[0];
    try {
      rmiRegPortNumb = Integer.parseInt(args[1]);
    } catch (NumberFormatException e) {
      GenericIO.writelnString("args[1] is not a number!");
      System.exit(1);
    }
    if ((rmiRegPortNumb < 4000) || (rmiRegPortNumb >= 65536)) {
      GenericIO.writelnString("args[1] is not a valid port number!");
      System.exit(1);
    }
    fileName = args[2];

    /* Generate random values */

    int[] maxDis = new int[SimulPar.M - 1]; // maximum displacement of each thief
    for (int i = 0; i < SimulPar.M - 1; i++)
      maxDis[i] = SimulPar.md + (int) Math.round(Math.random() * (SimulPar.MD - SimulPar.md));
    int[] numPaint = new int[SimulPar.N]; // number of paintings to be stolen
    int[] roomDist = new int[SimulPar.N]; // distance between rooms and outside gathering site
    for (int i = 0; i < SimulPar.N; i++) {
      numPaint[i] = SimulPar.p + (int) Math.round(Math.random() * (SimulPar.P - SimulPar.p));
      roomDist[i] = SimulPar.d + (int) Math.round(Math.random() * (SimulPar.D - SimulPar.d));
    }

    /* problem initialization */

    int numAssPart = (SimulPar.M - 1) / SimulPar.K; // number of assault parties

    String nameEntryGeneralRepository = "GeneralRepository"; // public name of the general repository object
    GeneralRepositoryInterface reposStub = null; // remote reference to the general repository object
    AssaultPartyInterface[] assPartStub = new AssaultPartyInterface[numAssPart]; // remote ref to the ass part obj
    String[] nameEntryAssaultParties = new String[numAssPart]; // public name of the assault parties objects
    for (int i = 0; i < numAssPart; i++) {
      nameEntryAssaultParties[i] = "AssaultParty" + (i + 1);
      assPartStub[i] = null;
    }
    String nameEntryConcentrationSite = "ConcentrationSite"; // public name of the concentration site object
    ConcentrationSiteInterface concSiteStub = null; // remote reference to the concentration site object
    String nameEntryControlCollectionSite = "ControlCollectionSite"; // public name of the cont and coll site object
    ControlCollectionSiteInterface contColSiteStub = null; // remote reference to the cont and coll site object
    String nameEntryMuseum = "Museum"; // public name of the museum object
    MuseumInterface museumStub = null; // remote reference to the museum object
    Registry registry = null; // remote reference for registration in the RMI registry service
    MasterThief masterThief = null; // master thief thread
    OrdinaryThief[] ordThieves = new OrdinaryThief[SimulPar.M - 1]; // array of barber threads

    try {
      registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
    } catch (RemoteException e) {
      GenericIO.writelnString("RMI registry creation exception: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }

    try {
      reposStub = (GeneralRepositoryInterface) registry.lookup(nameEntryGeneralRepository);
    } catch (RemoteException e) {
      GenericIO.writelnString("GeneralRepository lookup exception: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    } catch (NotBoundException e) {
      GenericIO.writelnString("GeneralRepository not bound exception: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }

    for (int i = 0; i < numAssPart; i++)
      try {
        assPartStub[i] = (AssaultPartyInterface) registry.lookup(nameEntryAssaultParties[i]);
      } catch (RemoteException e) {
        GenericIO.writelnString("AssaultParty" + (i + 1) + " lookup exception: " + e.getMessage());
        e.printStackTrace();
        System.exit(1);
      } catch (NotBoundException e) {
        GenericIO.writelnString("AssaultParty" + (i + 1) + " not bound exception: " + e.getMessage());
        e.printStackTrace();
        System.exit(1);
      }

    try {
      concSiteStub = (ConcentrationSiteInterface) registry.lookup(nameEntryConcentrationSite);
    } catch (RemoteException e) {
      GenericIO.writelnString("ConcentrationSite lookup exception: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    } catch (NotBoundException e) {
      GenericIO.writelnString("ConcentrationSite not bound exception: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }

    try {
      contColSiteStub = (ControlCollectionSiteInterface) registry.lookup(nameEntryControlCollectionSite);
    } catch (RemoteException e) {
      GenericIO.writelnString("ControlCollectionSite lookup exception: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    } catch (NotBoundException e) {
      GenericIO.writelnString("ControlCollectionSite not bound exception: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }

    try {
      museumStub = (MuseumInterface) registry.lookup(nameEntryMuseum);
    } catch (RemoteException e) {
      GenericIO.writelnString("Museum lookup exception: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    } catch (NotBoundException e) {
      GenericIO.writelnString("Museum not bound exception: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }

    try {
      museumStub.setRoomInfo(numPaint, roomDist);
    } catch (RemoteException e) {
      GenericIO.writelnString("Client generator remote exception on setRoomInfo: " + e.getMessage());
      System.exit(1);
    }

    try {
      reposStub.initSimul(fileName, maxDis, numPaint, roomDist);
    } catch (RemoteException e) {
      GenericIO.writelnString("Client generator remote exception on initSimul: " + e.getMessage());
      System.exit(1);
    }

    masterThief = new MasterThief("mas_0", contColSiteStub, concSiteStub, assPartStub);

    for (int i = 0; i < SimulPar.M - 1; i++)
      ordThieves[i] = new OrdinaryThief("ord_" + (i + 1), i, maxDis[i], contColSiteStub,
          concSiteStub, assPartStub, museumStub);

    /* start of the simulation */

    masterThief.start();

    for (int i = 0; i < SimulPar.M - 1; i++)
      ordThieves[i].start();

    /* waiting for the end of the simulation */

    for (int i = 0; i < SimulPar.M - 1; i++) {
      try {
        ordThieves[i].join();
      } catch (InterruptedException e) {
      }
      GenericIO.writelnString("The ordinary thief " + (i + 1) + " has terminated.");
    }
    GenericIO.writelnString();

    try {
      masterThief.join();
    } catch (InterruptedException e) {
    }
    GenericIO.writelnString("The master thief has terminated.");
    GenericIO.writelnString();

    for (int i = 0; i < numAssPart; i++)
      try {
        assPartStub[i].shutdown();
      } catch (RemoteException e) {
        GenericIO.writelnString("Customer generator remote exception on AssaultParty" + (i + 1) + " shutdown: "
            + e.getMessage());
        System.exit(1);
      }

    try {
      concSiteStub.shutdown();
    } catch (RemoteException e) {
      GenericIO.writelnString("Customer generator remote exception on ConcentrationSite shutdown: " + e.getMessage());
      System.exit(1);
    }

    try {
      contColSiteStub.shutdown();
    } catch (RemoteException e) {
      GenericIO
          .writelnString("Customer generator remote exception on ControlCollectionSite shutdown: " + e.getMessage());
      System.exit(1);
    }

    try {
      museumStub.shutdown();
    } catch (RemoteException e) {
      GenericIO.writelnString("Customer generator remote exception on Museum shutdown: " + e.getMessage());
      System.exit(1);
    }

    try {
      reposStub.shutdown();
    } catch (RemoteException e) {
      GenericIO.writelnString("Customer generator remote exception on GeneralRepository shutdown: " + e.getMessage());
      System.exit(1);
    }
  }
}
