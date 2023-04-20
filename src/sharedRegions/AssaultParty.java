package sharedRegions;

import entities.*;
import main.SimulPar;

/**
 * Assault party.
 *
 * It is responsible to (...) All public methods are executed in mutual exclusion. There are X
 * internal synchronization points: (...)
 */
public class AssaultParty {
    /**
     * Reference to the general repository.
     */
    private final GeneralRepository repos;

    /**
     * Assault party ID
     */
    private int assaultPartyId;

    /**
     * Number of thieves in the party
     */
    private int numberOfThievesInParty;

    /**
     * Indicates whether the party is in operation
     */
    private boolean inOperation;

    /**
     * Indicates what is the target room
     */
    private int targetRoom;

    /**
     * Stores the distance to the target room
     */
    private int targetRoomDistance;

    /**
     * Stores the ID's of the members of the party
     */
    private int[] thievesIdsInParty;

    /**
     * Indicates the next thief that's going to move
     */
    private int nextThiefToMove;

    /**
     * Indicates whether the assault is being executed
     */
    private boolean executeAssault;

    /**
     * Stores the positions of the members of the party
     */
    private int[] thievesPositionsInParty;

    /**
     * Stores the maximum position of the thief
     */
    private int maxThiefPosition;

    /**
     * Stores the minimum position of the thief
     */
    private int minThiefPosition;

    /**
     * Indicates how many thieves are at the room
     */
    private int numberOfThievesAtRoom;

    /**
     * Indicates how many thieves are at the control site
     */
    private int numberOfThievesAtControlSite;

    /**
     * Indicates how many thieves are outside the room
     */
    private int numberOfThievesOutsideRoom;

    /**
     * Instantiation of the assault party monitor.
     * 
     * @param repos  Reference to GeneralRepository 
     * @param assaultPartyId assault party id
     */
    public AssaultParty(GeneralRepository repos, int assaultPartyId) {
        this.repos = repos;
        this.assaultPartyId = assaultPartyId;
        this.numberOfThievesInParty = 0;
        this.inOperation = false;
        this.thievesIdsInParty = new int[SimulPar.K];
        this.executeAssault = false;
        this.thievesPositionsInParty = new int[SimulPar.K];
        this.maxThiefPosition = 0;
        this.minThiefPosition = 0;
        this.numberOfThievesAtRoom = 0;
        this.numberOfThievesAtControlSite = 0;
        this.numberOfThievesOutsideRoom = 0;
    }

    /**
     * Check status of the operation
     * @return status of the operation
     */
    public boolean operationStatus() {
        return inOperation;
    }

    /**
     * Starts the operation
     */
    public void startOperation() {
        this.inOperation = true;
    }

    /**
     * Ends the operation
     */
    public void endOperation() {
        this.inOperation = false;
    }

    /**
     * Get number of thieves in the assault party
     * @return number of thieves in the party
     */
    public int getNumberOfThievesInParty() {
        return numberOfThievesInParty;
    }

    /**
     * Set the target room to assault
     * @param targetRoom target room to assault
     * @param targetRoomDistance distance to the target room
     */
    public void setTargetRoom(int targetRoom, int targetRoomDistance) {
        this.targetRoom = targetRoom;
        this.targetRoomDistance = targetRoomDistance;
    }

    /**
     * Get the target room
     * @return target room
     */
    public int getTargetRoom() {
        return targetRoom;
    }

    /**
     * Assigns a new thief to an assault party
     * @param thiefId thief id
     */
    public void assignNewThief(int thiefId) {
        numberOfThievesInParty++;
        thievesIdsInParty[numberOfThievesInParty - 1] = thiefId;
    }

    /**
     * Removes the thief from the assault party
     * @param thiedId thied id
     */
    public void removeThief(int thiefId) {
        numberOfThievesInParty--;
        if (numberOfThievesInParty == 0)
            endOperation();
    }

