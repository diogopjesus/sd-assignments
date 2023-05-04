package serverSide.sharedRegions;

import clientSide.entities.*;
import genclass.*;
import serverSide.main.SimulPar;

/**
 * Assault Party.
 *
 * It is responsible to keep the members of the assault party and move them to inside the museum and
 * out and is implemented as an implicit monitor. All public methods are executed in mutual
 * exclusion. There are 2 internal synchronization points: an array of blocking points, one per each
 * ordinary thief, where he both waits his turn to move inside the museum and moves inside the
 * museum; an array of blocking points, one per each ordinary thief, where he both waits his turn to
 * move outside the museum and moves outside the museum.
 */
public class AssaultParty {
    /**
     * Flag to indicate that there is not a thief to be called.
     */
    private static final int NOT_A_THIEF = -1;

    /**
     * Flag to indicate that there is not a room.
     */
    private static final int NOT_A_ROOM = -1;

    /**
     * Assault party on hold state.
     */
    private static final int ON_HOLD = 0;

    /**
     * Assault party at concentration site.
     */
    private static final int AT_SITE = 1;

    /**
     * Assault party on crawling in state.
     */
    private static final int CRAWLING_IN = 2;

    /**
     * Assault party at the museum room.
     */
    private static final int AT_ROOM = 3;

    /**
     * Assault party on crawling out state.
     */
    private static final int CRAWLING_OUT = 4;

    /**
     * Reference to the general repository.
     */
    private final GeneralRepository repos;

    /**
     * Assault party id.
     */
    private final int assaultPartyId;

    /**
     * Assault party state.
     */
    private int assaultPartyState;

    /**
     * Assault party target room.
     */
    private int targetRoom;

    /**
     * Assault party target room distance.
     */
    private int targetRoomDistance;

    /**
     * Next thief to move.
     */
    private int nextToMove;

    /**
     * Assault party members and their positions. First row of the array stores the id. Second row
     * of the array stores the position. Third row of the array stores information about if the
     * thief is holding a canvas.
     */
    private int[][] assaultPartyMembers;

    /**
     * Number of assault party members.
     */
    private int numberOfAssaultPartyMembers;

    /**
     * Number of thieves that reversed direction after rolling a canvas.
     */
    private int numberOfReversedThieves;

    /**
     * Assault party constructor.
     *
     * @param repos general repository.
     * @param assaultPartyId assault party id.
     */
    public AssaultParty(GeneralRepository repos, int assaultPartyId) {
        this.repos = repos;
        this.assaultPartyId = assaultPartyId;
        this.assaultPartyState = ON_HOLD;
        this.targetRoom = NOT_A_ROOM;
        this.nextToMove = NOT_A_THIEF;
        this.assaultPartyMembers = new int[SimulPar.K][3];
        this.numberOfAssaultPartyMembers = 0;
        this.numberOfReversedThieves = 0;
    }

    /**
     * Operation send assault party.
     *
     * It is called by the master thief to send the assault party on mission
     */
    public synchronized void sendAssaultParty() {
        MasterThief mt = (MasterThief) Thread.currentThread();

        setAssaultPartyState(CRAWLING_IN);

        /* Define the first thief to move */
        updateNextToMove();

        /* Notify first thief to start crawling */
        notifyAll();

        mt.setMasterThiefState(MasterThiefStates.DECIDING_WHAT_TO_DO);
        repos.setMasterThiefState(MasterThiefStates.DECIDING_WHAT_TO_DO);
    }

