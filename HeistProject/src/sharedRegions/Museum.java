package sharedRegions;

import interfaces.MuseumInterface;
//import commInfra.*;
import entities.*;
import genclass.*;
import main.SimulPar;

public class Museum implements MuseumInterface {
	
	private final GeneralRepository repo;
	
	private int[] room_paintings;
	
	public Museum(GeneralRepository repo) {
        this.repo = repo;
        this.room_paintings = new int[SimulPar.N];
        for(int i=0; i<SimulPar.N; i++) 
        	room_paintings[i] = SimulPar.p +(int)(Math.random() * (SimulPar.P-SimulPar.p)+1); 
        repo.setRoomPaintings(room_paintings);
    }
	
	@Override
	public synchronized boolean rollACanvas(int room_id) {
        if(room_paintings[room_id]>0) {
        	room_paintings[room_id]--;
            repo.setRoomPaintings(room_paintings);
            return true;
        }
        return false;
    }
	
}
