package serverSide.objects;

import java.rmi.*;
import interfaces.*;
import serverSide.main.*;
import clientSide.entities.*;
import genclass.GenericIO;
import genclass.TextFile;
import java.util.Objects;

/**
 * General repository.
 *
 * It is responsible to keep the visible internal state of the heist and provide
 * means to it to be printed in the logging file.
 * It is implemented as an implicit monitor.
 * All public methods are executed in mutual exclusion.
 * There are no internal synchronization points.
 * Implementation of a client-server model of type 2 (server replication).
 * Communication is based on Java RMI.
 */
public class GeneralRepository implements GeneralRepositoryInterface {
    /**
     * Number of entity groups requesting the shutdown.
     */
    private int nEntities;

    /**
     * Name of the logging file.
     */
    private String logFileName;

    /**
     * State of the master thief.
     */
    private int masterThiefState;

    /**
     * State of the ordinary thief # (# - 1 .. M-1).
     */
    private int[] ordinaryThiefState;

    /**
     * Situation of the ordinary thief # (# - 1 .. M-1) either 'W' (waiting to join
     * a party) or 'P'
     * (in party).
     */
    private char[] ordinaryThiefSituation;

    /**
     * maximum displacement of the ordinary thief # (# - 1 .. M-1) a random number
     * between md and
     * MD.
     */
    private int[] ordinaryThiefMaximumDisplacement;

    /**
     * Assault party # (# - 1, (M-1)/K) elem # (# - 1 .. K) room identification (1
     * .. N).
     */
    private int[] assaultPartyRoomId;

    /**
     * Assault party # (# - 1, (M-1)/K) elem # (# - 1 .. K) member identification (1
     * .. N).
     */
    private int[][] assaultPartyElementId;

    /**
     * Assault party # (# - 1, (M-1)/K) elem # (# - 1 .. K) present position (0 ..
     * DT RId).
     */
    private int[][] assaultPartyElementPosition;

    /**
     * Assault party # (# - 1, (M-1)/K) elem # (# - 1 .. K) carrying a canvas (0,1).
     */
    private int[][] assaultPartyElementCanvas;

    /**
     * room identification (1 .. N) number of paintings presently hanging on the
     * walls.
     */
    private int[] museumRoomNumberPaintings;

    /**
     * Room identification (1 .. N) distance from outside gathering site, a random
     * number between d
     * and D.
     */
    private int[] museumRoomDistance;

    /**
     * Number of stolen paintings.
     */
    private int stolenPaintings;

    /**
     * Instantiation of a general repository object.
     */
    public GeneralRepository() {
        this.nEntities = 0;

        logFileName = "logger";

        /* Store master thief initial info */
        this.masterThiefState = MasterThiefStates.PLANNING_THE_HEIST;

        /* Store ordinary thieves initial info */
        this.ordinaryThiefState = new int[SimulPar.M - 1];
        this.ordinaryThiefSituation = new char[SimulPar.M - 1];
        for (int i = 0; i < SimulPar.M - 1; i++) {
            this.ordinaryThiefState[i] = OrdinaryThiefStates.CONCENTRATION_SITE;
            this.ordinaryThiefSituation[i] = 'W';
        }

        /* Store assault parties initial info */
        this.assaultPartyRoomId = new int[(SimulPar.M - 1) / SimulPar.K];
        this.assaultPartyElementId = new int[(SimulPar.M - 1) / SimulPar.K][SimulPar.K];
        this.assaultPartyElementPosition = new int[(SimulPar.M - 1) / SimulPar.K][SimulPar.K];
        this.assaultPartyElementCanvas = new int[(SimulPar.M - 1) / SimulPar.K][SimulPar.K];
        for (int i = 0; i < (SimulPar.M - 1) / SimulPar.K; i++) {
            this.assaultPartyRoomId[i] = -1;
            for (int j = 0; j < SimulPar.K; j++) {
                this.assaultPartyElementId[i][j] = -1;
                this.assaultPartyElementPosition[i][j] = -1;
                this.assaultPartyElementCanvas[i][j] = -1;
            }
        }

        /* Store museum initial info */
        this.museumRoomNumberPaintings = new int[SimulPar.N];
        this.museumRoomDistance = new int[SimulPar.N];

        /* Initial number of stolen paintings */
        this.stolenPaintings = 0;
    }

