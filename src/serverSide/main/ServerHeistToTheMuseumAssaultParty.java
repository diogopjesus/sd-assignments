package serverSide.main;

import java.rmi.registry.*;
import java.rmi.*;
import java.rmi.server.*;
import serverSide.objects.*;
import interfaces.*;
import genclass.GenericIO;

/**
 * Instantiation and registering of a assault party object.
 *
 * Implementation of a client-server model of type 2 (server replication).
 * Communication is based on Java RMI.
 */
public class ServerHeistToTheMuseumAssaultParty {
  /**
   * Flag signaling the end of operations.
   */
  private static boolean end = false;

  /**
   * Main method.
   *
   * args[0] - assault party identification (1, (SimulPar.M-1)/SimulPar.K)
   * args[1] - port number for listening to service requests
   * args[2] - name of the platform where is located the RMI registering service
   * args[3] - port number where the registering service is listening to service
   * requests
   */
  public static void main(String[] args) {
    int assPartId = -1; // assault party identification
    int portNumb = -1; // port number for listening to service requests
    String rmiRegHostName; // name of the platform where is located the RMI registering service
    int rmiRegPortNumb = -1; // port number where the registering service is listening to service requests

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
    if ((assPartId < 1) || (assPartId > (SimulPar.M - 1) / SimulPar.K)) {
      GenericIO.writelnString("args[0] is not a valid assault party identification!");
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
    rmiRegHostName = args[2];
    try {
      rmiRegPortNumb = Integer.parseInt(args[3]);
    } catch (NumberFormatException e) {
      GenericIO.writelnString("args[3] is not a number!");
      System.exit(1);
    }
    if ((rmiRegPortNumb < 4000) || (rmiRegPortNumb >= 65536)) {
      GenericIO.writelnString("args[3] is not a valid port number!");
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

    /* instantiate a assault party object */

    AssaultParty assPart = new AssaultParty(reposStub, assPartId); // assault party object
    AssaultPartyInterface assPartStub = null; // remote reference to the assault party object

    try {
      assPartStub = (AssaultPartyInterface) UnicastRemoteObject.exportObject(assPart, portNumb);
    } catch (RemoteException e) {
      GenericIO.writelnString("Assault party " + assPartId + " stub generation exception: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
    GenericIO.writelnString("Stub was generated!");

    /* register it with the general registry service */

    String nameEntryBase = "RegisterHandler"; // public name of the object that enables
                                              // the registration of other remote objects
    String nameEntryObject = "AssaultParty" + assPartId; // public name of the assault party object
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
      reg.bind(nameEntryObject, assPartStub);
    } catch (RemoteException e) {
      GenericIO.writelnString("Assault Party " + assPartId + " registration exception: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    } catch (AlreadyBoundException e) {
      GenericIO.writelnString("Assault Party " + assPartId + " already bound exception: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
    GenericIO.writelnString("Assault Party " + assPartId + " object was registered!");

    /* wait for the end of operations */

    GenericIO.writelnString("Assault Party " + assPartId + " is in operation!");
    try {
      while (!end)
        synchronized (Class.forName("serverSide.main.ServerHeistToTheMuseumAssaultParty")) {
          try {
            (Class.forName("serverSide.main.ServerHeistToTheMuseumAssaultParty")).wait();
          } catch (InterruptedException e) {
            GenericIO.writelnString("Assault Party " + assPartId + " main thread was interrupted!");
          }
        }
    } catch (ClassNotFoundException e) {
      GenericIO.writelnString("The data type ServerHeistToTheMuseumAssaultParty was not found (blocking)!");
      e.printStackTrace();
      System.exit(1);
    }

    /* server shutdown */

    boolean shutdownDone = false; // flag signalling the shutdown of the assault party service

    try {
      reg.unbind(nameEntryObject);
    } catch (RemoteException e) {
      GenericIO.writelnString("Assault Party " + assPartId + " deregistration exception: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    } catch (NotBoundException e) {
      GenericIO.writelnString("Assault Party " + assPartId + " not bound exception: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
    GenericIO.writelnString("Assault Party " + assPartId + " was deregistered!");

    try {
      shutdownDone = UnicastRemoteObject.unexportObject(assPart, true);
    } catch (NoSuchObjectException e) {
      GenericIO.writelnString("Assault Party " + assPartId + " unexport exception: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }

    if (shutdownDone)
      GenericIO.writelnString("Assault Party " + assPartId + " was shutdown!");
  }

  /**
   * Close of operations.
   */
  public static void shutdown() {
    end = true;
    try {
      synchronized (Class.forName("serverSide.main.ServerHeistToTheMuseumAssaultParty")) {
        (Class.forName("serverSide.main.ServerHeistToTheMuseumAssaultParty")).notify();
      }
    } catch (ClassNotFoundException e) {
      GenericIO.writelnString("The data type ServerHeistToTheMuseumAssaultParty was not found (waking up)!");
      e.printStackTrace();
      System.exit(1);
    }
  }
}
