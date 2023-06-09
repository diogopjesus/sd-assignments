package serverSide.main;

import java.rmi.registry.*;
import java.rmi.*;
import java.rmi.server.*;
import serverSide.objects.*;
import interfaces.*;
import genclass.GenericIO;

/**
 * Instantiation and registering of a museum object.
 *
 * Implementation of a client-server model of type 2 (server replication).
 * Communication is based on Java RMI.
 */
public class ServerHeistToTheMuseumMuseum {
  /**
   * Flag signaling the end of operations.
   */
  private static boolean end = false;

  /**
   * Main method.
   * 
   * @param args runtime arguments
   *             args[0] - port number for listening to service requests
   *             args[1] - name of the platform where is located the RMI
   *             registering service
   *             args[2] - port number where the registering service is listening
   *             to service
   *             requests
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

    /* instantiate a museum object */

    Museum museum = new Museum(reposStub, assPartStub); // museum object
    MuseumInterface museumStub = null; // remote reference to the museum object

    try {
      museumStub = (MuseumInterface) UnicastRemoteObject.exportObject(museum, portNumb);
    } catch (RemoteException e) {
      GenericIO.writelnString("Barber Shop stub generation exception: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
    GenericIO.writelnString("Stub was generated!");

    /* register it with the general registry service */

    String nameEntryBase = "RegisterHandler"; // public name of the object that enables
                                              // the registration of other remote objects
    String nameEntryObject = "Museum"; // public name of the museum object
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
      reg.bind(nameEntryObject, museumStub);
    } catch (RemoteException e) {
      GenericIO.writelnString("Museum registration exception: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    } catch (AlreadyBoundException e) {
      GenericIO.writelnString("Museum already bound exception: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
    GenericIO.writelnString("Museum object was registered!");

    /* wait for the end of operations */

    GenericIO.writelnString("Museum is in operation!");
    try {
      while (!end)
        synchronized (Class.forName("serverSide.main.ServerHeistToTheMuseumMuseum")) {
          try {
            (Class.forName("serverSide.main.ServerHeistToTheMuseumMuseum")).wait();
          } catch (InterruptedException e) {
            GenericIO.writelnString("Museum main thread was interrupted!");
          }
        }
    } catch (ClassNotFoundException e) {
      GenericIO.writelnString("The data type ServerHeistToTheMuseumMuseum was not found (blocking)!");
      e.printStackTrace();
      System.exit(1);
    }

    /* server shutdown */

    boolean shutdownDone = false; // flag signalling the shutdown of the museum service

    try {
      reg.unbind(nameEntryObject);
    } catch (RemoteException e) {
      GenericIO.writelnString("Museum deregistration exception: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    } catch (NotBoundException e) {
      GenericIO.writelnString("Museum not bound exception: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
    GenericIO.writelnString("Museum was deregistered!");

    try {
      shutdownDone = UnicastRemoteObject.unexportObject(museum, true);
    } catch (NoSuchObjectException e) {
      GenericIO.writelnString("Museum unexport exception: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }

    if (shutdownDone)
      GenericIO.writelnString("Museum was shutdown!");
  }

  /**
   * Close of operations.
   */
  public static void shutdown() {
    end = true;
    try {
      synchronized (Class.forName("serverSide.main.ServerHeistToTheMuseumMuseum")) {
        (Class.forName("serverSide.main.ServerHeistToTheMuseumMuseum")).notify();
      }
    } catch (ClassNotFoundException e) {
      GenericIO.writelnString("The data type ServerHeistToTheMuseumMuseum was not found (waking up)!");
      e.printStackTrace();
      System.exit(1);
    }
  }
}
