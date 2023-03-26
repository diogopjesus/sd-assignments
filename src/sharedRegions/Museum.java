package sharedRegions;

import entities.*;
import main.*;

/**
 * Museum.
 * 
 * It is responsible to (...)
 * All public methods are executed in mutual exclusion.
 * There are X internal synchronization points: (...)
 */

public class Museum
{
    /**
     * Reference to the general repository.
     */
    private final GeneralRepository repos;

    /**
     * 
     */
    private int [] paitingsInRoom;

    /**
     * 
     */
    private int [] roomDistances;


    /**
     * 
     */
    public Museum(GeneralRepository repos)
    {
        this.repos = repos;
        generatePaintingsInRooms();
        calculateRoomDistances();
        //TODO: repos

    }



    /**
     * 
     */
    private void generatePaintingsInRooms()
    {
        paitingsInRoom = new int[SimulPar.N];
        
        for(int i = 0; i < SimulPar.N; i++)
            paitingsInRoom[i] = SimulPar.p + (int)Math.round(Math.random() * (SimulPar.P - SimulPar.p));
    }

    /**
     * 
     */
    private void calculateRoomDistances()
    {
        roomDistances = new int[SimulPar.N];

        for(int i = 0; i < SimulPar.N; i++)
            roomDistances[i] = SimulPar.d + (int)Math.round(Math.random() * (SimulPar.D - SimulPar.d));
    }



    /**
     * 
     */
    public int getRoomDistance(int roomId)
    {
        if(roomId < SimulPar.N && roomId >= 0)
            return roomDistances[roomId];
        return -1;
    }



    /**
     * 
     */
    public synchronized void rollACanvas(int roomId)
    {
        OrdinaryThief ot = (OrdinaryThief)Thread.currentThread();

        System.out.println("Ordinary thief " + ot.getOrdinaryThiefId() + " entered rollACanvas");

        if(paitingsInRoom[roomId] > 0)
        {   paitingsInRoom[roomId]--;
            ot.holdCanvas();
        }
        else
        {   ot.dropCanvas();
        }

        System.out.println("Ordinary thief " + ot.getOrdinaryThiefId() + " left rollACanvas");
    }
}
