package sharedRegions;

import main.SimulPar;
import entities.MasterThief;
import entities.MasterThiefStates;
import entities.CommonThief;
import entities.CommonThiefStates;

public class AssaultParty {

    private int numberOfThieves;
    private int targetRoom;
    private int distanceToTargetRoom;
    private int[] thievesInParty = new int[SimulPar.K];

    private int nextToMove;
    private boolean executeAssault, executeEscape;
    private int[] thievesPositions = new int[SimulPar.K];
    private int maxPosition, minPosition;

    private int numberOfThievesAtRoom;
    private int numberOfThievesAtControlSite;

    public void assignNewThief(int thiefID) {
        numberOfThieves = numberOfThieves + 1;
        thievesInParty[numberOfThieves - 1] = thiefID;
    }

    public void setTargetRoom(int targetRoom) {
        this.targetRoom = targetRoom;
    }

    public int getTargetRoom() {
        return targetRoom;
    }

    public void setDistanceToTargetRoom(int distanceToTargetRoom) {
        this.distanceToTargetRoom = distanceToTargetRoom;
    }

    public int getDistanceToTargetRoom() {
        return distanceToTargetRoom;
    }

    public int getNumberOfThieves() {
        return numberOfThieves;
    }

    public synchronized void reverseDirection() {
        CommonThief ct = (CommonThief)Thread.currentThread();
    
        numberOfThievesAtRoom--;
    
        if(numberOfThievesAtRoom == 0) {
            executeAssault = false;
            executeEscape = true;
            nextToMove = thievesInParty[0];
            notifyAll();
        }

        ct.setCommonThiefState(CommonThiefStates.CRAWLING_OUTWARDS);
    }

    public synchronized void sendAssaultParty() {
        MasterThief mt = (MasterThief) Thread.currentThread();

        numberOfThievesAtRoom = 0;
        numberOfThievesAtControlSite = 0;

        executeAssault = true;
        executeEscape = false;

        nextToMove = thievesInParty[0];

        for (int i = 0; i < SimulPar.K; i++)
            thievesPositions[i] = 0;

        maxPosition = minPosition = 0;

        notifyAll();

        mt.setMasterThiefState(MasterThiefStates.DECIDING_WHAT_TO_DO);
    }

