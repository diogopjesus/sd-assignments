package sharedRegions;

import interfaces.ConcentrationSiteInterface;
import main.SimulPar;
import commInfra.*;
import entities.*;
import genclass.GenericIO;

public class ConcentrationSite implements ConcentrationSiteInterface {
	
	private int waiting_thieves;
	private int recruited_thieves;
	private int heisting_thieves;
	private boolean end_heist;
	private int summoned_thief_id;
	private boolean summoned;
	private int prep_assParty_id;
	private int[] assParty_members;
	private int[] assParty_room;
	private MemFIFO<Integer> thieves_queue;
	private final GeneralRepository repo;
		
	public ConcentrationSite(GeneralRepository repo) {
		try {
			this.thieves_queue = new MemFIFO<>(new Integer[SimulPar.M-1]);
		} 
		catch(MemException e) {
			GenericIO.writelnString("An error ocurred while trying to instantiate the waiting FIFO " + e.getMessage());
			System.exit(1);
		}
		
		this.repo = repo;
		this.waiting_thieves = 0;
		this.recruited_thieves = 0;
		this.heisting_thieves = 0;
		this.end_heist = false;
		this.summoned_thief_id = -1;
		this.summoned = true;
		this.prep_assParty_id = -1;
		this.assParty_members = new int[]{0, 0};
		this.assParty_room = new int[]{-1, -1};
	}
	
	@Override
	public synchronized void prepareAssaultParty(int assParty_id, int room_id) {
		prep_assParty_id = assParty_id;
		while(recruited_thieves < SimulPar.E) {
			if(summoned) {
				summoned = !summoned;
				try {
					summoned_thief_id = thieves_queue.read();
					waiting_thieves--;
				} catch(Exception e){}
				notifyAll();
			}
		}
		
		try {
			wait();
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
		
		recruited_thieves = 0;
		summoned_thief_id = -1;
		prep_assParty_id = -1;
		assParty_room[assParty_id] = room_id;
		heisting_thieves += SimulPar.E;
		
		((MasterThief) Thread.currentThread()).setMasterThiefState(MasterThiefStates.ASSEMBLING_A_GROUP);
		repo.setMasterThiefState(((MasterThief) Thread.currentThread()).getMasterThiefState());
	}

	@Override
	public synchronized char appraiseSit(boolean roomsEmpty) {
		
		if(roomsEmpty){
			if(heisting_thieves > 0) {
				heisting_thieves--;
				return 'R';
			}
			return 'E';
		}
		
		if(waiting_thieves >= SimulPar.E && (assParty_room[0] < 0 || assParty_room[1] < 0)) return 'P';
		if(heisting_thieves > 0) {
			heisting_thieves--;
			return 'R';
		}
		
		
		while(waiting_thieves < SimulPar.E) {
			try {
				wait();
			}
			catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		return 'P';
	}

	@Override
	public synchronized boolean amINeeded(int assParty_id) {
		// Checks if the thief was the last one to arrive from his assault party and updates the value of the room_id associated to that assParty
		if(assParty_id >= 0 && --assParty_members[assParty_id] == 0) {
			assParty_room[assParty_id] = -1;
		}
		
		int commonThief_id = ((CommonThief) Thread.currentThread()).getCommonThiefId();
		repo.setCommonThiefSituation(commonThief_id, 'W');
		try {
			thieves_queue.write(commonThief_id);
			waiting_thieves++;
		}
		catch(Exception e) {}
		notifyAll();
		
		while(summoned_thief_id != commonThief_id && !end_heist) {
			try {
				wait();
			}
			catch(InterruptedException e) {
				GenericIO.writelnString("\t" + e.getMessage());
				System.exit(0);
			}
		}
		
		((CommonThief) Thread.currentThread()).setCommonThiefState(CommonThiefStates.CONCENTRATION_SITE);
		repo.setCommonThiefState(commonThief_id, ((CommonThief) Thread.currentThread()).getCommonThiefState());

        return summoned_thief_id==commonThief_id;
	}

	@Override
	public synchronized int prepareExcursion() {
		int commonThief_id = ((CommonThief) Thread.currentThread()).getCommonThiefId();
        ((CommonThief) Thread.currentThread()).setCommonThiefState(CommonThiefStates.CRAWLING_INWARDS);
        repo.setCommonThiefState(commonThief_id, ((CommonThief) Thread.currentThread()).getCommonThiefState());
        
        summoned = true;
        recruited_thieves++;
        notifyAll();

        assParty_members[prep_assParty_id]++;
        repo.setCommonThiefSituation(commonThief_id, 'P');
        return prep_assParty_id;
	}

	@Override
	public synchronized void sumUpResults() {
		end_heist = true;
        notifyAll();
        
        ((MasterThief) Thread.currentThread()).setMasterThiefState(MasterThiefStates.PRESENTING_THE_REPORT);
        repo.setMasterThiefState(((MasterThief) Thread.currentThread()).getMasterThiefState());

	}
	
	@Override
	public synchronized int getAssPartyRoom(int assParty_id) {
		return assParty_room[assParty_id];
	}
	
	@Override
	public synchronized int getAssParty() {
		if (assParty_room[0] < 0) return 0;
		else return 1;
	}
}
