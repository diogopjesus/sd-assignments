package sharedRegions;

import main.*;
import entities.*;
import genclass.*;

/**
 * General repository.
 *
 * It is responsible to keep the visible internal state of the heist and provide means to it to be
 * printed in the logging file. It is implemented as an implicit monitor. All public methods are
 * executed in mutual exclusion. There are no internal synchronization points.
 */
public class GeneralRepository {
    /**
     * Name of the logging file.
     */
    private final String logFileName;

    /**
     * State of the master thief.
     */
    private int masterThiefState;

    /**
     * State of the ordinary thief # (# - 1 .. M-1).
     */
    private int[] ordinaryThiefState;

    /**
     * Situation of the ordinary thief # (# - 1 .. M-1) either 'W' (waiting to join a party) or 'P'
     * (in party).
     */
    private char[] ordinaryThiefSituation;

    /**
     * maximum displacement of the ordinary thief # (# - 1 .. M-1) a random number between md and
     * MD.
     */
    private int[] ordinaryThiefMaximumDisplacement;

    /**
     * Assault party # (# - 1, (M-1)/K) elem # (# - 1 .. K) room identification (1 .. N).
     */
    private int[] assaultPartyRoomId;

    /**
     * Assault party # (# - 1, (M-1)/K) elem # (# - 1 .. K) member identification (1 .. N).
     */
    private int[][] assaultPartyElementId;

    /**
     * Assault party # (# - 1, (M-1)/K) elem # (# - 1 .. K) present position (0 .. DT RId).
     */
    private int[][] assaultPartyElementPosition;

    /**
     * Assault party # (# - 1, (M-1)/K) elem # (# - 1 .. K) carrying a canvas (0,1).
     */
    private int[][] assaultPartyElementCanvas;

    /**
     * room identification (1 .. N) number of paintings presently hanging on the walls.
     */
    private int[] museumRoomNumberPaintings;

    /**
     * Room identification (1 .. N) distance from outside gathering site, a random number between d
     * and D.
     */
    private int[] museumRoomDistance;

    /**
     * Number of stolen paintings.
     */
    private int stolenPaintings;

    /**
     * Instantiation of a general repository object.
     * 
     * @param logFileName name of the logging file.
     * @param maxDis maximum displacement of the ordinary thieves.
     * @param numPaint number of paintings in each room.
     * @param roomDist distance to each room.
     */
    public GeneralRepository(String logFileName, int[] maxDis, int[] numPaint, int[] roomDist) {
        /* Store the logging filename */
        if ((logFileName == null) || logFileName.compareTo("") == 0)
            this.logFileName = "internalState.log";
        else
            this.logFileName = logFileName;

        /* Store master thief initial info */
        this.masterThiefState = MasterThiefStates.PLANNING_THE_HEIST;

        /* Store ordinary thieves initial info */
        this.ordinaryThiefState = new int[SimulPar.M - 1];
        this.ordinaryThiefSituation = new char[SimulPar.M - 1];
        this.ordinaryThiefMaximumDisplacement = new int[SimulPar.M - 1];
        for (int i = 0; i < SimulPar.M - 1; i++) {
            this.ordinaryThiefState[i] = OrdinaryThiefStates.CONCENTRATION_SITE;
            this.ordinaryThiefSituation[i] = 'W';
            this.ordinaryThiefMaximumDisplacement[i] = maxDis[i];
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
        for (int i = 0; i < SimulPar.N; i++) {
            this.museumRoomNumberPaintings[i] = numPaint[i];
            this.museumRoomDistance[i] = roomDist[i];
        }

        /* Initial number of stolen paintings */
        this.stolenPaintings = 0;

        reportInitialStatus();
    }

    /**
     * Write the header and first state line to the logging file. Internal operation.
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
            lineStatus += getOrdinaryThiefSituation(thief) + "  ";
            lineStatus += getOrdinaryThiefMaximumDisplacement(thief) + "    ";
        }
        log.writelnString(lineStatus);

        /* Write second status line */
        lineStatus = "     ";
        for (int assPart = 0; assPart < (SimulPar.M - 1) / SimulPar.K; assPart++) {
            int roomId = getAssaultPartyRoomId(assPart);
            lineStatus += (roomId == -1 ? "#" : roomId + 1) + "    ";
            for (int elem = 0; elem < SimulPar.K; elem++) {
                int elementId = getAssaultPartyElementId(assPart, elem);
                lineStatus += (elementId == -1 ? "#" : elementId + 1) + "  ";
                int position = getAssaultPartyElementPosition(assPart, elem);
                lineStatus += (position == -1 ? "##" : String.format("%02d", position)) + "  ";
                int canvas = getAssaultPartyElementCanvas(assPart, elem);
                lineStatus += (canvas == -1 ? "#" : canvas) + "   ";
            }
        }
        for (int room = 0; room < SimulPar.N; room++) {
            lineStatus += String.format("%02d", getMuseumRoomNumberPaintings(room)) + " ";
            lineStatus += String.format("%02d", getMuseumRoomDistance(room)) + "   ";
        }
        log.writelnString(lineStatus);

        // log.writelnString();

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

        log.writelnString("My friends, tonight's effort produced " + getStolenPaintings()
                + " priceless paintings!");

        if (!log.close()) {
            GenericIO
                    .writelnString("The operation of closing the file " + logFileName + " failed!");
            System.exit(1);
        }
    }