    /**
     * Operation initialization of simulation.
     *
     * @param logFileName name of the logging file.
     * @param maxDis      maximum displacement of the ordinary thieves.
     * @param numPaint    number of paintings in each room.
     * @param roomDist    distance between each room and the outside gathering site.
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry service fails
     */
    @Override
    public synchronized void initSimul(String logFileName, int[] maxDis, int[] numPaint,
            int[] roomDist) throws RemoteException {
        /* Store the logging filename */
        if (!Objects.equals(logFileName, ""))
            this.logFileName = logFileName;

        /* Store ordinary thief maximimum displacement */
        this.ordinaryThiefMaximumDisplacement = new int[SimulPar.M - 1];
        for (int i = 0; i < SimulPar.M - 1; i++)
            this.ordinaryThiefMaximumDisplacement[i] = maxDis[i];

        /* Store museum initial info */
        this.museumRoomNumberPaintings = new int[SimulPar.N];
        this.museumRoomDistance = new int[SimulPar.N];
        for (int i = 0; i < SimulPar.N; i++) {
            this.museumRoomNumberPaintings[i] = numPaint[i];
            this.museumRoomDistance[i] = roomDist[i];
        }

        reportInitialStatus();
    }

    /**
     * Set the current state of the master thief.
     *
     * @param masterThiefState the current state of the master thief.
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry service fails
     */
    @Override
    public synchronized void setMasterThiefState(int masterThiefState) throws RemoteException {
        this.masterThiefState = masterThiefState;
        reportStatus();
    }

    /**
     * Set the current state of an ordinary thief.
     *
     * @param thiefId            the thief id.
     * @param ordinaryThiefState the ordinary thief state.
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry service fails
     */
    @Override
    public synchronized void setOrdinaryThiefState(int thiefId, int ordinaryThiefState) throws RemoteException {
        this.ordinaryThiefState[thiefId] = ordinaryThiefState;
        reportStatus();
    }

    /**
     * Set the current room id of an assault party.
     *
     * @param assaultPartyId     the assault party id.
     * @param assaultPartyRoomId the target room id of the assault party.
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry service fails
     */
    @Override
    public synchronized void setAssaultPartyRoomId(int assaultPartyId, int assaultPartyRoomId) throws RemoteException {
        this.assaultPartyRoomId[assaultPartyId] = assaultPartyRoomId;
        reportStatus();
    }

    /**
     * Set the current element id of an assault party.
     *
     * @param assaultPartyId the assault party id.
     * @param elementId      the element id.
     * @param thiefId        thief id.
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry service fails
     */
    @Override
    public synchronized void setAssaultPartyElementId(int assaultPartyId, int elementId,
            int thiefId) throws RemoteException {
        /* Change state to crawling inwards */
        this.ordinaryThiefState[thiefId] = OrdinaryThiefStates.CRAWLING_INWARDS;

        /* Update thief situation */
        ordinaryThiefSituation[thiefId] = 'P';

        /* Update assault party info */
        this.assaultPartyElementId[assaultPartyId][elementId] = thiefId;
        this.assaultPartyElementPosition[assaultPartyId][elementId] = 0;
        this.assaultPartyElementCanvas[assaultPartyId][elementId] = 0;

        reportStatus();
    }

    /**
     * Set the current element position of an assault party.
     *
     * @param assaultPartyId              the assault party id.
     * @param elementId                   the element id.
     * @param assaultPartyElementPosition the target element position of the assault
     *                                    party.
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry service fails
     */
    @Override
    public synchronized void setAssaultPartyElementPosition(int assaultPartyId, int elementId,
            int assaultPartyElementPosition) throws RemoteException {
        this.assaultPartyElementPosition[assaultPartyId][elementId] = assaultPartyElementPosition;
        reportStatus();
    }