    public synchronized boolean crawlIn() {
        CommonThief ct = (CommonThief) Thread.currentThread();

        while (!executeAssault || nextToMove != ct.getCommonThiefId()) {
            try {
                wait();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }

        int thiefIndex = getThiefIndex(ct.getCommonThiefId());
        int currentThiefPosition = thievesPositions[thiefIndex];

        // Loop to allow multiple moves of the same thief
        while (currentThiefPosition > thievesPositions[thiefIndex]) {
            if (currentThiefPosition == maxPosition) {
                int previousThiefPosition = thievesPositions[getPreviousThiefIndex(thiefIndex)];
                currentThiefPosition += Math.min(ct.getMaximumDisplacement(), SimulPar.S - (currentThiefPosition - previousThiefPosition));
                maxPosition = currentThiefPosition;
            }

            else if (currentThiefPosition == minPosition) {
                currentThiefPosition = calculateAvailablePosition(currentThiefPosition, ct.getMaximumDisplacement(), true);
                updateMinMaxPositions();
            }

            else {
                // crawl the maximum possible if the distance between the first and the last is
                // <= MaxSeparation
                if (maxPosition - minPosition <= SimulPar.S)
                    currentThiefPosition = calculateAvailablePosition(currentThiefPosition, ct.getMaximumDisplacement(), true);

                else {
                    // get the distance to the thief before and after the current one
                    int distanceToPrevious = currentThiefPosition - thievesPositions[getPreviousThiefIndex(thiefIndex)];
                    int distanceToNext = thievesPositions[getNextThiefIndex(thiefIndex)] - currentThiefPosition;

                    if (distanceToNext - distanceToPrevious <= SimulPar.S)
                        currentThiefPosition = calculateAvailablePosition(currentThiefPosition, ct.getMaximumDisplacement(), true);
                    else
                        currentThiefPosition = calculateAvailablePosition(currentThiefPosition, Math.min(SimulPar.S - distanceToPrevious, ct.getMaximumDisplacement()), true);
                }

                updateMinMaxPositions();
            }

            thievesPositions[thiefIndex] = Math.min(distanceToTargetRoom,currentThiefPosition);
                                            
        }

        nextToMove = thievesInParty[getPreviousThiefIndex(thiefIndex)];

        notifyAll();

        if(currentThiefPosition == distanceToTargetRoom) {
            numberOfThievesAtRoom++;
            while(numberOfThievesAtRoom < SimulPar.K) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            notifyAll();

            ct.setCommonThiefState(CommonThiefStates.AT_A_ROOM);
        }

        return currentThiefPosition < distanceToTargetRoom;
    }

    public synchronized boolean crawlOut() {
        CommonThief ct = (CommonThief) Thread.currentThread();

        while (!executeEscape || nextToMove != ct.getCommonThiefId()) {
            try {
                wait();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }

        int thiefIndex = getThiefIndex(ct.getCommonThiefId());
        int currentThiefPosition = thievesPositions[thiefIndex];

        // Loop to allow multiple moves of the same thief
        while (currentThiefPosition < thievesPositions[thiefIndex]) {
            if (currentThiefPosition == maxPosition) {
                currentThiefPosition = calculateAvailablePosition(currentThiefPosition, ct.getMaximumDisplacement(), false);
                updateMinMaxPositions();
            }

            else if (currentThiefPosition == minPosition) {
                int previousThiefPosition = thievesPositions[getPreviousThiefIndex(thiefIndex)];
                currentThiefPosition += Math.min(ct.getMaximumDisplacement(), SimulPar.S - (currentThiefPosition - previousThiefPosition));
                maxPosition = currentThiefPosition;
            }

            else {
                // crawl the maximum possible if the distance between the first and the last is
                // <= MaxSeparation
                if (maxPosition - minPosition <= SimulPar.S)
                    currentThiefPosition = calculateAvailablePosition(currentThiefPosition, ct.getMaximumDisplacement(), false);

                else {
                    // get the distance to the thief before and after the current one
                    int distanceToPrevious = currentThiefPosition - thievesPositions[getPreviousThiefIndex(thiefIndex)];
                    int distanceToNext = thievesPositions[getNextThiefIndex(thiefIndex)] - currentThiefPosition;

                    if (distanceToNext - distanceToPrevious <= SimulPar.S)
                        currentThiefPosition = calculateAvailablePosition(currentThiefPosition, ct.getMaximumDisplacement(), false);
                    else
                        currentThiefPosition = calculateAvailablePosition(currentThiefPosition,
                                Math.min(SimulPar.S - distanceToNext, ct.getMaximumDisplacement()), false);
                }

                updateMinMaxPositions();
            }

            thievesPositions[thiefIndex] = Math.max(0,currentThiefPosition);
        }

        nextToMove = thievesInParty[getNextThiefIndex(thiefIndex)];

        notifyAll();

        if(currentThiefPosition == 0) {
            numberOfThievesAtControlSite++;
            while(numberOfThievesAtControlSite < SimulPar.K) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            notifyAll();

            ct.setCommonThiefState(CommonThiefStates.COLLECTION_SITE);
        }

        return currentThiefPosition > 0;
    }

    private int getThiefIndex(int commonThiefId) {
        int thiefIndex = -1;
        for (int i = 0; i < SimulPar.K; i++) {
            if (thievesInParty[i] == commonThiefId) {
                thiefIndex = i;
                break;
            }
        }
        return thiefIndex;
    }

    private int getPreviousThiefIndex(int thiefIndex) {
        // if the last thief then get value to the first thief
        if (minPosition == thievesPositions[thiefIndex])
            for (int i = 0; i < SimulPar.K; i++)
                if (thievesPositions[i] == maxPosition)
                    return i;

        int distanceBetweenThieves, minDistanceBetweenThieves = SimulPar.S, previousThiefIndex = -1;

        for (int i = 0; i < SimulPar.K; i++) {
            distanceBetweenThieves = thievesPositions[thiefIndex] - thievesPositions[i];
            if (distanceBetweenThieves > 0 && distanceBetweenThieves < minDistanceBetweenThieves) {
                minDistanceBetweenThieves = distanceBetweenThieves;
                previousThiefIndex = i;
            }
        }

        return previousThiefIndex;
    }

    private int getNextThiefIndex(int thiefIndex) {
        // if the first thief then get value to the last thief
        if (maxPosition == thievesPositions[thiefIndex])
            for (int i = 0; i < SimulPar.K; i++)
                if (thievesPositions[i] == minPosition)
                    return i;

        int distanceBetweenThieves, minDistanceBetweenThieves = SimulPar.S, nextThiefIndex = -1;
        for (int i = 0; i < SimulPar.K; i++) {
            distanceBetweenThieves = thievesPositions[i] - thievesPositions[thiefIndex];
            if (distanceBetweenThieves > 0 && distanceBetweenThieves < minDistanceBetweenThieves) {
                minDistanceBetweenThieves = distanceBetweenThieves;
                nextThiefIndex = i;
            }
        }

        return nextThiefIndex;
    }

    private void updateMinMaxPositions() {
        // Update maximum and minimum position
        minPosition = maxPosition;
        for (int i = 0; i < SimulPar.K; i++) {
            if (thievesPositions[i] < minPosition)
                minPosition = thievesPositions[i];

            if (thievesPositions[i] > maxPosition)
                maxPosition = thievesPositions[i];
        }
    }

    private int calculateAvailablePosition(int currentPosition, int maximumDistance, boolean crawlIn) {
        int tempPosition;

        for (int d = maximumDistance; d > 0; d--) {
            if(crawlIn)
                tempPosition = Math.max(currentPosition + d, SimulPar.K-1);
            else
                tempPosition = Math.min(currentPosition - d, 0);

            int i;
            for (i = 0; i < SimulPar.K; i++)
                if (tempPosition == thievesPositions[i])
                    break;

            if (i == SimulPar.K) {
                currentPosition = tempPosition;
                break;
            }
        }

        return currentPosition;
    }
}