    /**
     * Set the current state of the master thief.
     * 
     * @param masterThiefState the current state of the master thief.
     */
    public synchronized void setMasterThiefState(int masterThiefState) {
        this.masterThiefState = masterThiefState;
        reportStatus();
    }

    /**
     * Set the current state of an ordinary thief.
     * 
     * @param thiefId the thief id.
     * @param ordinaryThiefState the ordinary thief state.
     */
    public synchronized void setOrdinaryThiefState(int thiefId, int ordinaryThiefState) {
        this.ordinaryThiefState[thiefId] = ordinaryThiefState;
        reportStatus();
    }

    /**
     * Set the current room id of an assault party.
     * 
     * @param assaultPartyId the assault party id.
     * @param assaultPartyRoomId the target room id of the assault party.
     */
    public synchronized void setAssaultPartyRoomId(int assaultPartyId, int assaultPartyRoomId) {
        this.assaultPartyRoomId[assaultPartyId] = assaultPartyRoomId;
        reportStatus();
    }

    /**
     * Set the current element id of an assault party.
     * 
     * @param assaultPartyId the assault party id.
     * @param elementId the element id.
     * @param thiefId thief id.
     */
    public synchronized void setAssaultPartyElementId(int assaultPartyId, int elementId,
            int thiefId) {
        /* Change state to crawling inwards */
        ordinaryThiefState[thiefId] = OrdinaryThiefStates.CRAWLING_INWARDS;

        /* Update thief situation */
        setOrdinaryThiefSituation(thiefId, 'P');

        /* Update assault party info */
        this.assaultPartyElementId[assaultPartyId][elementId] = thiefId;
        this.assaultPartyElementPosition[assaultPartyId][elementId] = 0;
        this.assaultPartyElementCanvas[assaultPartyId][elementId] = 0;

        reportStatus();
    }

    /**
     * Set the current element position of an assault party.
     * 
     * @param assaultPartyId the assault party id.
     * @param elementId the element id.
     * @param assaultPartyElementPosition the target element position of the assault party.
     */
    public synchronized void setAssaultPartyElementPosition(int assaultPartyId, int elementId,
            int assaultPartyElementPosition) {
        this.assaultPartyElementPosition[assaultPartyId][elementId] = assaultPartyElementPosition;
        reportStatus();
    }

    /**
     * Set the current element canvas of an assault party.
     * 
     * @param assaultPartyId the assault party id.
     * @param elementId the element id.
     * @param assaultPartyElementCanvas the target element canvas of the assault party.
     */
    public synchronized void setAssaultPartyElementCanvas(int assaultPartyId, int elementId,
            boolean assaultPartyElementCanvas) {
        this.assaultPartyElementCanvas[assaultPartyId][elementId] =
                assaultPartyElementCanvas ? 1 : 0;

        /* Decrement the number of canvas in the room */
        if (assaultPartyElementCanvas)
            decrementMuseumRoomNumberPaintings(getAssaultPartyRoomId(assaultPartyId));

        reportStatus();
    }

    /**
     * Increment canvas stolen by a thief, if he has one and remove the thief from the assault
     * party.
     * 
     * @param canvas true if the thief is holding a canvas - false, otherwise.
     * @param assaultPartyId assault party id.
     * @param elementId element id (position of the thief in the party).
     */
    public synchronized void setCollectACanvas(boolean canvas, int assaultPartyId, int elementId) {
        /* Increment number of paintings stolen if there's a canvas */
        if (canvas)
            incrementStolenPaintings();

        /* Remove ordinary thief from the assault party */
        quitAssaultParty(assaultPartyId, elementId);

        /* Change master thief state to deciding what to do */
        masterThiefState = MasterThiefStates.DECIDING_WHAT_TO_DO;

        reportStatus();
    }