    /**
     * Get the thief index 
     * @param commonThiefId common thief id
     * @return index of the thief 
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
     * Get previous thief index
     * @param thiefIndex thief index
     * @return index of the previous thief 
     */
    private int getPreviousThiefIndex(int thiefIndex) {
        // if they are all at the same step send the next one based on index (to prevent
        // blockings)
        boolean atTheSameStep = true;
        for (int i = 0; i < SimulPar.K - 1; i++) {
            if (thievesPositionsInParty[i] != thievesPositionsInParty[i + 1]) {
                atTheSameStep = false;
                break;
            }
        }
        if (atTheSameStep) {
            return (thiefIndex + 1) % SimulPar.K;
        }

        // if the last thief then get value to the first thief
        if (minThiefPosition == thievesPositionsInParty[thiefIndex])
            for (int i = 0; i < SimulPar.K; i++)
                if (thievesPositionsInParty[i] == maxThiefPosition)
                    return i;

        int distanceBetweenThieves, minDistanceBetweenThieves = SimulPar.S, previousThiefIndex = -1;
        for (int i = 0; i < SimulPar.K; i++) {
            distanceBetweenThieves =
                    thievesPositionsInParty[thiefIndex] - thievesPositionsInParty[i];
            if (distanceBetweenThieves > 0 && distanceBetweenThieves <= minDistanceBetweenThieves) {
                minDistanceBetweenThieves = distanceBetweenThieves;
                previousThiefIndex = i;
            }
        }

        return previousThiefIndex;
    }

    /**
     * Get next thief index
     * @param thiefIndex thief index 
     * @return index of the next thief 
     */
    private int getNextThiefIndex(int thiefIndex) {
        // if they are all at the same step send the next one based on index (to prevent
        // blockings)
        boolean atTheSameStep = true;
        for (int i = 0; i < SimulPar.K - 1; i++) {
            if (thievesPositionsInParty[i] != thievesPositionsInParty[i + 1]) {
                atTheSameStep = false;
                break;
            }
        }
        if (atTheSameStep) {
            return (thiefIndex + 1) % SimulPar.K;
        }

        // if the first thief then get value to the last thief
        if (maxThiefPosition == thievesPositionsInParty[thiefIndex]) {
            for (int i = 0; i < SimulPar.K; i++) {
                if (thievesPositionsInParty[i] == minThiefPosition)
                    return i;
            }
        }

        int distanceBetweenThieves, minDistanceBetweenThieves = SimulPar.S, nextThiefIndex = -1;
        for (int i = 0; i < SimulPar.K; i++) {
            distanceBetweenThieves =
                    thievesPositionsInParty[i] - thievesPositionsInParty[thiefIndex];
            if (distanceBetweenThieves > 0 && distanceBetweenThieves <= minDistanceBetweenThieves) {
                minDistanceBetweenThieves = distanceBetweenThieves;
                nextThiefIndex = i;
            }
        }

        return nextThiefIndex;
    }

