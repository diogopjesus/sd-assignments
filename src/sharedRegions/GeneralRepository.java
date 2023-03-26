package sharedRegions;

import main.*;
import entities.*;
import genclass.*;

/**
 * General repository.
 * 
 * It is responsible to keep the visible internal state of the heist and provide means to it to be printed in the logging file.
 * It is implemented as an implicit monitor.
 * All public methods are executed in mutual exclusion.
 * There are no internal synchronization points.
 */

public class GeneralRepository
{
    /**
     * 
     */
    
    private final String logFileName;

    /**
     * 
     */

    private int masterThiefState;

    /**
     * 
     */

    private int [] ordinaryThiefState;

    /**
     * 
     */

    private char [] ordinaryThiefSituation;

    /**
     * 
     */

    private int [] ordinaryThiefMaximumDisplacement;

    /**
     * 
     */
    
    private int [] assaultPartyRoomId;

    /**
     * 
     */
    
    private int [][] assaultPartyElementId;

    /**
     * 
     */
    
    private int [][] assaultPartyElementPosition;

    /**
     * 
     */
    
    private boolean [][] assaultPartyElementCanvas;

    /**
     * 
     */
    
    private int [] museumRoomNumberPaitings;

    /**
     * 
     */

    private int [] museumRoomDistance;



    /**
     * Instantiation of a general repository object.
     */

    public GeneralRepository(String logFileName, int [] maxDis, int [] numPaint, int [] roomDist)
    {
        if((logFileName == null) || logFileName.compareTo("") == 0)
            this.logFileName = "internalState.log";
        else
            this.logFileName = logFileName;
        
        masterThiefState = MasterThiefStates.PLANNING_THE_HEIST;

        ordinaryThiefState = new int [SimulPar.M-1];
        ordinaryThiefSituation = new char [SimulPar.M-1];
        ordinaryThiefMaximumDisplacement = new int [SimulPar.M-1];
        for(int i = 0; i < SimulPar.M-1; i++)
        {   ordinaryThiefState[i] = OrdinaryThiefStates.CONCENTRATION_SITE;
            ordinaryThiefSituation[i] = 'W';
            ordinaryThiefMaximumDisplacement[i] = maxDis[i];
        }

        assaultPartyRoomId = new int [(SimulPar.M-1) / SimulPar.K];
        assaultPartyElementId = new int [(SimulPar.M-1) / SimulPar.K] [SimulPar.K];
        assaultPartyElementPosition = new int [(SimulPar.M-1) / SimulPar.K] [SimulPar.K];
        assaultPartyElementCanvas = new boolean [(SimulPar.M-1) / SimulPar.K] [SimulPar.K];
        for(int i = 0; i < (SimulPar.M-1)/SimulPar.K; i++)
        {   assaultPartyRoomId[i] = 0;
            for(int j = 0; j < SimulPar.K; j++)
            {   assaultPartyElementId[i][j] = 0;
                assaultPartyElementPosition[i][j] = 0;
                assaultPartyElementCanvas[i][j] = false;
            }
        }

        museumRoomNumberPaitings = new int [SimulPar.N];
        museumRoomDistance = new int [SimulPar.N];
        for(int i = 0; i < SimulPar.N; i++)
        {   museumRoomNumberPaitings[i] = numPaint[i];
            museumRoomDistance[i] = roomDist[i];
        }

        reportInitialStatus();
    }



    /**
     * 
     * @param state
     */

    public synchronized void setMasterThiefState(int state)
    {
        masterThiefState = state;
        reportStatus();
    }

    /**
     * 
     * @param id
     * @param state
     */

    public synchronized void setOrdinaryThiefState(int id, int state)
    {
        ordinaryThiefState[id] = state;
        reportStatus();
    }

    /**
     * 
     * @param id
     * @param roomId
     */

    public synchronized void setAssaultPartyRoomId(int id, int roomId)
    {
        assaultPartyRoomId[id] = roomId+1;
        reportStatus();
    }

    /**
     * 
     * @param id
     * @param elemId
     */

    public synchronized void addAssaultPartyElement(int id, int elemId)
    {
        
        int i = 0;
        while(assaultPartyElementId[id][i] > 0) i++;
        // TODO: if i >= SimulPar.K
        assaultPartyElementId[id][i] = elemId+1;
        assaultPartyElementPosition[id][i] = 0;
        assaultPartyElementCanvas[id][i] = false;
        
        ordinaryThiefSituation[elemId] = 'P';
        
        reportStatus();
    }

    /**
     * 
     * @param id
     * @param elemId
     */

    public synchronized void removeAssaultPartyElement(int id, int elemId)
    {
        int i = 0;
        while(assaultPartyElementId[id][i] != elemId+1) i++;
        // TODO: if i >= SimulPar.K
        assaultPartyElementId[id][i] = 0;

        // Check if assault party becomes empty
        i = 0;
        while(i < SimulPar.K && assaultPartyElementId[id][i] == 0) i++;
        if(i == SimulPar.K)
            assaultPartyRoomId[id] = 0;

        ordinaryThiefSituation[elemId] = 'W';

        reportStatus();
    }

    /**
     * 
     * @param id
     * @param elemId
     * @param pos
     */