    /**
     * Get the current state of the master thief.
     * 
     * @return the current state of the master thief.
     */
    private String getMasterThiefState() {
        switch (this.masterThiefState) {
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
        switch (this.ordinaryThiefState[thiefId]) {
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

    /**
     * Get the current situation of an ordinary thief.
     * 
     * @param thiefId the thief id.
     * @return the current situation of the ordinary thief.
     */
    private char getOrdinaryThiefSituation(int thiefId) {
        return this.ordinaryThiefSituation[thiefId];
    }

    /**
     * Set the current situation of an ordinary thief.
     * 
     * @param thiefId the thief id.
     * @param ordinaryThiefSituation the ordinary thief situation.
     */
    private void setOrdinaryThiefSituation(int thiefId, char ordinaryThiefSituation) {
        this.ordinaryThiefSituation[thiefId] = ordinaryThiefSituation;
    }

    /**
     * Get the current maximum displacement of an ordinary thief.
     * 
     * @param thiefId the thief id.
     * @return the maximum displacement of the ordinary thief.
     */
    private int getOrdinaryThiefMaximumDisplacement(int thiefId) {
        return this.ordinaryThiefMaximumDisplacement[thiefId];
    }

    /**
     * Get the current room id of an assault party.
     * 
     * @param assaultPartyId the assault party id.
     * @return the target room id of the assault party.
     */
    private int getAssaultPartyRoomId(int assaultPartyId) {
        return this.assaultPartyRoomId[assaultPartyId];
    }

    /**
     * Get the current element id of an assault party.
     * 
     * @param assaultPartyId the assault party id.
     * @param elementId the element id.
     * @return the target element id of the assault party.
     */
    private int getAssaultPartyElementId(int assaultPartyId, int elementId) {
        return this.assaultPartyElementId[assaultPartyId][elementId];
    }

    /**
     * Get the current element position of an assault party.
     * 
     * @param assaultPartyId the assault party id.
     * @param elementId the element id.
     * @return the target element position of the assault party.
     */
    private int getAssaultPartyElementPosition(int assaultPartyId, int elementId) {
        return this.assaultPartyElementPosition[assaultPartyId][elementId];
    }

    /**
     * Get the current element canvas of an assault party.
     * 
     * @param assaultPartyId the assault party id.
     * @param elementId the element id.
     * @return the target element canvas of the assault party.
     */
    private int getAssaultPartyElementCanvas(int assaultPartyId, int elementId) {
        return this.assaultPartyElementCanvas[assaultPartyId][elementId];
    }

    /**
     * Get the current number of paintings in a room.
     * 
     * @param roomId the room id.
     * @return the number of paintings in the room.
     */
    private int getMuseumRoomNumberPaintings(int roomId) {
        return this.museumRoomNumberPaintings[roomId];
    }

    /**
     * Set the current number of paintings in a room.
     * 
     * @param roomId the room id.
     * @param museumRoomNumberPaintings the number of paintings in the room.
     */
    private void decrementMuseumRoomNumberPaintings(int roomId) {
        this.museumRoomNumberPaintings[roomId]--;
    }

    /**
     * Get the current distance to a room.
     * 
     * @param roomId the room id.
     * @return the distance to the room.
     */
    private int getMuseumRoomDistance(int roomId) {
        return this.museumRoomDistance[roomId];
    }

    /**
     * Get the number of stolen paintings.
     * 
     * @return the number of stolen paintings.
     */
    private int getStolenPaintings() {
        return this.stolenPaintings;
    }

    /**
     * Increment the number of stolen paintings.
     */
    private void incrementStolenPaintings() {
        this.stolenPaintings++;
    }

    /**
     * Remove the association of a thief to an assault party.
     * 
     * @param assaultPartyId assault party id.
     * @param elementId element id (position of the thief inside the assault party).
     */
    private void quitAssaultParty(int assaultPartyId, int elementId) {
        /* Update thief situation */
        this.ordinaryThiefSituation[this.assaultPartyElementId[assaultPartyId][elementId]] = 'W';

        /* Remove thief info from assault party */
        this.assaultPartyElementId[assaultPartyId][elementId] = -1;
        this.assaultPartyElementPosition[assaultPartyId][elementId] = -1;
        this.assaultPartyElementCanvas[assaultPartyId][elementId] = -1;

        /* End method if there still is thiefs in party */
        for (int i = 0; i < SimulPar.K; i++)
            if (this.assaultPartyElementId[assaultPartyId][i] != -1)
                return;

        /* Clear assault party target room */
        this.assaultPartyRoomId[assaultPartyId] = -1;
    }
}