    /**
     * Set the current element canvas of an assault party.
     *
     * @param assaultPartyId            the assault party id.
     * @param elementId                 the element id.
     * @param assaultPartyElementCanvas the target element canvas of the assault
     *                                  party.
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry service fails
     */
    @Override
    public synchronized void setAssaultPartyElementCanvas(int assaultPartyId, int elementId,
            boolean assaultPartyElementCanvas) throws RemoteException {
        this.assaultPartyElementCanvas[assaultPartyId][elementId] = assaultPartyElementCanvas ? 1 : 0;

        /* Decrement the number of canvas in the room */
        if (assaultPartyElementCanvas)
            museumRoomNumberPaintings[assaultPartyRoomId[assaultPartyId]]--;

        reportStatus();
    }

    /**
     * Increment canvas stolen by a thief, if he has one and remove the thief from
     * the assault
     * party.
     *
     * @param assaultPartyId assault party id.
     * @param elementId      element id (position of the thief in the party).
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry service fails
     */
    @Override
    public synchronized void endAssaultPartyElementMission(int assaultPartyId, int elementId) throws RemoteException {
        /* Increment number of paintings stolen if there's a canvas */
        if (assaultPartyElementCanvas[assaultPartyId][elementId] > 0)
            stolenPaintings++;

        /* Update thief situation */
        this.ordinaryThiefSituation[this.assaultPartyElementId[assaultPartyId][elementId]] = 'W';

        /* Remove thief info from assault party */
        this.assaultPartyElementId[assaultPartyId][elementId] = -1;
        this.assaultPartyElementPosition[assaultPartyId][elementId] = -1;
        this.assaultPartyElementCanvas[assaultPartyId][elementId] = -1;

        /* Check if there are still thieves in party */
        int i = 0;
        for (; i < SimulPar.K; i++)
            if (this.assaultPartyElementId[assaultPartyId][i] != -1)
                break;

        /* Clear assault party target room if all thieves have leaved */
        if (i == SimulPar.K)
            this.assaultPartyRoomId[assaultPartyId] = -1;

        /* Change master thief state to deciding what to do */
        this.masterThiefState = MasterThiefStates.DECIDING_WHAT_TO_DO;

        reportStatus();
    }

    /**
     * Operation server shutdown.
     * 
     * @throws RemoteException if either the invocation of the remote method, or the
     *                         communication with the registry service fails
     */
    @Override
    public synchronized void shutdown() throws RemoteException {
        nEntities += 1;
        if (nEntities >= SimulPar.E)
            ServerHeistToTheMuseumGeneralRepository.shutdown();
    }

    /**
     * Write the header and first state line to the logging file. Internal
     * operation.
     */
    private void reportInitialStatus() {
        TextFile log = new TextFile();

        /* Write header to logging file */
        if (!log.openForWriting(".", logFileName)) {
            GenericIO.writeString("The operation of creating the file " + logFileName + " failed!");
            System.exit(1);
        }
        log.writelnString(
                "                             Heist to the Museum - Description of the internal state");
        log.writelnString();
        log.writelnString(
                "MstT   Thief 1      Thief 2      Thief 3      Thief 4      Thief 5      Thief 6");
        log.writelnString(
                "Stat  Stat S MD    Stat S MD    Stat S MD    Stat S MD    Stat S MD    Stat S MD");
        log.writelnString(
                "                   Assault party 1                       Assault party 2                       Museum");
        log.writelnString(
                "           Elem 1     Elem 2     Elem 3          Elem 1     Elem 2     Elem 3   Room 1  Room 2  Room 3  Room 4  Room 5");
        log.writelnString(
                "    RId  Id Pos Cv  Id Pos Cv  Id Pos Cv  RId  Id Pos Cv  Id Pos Cv  Id Pos Cv   NP DT   NP DT   NP DT   NP DT   NP DT");
        log.writelnString();
        if (!log.close()) {
            GenericIO
                    .writelnString("The operation of closing the file " + logFileName + " failed!");
            System.exit(1);
        }

        /* Write first status line */
        reportStatus();
    }

