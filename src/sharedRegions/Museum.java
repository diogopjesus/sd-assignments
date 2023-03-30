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
     *
     */
    private int[] paitingsInRoom;

    /**
     *
     */
    private int[] roomDistances;

    /**
     *
     */
    public Museum(GeneralRepository repos, int[] numPaint, int[] roomDist) {
        this.repos = repos;
        paitingsInRoom = numPaint;
        roomDistances = roomDist;
    }

    /**
     *
     */
    public int getRoomDistance(int roomId) {
        if (roomId < SimulPar.N && roomId >= 0)
            return roomDistances[roomId];
        return -1;
    }

    /**
     *
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
