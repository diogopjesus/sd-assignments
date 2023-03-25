package sharedRegions;

import commInfra.MemException;
import commInfra.MemFIFO;
import entities.CommonThief;
import entities.MasterThief;
import entities.MasterThiefStates;
import main.SimulPar;

public class ControlSite {

    ConcentrationSite concentrationSite;

    MemFIFO<Integer> waitingThieves;
    int nextThiefInLine;
    int currentAssaultParty;
    boolean holdingACanvas;
    boolean canvasCollected;
    boolean canvasHanded;
    boolean[] isRoomEmpty;
    int numberOfCanvas;

    /**
     * 
     */
    public synchronized void startOperations() {
        MasterThief mt = (MasterThief)Thread.currentThread();
        mt.setMasterThiefState(MasterThiefStates.DECIDING_WHAT_TO_DO);
        // TODO: log
    }

    
    /** 
     * @return char
     */
    public synchronized char appraiseSit() {
        
        if(allRoomsCleared()) {
            if(concentrationSite.getNumberOfWaitingThieves() < SimulPar.N)
                return 'R';

            return 'E';
        }

        int partiesInOp = this.thievesInOperation / SimulPar.K;
        int numberOfEmptyRooms;
        int roomNotEmptyID;
        int partyInOperationID;

        /* Wait arrival of ordinary thieves */
        if(partiesInOp == (SimulPar.M-1)/SimulPar.K || /* Both parties are in operation */
          (partiesInOp == 1) && (numberOfEmptyRooms == (SimulPar.N-1)) && (roomNotEmptyID == assaultParties[partyInOperationID].getTargetRoom())) /* if there is only one room left we must wait for the arrival of all elements to know if the room is empty or not */
        {
            return 'R';
        }

        return 'P';
    }

    public synchronized void takeARest() {
        MasterThief mt = (MasterThief)Thread.currentThread();

        mt.setMasterThiefState(MasterThiefStates.WAITING_FOR_GROUP_ARRIVAL);

        while(waitingThieves.empty()) {
            try {
                wait();
            } catch(InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }


    public synchronized void handACanvas() {
        CommonThief ct = (CommonThief)Thread.currentThread();
        
        try{
            waitingThieves.write(ct.getCommonThiefId());
        } catch(MemException me) {
            //TODO
        }
        notifyAll();

        while(nextThiefInLine != ct.getCommonThiefId()) {
            try {
                wait();
            } catch(InterruptedException ie) {
                ie.printStackTrace();
            }
        }

        holdingACanvas = ct.getHoldingACanvas();
        canvasHanded = true;
        canvasCollected = false;

        notifyAll();

        while(!canvasCollected) {
            try {
                wait();
            } catch(InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }


    public synchronized void collectACanvas() {
        MasterThief mt = (MasterThief)Thread.currentThread();
        
        nextThiefInLine = waitingThieves.read();

        notifyAll();
        
        while(!canvasHanded) {
            try {
                wait();
            } catch(InterruptedException ie) {
                ie.printStackTrace();
            }
        }

        if(holdingACanvas) {
            numberOfCanvas++;
            //repos.setRobbedPaintings();
        }
        else isRoomEmpty[roomID] = true;

        canvasHanded = false;
        canvasCollected = true;

        notifyAll();

        mt.setMasterThiefState(MasterThiefStates.DECIDING_WHAT_TO_DO);
    }


    public int getAssaultID() {
        return -1;
    }

    public int getRoomID() {
        return -1;
    }

    private boolean allRoomsCleared() {
        return false;
    }

    private boolean canCreateAssaultParty() {
        return false;
    }

    public int getNumberOfCanvas() {
        return numberOfCanvas;
    }
}
