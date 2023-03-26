package entities;

import main.SimulPar;
import sharedRegions.*;

/**
 * Ordinary thief thread.
 * 
 * It simulates the ordinary thief life cycle.
 */

public class OrdinaryThief extends Thread
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
    private int ordinaryThiefId;

    /**
     * 
     */
    private int ordinaryThiefState;
    
    /**
     * 
     */
    private int assaultPartyId;

    /**
     * 
     */
    private int maximumDisplacement;

    /**
     * 
     */
    private boolean withCanvas;

    /**
     * 
     * @param repos
     * @param contColSite
     * @param concentSite
     * @param assaultParties
     * @param museum
     * @param ordinaryThiefId
     */
    public OrdinaryThief(GeneralRepository repos, ControlCollectionSite contColSite, ConcentrationSite concentSite, AssaultParty [] assaultParties, Museum museum, int ordinaryThiefId)
    {
        this.repos = repos;
        this.contColSite = contColSite;
        this.concentSite = concentSite;
        this.assaultParties = assaultParties;
        this.museum = museum;

        this.ordinaryThiefId = ordinaryThiefId;
        this.ordinaryThiefState = OrdinaryThiefStates.COLLECTION_SITE;
        this.maximumDisplacement = SimulPar.md +(int)Math.round(Math.random() * (SimulPar.MD - SimulPar.md));
        this.withCanvas = false;
    }



    /**
     * 
     * @return
     */
    public int getOrdinaryThiefId()
    {
        return ordinaryThiefId;
    }

    /**
     * 
     * @return
     */
    public int getOrdinaryThiefState()
    {
        return ordinaryThiefState;
    }

    /**
     * 
     * @param ordinaryThiefState
     */
    public void setOrdinaryThiefState(int ordinaryThiefState)
    {
        this.ordinaryThiefState = ordinaryThiefState;
    }

    /**
     * 
     * @return
     */
    public int getMaximumDisplacement()
    {
        return maximumDisplacement;
    }

    /**
     * 
     */
    public boolean isHoldingCanvas()
    {
        return withCanvas;
    }

    /**
     * 
     */
    public void holdCanvas()
    {
        withCanvas = true;
    }

    /**
     * 
     */
    public void dropCanvas()
    {
        withCanvas = false;
    }




    @Override
    public void run()
    {
        int roomId;

        while(concentSite.amINeeded() != 'E')
        {
            assaultPartyId = concentSite.prepareExcursion();

            roomId = assaultParties[assaultPartyId].getTargetRoom();

            while(assaultParties[assaultPartyId].crawlIn());

            museum.rollACanvas(roomId);

            assaultParties[assaultPartyId].reverseDirection();

            while(assaultParties[assaultPartyId].crawlOut());

            contColSite.handACanvas(assaultPartyId, roomId);
        }
    }
}