    /**
     * Operation crawl in.
     *
     * It is called by the ordinary thief to crawl into the museum.
     *
     * @return true if can continue to crawl in - false otherwise.
     */
    public synchronized boolean crawlIn() {
        OrdinaryThief ot = (OrdinaryThief) Thread.currentThread();

        /* Wait until is time to move */
        while (nextToMove != ot.getOrdinaryThiefId()) {
            try {
                wait();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }

        /* Execute crawling */
        boolean moved;
        int thiefPosition, thiefMovement, thiefNextPosition;
        do {
            moved = false;
            thiefPosition = getThiefPosition(ot.getOrdinaryThiefId());
            thiefMovement = 0;

            /* If the thieves are at the start */
            if (thiefAtFront() == thiefAtBack()) {
                thiefMovement = Math.min(SimulPar.S, ot.getMaximumDisplacement());
                moved = true;
            }
            /* If thief is at the front of the pack */
            else if (thiefAtFront() == ot.getOrdinaryThiefId()) {
                /* Get thief behind him */
                int thiefBehind = thiefBehind(ot.getOrdinaryThiefId());
                /* Check if the distance between the 2 is less than the maximum separation limit */
                if (thiefPosition - getThiefPosition(thiefBehind) < SimulPar.S) {
                    /* Maximum movement allowed to thief */
                    thiefMovement =
                            Math.min(SimulPar.S - (thiefPosition - getThiefPosition(thiefBehind)),
                                    ot.getMaximumDisplacement());
                    moved = true;
                }
            }
            /* If thief is at the back of the pack */
            else if (thiefAtBack() == ot.getOrdinaryThiefId()) {
                /* Maximum movement allowed to thief */
                thiefMovement = Math.min(SimulPar.S, ot.getMaximumDisplacement());
                /* Search for the furthest available position */
                while (thiefInPosition(thiefPosition + thiefMovement) && thiefMovement > 0)
                    thiefMovement--;
                /* If there is a movement to be made */
                moved = thiefMovement > 0;
            }
            /* If the thief is in the middle of the pack */
            else {
                /* Get thieves behind and ahead */
                int thiefBehind = thiefBehind(ot.getOrdinaryThiefId());
                int thiefAhead = thiefAhead(ot.getOrdinaryThiefId());
                /* if thief ahead and behind are separated by less than the maximum separation */
                if (getThiefPosition(thiefAhead) - getThiefPosition(thiefBehind) <= SimulPar.S) {
                    /* Maximum movement allowed to thief */
                    thiefMovement = Math.min(SimulPar.S, ot.getMaximumDisplacement());
                    /* Search for the furthest available position */
                    while (thiefInPosition(thiefPosition + thiefMovement) && thiefMovement > 0)
                        thiefMovement--;
                    /* If there is a movement to be made */
                    moved = thiefMovement > 0;
                } else {
                    /* Maximum movement allowed to thief */
                    thiefMovement =
                            Math.min(SimulPar.S - (thiefPosition - getThiefPosition(thiefBehind)),
                                    ot.getMaximumDisplacement());
                    /* Search for the furthest available position */
                    while (thiefInPosition(thiefPosition + thiefMovement) && thiefMovement > 0)
                        thiefMovement--;
                    /* If there is a movement to be made */
                    moved = thiefMovement > 0;
                }
            }

            /* update thief next position */
            thiefNextPosition = Math.min(thiefPosition + thiefMovement, getTargetRoomDistance());
            if (moved) {
                /* Update thief position */
                updateThiefPosition(ot.getOrdinaryThiefId(), thiefNextPosition);
                repos.setAssaultPartyElementPosition(assaultPartyId,
                        getThiefElement(ot.getOrdinaryThiefId()), thiefNextPosition);
            }

            /* Update moved to check if thief already arrived at room */
            moved = moved && (thiefNextPosition < getTargetRoomDistance());
        } while (moved);

        /* Update the next thief to move (updates assault party state also) */
        updateNextToMove();

        /* notify all thieves to check if it is time to crawl or proceed operations */
        notifyAll();

        /* Return wether the thief arrived at the room or not */
        if (getThiefPosition(ot.getOrdinaryThiefId()) >= getTargetRoomDistance()) {
            /* Change ordinary thief state */
            ot.setOrdinaryThiefState(OrdinaryThiefStates.AT_A_ROOM);
            repos.setOrdinaryThiefState(ot.getOrdinaryThiefId(), OrdinaryThiefStates.AT_A_ROOM);
            return false;
        }
        return true;
    }

    /**
     * Operation reverse direction.
     *
     * It is called by the ordinary thief to reverse the crawling direction from in to out.
     */
    public synchronized void reverseDirection() {
        OrdinaryThief ot = (OrdinaryThief) Thread.currentThread();

        numberOfReversedThieves++;

        /* If all thieves reversed direction, update assault party state */
        if (numberOfReversedThieves == SimulPar.K) {
            setAssaultPartyState(CRAWLING_OUT);
            updateNextToMove();
            notifyAll();
        }

        ot.setOrdinaryThiefState(OrdinaryThiefStates.CRAWLING_OUTWARDS);
        repos.setOrdinaryThiefState(ot.getOrdinaryThiefId(), OrdinaryThiefStates.CRAWLING_OUTWARDS);
    }

    /**
     * Operation crawl out.
     *
     * It is called by the ordinary thief to crawl out of the museum.
     *
     * @return true if can continue to crawl out - false otherwise.
     */
    public synchronized boolean crawlOut() {
        OrdinaryThief ot = (OrdinaryThief) Thread.currentThread();

        /* Wait until is time to move */
        while (nextToMove != ot.getOrdinaryThiefId()) {
            try {
                wait();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }

        /* Execute crawling */
        boolean moved;
        int thiefPosition, thiefMovement, thiefNextPosition;
        do {
            moved = false;
            thiefPosition = getThiefPosition(ot.getOrdinaryThiefId());
            thiefMovement = 0;

            /* If the thieves are at the start */
            if (thiefAtFront() == thiefAtBack()) {
                thiefMovement = Math.min(SimulPar.S, ot.getMaximumDisplacement());
                moved = true;
            }
            /* If thief is at the front of the pack */
            else if (thiefAtFront() == ot.getOrdinaryThiefId()) {
                /* Get thief behind him */
                int thiefBehind = thiefBehind(ot.getOrdinaryThiefId());
                /* Check if the distance between the 2 is less than the maximum separation limit */
                if (getThiefPosition(thiefBehind) - thiefPosition < SimulPar.S) {
                    /* Maximum movement allowed to thief */
                    thiefMovement =
                            Math.min(SimulPar.S - (getThiefPosition(thiefBehind) - thiefPosition),
                                    ot.getMaximumDisplacement());
                    moved = true;
                }
            }
            /* If thief is at the back of the pack */
            else if (thiefAtBack() == ot.getOrdinaryThiefId()) {
                /* Maximum movement allowed to thief */
                thiefMovement = Math.min(SimulPar.S, ot.getMaximumDisplacement());
                /* Search for the furthest available position */
                while (thiefInPosition(thiefPosition - thiefMovement) && thiefMovement > 0)
                    thiefMovement--;
                /* If there is a movement to be made */
                moved = thiefMovement > 0;
            }
            /* If the thief is in the middle of the pack */
            else {
                /* Get thieves behind and ahead */
                int thiefBehind = thiefBehind(ot.getOrdinaryThiefId());
                int thiefAhead = thiefAhead(ot.getOrdinaryThiefId());
                /* if thief ahead and behind are separated by less than the maximum separation */
                if (getThiefPosition(thiefBehind) - getThiefPosition(thiefAhead) <= SimulPar.S) {
                    /* Maximum movement allowed to thief */
                    thiefMovement = Math.min(SimulPar.S, ot.getMaximumDisplacement());
                    /* Search for the furthest available position */
                    while (thiefInPosition(thiefPosition - thiefMovement) && thiefMovement > 0)
                        thiefMovement--;
                    /* If there is a movement to be made */
                    moved = thiefMovement > 0;
                } else {
                    /* Maximum movement allowed to thief */
                    thiefMovement =
                            Math.min(SimulPar.S - (getThiefPosition(thiefBehind) - thiefPosition),
                                    ot.getMaximumDisplacement());
                    /* Search for the furthest available position */
                    while (thiefInPosition(thiefPosition - thiefMovement) && thiefMovement > 0)
                        thiefMovement--;
                    /* If there is a movement to be made */
                    moved = thiefMovement > 0;
                }
            }

            /* update thief next position */
            thiefNextPosition = Math.max(thiefPosition - thiefMovement, 0);
            if (moved) {
                /* Update thief position */
                updateThiefPosition(ot.getOrdinaryThiefId(), thiefNextPosition);
                repos.setAssaultPartyElementPosition(assaultPartyId,
                        getThiefElement(ot.getOrdinaryThiefId()), thiefNextPosition);
            }

            /* Update moved to check if thief already arrived at room */
            moved = moved && (thiefNextPosition > 0);
        } while (moved);

        /* Update the next thief to move (updates assault party state also) */
        updateNextToMove();

        /* notify all thieves to check if it is time to crawl or proceed operations */
        notifyAll();

        /* Return wether the thief arrived at the controlSite or not */
        if (getThiefPosition(ot.getOrdinaryThiefId()) <= 0) {
            ot.setOrdinaryThiefState(OrdinaryThiefStates.COLLECTION_SITE);
            repos.setOrdinaryThiefState(ot.getOrdinaryThiefId(),
                    OrdinaryThiefStates.COLLECTION_SITE);
            return false;
        }

        return true;
    }

    /**
     * Get assault party id.
     *
     * @return Assault party id.
     */
    protected int getAssaultPartyId() {
        return assaultPartyId;
    }

    /**
     * Set the assault party target room for mission.
     *
     * @param targetRoom target room id.
     */
    protected void setTargetRoom(int targetRoom) {
        this.targetRoom = targetRoom;
    }

    /**
     * Get the assault party target room for mission.
     *
     * @return Target room.
     */
    protected int getTargetRoom() {
        return targetRoom;
    }

    /**
     * Set the assault party target room distance for mission.
     *
     * @param targetRoomDistance target room distance.
     */
    protected void setTargetRoomDistance(int targetRoomDistance) {
        this.targetRoomDistance = targetRoomDistance;
    }

    /**
     * Get the assault party target room distance for mission.
     *
     * @return Target room distance.
     */
    protected int getTargetRoomDistance() {
        return targetRoomDistance;
    }

    /**
     * Set the assault party state
     *
     * @param assaultPartyState assault party state.
     */
    protected void setAssaultPartyState(int assaultPartyState) {
        this.assaultPartyState = assaultPartyState;
    }

    /**
     * Get the assault party state.
     *
     * @return Assault party state.
     */
    protected int getAssaultPartyState() {
        return assaultPartyState;
    }

    /**
     * Check if the assault party is available.
     *
     * @return true if the assault party is available, false otherwise.
     */
    protected boolean isAvailable() {
        return assaultPartyState == ON_HOLD;
    }

    /**
     * Check if the assault party is full.
     *
     * @return true if the assault party is full, false otherwise.
     */
    protected boolean isFull() {
        return numberOfAssaultPartyMembers == SimulPar.K;
    }

    /**
     * Add new thief to the assault party.
     *
     * @param thiefId thief id.
     */
    protected void joinAssaultParty(int thiefId) {
        try {
            /* Store thief id */
            assaultPartyMembers[numberOfAssaultPartyMembers][0] = thiefId;
            /* Store thief position */
            assaultPartyMembers[numberOfAssaultPartyMembers][1] = 0;
            numberOfAssaultPartyMembers++;
        } catch (ArrayIndexOutOfBoundsException e) {
            GenericIO.writelnString("Assault party is full: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Remove thief from the assault party.
     *
     * @param thiefId thief id.
     */
    protected void quitAssaultParty(int thiefId) {
        numberOfAssaultPartyMembers--;
        if (numberOfAssaultPartyMembers == 0)
            cleanAssaultParty();
    }

    /**
     * Clean all the assault party data related to the concluded mission.
     */
    protected void cleanAssaultParty() {
        this.assaultPartyState = ON_HOLD;
        this.targetRoom = NOT_A_ROOM;
        this.nextToMove = NOT_A_THIEF;
        this.assaultPartyMembers = new int[SimulPar.K][3];
        this.numberOfAssaultPartyMembers = 0;
        this.numberOfReversedThieves = 0;
    }

    /**
     * Update the next thief to move.
     */
    protected void updateNextToMove() {
        /* If it is the first movement */
        if (nextToMove == NOT_A_THIEF)
            nextToMove = assaultPartyMembers[0][0];
        /* Get the thief right behind in line */
        else {
            int nextThief = getNextThiefInLine();

            /* Update party state if all thieves are already at the room */
            if (getThiefPosition(nextThief) == getThiefPosition(nextToMove)) {
                setAssaultPartyState(getThiefPosition(nextThief) == 0 ? AT_SITE : AT_ROOM);
                nextToMove = NOT_A_THIEF;
            } else {
                nextToMove = nextThief;
            }
        }
    }

    /**
     * Get the next thief in the crawling line.
     *
     * @return Thief id.
     */
    private int getNextThiefInLine() {
        int thiefPlace = 0;
        int nextThief = NOT_A_THIEF;
        int nextThiefPosition;

        try {
            /* Search for the placement of thief that executed the previous movement */
            while (assaultPartyMembers[thiefPlace][0] != nextToMove)
                thiefPlace++;

            /*
             * Search for the thief behind the old nextToMove, or, in case he is the last thief, the
             * first thief in line
             */

            /* Get thief if party is crawling in */
            if (getAssaultPartyState() == CRAWLING_IN) {
                nextThiefPosition = Integer.MIN_VALUE;
                for (int i = 0; i < SimulPar.K; i++)
                    if (thiefPlace != i
                            && assaultPartyMembers[i][1] < assaultPartyMembers[thiefPlace][1]
                            && assaultPartyMembers[i][1] > nextThiefPosition) {
                        nextThief = assaultPartyMembers[i][0];
                        nextThiefPosition = assaultPartyMembers[i][1];
                    }

                /* If the thief is behind in line, search for the thief in front */
                if (nextThief == NOT_A_THIEF)
                    for (int i = 0; i < SimulPar.K; i++)
                        if (assaultPartyMembers[i][1] > nextThiefPosition) {
                            nextThief = assaultPartyMembers[i][0];
                            nextThiefPosition = assaultPartyMembers[i][1];
                        }
            }

            /* Get thief if party is crawling out */
            if (getAssaultPartyState() == CRAWLING_OUT) {
                nextThiefPosition = Integer.MAX_VALUE;
                for (int i = 0; i < SimulPar.K; i++)
                    if (thiefPlace != i
                            && assaultPartyMembers[i][1] > assaultPartyMembers[thiefPlace][1]
                            && assaultPartyMembers[i][1] < nextThiefPosition) {
                        nextThief = assaultPartyMembers[i][0];
                        nextThiefPosition = assaultPartyMembers[i][1];
                    }

                /* If the thief is behind in line, search for the thief in front */
                if (nextThief == NOT_A_THIEF)
                    for (int i = 0; i < SimulPar.K; i++)
                        if (assaultPartyMembers[i][1] < nextThiefPosition) {
                            nextThief = assaultPartyMembers[i][0];
                            nextThiefPosition = assaultPartyMembers[i][1];
                        }
            }

        } catch (ArrayIndexOutOfBoundsException e) {
            GenericIO.writelnString("Thief not found in assault party: " + e.getMessage());
            System.exit(1);
        }

        return nextThief;
    }

    /**
     * Update the position of a thief.
     *
     * @param thiefId thief id.
     * @param position thief position.
     */
    private void updateThiefPosition(int thiefId, int position) {
        int thiefPlace = 0;

        try {
            while (assaultPartyMembers[thiefPlace][0] != thiefId)
                thiefPlace++;
        } catch (ArrayIndexOutOfBoundsException e) {
            GenericIO.writelnString("Thief not found in assault party: " + e.getMessage());
            System.exit(1);
        }

        assaultPartyMembers[thiefPlace][1] = position;
    }

    /**
     * Get the thief position based on his id.
     *
     * @param thiefId
     * @return Thief position.
     */
    private int getThiefPosition(int thiefId) {
        int thiefPlace = 0;
        try {
            while (assaultPartyMembers[thiefPlace][0] != thiefId)
                thiefPlace++;
        } catch (ArrayIndexOutOfBoundsException e) {
            GenericIO.writelnString("Thief not found in assault party: " + e.getMessage());
            System.exit(1);
        }

        return assaultPartyMembers[thiefPlace][1];
    }

    /**
     * Set thief canvas state.
     *
     * @param thiefId thief id.
     * @param canvas true if is holding a canvas - false, otherwise.
     */
    protected void setHoldingCanvas(int thiefId, boolean canvas) {
        int thiefPlace = 0;
        try {
            while (assaultPartyMembers[thiefPlace][0] != thiefId)
                thiefPlace++;
        } catch (ArrayIndexOutOfBoundsException e) {
            GenericIO.writelnString("Thief not found in assault party: " + e.getMessage());
            System.exit(1);
        }

        assaultPartyMembers[thiefPlace][2] = canvas ? 1 : 0;
    }

    /**
     * Check wether a thief is holding a canvas.
     *
     * @param thiefId thief id.
     * @return True if is holding a canvas, false otherwise.
     */
    protected boolean isHoldingCanvas(int thiefId) {
        int thiefPlace = 0;
        try {
            while (assaultPartyMembers[thiefPlace][0] != thiefId)
                thiefPlace++;
        } catch (ArrayIndexOutOfBoundsException e) {
            GenericIO.writelnString("Thief not found in assault party: " + e.getMessage());
            System.exit(1);
        }

        return assaultPartyMembers[thiefPlace][2] == 1;
    }

    /**
     * Get the thief id that is in front.
     *
     * @return thief id.
     */
    protected int thiefAtFront() {
        int thiefId = NOT_A_THIEF;
        int thiefPosition;
        if (getAssaultPartyState() == CRAWLING_IN) {
            thiefPosition = Integer.MIN_VALUE;
            for (int i = 0; i < SimulPar.K; i++)
                if (assaultPartyMembers[i][1] > thiefPosition) {
                    thiefId = assaultPartyMembers[i][0];
                    thiefPosition = assaultPartyMembers[i][1];
                }
        } else if (getAssaultPartyState() == CRAWLING_OUT) {
            thiefPosition = Integer.MAX_VALUE;
            for (int i = 0; i < SimulPar.K; i++)
                if (assaultPartyMembers[i][1] < thiefPosition) {
                    thiefId = assaultPartyMembers[i][0];
                    thiefPosition = assaultPartyMembers[i][1];
                }
        } else {
            /* return the first assault party member id */
            thiefId = assaultPartyMembers[0][0];
        }

        return thiefId;
    }

    /**
     * Get the thief id that is in the back.
     *
     * @return thief id.
     */
    protected int thiefAtBack() {
        int thiefId = NOT_A_THIEF;
        int thiefPosition;
        if (getAssaultPartyState() == CRAWLING_IN) {
            thiefPosition = Integer.MAX_VALUE;
            for (int i = 0; i < SimulPar.K; i++)
                if (assaultPartyMembers[i][1] < thiefPosition) {
                    thiefId = assaultPartyMembers[i][0];
                    thiefPosition = assaultPartyMembers[i][1];
                }
        } else if (getAssaultPartyState() == CRAWLING_OUT) {
            thiefPosition = Integer.MIN_VALUE;
            for (int i = 0; i < SimulPar.K; i++)
                if (assaultPartyMembers[i][1] > thiefPosition) {
                    thiefId = assaultPartyMembers[i][0];
                    thiefPosition = assaultPartyMembers[i][1];
                }
        } else {
            /* return the first assault party member id */
            thiefId = assaultPartyMembers[0][0];
        }

        return thiefId;
    }

    /**
     * Get the thief id that is behind the thief with the given id.
     *
     * @param thiefId thief id.
     * @return thief behind id.
     */
    protected int thiefBehind(int thiefId) {
        int thiefBehind = NOT_A_THIEF;
        int thiefBehindPosition;
        if (getAssaultPartyState() == CRAWLING_IN) {
            thiefBehindPosition = Integer.MIN_VALUE;
            for (int i = 0; i < SimulPar.K; i++)
                if (assaultPartyMembers[i][1] < getThiefPosition(thiefId)
                        && assaultPartyMembers[i][1] > thiefBehindPosition) {
                    thiefBehind = assaultPartyMembers[i][0];
                    thiefBehindPosition = assaultPartyMembers[i][1];
                }
        } else if (getAssaultPartyState() == CRAWLING_OUT) {
            thiefBehindPosition = Integer.MAX_VALUE;
            for (int i = 0; i < SimulPar.K; i++)
                if (assaultPartyMembers[i][1] > getThiefPosition(thiefId)
                        && assaultPartyMembers[i][1] < thiefBehindPosition) {
                    thiefBehind = assaultPartyMembers[i][0];
                    thiefBehindPosition = assaultPartyMembers[i][1];
                }
        }

        return thiefBehind;
    }

    /**
     * Get the thief id that is in front of the thief with the given id.
     *
     * @param thiefId thief id.
     * @return thief ahead id.
     */
    protected int thiefAhead(int thiefId) {
        int thiefAhead = NOT_A_THIEF;
        int thiefAheadPosition;
        if (getAssaultPartyState() == CRAWLING_IN) {
            thiefAheadPosition = Integer.MAX_VALUE;
            for (int i = 0; i < SimulPar.K; i++)
                if (assaultPartyMembers[i][1] > getThiefPosition(thiefId)
                        && assaultPartyMembers[i][1] < thiefAheadPosition) {
                    thiefAhead = assaultPartyMembers[i][0];
                    thiefAheadPosition = assaultPartyMembers[i][1];
                }
        } else if (getAssaultPartyState() == CRAWLING_OUT) {
            thiefAheadPosition = Integer.MIN_VALUE;
            for (int i = 0; i < SimulPar.K; i++)
                if (assaultPartyMembers[i][1] < getThiefPosition(thiefId)
                        && assaultPartyMembers[i][1] > thiefAheadPosition) {
                    thiefAhead = assaultPartyMembers[i][0];
                    thiefAheadPosition = assaultPartyMembers[i][1];
                }
        }
        return thiefAhead;
    }

    /**
     * Check if a thief is in the given position.
     *
     * @param position position to be checked.
     * @return true if there's a thief in position - false, otherwise.
     */
    protected boolean thiefInPosition(int position) {
        if (position < 0 || position > getTargetRoomDistance())
            return true;

        if (position == 0 || position == getTargetRoomDistance())
            return false;

        for (int i = 0; i < SimulPar.K; i++)
            if (assaultPartyMembers[i][1] == position) {
                return true;
            }
        return false;
    }

    /**
     * Get the element (position inside the assault party) of the thief with the given id.
     *
     * @param thiefId thief id.
     * @return thief element.
     */
    protected int getThiefElement(int thiefId) {
        int thiefElement = -1;
        for (int i = 0; i < SimulPar.K; i++)
            if (assaultPartyMembers[i][0] == thiefId) {
                thiefElement = i;
                break;
            }
        return thiefElement;
    }
}