    /**
     * Updates minimum and maximum positions of the thieves
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
     * Calculate the available position
     * @param currentPosition current position of the thief
     * @param maximumDistance maximum distance
     * @param crawlIn crawl in 
     * @return available position for the thief to move
     */
    private int calculateAvailablePosition(int currentPosition, int maximumDistance,
            boolean crawlIn) {
        int tempPosition;

        for (int d = maximumDistance; d > 0; d--) {
            if (crawlIn)
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
     * The Master Thief sends an assault party to a room
     */
    public synchronized void sendAssaultParty() {
        MasterThief mt = (MasterThief) Thread.currentThread();

        numberOfThievesAtRoom = 0;
        numberOfThievesAtControlSite = 0;
        numberOfThievesOutsideRoom = 0;

        executeAssault = true;

        nextThiefToMove = thievesIdsInParty[0];

        for (int i = 0; i < SimulPar.K; i++)
            thievesPositionsInParty[i] = 0;

        maxThiefPosition = minThiefPosition = 0;

        notifyAll();

        mt.setMasterThiefState(MasterThiefStates.DECIDING_WHAT_TO_DO);
    }

    /**
     * The assault party get in line and crawl to the room of the museum
     */
    public synchronized boolean crawlIn() {
        OrdinaryThief ot = (OrdinaryThief) Thread.currentThread();

        while (!executeAssault || nextThiefToMove != ot.getOrdinaryThiefId()) {
            try {
                wait();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }

        int thiefIndex = getThiefIndex(ot.getOrdinaryThiefId());

        if (thievesPositionsInParty[thiefIndex] < targetRoomDistance) {
            int temporaryThiefPosition = thievesPositionsInParty[thiefIndex];
            boolean thiefInMovement;

            // Loop to allow multiple moves of the same thief
            do {
                thiefInMovement = false;

                if (temporaryThiefPosition == maxThiefPosition) {
                    int previousThiefPosition =
                            thievesPositionsInParty[getPreviousThiefIndex(thiefIndex)];
                    temporaryThiefPosition += Math.min(ot.getMaximumDisplacement(),
                            SimulPar.S - (temporaryThiefPosition - previousThiefPosition));
                }

                else if (temporaryThiefPosition == minThiefPosition) {
                    temporaryThiefPosition = calculateAvailablePosition(temporaryThiefPosition,
                            Math.min(ot.getMaximumDisplacement(),
                                    (maxThiefPosition - temporaryThiefPosition) + SimulPar.S),
                            true);
                }

                else {
                    // crawl the maximum possible if the distance between the first and the last is
                    // <= MaxSeparation
                    if (maxThiefPosition - minThiefPosition <= SimulPar.S) {
                        temporaryThiefPosition = calculateAvailablePosition(temporaryThiefPosition,
                                Math.min(ot.getMaximumDisplacement(),
                                        (maxThiefPosition - temporaryThiefPosition) + SimulPar.S),
                                true);
                    } else {
                        // get the distance to the thief before and after the current one
                        int distanceToPrevious = temporaryThiefPosition
                                - thievesPositionsInParty[getPreviousThiefIndex(thiefIndex)];
                        int distanceToNext = thievesPositionsInParty[getNextThiefIndex(thiefIndex)]
                                - temporaryThiefPosition;

                        if (distanceToNext + distanceToPrevious <= SimulPar.S)
                            temporaryThiefPosition =
                                    calculateAvailablePosition(temporaryThiefPosition,
                                            Math.min(ot.getMaximumDisplacement(),
                                                    (maxThiefPosition - temporaryThiefPosition)
                                                            + SimulPar.S),
                                            true);
                        else
                            temporaryThiefPosition =
                                    calculateAvailablePosition(temporaryThiefPosition,
                                            Math.min(SimulPar.S - distanceToPrevious,
                                                    ot.getMaximumDisplacement()),
                                            true);
                    }
                }

                if (thievesPositionsInParty[thiefIndex] < Math.min(targetRoomDistance,
                        temporaryThiefPosition)) {
                    thiefInMovement = true;
                    thievesPositionsInParty[thiefIndex] = temporaryThiefPosition =
                            Math.min(targetRoomDistance, temporaryThiefPosition);
                    updateMinMaxPositions();
                    repos.updateAssaultPartyElementPosition(assaultPartyId, ot.getOrdinaryThiefId(),
                            thievesIdsInParty[thiefIndex]);
                }
            } while (thiefInMovement);

            if (thievesPositionsInParty[thiefIndex] == targetRoomDistance) {
                numberOfThievesAtRoom++;
                ot.setOrdinaryThiefState(OrdinaryThiefStates.AT_A_ROOM);
            }
        }

        nextThiefToMove = thievesIdsInParty[getPreviousThiefIndex(thiefIndex)];

        notifyAll();

        return (thievesPositionsInParty[thiefIndex] < targetRoomDistance)
                || (numberOfThievesAtRoom < SimulPar.K);
    }

    /**
     * Ordinary Thief reverses his direction towards the colection site 
     */
    public synchronized void reverseDirection() {
        OrdinaryThief ot = (OrdinaryThief) Thread.currentThread();

        numberOfThievesOutsideRoom++;

        if (numberOfThievesOutsideRoom == 3) {
            executeAssault = false;
            nextThiefToMove = thievesIdsInParty[0];
            notifyAll();
        }

        ot.setOrdinaryThiefState(OrdinaryThiefStates.CRAWLING_OUTWARDS);
    }

    /**
     * The assault party get in line and crawl to the colection site
     */
    public synchronized boolean crawlOut() {
        OrdinaryThief ot = (OrdinaryThief) Thread.currentThread();

        while (executeAssault || nextThiefToMove != ot.getOrdinaryThiefId()) {
            try {
                wait();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }

        int thiefIndex = getThiefIndex(ot.getOrdinaryThiefId());

        if (thievesPositionsInParty[thiefIndex] > 0) {
            int temporaryThiefPosition = thievesPositionsInParty[thiefIndex];
            boolean thiefInMovement;

            // Loop to allow multiple moves of the same thief
            do {
                thiefInMovement = false;

                if (temporaryThiefPosition == maxThiefPosition) {
                    temporaryThiefPosition = calculateAvailablePosition(temporaryThiefPosition,
                            Math.min(ot.getMaximumDisplacement(),
                                    (temporaryThiefPosition - minThiefPosition) + SimulPar.S),
                            false);
                }

                else if (temporaryThiefPosition == minThiefPosition) {
                    int nextThiefPosition = thievesPositionsInParty[getNextThiefIndex(thiefIndex)];
                    temporaryThiefPosition -= Math.min(ot.getMaximumDisplacement(),
                            SimulPar.S - (nextThiefPosition - temporaryThiefPosition));
                }

                else {
                    // crawl the maximum possible if the distance between the first and the last is
                    // <= MaxSeparation
                    if (maxThiefPosition - minThiefPosition <= SimulPar.S)
                        temporaryThiefPosition = calculateAvailablePosition(temporaryThiefPosition,
                                Math.min(ot.getMaximumDisplacement(),
                                        (temporaryThiefPosition - minThiefPosition) + SimulPar.S),
                                false);

                    else {
                        // get the distance to the thief before and after the current one
                        int distanceToPrevious = temporaryThiefPosition
                                - thievesPositionsInParty[getPreviousThiefIndex(thiefIndex)];
                        int distanceToNext = thievesPositionsInParty[getNextThiefIndex(thiefIndex)]
                                - temporaryThiefPosition;

                        if (distanceToNext + distanceToPrevious <= SimulPar.S)
                            temporaryThiefPosition =
                                    calculateAvailablePosition(temporaryThiefPosition,
                                            Math.min(ot.getMaximumDisplacement(),
                                                    (temporaryThiefPosition - minThiefPosition)
                                                            + SimulPar.S),
                                            false);
                        else
                            temporaryThiefPosition = calculateAvailablePosition(
                                    temporaryThiefPosition, Math.min(ot.getMaximumDisplacement(),
                                            (SimulPar.S - distanceToNext)),
                                    false);
                    }
                }

                if (thievesPositionsInParty[thiefIndex] > Math.max(0, temporaryThiefPosition)) {
                    thiefInMovement = true;
                    thievesPositionsInParty[thiefIndex] =
                            temporaryThiefPosition = Math.max(0, temporaryThiefPosition);
                    updateMinMaxPositions();
                    repos.updateAssaultPartyElementPosition(assaultPartyId, ot.getOrdinaryThiefId(),
                            temporaryThiefPosition);
                }
            } while (thiefInMovement);

            if (thievesPositionsInParty[thiefIndex] == 0) {
                numberOfThievesAtControlSite++;

                ot.setOrdinaryThiefState(OrdinaryThiefStates.COLLECTION_SITE);
            }
        }

        nextThiefToMove = thievesIdsInParty[getNextThiefIndex(thiefIndex)];

        notifyAll();

        return (thievesPositionsInParty[thiefIndex] > 0)
                || (numberOfThievesAtControlSite < SimulPar.K);
    }
}
