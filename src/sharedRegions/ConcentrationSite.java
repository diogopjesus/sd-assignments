package sharedRegions;

import main.SimulPar;
import commInfra.*;
import entities.*;

public class ConcentrationSite {

	private MemFIFO<Integer> waitingThieves;
	private int numberOfWaitingThieves;
	private boolean endOfOps;
	private int availableAssaultParty;
	private int[] thievesOnHold = new int[SimulPar.K]; 

	private AssaultParty[] assParts;

	public synchronized char amINeeded() {
		CommonThief ct = (CommonThief)Thread.currentThread();
		
		if(ct.getCommonThiefState() == CommonThiefStates.COLLECTION_SITE) {
			ct.setCommonThiefState(CommonThiefStates.CONCENTRATION_SITE);

			try {
				waitingThieves.write(ct.getCommonThiefId());
				numberOfWaitingThieves++;
			} catch(MemException me) {
				// TODO;
			}
		}
		
		while(!endOfOps || thievesOnHold[0] == ct.getCommonThiefId()) {
			try {
				wait();
			} catch(InterruptedException ie) {
				ie.printStackTrace();
			}
		}

		return endOfOps ? 'E' : 'P';
	}

	public synchronized void prepareAssaultParty(int asspart_id, int room_id) {
		
		MasterThief mt = (MasterThief)Thread.currentThread();
		//TODO: LOG

		while(numberOfWaitingThieves < SimulPar.K) {
			try {
				wait();
			} catch(InterruptedException ie) {
				ie.printStackTrace();
			}
		}

		this.availableAssaultParty = asspart_id;

		assParts[availableAssaultParty].setTargetRoom(room_id);

		for(int i = 0; i < SimulPar.K; i++) {
			try {
				thievesOnHold[i] = waitingThieves.read();
			} catch(MemException me) {
				//TODO
			}

			numberOfWaitingThieves--;
		}
		notifyAll();
		
		while(assParts[availableAssaultParty].getNumberOfThieves() < SimulPar.K) {
			try{
				wait();
			} catch(InterruptedException ie) {
				ie.printStackTrace();
			}
		}

		// TODO: check whether its here
		mt.setMasterThiefState(MasterThiefStates.ASSEMBLING_A_GROUP);
	}


	public synchronized int prepareExcursion() {
		CommonThief ct = (CommonThief)Thread.currentThread();
		
		assParts[availableAssaultParty].assignNewThief(ct.getCommonThiefId());

		ct.setCommonThiefState(CommonThiefStates.CRAWLING_INWARDS);

		for(int i= 0; i < SimulPar.K-1; i++) {
			thievesOnHold[i] = thievesOnHold[i+1];
		}

		notifyAll();

		return availableAssaultParty;
	}

	public synchronized void sumUpResults(int numberOfPaintings) {
		MasterThief mt = (MasterThief)Thread.currentThread();
		
		while(numberOfWaitingThieves < SimulPar.M) {
			try {
				wait();
			} catch(InterruptedException ie) {
				ie.printStackTrace();
			}
		}

		endOfOps = true;
        notifyAll();
        
        mt.setMasterThiefState(MasterThiefStates.PRESENTING_THE_REPORT);
        //repo.setMasterThiefState(((MasterThief) Thread.currentThread()).getMasterThiefState());
	}
}
