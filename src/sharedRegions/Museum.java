package sharedRegions;

import main.SimulPar;
import entities.CommonThief;

public class Museum {
	
    private final AssaultParty[] assPart;
	private final GeneralRepository repo;
	
	private int[] paintings;
	
	public Museum(GeneralRepository repo, AssaultParty[] assPart) {
        this.repo = repo;
        this.assPart = assPart;
        this.paintings = new int[SimulPar.N];
        for(int i=0; i<SimulPar.N; i++) 
            paintings[i] = SimulPar.p +(int)(Math.random() * (SimulPar.P-SimulPar.p)+1); 
        repo.setRoomPaintings(paintings);
    }
	
	public synchronized void rollACanvas() {
        CommonThief ct = (CommonThief)Thread.currentThread();

        int room = assPart[ct.getAssaultParty()].getTargetRoom();
        
        if(paintings[room] > 0) {
        	paintings[room]--;
            
            ct.setHoldingACanvas();
            
            repo.setRoomPaintings(paintings);
        }
        else {
            ct.unsetHoldingACanvas();
        }
    }
}
