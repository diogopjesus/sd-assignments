package sharedRegions;

import entities.*;
import main.SimulPar;

/**
 * Assault party.
 * 
 * It is responsible to (...)
 * All public methods are executed in mutual exclusion.
 * There are X internal synchronization points: (...)
 */

public class AssaultParty
{
    /**
     * Reference to the general repository.
     */
    private final GeneralRepository repos;

    /**
     * 
     */
    private int numberOfThievesInParty;

    /**
     * 
     */
    private boolean inOperation;

    /**
     * 
     */
    private int targetRoom;

    /**
     * 
     */
    private int targetRoomDistance;

    /**
     * 
     */
    private int [] thievesIdsInParty;

    /**
     * 
     */
    private int nextThiefToMove;

    /**
     * 
     */
    private boolean executeAssault;

    /**
     * 
     */
    private int [] thievesPositionsInParty;

    /**
     * 
     */
    private int maxThiefPosition;

    /**
     * 
     */
    private int minThiefPosition;

    /**
     * 
     */
    private int numberOfThievesAtRoom;

    /**
     * 
     */
    private int numberOfThievesAtControlSite;


    /**
     * 
     */
    public AssaultParty(GeneralRepository repos)
    {
        this.repos = repos;
        this.numberOfThievesInParty = 0;
        this.inOperation = false;
        this.thievesIdsInParty = new int[SimulPar.K];
        this.executeAssault = false;
        this.thievesPositionsInParty = new int[SimulPar.K];
        this.maxThiefPosition = 0;
        this.minThiefPosition = 0;
        this.numberOfThievesAtRoom = 0;
        this.numberOfThievesAtControlSite = 0; 
    }



    /**
     * 
     */
    public boolean operationStatus()
    {
        return inOperation;
    }

    /**
     * 
     */
    public void startOperation()
    {
        this.inOperation = true;
    }

    /**
     * 
     */
    public void endOperation()
    {
        this.inOperation = false;
    }

    /**
     * 
     * @return
     */
    public int getNumberOfThievesInParty()
    {
        return numberOfThievesInParty;
    }

    /**
     * 
     */
    public void setTargetRoom(int targetRoom, int targetRoomDistance)
    {
        this.targetRoom = targetRoom;
        this.targetRoomDistance = targetRoomDistance;
    }

    /**
     * 
     */
    public int getTargetRoom()
    {
        return targetRoom;
    }

    /**
     * 
     */
    public void assignNewThief(int thiefId)
    {
        numberOfThievesInParty++;
        thievesIdsInParty[numberOfThievesInParty-1] = thiefId;
    }

    /**
     * 
     */
    public void removeThief(int thiefId)
    {
        for(int i = 0; i < numberOfThievesInParty-1; i++)
        {
            if(thievesIdsInParty[i] == thiefId)
            {
                thievesIdsInParty[i] = thievesIdsInParty[numberOfThievesInParty-1];
                thievesIdsInParty[numberOfThievesInParty-1] = -1;
            }
        }

        numberOfThievesInParty--;
        if(numberOfThievesInParty == 0)
            endOperation();
    }

    /**
     * 
     * @param commonThiefId
     * @return
     */
    private int getThiefIndex(int commonThiefId) {
        int thiefIndex = -1;
        for (int i = 0; i < SimulPar.K; i++) {
            if (thievesIdsInParty[i] == commonThiefId) {
                thiefIndex = i;
                break;
            }
        }
        return thiefIndex;
    }

    /**
     * 
     * @param thiefIndex
     * @return
     */
    private int getPreviousThiefIndex(int thiefIndex) {
        // if the last thief then get value to the first thief
        if (minThiefPosition == thievesPositionsInParty[thiefIndex])
            for (int i = 0; i < SimulPar.K; i++)
                if (thievesPositionsInParty[i] == maxThiefPosition)
                    return i;

        int distanceBetweenThieves, minDistanceBetweenThieves = SimulPar.S, previousThiefIndex = -1;
        for (int i = 0; i < SimulPar.K; i++) {
            distanceBetweenThieves = thievesPositionsInParty[thiefIndex] - thievesPositionsInParty[i];
            if (distanceBetweenThieves > 0 && distanceBetweenThieves <= minDistanceBetweenThieves) {
                minDistanceBetweenThieves = distanceBetweenThieves;
                previousThiefIndex = i;
            }
        }

        return previousThiefIndex;
    }

    /**
     * 
     * @param thiefIndex
     * @return
     */
    private int getNextThiefIndex(int thiefIndex) {
        // if the first thief then get value to the last thief
        if (maxThiefPosition == thievesPositionsInParty[thiefIndex]){
            for (int i = 0; i < SimulPar.K; i++){
                if (thievesPositionsInParty[i] == minThiefPosition)
                    return i;
            }
        }

        int distanceBetweenThieves, minDistanceBetweenThieves = SimulPar.S, nextThiefIndex = -1;
        for (int i = 0; i < SimulPar.K; i++) {
            distanceBetweenThieves = thievesPositionsInParty[i] - thievesPositionsInParty[thiefIndex];
            if (distanceBetweenThieves > 0 && distanceBetweenThieves <= minDistanceBetweenThieves) {
                minDistanceBetweenThieves = distanceBetweenThieves;
                nextThiefIndex = i;
            }
        }

        return nextThiefIndex;
    }

