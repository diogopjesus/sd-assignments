package sharedRegions;

import interfaces.GeneralRepositoryInterface;
import entities.*;
import main.SimulPar;
import genclass.GenericIO;
import genclass.TextFile;
import java.util.Objects;

public class GeneralRepository {
	
	private final String log_filename;
	private int masterThiefState;
	private int[] commonThiefStates;
	private char[] commonThiefSituations;
	private int[] commonThiefMD;
	private int[][] thieves; // each thief stores his id, the id of the room where he is located, his position and the number of canvas that he carries
	private int[] room_paintings;
	private int[] room_distances;
	private int stolen_paintings;
	
	// Structure of thieves: 
	// thieves[elem_id][0] -> thief_id
	// thieves[elem_id][1] -> room_id
	// thieves[elem_id][2] -> thief_pos
	// thieves[elem_id][3] -> number of canvas
	
	public GeneralRepository(String log_filename) {
        if ((log_filename == null) || Objects.equals(log_filename, "")) {
            this.log_filename = "log_file";
        }
        else {
            this.log_filename = log_filename;
        }
        // Master Thief
        masterThiefState = MasterThiefStates.PLANNING_THE_HEIST;

        // Common Thieves
        commonThiefStates = new int[SimulPar.M - 1];
        commonThiefSituations = new char[SimulPar.M - 1];
        commonThiefMD = new int[SimulPar.M - 1];
        
        for (int i = 0; i < SimulPar.M - 1; i++) {
        	commonThiefStates[i] = CommonThiefStates.CONCENTRATION_SITE;
        	commonThiefSituations[i] = 'W';
        	commonThiefMD[i] = 0;
        }
        
        // Assault parties
        thieves = new int[SimulPar.M - 1][4];
        for (int i = 0; i < SimulPar.M - 1; i++) {
        	resetAssPartElem(i);
        }
        
        // Museum
        room_paintings = new int[SimulPar.N];
        room_distances = new int[SimulPar.N];
        
        // Stolen paintings
        stolen_paintings = 0;

        logInitialStatus();
    }
	
	public synchronized void setMasterThiefState(int state) {
		masterThiefState = state;
		logStatus();
	}
	
	public synchronized void setCommonThiefState(int thief_id, int state){
		commonThiefStates[thief_id] = state;
		logStatus();
	}
	
	public synchronized void setCommonThiefSituation(int thief_id, char situation) {
		commonThiefSituations[thief_id] = situation;
		logStatus();
	}
	
	public synchronized void setCommonThiefMD(int thief_id, int md) {
		this.commonThiefMD[thief_id] = md;
	}
	
	public synchronized void setAssPartElem(int thief_id, int room_id, int elem) {
		thieves[elem][0] = thief_id;
        thieves[elem][1] = room_id;
        thieves[elem][2] = 0;
        thieves[elem][3] = 0;
	}
	
	public synchronized void resetAssPartElem(int elem) {
		thieves[elem][0] = -1;
        thieves[elem][1] = -1;
        thieves[elem][2] = 0;
        thieves[elem][3] = 0;
	}
	
	public synchronized void setCommonThiefPosition(int elem, int pos) {
		thieves[elem][2] = pos;
        logStatus();
	}
	
	public synchronized void setCarryCanvas(int elem, int canvas) {
		thieves[elem][3] = canvas;
		logStatus();
	}
	
	public synchronized void setRoomPaintings(int[] room_paintings) {
		this.room_paintings = room_paintings;
        logStatus();
	}
	
	public synchronized void setRoomDistance(int[] distances) {
		this.room_distances = distances;
	}
	
	public synchronized void incStolenPaintings() {
		this.stolen_paintings++;
	}
	
