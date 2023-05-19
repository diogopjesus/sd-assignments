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