    /**
     * 
     */
    private void updateMinMaxPositions() {
        // Update maximum and minimum position
        int temp = minThiefPosition;
        minThiefPosition = maxThiefPosition;
        maxThiefPosition = temp;
        for (int i = 0; i < SimulPar.K; i++) {
            if (thievesPositionsInParty[i] < minThiefPosition)
                minThiefPosition = thievesPositionsInParty[i];

            if (thievesPositionsInParty[i] > maxThiefPosition)
                maxThiefPosition = thievesPositionsInParty[i];
        }
    }

    /**
     * 
     * @param currentPosition
     * @param maximumDistance
     * @param crawlIn
     * @return
     */
    private int calculateAvailablePosition(int currentPosition, int maximumDistance, boolean crawlIn) {
        int tempPosition;

        for (int d = maximumDistance; d > 0; d--) {
            if(crawlIn)
                tempPosition = currentPosition + d;
            else
                tempPosition = currentPosition - d;

            int i;
            for (i = 0; i < SimulPar.K; i++)
                if (tempPosition == thievesPositionsInParty[i])
                    break;

            if (i == SimulPar.K) {
                currentPosition = tempPosition;
                break;
            }
        }

        return currentPosition;
    }



    /**
     * 
     */
    public synchronized void sendAssaultParty()
    {
        System.out.println("Master thief entered sendAssaultParty");

        MasterThief mt = (MasterThief)Thread.currentThread();

        numberOfThievesAtRoom = 0;
        numberOfThievesAtControlSite = 0;
        
        executeAssault = true;

        nextThiefToMove = thievesIdsInParty[0];

        for(int i = 0; i < SimulPar.K; i++)
            thievesPositionsInParty[i] = 0;
        
        maxThiefPosition = minThiefPosition = 0;

        notifyAll();

        mt.setMasterThiefState(MasterThiefStates.DECIDING_WHAT_TO_DO);

        System.out.println("Master thief left sendAssaultParty");
    }

