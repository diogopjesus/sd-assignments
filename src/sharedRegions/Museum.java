package sharedRegions;

import entities.*;
import main.*;

/**
 * Museum.
 *
 * It is responsible to (...) All public methods are executed in mutual exclusion. There are X
 * internal synchronization points: (...)
 */
public class Museum {
    /**
     * Reference to the general repository.
     */
    private final GeneralRepository repos;

    /**
     * Number of paintings in each room 
     */
    private int[] paitingsInRoom;

    /**
     * Distances to each room in the museum  
     */
    private int[] roomDistances;

    /**
     * Instantiation of the museum monitor.
     * 
     * @param repos Reference to GeneralRepository 
     * @param numPaint number of paintings of each room 
     * @param roomDist distances to each room
     */
    public Museum(GeneralRepository repos, int[] numPaint, int[] roomDist) {
        this.repos = repos;
        paitingsInRoom = numPaint;
        roomDistances = roomDist;
    }

    /**
     * Get the room distance 
     * 
     * @return the distance to the room
     */
    public int getRoomDistance(int roomId) {
        if (roomId < SimulPar.N && roomId >= 0)
            return roomDistances[roomId];
        return -1;
    }

    /**
     * Ordinary thief rolls a canvas 
     * 
     * @param assaultPartyId assault party ID
     * @param roomId id of the room
     */
    public synchronized void rollACanvas(int assaultPartyId, int roomId) {
        OrdinaryThief ot = (OrdinaryThief) Thread.currentThread();

        if (paitingsInRoom[roomId] > 0) {
            paitingsInRoom[roomId]--;
            ot.holdCanvas();
            repos.holdAssaultPartyElementCanvas(assaultPartyId, ot.getOrdinaryThiefId(), roomId);
        } else {
            ot.dropCanvas();
        }
    }
}
