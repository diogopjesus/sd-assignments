package sharedRegions;

import commInfra.*;
import entities.*;
import genclass.*;
import main.*;

/**
 * Master thief control and collection site.
 * 
 * It is responsible to (...)
 * All public methods are executed in mutual exclusion.
 * There are X internal synchronization points: (...)
 */

public class ControlCollectionSite
{
    /**
     * Reference to the general repository.
     */
    private final GeneralRepository repos;

    /**
     * 
     */
    private AssaultParty [] assaultParties;

    /**
     * 
     */
    private int numberOfCanvas;

    /**
     * 
     */
    private boolean[] emptyRooms;

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
    private int nextThiefInLine;

    /**
     * 
     */
    private int clearedOrdinaryThieves;

    /**
     * 
     */
    private int activeAssaultParties;

    private boolean infoUpdated;



    /**
     * 
     * @param repos
    */
    public ControlCollectionSite(GeneralRepository repos, AssaultParty [] assaultParties)
    {
        this.repos = repos;
        this.assaultParties = assaultParties;
        
        this.numberOfCanvas = 0;
        this.emptyRooms = new boolean[SimulPar.N];
        for(int i = 0; i < SimulPar.N; i++)
            this.emptyRooms[i] = false;
        
        try
        {
            this.waitingThieves = new MemFIFO<>(new Integer [SimulPar.M - 1] );
        }
        catch(MemException e)
        {
            GenericIO.writelnString ("Instance of waiting  FIFO failed: " + e.getMessage());
            System.exit(1);
        }
        this.numberOfWaitingThieves = 0;

        this.nextThiefInLine = -1;

        this.clearedOrdinaryThieves = 6;
        this.activeAssaultParties = 0;

        this.infoUpdated = false;
    }



    /**
     * 
     * @return
     */
    public int getNumberOfCanvas()
    {
        return numberOfCanvas;
    }

    /**
     * 
     */
    public int getAvailableAssaultParty()
    {
        for(int i = 0; i < (SimulPar.M-1)/SimulPar.K; i++)
            if(!assaultParties[i].operationStatus())
                return i;
        
        return -1;
    }

    /**
     * 
     * @return
     */
    public int getAvailableRoom()
    {
        for(int i = 0; i < SimulPar.N; i++)
            if(!emptyRooms[i])
                for(int j = 0; j < SimulPar.M/SimulPar.K; j++)
                    if(!assaultParties[j].operationStatus() && assaultParties[j].getTargetRoom() != i)
                        return i;
        
        return -1;
    }



    /**
     * 
     */
    private boolean allRoomsCleared()
    {
        for(int i = 0; i < SimulPar.N; i++)
            if(!emptyRooms[i])
                return false;
        
        return true;
    }



    /**
     * 
     */
    public synchronized void startOperations()
    {
        System.out.println("Master thief entered startOperations");
     
        MasterThief mt = (MasterThief)Thread.currentThread();

        mt.setMasterThiefState(MasterThiefStates.DECIDING_WHAT_TO_DO);
        
        System.out.println("Master thief left startOperations");
        //TODO: log;
    }