    /**
     * 
     */
    public synchronized boolean crawlIn()
    {
        OrdinaryThief ot = (OrdinaryThief)Thread.currentThread();

        System.out.println("Ordinary thief " + ot.getOrdinaryThiefId() + " entered crawlIn");

        System.out.println("Number of thieves at room: " + numberOfThievesAtRoom);
        while ((!executeAssault || nextThiefToMove != ot.getOrdinaryThiefId()) && numberOfThievesAtRoom < SimulPar.K) {
            try {
                wait();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }

        /* check if all thieves are already at room */
        if(numberOfThievesAtRoom == SimulPar.K)
        {
            System.out.println("Ordinary thief " + ot.getOrdinaryThiefId() + " left crawlIn");
            return false;
        }

        int thiefIndex = getThiefIndex(ot.getOrdinaryThiefId());
        int temporaryThiefPosition = thievesPositionsInParty[thiefIndex];
        boolean thiefInMovement;

        // Loop to allow multiple moves of the same thief
        do
        {
            thiefInMovement = false;

            if (temporaryThiefPosition == maxThiefPosition) {
                int previousThiefPosition = thievesPositionsInParty[getPreviousThiefIndex(thiefIndex)];
                temporaryThiefPosition += Math.min(ot.getMaximumDisplacement(), SimulPar.S - (temporaryThiefPosition - previousThiefPosition));
            }

            else if (temporaryThiefPosition == minThiefPosition) {
                temporaryThiefPosition = calculateAvailablePosition(temporaryThiefPosition, Math.min(ot.getMaximumDisplacement(), (maxThiefPosition - temporaryThiefPosition) + SimulPar.S), true);
            }

            else {
                // crawl the maximum possible if the distance between the first and the last is
                // <= MaxSeparation
                if (maxThiefPosition - minThiefPosition <= SimulPar.S)
                {   temporaryThiefPosition = calculateAvailablePosition(temporaryThiefPosition, Math.min(ot.getMaximumDisplacement(), (maxThiefPosition - temporaryThiefPosition) + SimulPar.S), true);
                }
                else
                {
                    // get the distance to the thief before and after the current one
                    int distanceToPrevious = temporaryThiefPosition - thievesPositionsInParty[getPreviousThiefIndex(thiefIndex)];
                    int distanceToNext = thievesPositionsInParty[getNextThiefIndex(thiefIndex)] - temporaryThiefPosition;

                    if (distanceToNext + distanceToPrevious <= SimulPar.S)
                        temporaryThiefPosition = calculateAvailablePosition(temporaryThiefPosition, Math.min(ot.getMaximumDisplacement(), (maxThiefPosition - temporaryThiefPosition) + SimulPar.S), true);
                    else
                        temporaryThiefPosition = calculateAvailablePosition(temporaryThiefPosition, Math.min(SimulPar.S - distanceToPrevious, ot.getMaximumDisplacement()), true);
                }
            }

            if(thievesPositionsInParty[thiefIndex] < Math.min(targetRoomDistance, temporaryThiefPosition))
                thiefInMovement = true;

            thievesPositionsInParty[thiefIndex] = temporaryThiefPosition = Math.min(targetRoomDistance, temporaryThiefPosition);
            updateMinMaxPositions();
        } while (thiefInMovement);

        nextThiefToMove = thievesIdsInParty[getPreviousThiefIndex(thiefIndex)];

        if(thievesPositionsInParty[thiefIndex] == targetRoomDistance) {
            numberOfThievesAtRoom++;
        
            ot.setOrdinaryThiefState(OrdinaryThiefStates.AT_A_ROOM);
        }
        
        notifyAll();

        System.out.println("Ordinary thief " + ot.getOrdinaryThiefId() + " left crawlIn");

        return true;
    }

    /**
     * 
     */
    public synchronized void reverseDirection()
    {
        OrdinaryThief ot = (OrdinaryThief)Thread.currentThread();

        System.out.println("Ordinary thief " + ot.getOrdinaryThiefId() + " entered reverseDirection");

        numberOfThievesAtRoom--;

        if(numberOfThievesAtRoom == 0) {
            executeAssault = false;
            nextThiefToMove = thievesIdsInParty[0];
            notifyAll();
        }

        ot.setOrdinaryThiefState(OrdinaryThiefStates.CRAWLING_OUTWARDS);

        System.out.println("Ordinary thief " + ot.getOrdinaryThiefId() + " left reverseDirection");

    }

    /**
     * 
     */
    public synchronized boolean crawlOut()
    {
        OrdinaryThief ot = (OrdinaryThief)Thread.currentThread();

        System.out.println("Ordinary thief " + ot.getOrdinaryThiefId() + " entered crawlOut");

        System.out.println("CRAWLOUT: netxtThiefToMove=" + nextThiefToMove + " | executeAssault=" + executeAssault);
        while (executeAssault || nextThiefToMove != ot.getOrdinaryThiefId()) {
            try {
                wait();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }

        int thiefIndex = getThiefIndex(ot.getOrdinaryThiefId());
        int temporaryThiefPosition = thievesPositionsInParty[thiefIndex];
        boolean thiefInMovement;

        // Loop to allow multiple moves of the same thief
        do
        {
            thiefInMovement = false;

            if (temporaryThiefPosition == maxThiefPosition) {
                temporaryThiefPosition = calculateAvailablePosition(temporaryThiefPosition, Math.min(ot.getMaximumDisplacement(), (temporaryThiefPosition - minThiefPosition) + SimulPar.S), false);
            }

            else if (temporaryThiefPosition == minThiefPosition) {
                int nextThiefPosition = thievesPositionsInParty[getNextThiefIndex(thiefIndex)];
                temporaryThiefPosition -= Math.min(ot.getMaximumDisplacement(), SimulPar.S - (nextThiefPosition - temporaryThiefPosition));
            }

            else {
                // crawl the maximum possible if the distance between the first and the last is
                // <= MaxSeparation
                if (maxThiefPosition - minThiefPosition <= SimulPar.S)
                    temporaryThiefPosition = calculateAvailablePosition(temporaryThiefPosition, Math.min(ot.getMaximumDisplacement(), (temporaryThiefPosition - minThiefPosition) + SimulPar.S), false);

                else {
                    // get the distance to the thief before and after the current one
                    int distanceToPrevious = temporaryThiefPosition - thievesPositionsInParty[getPreviousThiefIndex(thiefIndex)];
                    int distanceToNext = thievesPositionsInParty[getNextThiefIndex(thiefIndex)] - temporaryThiefPosition;

                    if (distanceToNext + distanceToPrevious <= SimulPar.S)
                        temporaryThiefPosition = calculateAvailablePosition(temporaryThiefPosition, Math.min(ot.getMaximumDisplacement(), (temporaryThiefPosition - minThiefPosition) + SimulPar.S), false);
                    else
                        temporaryThiefPosition = calculateAvailablePosition(temporaryThiefPosition, Math.min(ot.getMaximumDisplacement(), (SimulPar.S - distanceToNext)), false);
                }       
            }

            if(thievesPositionsInParty[thiefIndex] > Math.max(0, temporaryThiefPosition))
                thiefInMovement = true;

            thievesPositionsInParty[thiefIndex] = temporaryThiefPosition = Math.max(0,temporaryThiefPosition);
            updateMinMaxPositions();
        } while(thiefInMovement);

        nextThiefToMove = thievesIdsInParty[getNextThiefIndex(thiefIndex)];

        if(thievesPositionsInParty[thiefIndex] == 0) {
            numberOfThievesAtControlSite++;

            ot.setOrdinaryThiefState(OrdinaryThiefStates.COLLECTION_SITE);
        }

        notifyAll();
        
        if(thievesPositionsInParty[thiefIndex] == 0)
            System.out.println("Ordinary thief " + ot.getOrdinaryThiefId() + " left crawlOut");

        return thievesPositionsInParty[thiefIndex] > 0;
    }
}
