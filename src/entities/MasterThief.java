package entities;

import sharedRegions.*;

/**
 * Master thief thread.
 * 
 * It simulates the master thief life cycle.
 */

public class MasterThief extends Thread
{
    /**
     * 
     */
    private GeneralRepository repos;

    /**
     * 
     */
    private ControlCollectionSite contColSite;
    
    /**
     * 
     */
    private ConcentrationSite concentSite;

    /**
     * 
     */
    private AssaultParty [] assaultParties;

    /**
     * 
     */
    private Museum museum;

    /**
     * 
     */
    private int masterThiefId;

    /**
     * 
     */
    private int masterThiefState;


    
    /**
     * 
     * @param repos
     * @param contColSite
     * @param concentSite
     * @param assaultParties
     * @param masterThiefId
     */
    public MasterThief(GeneralRepository repos, ControlCollectionSite contColSite, ConcentrationSite concentSite, AssaultParty [] assaultParties, Museum museum, int masterThiefId)
    {
        this.repos = repos;
        this.contColSite = contColSite;
        this.concentSite = concentSite;
        this.assaultParties = assaultParties;
        this.museum = museum;

        this.masterThiefId = masterThiefId;
        this.masterThiefState = MasterThiefStates.PLANNING_THE_HEIST;
    }



    /**
     * 
     * @return
     */
    public int getMasterThiefId()
    {
        return masterThiefId;
    }

    /**
     * 
     * @return
     */
    public int getMasterThiefState()
    {
        return masterThiefState;
    }

    /**
     * 
     * @param masterThiefState
     */
    public void setMasterThiefState(int masterThiefState)
    {
        this.masterThiefState = masterThiefState;
    }



    @Override
    public void run()
    {
        char oper;
        int assaultPartyId, roomId, roomDistance;
        int numberOfCanvas;

        contColSite.startOperations();
        
        while((oper = contColSite.appraiseSit()) != 'E')
        {
            switch(oper)
            {
                case 'P':
                    assaultPartyId = contColSite.getAvailableAssaultParty();
                    roomId = contColSite.getAvailableRoom();
                    roomDistance = museum.getRoomDistance(roomId);

                    concentSite.prepareAssaultParty(assaultPartyId, roomId, roomDistance);

                    assaultParties[assaultPartyId].sendAssaultParty();

                    break;
                
                case 'R':
                    contColSite.takeARest();

                    contColSite.collectACanvas();
                    
                    break;
            }
        }

        numberOfCanvas = contColSite.getNumberOfCanvas();
        
        concentSite.sumUpResults(numberOfCanvas);
    }
}
