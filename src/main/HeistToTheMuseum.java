package main;

import entities.*;
import sharedRegions.*;

/**
 * Simulation of the heist to the museum.
 */

public class HeistToTheMuseum
{
    /**
     * Main method.
     * 
     * @param args runtime arguments
     */

    public static void main(String[] args)
    {
        MasterThief masterThief;
        OrdinaryThief [] ordinaryThieves = new OrdinaryThief [SimulPar.M-1];

        AssaultParty [] assaultParties = new AssaultParty [(SimulPar.M-1)/SimulPar.K];
        ConcentrationSite concentSite;
        ControlCollectionSite contColSite;
        GeneralRepository repos;
        Museum museum;

        // Init
        repos = new GeneralRepository();
        
        for(int i = 0; i < (SimulPar.M-1)/SimulPar.K; i++)
            assaultParties[i] = new AssaultParty(repos);

        concentSite = new ConcentrationSite(repos, assaultParties);
        
        contColSite = new ControlCollectionSite(repos, assaultParties);
        
        museum = new Museum(repos);

        masterThief = new MasterThief(repos, contColSite, concentSite, assaultParties, museum, 0);

        System.out.println("Start Simulation");

        for(int i = 0; i < SimulPar.M-1; i++)
            ordinaryThieves[i] = new OrdinaryThief(repos, contColSite, concentSite, assaultParties, museum, i);
        
        masterThief.start();
        for(int i = 0; i < SimulPar.M-1; i++)
            ordinaryThieves[i].start();
    }
}