    /**
     * Write a state line at the end of the logging file. Internal operation.
     */
    private void reportStatus() {
        TextFile log = new TextFile();

        String lineStatus = "";

        if (!log.openForAppending(".", logFileName)) {
            GenericIO.writeString("The operation of creating the file " + logFileName + " failed!");
            System.exit(1);
        }

        /* Write first status line */
        lineStatus += getMasterThiefState() + "  ";
        for (int thief = 0; thief < SimulPar.M - 1; thief++) {
            lineStatus += getOrdinaryThiefState(thief) + " ";
            lineStatus += ordinaryThiefSituation[thief] + "  ";
            lineStatus += ordinaryThiefMaximumDisplacement[thief] + "    ";
        }
        log.writelnString(lineStatus);

        /* Write second status line */
        lineStatus = "     ";
        for (int assPart = 0; assPart < (SimulPar.M - 1) / SimulPar.K; assPart++) {
            int roomId = assaultPartyRoomId[assPart];
            lineStatus += (roomId == -1 ? "-" : roomId + 1) + "    ";
            for (int elem = 0; elem < SimulPar.K; elem++) {
                int elementId = assaultPartyElementId[assPart][elem];
                lineStatus += (elementId == -1 ? "-" : elementId + 1) + "  ";
                int position = assaultPartyElementPosition[assPart][elem];
                lineStatus += (position == -1 ? "--" : String.format("%02d", position)) + "  ";
                int canvas = assaultPartyElementCanvas[assPart][elem];
                lineStatus += (canvas == -1 ? "-" : canvas) + "   ";
            }
        }
        for (int room = 0; room < SimulPar.N; room++) {
            lineStatus += String.format("%02d", museumRoomNumberPaintings[room]) + " ";
            lineStatus += String.format("%02d", museumRoomDistance[room]) + "   ";
        }
        log.writelnString(lineStatus);

        if (!log.close()) {
            GenericIO
                    .writelnString("The operation of closing the file " + logFileName + " failed!");
            System.exit(1);
        }

        if (getMasterThiefState().compareTo("PRES") == 0) {
            reportFinalStatus();
        }
    }

    /**
     * Write final status to the logging file. Internal operation.
     */
    private void reportFinalStatus() {
        TextFile log = new TextFile();

        if (!log.openForAppending(".", logFileName)) {
            GenericIO.writeString("The operation of creating the file " + logFileName + " failed!");
            System.exit(1);
        }

        log.writelnString("My friends, tonight's effort produced " + stolenPaintings + " priceless paintings!");

        if (!log.close()) {
            GenericIO
                    .writelnString("The operation of closing the file " + logFileName + " failed!");
            System.exit(1);
        }
    }

    /**
     * Get the current state of the master thief.
     *
     * @return the current state of the master thief.
     */
    private String getMasterThiefState() {
        switch (masterThiefState) {
            case MasterThiefStates.PLANNING_THE_HEIST:
                return "PLAN";
            case MasterThiefStates.DECIDING_WHAT_TO_DO:
                return "DECI";
            case MasterThiefStates.ASSEMBLING_A_GROUP:
                return "ASSE";
            case MasterThiefStates.WAITING_FOR_GROUP_ARRIVAL:
                return "WAIT";
            case MasterThiefStates.PRESENTING_THE_REPORT:
                return "PRES";
            default:
                return "ERRO";
        }
    }

    /**
     * Get the current state of an ordinary thief.
     *
     * @param thiefId the thief id.
     * @return the current state of the ordinary thief.
     */
    private String getOrdinaryThiefState(int thiefId) {
        switch (ordinaryThiefState[thiefId]) {
            case OrdinaryThiefStates.CONCENTRATION_SITE:
                return "CONC";
            case OrdinaryThiefStates.CRAWLING_INWARDS:
                return "INWA";
            case OrdinaryThiefStates.AT_A_ROOM:
                return "ROOM";
            case OrdinaryThiefStates.CRAWLING_OUTWARDS:
                return "OUTW";
            case OrdinaryThiefStates.COLLECTION_SITE:
                return "COLL";
            default:
                return "ERRO";
        }
    }
}