    /**
     * 
     * @return 
     */
    public synchronized char appraiseSit()
    {
        // MasterThief mt = (MasterThief)Thread.currentThread();
        System.out.println("Master thief entered appraiseSit");


        /* Get Number of Empty Rooms and ID of a not empty room */
        int numberOfEmptyRooms = 0;
        int roomNotEmptyId = 0;
        for(int i = 0; i < SimulPar.N; i++)
        {
            if(emptyRooms[i])
                numberOfEmptyRooms++;
            else
                roomNotEmptyId = i;
        }

        /* Get id of an assault party in operation */
        int assaultPartyId = -1;
        for(int i = 0; i < (SimulPar.M-1)/SimulPar.K; i++)
            if(assaultParties[i].operationStatus())
            {
                assaultPartyId = i;
                break;
            }
        
        // Wait arrival of ordinary thieves
        if((activeAssaultParties == ((SimulPar.M-1)/SimulPar.K)) ||
           ((activeAssaultParties == 1) && (numberOfEmptyRooms == SimulPar.N-1) && (assaultParties[assaultPartyId].getTargetRoom() == roomNotEmptyId)))
        {   System.out.println("Master thief left appraiseSit");
            return 'R';
        }
        
        if(allRoomsCleared())
        {
            while(clearedOrdinaryThieves < SimulPar.M)
            {
                try
                {   wait();
                }
                catch(InterruptedException e)
                {   e.printStackTrace();
                }
            }
            
            System.out.println("Master thief left appraiseSit");
            return 'E';
        }

        while(clearedOrdinaryThieves < SimulPar.K || activeAssaultParties == 2)
        {
            try
            {   wait();
            }
            catch(InterruptedException e)
            {   e.printStackTrace();
            }
        }
        
        activeAssaultParties++;
        clearedOrdinaryThieves-=3;

        System.out.println("Master thief left appraiseSit");
        return 'P';
    }

    /**
     * 
     */
    public synchronized void takeARest()
    {
        System.out.println("Master thief entered takeARest");

        MasterThief mt = (MasterThief)Thread.currentThread();

        mt.setMasterThiefState(MasterThiefStates.WAITING_FOR_GROUP_ARRIVAL);

        while(numberOfWaitingThieves == 0)
        {
            try
            {   wait();
            } catch(InterruptedException ie) {
                ie.printStackTrace();
            }
        }
        System.out.println("Master thief left takeARest");
    }

    /**
     * 
     */
    public synchronized void handACanvas(int assaultPartyId, int roomId)
    {
        OrdinaryThief ot = (OrdinaryThief)Thread.currentThread();

        System.out.println("Ordinary thief " + ot.getOrdinaryThiefId() + " entered handACanvas");

        ot.setOrdinaryThiefState(OrdinaryThiefStates.COLLECTION_SITE);

        if(ot.isHoldingCanvas() || !emptyRooms[roomId])
        {
            try
            {   waitingThieves.write(ot.getOrdinaryThiefId());
                numberOfWaitingThieves++;
            } catch(MemException e)
            {   GenericIO.writelnString ("Retrieval of customer id from waiting FIFO failed: " + e.getMessage ());
                System.exit(1);
            }

            notifyAll();
        
            while(nextThiefInLine != ot.getOrdinaryThiefId())
            {
                try
                {   wait();
                } catch(InterruptedException e)
                {   e.printStackTrace();
                }
            }

            if(ot.isHoldingCanvas())
                numberOfCanvas++;
            else
                emptyRooms[roomId] = true;            
            
            infoUpdated = true;

            System.out.println("Number of paintings: " + numberOfCanvas);    
        }

        assaultParties[assaultPartyId].removeThief(ot.getOrdinaryThiefId());
        if(!assaultParties[assaultPartyId].operationStatus())
            activeAssaultParties--;
        
        clearedOrdinaryThieves++;

        notifyAll();

        System.out.println("Ordinary thief " + ot.getOrdinaryThiefId() + " left handACanvas");
    }

    /**
     * 
     */
    public synchronized void collectACanvas()
    {
        System.out.println("Master thief entered collectACanvas");

        MasterThief mt = (MasterThief)Thread.currentThread();

        try
        {   nextThiefInLine = waitingThieves.read();
            numberOfWaitingThieves--;
        }
        catch(MemException e)
        {   GenericIO.writelnString ("Instance of waiting  FIFO failed: " + e.getMessage());
            System.exit(1);
        }
        
        infoUpdated = false;

        notifyAll();

        while(!infoUpdated)
        {
            try
            {   wait();
            } catch(InterruptedException e)
            {   e.printStackTrace();
            }
        }

        mt.setMasterThiefState(MasterThiefStates.DECIDING_WHAT_TO_DO);
        System.out.println("Master thief left collectACanvas");
    }
}
