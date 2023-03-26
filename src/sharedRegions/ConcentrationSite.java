package sharedRegions;

import commInfra.*;
import genclass.*;
import entities.*;
import main.*;

/**
 * Ordinary thieves concentration site.
 * 
 * It is responsible to (...)
 * All public methods are executed in mutual exclusion.
 * There are three internal synchronization points:
 *   - a single blocking point for the master thief, where he waits for sufficient ordinary thieves to form a assault party.
 *   - a single blocking point for the master thief, where he waits for every ordinary thieves to sum up results.
 *   - an array of blocking points, one per each ordinary thief, where he waits to be called to an assault party or to terminate operations.
 */

public class ConcentrationSite
{
    /**
     * Reference to the general repository.
     */
    private final GeneralRepository repos;

    /**
     * 
     */
    private final AssaultParty [] assaultParties;

    /**
     * 
     */
    private MemFIFO<Integer> waitingThieves;

    /**
     * 
     */
    private int numberOfWaitingThieves;

    /**
     * 
     */
    private int [] summonedThieves;

    /**
     * 
     */
    private int availableAssaultParty;

    /**
     * 
     */
    private boolean endOfOps;



    /**
     * 
     */
    public ConcentrationSite(GeneralRepository repos, AssaultParty [] assaultParties)
    {
        this.repos = repos;
        this.assaultParties = assaultParties;
        
        try
        {   this.waitingThieves = new MemFIFO<>(new Integer [SimulPar.M - 1] );
        }
        catch(MemException e)
        {   GenericIO.writelnString ("Instance of waiting  FIFO failed: " + e.getMessage());
            System.exit(1);
        }
        this.numberOfWaitingThieves = 0;

        this.endOfOps = false;

        this.summonedThieves = new int[SimulPar.K];
        summonedThieves = new int[]{-1,-1,-1};
    }



    /**
     * 
     * @return
     */
    public synchronized char amINeeded()
    {
        OrdinaryThief ot = (OrdinaryThief)Thread.currentThread();

        /* Check if thief returns from ControlSite */
        if(ot.getOrdinaryThiefState() == OrdinaryThiefStates.COLLECTION_SITE)
        {
            ot.setOrdinaryThiefState(OrdinaryThiefStates.CONCENTRATION_SITE);
            
            try
            {   waitingThieves.write(ot.getOrdinaryThiefId());
            }
            catch(MemException e)
            {   GenericIO.writelnString ("Instantiation of waiting FIFO failed: " + e.getMessage ());
                System.exit(1);
            }
            numberOfWaitingThieves++;

            notifyAll();
        }

        while(!endOfOps && summonedThieves[0] != ot.getOrdinaryThiefId())
        {
            try
            {   wait();
            }
            catch(InterruptedException e)
            {   e.printStackTrace();
            }
        }

        if(endOfOps) {
            for(int i = 0; i < SimulPar.K-1; i++)
                summonedThieves[i] = summonedThieves[i+1];
            notifyAll();
        }

        return endOfOps ? 'E' : 'P';
    }

    /**
     * 
     */
    public synchronized void prepareAssaultParty(int assaultPartyId, int roomId, int roomDistance)
    {
        MasterThief mt = (MasterThief)Thread.currentThread();
    
        while(numberOfWaitingThieves < SimulPar.K)
        {
            try
            {   wait();
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
        }

        assaultParties[assaultPartyId].setTargetRoom(roomId, roomDistance);
        assaultParties[assaultPartyId].startOperation();

        for(int i = 0; i < SimulPar.K; i++) {
			try
            {   summonedThieves[i] = waitingThieves.read();
                numberOfWaitingThieves--;
			} catch(MemException e)
            {   GenericIO.writelnString ("Retrieval of customer id from waiting FIFO failed: " + e.getMessage ());
                System.exit(1);
			}
		}

    
        mt.setMasterThiefState(MasterThiefStates.ASSEMBLING_A_GROUP);
    
        availableAssaultParty = assaultPartyId;
        notifyAll();

        while(assaultParties[assaultPartyId].getNumberOfThievesInParty() < SimulPar.K)
        {
            try
            {   wait();
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
        }

        // Clear summonedThieves array
        summonedThieves = new int[]{-1,-1,-1};
    }

    /**
     * @return
     */
    public synchronized int prepareExcursion()
    {
        OrdinaryThief ot = (OrdinaryThief)Thread.currentThread();
   
        assaultParties[availableAssaultParty].assignNewThief(ot.getOrdinaryThiefId());
        
        ot.setOrdinaryThiefState(OrdinaryThiefStates.CRAWLING_INWARDS);
        
        for(int i = 0; i < SimulPar.K-1; i++)
            summonedThieves[i] = summonedThieves[i+1];

        notifyAll();

        return availableAssaultParty;
    }

    /**
     * 
     */
    public synchronized void sumUpResults(int numberOfCanvas)
    {
        MasterThief mt = (MasterThief)Thread.currentThread();

        while(numberOfWaitingThieves < SimulPar.M-1) {
			try {
				wait();
			} catch(InterruptedException ie) {
				ie.printStackTrace();
			}
		}

        endOfOps = true;
        notifyAll();

        mt.setMasterThiefState(MasterThiefStates.PRESENTING_THE_REPORT);
    }
}