	public void logInitialStatus() {
		TextFile logs = new TextFile(); // instantiate a text file handler

        if (!logs.openForWriting(".", log_filename)) {
            GenericIO.writelnString("The operation of creating the file " + log_filename + " failed!");
            System.exit(1);
        }

        logs.writelnString("                           Heist to the Museum - Description of the internal state\n");
        
        logs.writelnString("  MstT   Thief 1     Thief 2     Thief 3     Thief 4     Thief 5     Thief 6");
        
        logs.writelnString("Stat    Stat S MD   Stat S MD   Stat S MD   Stat S MD   Stat S MD   Stat S MD");

        logs.writelnString(
"                Assault party 1                                 Assault party 2                             Museum");
        logs.writelnString(
                "    Elem 1          Elem 2          Elem 3          Elem 1          Elem 2          Elem 3      Room 1   Room 2   Room 3   Room 4   Room 5");
        logs.writelnString(
                "RId Id Pos Cv   RId Id Pos Cv   RId Id Pos Cv   RId Id Pos Cv   RId Id Pos Cv   RId Id Pos Cv   NP DT    NP DT    NP DT    NP DT    NP DT");

        if (!logs.close()) {
            GenericIO.writelnString("An error ocurred while closing the file " + log_filename);
            System.exit(1);
        }
        logStatus();
    }
	
	public void logStatus() {
		TextFile logs = new TextFile(); // instantiate a text file handler
        String status = ""; // status to be printed

        if (!logs.openForAppending(".", log_filename)) {
            GenericIO.writelnString("An error ocurred while trying to append to the file " + log_filename);
            System.exit(1);
        }

        switch (masterThiefState) {
            case MasterThiefStates.PLANNING_THE_HEIST:
                status += "PTH  ";
                break;
            case MasterThiefStates.DECIDING_WHAT_TO_DO:
                status += "DWTD ";
                break;
            case MasterThiefStates.ASSEMBLING_A_GROUP:
                status += "AAG  ";
                break;
            case MasterThiefStates.WAITING_FOR_GROUP_ARRIVAL:
                status += "WFGA ";
                break;
            case MasterThiefStates.PRESENTING_THE_REPORT:
                status += "PTR  ";
                break;
        }

        for (int i = 0; i < SimulPar.M - 1; i++)
            switch (commonThiefStates[i]) {
                case CommonThiefStates.CONCENTRATION_SITE:
                    status += "   CNS  " + commonThiefSituations[i] + "  " + commonThiefMD[i];
                    break;
                case CommonThiefStates.CRAWLING_INWARDS:
                    status += "   CIN  " + commonThiefSituations[i] + "  " + commonThiefMD[i];
                    break;
                case CommonThiefStates.AT_A_ROOM:
                    status += "   AAR  " + commonThiefSituations[i] + "  " + commonThiefMD[i];
                    break;
                case CommonThiefStates.CRAWLING_OUTWARDS:
                    status += "   COUT  " + commonThiefSituations[i] + "  " + commonThiefMD[i];
                    break;
                case CommonThiefStates.COLLECTION_SITE:
                    status += "   CLS " + commonThiefSituations[i] + " " + commonThiefMD[i];
                    break;
            }
        
        // Thieves status
        logs.writelnString(status); 
        
        status = "";
        String.format(
                "RId Id Pos Cv   RId Id Pos Cv   RId Id Pos Cv   RId Id Pos Cv   RId Id Pos Cv   RId Id Pos Cv   NP DT    NP DT    NP DT    NP DT    NP DT");
        for (int i = 0; i < SimulPar.M - 1; i++) {
            status += String.format("%2d  %2d  %2d %2d | ", thieves[i][1], thieves[i][0], thieves[i][2], thieves[i][3]);
        }
        
        for (int i = 0; i < SimulPar.N; i++) {
            status += String.format("%2d %2d    ", room_paintings[i], room_distances[i]);
        }
        
        // Museum status
        logs.writelnString(status + '\n'); 

        if (!logs.close()) {
            GenericIO.writelnString("An error ocurred while closing the file " + log_filename);
            System.exit(1);
        }
	}
	
	public void logFinalStatus() {
		TextFile logs = new TextFile();

        if (!logs.openForAppending(".", log_filename)) {
        	GenericIO.writelnString("An error ocurred while trying to append to the file " + log_filename);
            System.exit(1);
        }

        logs.writelnString("My friends, tonight's effort produced " + stolen_paintings + " priceless paintings!");

        if (!logs.close()) {
        	GenericIO.writelnString("An error ocurred while closing the file " + log_filename);
            System.exit(1);
        }
	}
}
