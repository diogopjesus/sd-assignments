package sharedRegions;

import interfaces.ControlSiteInterface;
import entities.*;
import main.*;

public class ControlSite implements ControlSiteInterface {
	
	private int canvas;
	private boolean handCanvas;
	private boolean collectCanvas;
	private int room_id;  // id of the room that has been assaulted
	private boolean room_states[]; // indicates if a room still have paintings 
	//private int room_idx;    // index of the rooms that still have paintings
	
	private final GeneralRepository repo;
	
	public ControlSite(GeneralRepository repo){
        this.repo = repo;
        this.canvas = -1;
        this.room_id = -1;
        this.room_states = new boolean[SimulPar.N];
        for(int i=0; i<SimulPar.N; i++) room_states[i]=true;
        this.handCanvas = false;
        this.collectCanvas = false;
    }
	
	@Override
	public synchronized void startOperations() {
		((MasterThief) Thread.currentThread()).setMasterThiefState(MasterThiefStates.DECIDING_WHAT_TO_DO);
		repo.setMasterThiefState(((MasterThief) Thread.currentThread()).getMasterThiefState());
	}

	@Override
	public synchronized void takeARest() {
		
	}
	
	@Override
	public synchronized void collectACanvas() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public synchronized void handACanvas(int room_id, int canvas) {
		// TODO Auto-generated method stub

	}

	@Override
	public synchronized int getRoomId() {
		int idx = 0;
		for(; idx < SimulPar.N; idx++) {
			if(room_states[idx]) break;
		}
		
        return idx;
	}

}