    public synchronized void updateAssaultPartyElementPosition(int id, int elemId, int pos)
    {
        int i = 0;
        while(assaultPartyElementId[id][i] != elemId+1) i++;
        // TODO: if i >= SimulPar.K
        assaultPartyElementPosition[id][i] = pos;
        reportStatus();
    }

    /**
     * 
     * @param id
     * @param elemId
     */

    public synchronized void holdAssaultPartyElementCanvas(int id, int elemId, int roomId)
    {
        int i = 0;
        while(assaultPartyElementId[id][i] != elemId+1) i++;
        // TODO: if i >= SimulPar.K
        assaultPartyElementCanvas[id][i] = true;
        museumRoomNumberPaitings[roomId]--;
        reportStatus();
    }

    /**
     * 
     * @param id
     * @param elemId
     */
    public synchronized void yieldAssaultPartyElementCanvas(int id, int elemId)
    {
        int i = 0;
        while(assaultPartyElementId[id][i] != elemId+1) i++;
        // TODO: if i >= SimulPar.K
        assaultPartyElementCanvas[id][i] = false;
        reportStatus();
    }

    public synchronized void endAssault(int numberOfCanvas)
    {
        reportFinalStatus(numberOfCanvas);
    }



    /**
     * Write the header to the logging file.
     * Internal operation.
     */

    private void reportInitialStatus()
    {
        TextFile log = new TextFile();

        if(!log.openForWriting(".", logFileName))
        {   GenericIO.writeString("The operation of creating the file " + logFileName + " failed!");
            System.exit(1);
        }

        log.writelnString("                             Heist to the Museum - Description of the internal state");
        log.writelnString();
        log.writelnString("MstT   Thief 1      Thief 2      Thief 3      Thief 4      Thief 5      Thief 6");
        log.writelnString("Stat  Stat S MD    Stat S MD    Stat S MD    Stat S MD    Stat S MD    Stat S MD");
        log.writelnString("                   Assault party 1                       Assault party 2                       Museum");
        log.writelnString("           Elem 1     Elem 2     Elem 3          Elem 1     Elem 2     Elem 3   Room 1  Room 2  Room 3  Room 4  Room 5");
        log.writelnString("    RId  Id Pos Cv  Id Pos Cv  Id Pos Cv  RId  Id Pos Cv  Id Pos Cv  Id Pos Cv   NP DT   NP DT   NP DT   NP DT   NP DT");
        log.writelnString();
        
        if (!log.close ())
        {   GenericIO.writelnString ("The operation of closing the file " + logFileName + " failed!");
            System.exit (1);
        }

        reportStatus();
    } 

    /**
     * Write a state line at the end of the logging file.
     * Internal operation.
     */

    private void reportStatus()
    {
        TextFile log = new TextFile();

        String lineStatus = "";

        if(!log.openForAppending(".", logFileName))
        {   GenericIO.writeString("The operation of creating the file " + logFileName + " failed!");
            System.exit(1);
        }

        /* Write first status line */
        lineStatus += getMasterThiefStateRep() + "  ";
        for(int i = 0; i < SimulPar.M-1; i++)
        {   lineStatus += getOrdinaryThiefStateRep(i) + " ";
            lineStatus += ordinaryThiefSituation[i] + "  ";
            lineStatus += ordinaryThiefMaximumDisplacement[i] + "    ";
        }
        log.writelnString(lineStatus);

        /* Write second status line */
        lineStatus = "     ";
        for(int i = 0; i < (SimulPar.M-1)/SimulPar.K; i++)
        {   lineStatus += assaultPartyRoomId[i] + "    ";
            for(int j = 0; j < SimulPar.K; j++)
            {   lineStatus += assaultPartyElementId[i][j] + "  ";
                lineStatus += String.format("%02d",assaultPartyElementPosition[i][j]) + "  ";
                lineStatus += (assaultPartyElementCanvas[i][j] ? 1 : 0) + "   ";
            }
        }
        for(int i = 0; i < SimulPar.N; i++)
        {   lineStatus += String.format("%02d", museumRoomNumberPaitings[i]) + " ";
            lineStatus += String.format("%02d", museumRoomDistance[i]) + "   ";
        }
        log.writelnString(lineStatus);

        if (!log.close ())
        {   GenericIO.writelnString ("The operation of closing the file " + logFileName + " failed!");
            System.exit (1);
        }
    }

    /**
     * 
     * @param numberOfPaintings
     */

    private void reportFinalStatus(int numberOfPaintings)
    {
        TextFile log = new TextFile();

        if(!log.openForAppending(".", logFileName))
        {   GenericIO.writeString("The operation of creating the file " + logFileName + " failed!");
            System.exit(1);
        }

        log.writelnString("My friends, tonight's effort produced " + numberOfPaintings + " priceless paintings!");
        
        if (!log.close ())
        {   GenericIO.writelnString ("The operation of closing the file " + logFileName + " failed!");
            System.exit (1);
        }
    }

    /**
     * 
     * @return
     */

    private String getMasterThiefStateRep()
    {
        switch(masterThiefState)
        {   case MasterThiefStates.PLANNING_THE_HEIST:
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
     * 
     * @param thiefId
     * @return
     */
    
    private String getOrdinaryThiefStateRep(int thiefId)
    {
        switch(ordinaryThiefState[thiefId])
        {   case OrdinaryThiefStates.CONCENTRATION_SITE:
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
