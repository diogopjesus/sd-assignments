package serverSide.main;

import java.rmi.registry.*;
import java.rmi.*;
import java.rmi.server.*;
import serverSide.objects.*;
import interfaces.*;
import genclass.GenericIO;

/**
 * Instantiation and registering of a concentration site object.
 *
 * Implementation of a client-server model of type 2 (server replication).
 * Communication is based on Java RMI.
 */
public class ServerHeistToTheMuseumControlCollectionSite {
  /**
   * Flag signaling the end of operations.
   */
  private static boolean end = false;

  /**
   * Main method.
   *
   * args[0] - port number for listening to service requests
   * args[1] - name of the platform where is located the RMI registering service
   * args[2] - port number where the registering service is listening to service
   * requests
   */
  public static void main(String[] args) {
    int portNumb = -1; // port number for listening to service requests
    String rmiRegHostName; // name of the platform where is located the RMI registering service
    int rmiRegPortNumb = -1; // port number where the registering service is listening to service requests

    if (args.length != 3) {
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
    rmiRegHostName = args[1];
    try {
      rmiRegPortNumb = Integer.parseInt(args[2]);
    } catch (NumberFormatException e) {
      GenericIO.writelnString("args[2] is not a number!");
      System.exit(1);
    }
    if ((rmiRegPortNumb < 4000) || (rmiRegPortNumb >= 65536)) {
      GenericIO.writelnString("args[2] is not a valid port number!");
      System.exit(1);
    }

    /* create and install the security manager */

    if (System.getSecurityManager() == null)
      System.setSecurityManager(new SecurityManager());
    GenericIO.writelnString("Security manager was installed!");

    /* get a remote reference to the general repository object */

    String nameEntryGeneralRepository = "GeneralRepository"; // public name of the general repository object
    GeneralRepositoryInterface reposStub = null; // remote reference to the general repository object
    Registry registry = null; // remote reference for registration in the RMI registry service

    try {
      registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
    } catch (RemoteException e) {
      GenericIO.writelnString("RMI registry creation exception: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
    GenericIO.writelnString("RMI registry was created!");

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

    /* get a remote reference to the assault parties objects */

    int numAssPart = (SimulPar.M - 1) / SimulPar.K;
    String[] nameEntryAssaultParties = new String[numAssPart]; // public names of the assault parties objects
    AssaultPartyInterface[] assPartStub = new AssaultPartyInterface[numAssPart]; // remote references to the
                                                                                 // assault parties objects

    for (int i = 0; i < numAssPart; i++) {
      nameEntryAssaultParties[i] = "AssaultParty" + (i + 1);
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
    }

    /* instantiate a control collection site object */

    ControlCollectionSite contColSite = new ControlCollectionSite(reposStub, assPartStub); // cont coll site object
    ControlCollectionSiteInterface contColSiteStub = null; // remote reference to the control collection site object

    try {
      contColSiteStub = (ControlCollectionSiteInterface) UnicastRemoteObject.exportObject(contColSite, portNumb);
    } catch (RemoteException e) {
      GenericIO.writelnString("Control collection site stub generation exception: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
    GenericIO.writelnString("Stub was generated!");

    /* register it with the general registry service */

    String nameEntryBase = "RegisterHandler"; // public name of the object that enables
                                              // the registration of other remote objects
    String nameEntryObject = "ControlCollectionSite"; // public name of the control collection site object
    Register reg = null; // remote reference to the object that enables the registration
                         // of other remote objects

    try {
      reg = (Register) registry.lookup(nameEntryBase);
    } catch (RemoteException e) {
      GenericIO.writelnString("RegisterRemoteObject lookup exception: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    } catch (NotBoundException e) {
      GenericIO.writelnString("RegisterRemoteObject not bound exception: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }

    try {
      reg.bind(nameEntryObject, contColSiteStub);
    } catch (RemoteException e) {
      GenericIO.writelnString("ControlCollectionSite registration exception: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    } catch (AlreadyBoundException e) {
      GenericIO.writelnString("ControlCollectionSite already bound exception: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
    GenericIO.writelnString("Control Collection Site object was registered!");

    /* wait for the end of operations */

    GenericIO.writelnString("Control Collection Site is in operation!");
    try {
      while (!end)
        synchronized (Class.forName("serverSide.main.ServerHeistToTheMuseumControlCollectionSite")) {
          try {
            (Class.forName("serverSide.main.ServerHeistToTheMuseumControlCollectionSite")).wait();
          } catch (InterruptedException e) {
            GenericIO.writelnString("Control Collection Site main thread was interrupted!");
          }
        }
    } catch (ClassNotFoundException e) {
      GenericIO.writelnString("The data type ServerHeistToTheMuseumControlCollectionSite was not found (blocking)!");
      e.printStackTrace();
      System.exit(1);
    }

    /* server shutdown */

    boolean shutdownDone = false; // flag signalling the shutdown of the control collection site service

    try {
      reg.unbind(nameEntryObject);
    } catch (RemoteException e) {
      GenericIO.writelnString("Control Collection Site deregistration exception: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    } catch (NotBoundException e) {
      GenericIO.writelnString("Control Collection Site not bound exception: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
    GenericIO.writelnString("Control Collection Site was deregistered!");

    try {
      shutdownDone = UnicastRemoteObject.unexportObject(contColSite, true);
    } catch (NoSuchObjectException e) {
      GenericIO.writelnString("Control Collection Site unexport exception: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }

    if (shutdownDone)
      GenericIO.writelnString("Control Collection Site was shutdown!");
  }

  /**
   * Close of operations.
   */
  public static void shutdown() {
    end = true;
    try {
      synchronized (Class.forName("serverSide.main.ServerHeistToTheMuseumControlCollectionSite")) {
        (Class.forName("serverSide.main.ServerHeistToTheMuseumControlCollectionSite")).notify();
      }
    } catch (ClassNotFoundException e) {
      GenericIO.writelnString(
          "The data type ServerHeistToTheMuseumControlCollectionSite was not found (waking up)!");
      e.printStackTrace();
      System.exit(1);
    }
  }
}
