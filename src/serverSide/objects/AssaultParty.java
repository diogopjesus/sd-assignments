package serverSide.objects;

import java.rmi.*;
import interfaces.*;
import serverSide.main.*;
import clientSide.entities.*;
import genclass.GenericIO;

/**
 * Assault Party.
 *
 * It is responsible to keep the members of the assault party and move them to
 * inside the museum and out and is implemented as an implicit monitor.
 * All public methods are executed in mutual exclusion.
 * There are 2 internal synchronization points: an array of blocking points, one
 * per each ordinary thief, where he both waits his turn to move inside the
 * museum and moves inside the museum; an array of blocking points, one per each
 * ordinary thief, where he both waits his turn to move outside the museum and
 * moves outside the museum.
 * Implementation of a client-server model of type 2 (server replication).
 * Communication is based on Java RMI.
 */
public class AssaultParty implements AssaultPartyInterface {
  /**
   * State of the ordinary thieves.
   */
  private final int[] ordStates;

  /**
   * State of the master thief.
   */
  private int masState;

  /**
   * Number of entity groups requesting the shutdown.
   */
  private int nEntities;

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
  private final GeneralRepositoryInterface reposStub;

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
   * Assault party members and their positions. First row of the array stores the
   * id. Second row of the array stores the position. Third row of the array
   * stores information about if the thief is holding a canvas.
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
   * @param reposStub      reference to the stub of the general repository.
   * @param assaultPartyId assault party id.
   */
  public AssaultParty(GeneralRepositoryInterface reposStub, int assaultPartyId) {
    masState = -1;
    ordStates = new int[SimulPar.M - 1];
    for (int i = 0; i < SimulPar.M - 1; i++)
      ordStates[i] = -1;
    this.nEntities = 0;
    this.reposStub = reposStub;
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
  @Override
  public synchronized int sendAssaultParty() throws RemoteException {
    assaultPartyState = CRAWLING_IN;

    /* Define the first thief to move */
    updateNextToMove();

    /* Notify first thief to start crawling */
    notifyAll();

    masState = MasterThiefStates.DECIDING_WHAT_TO_DO;
    try {
      reposStub.setMasterThiefState(MasterThiefStates.DECIDING_WHAT_TO_DO);
    } catch (RemoteException e) {
      GenericIO.writelnString(
          "MasterThief remote exception on sendAssaultParty - setMasterThiefState: " + e.getMessage());
      System.exit(1);
    }

    return masState;
  }

  /**
   * Operation crawl in.
   *
   * It is called by the ordinary thief to crawl into the museum.
   *
   * @return true if can continue to crawl in - false otherwise.
   */
  @Override
  public synchronized ReturnBoolean crawlIn(int ordId, int maxDis) throws RemoteException {
    ordStates[ordId] = OrdinaryThiefStates.CRAWLING_INWARDS;

    /* Wait until is time to move */
    while (nextToMove != ordId) {
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
      thiefPosition = getThiefPosition(ordId);
      thiefMovement = 0;

      /* If the thieves are at the start */
      if (thiefAtFront() == thiefAtBack()) {
        thiefMovement = Math.min(SimulPar.S, maxDis);
        moved = true;
      }
      /* If thief is at the front of the pack */
      else if (thiefAtFront() == ordId) {
        /* Get thief behind him */
        int thiefBehind = thiefBehind(ordId);
        /*
         * Check if the distance between the 2 is less than the maximum separation limit
         */
        if (thiefPosition - getThiefPosition(thiefBehind) < SimulPar.S) {
          /* Maximum movement allowed to thief */
          thiefMovement = Math.min(SimulPar.S - (thiefPosition - getThiefPosition(thiefBehind)), maxDis);
          moved = true;
        }
      }
      /* If thief is at the back of the pack */
      else if (thiefAtBack() == ordId) {
        /* Maximum movement allowed to thief */
        thiefMovement = maxDis;
        /* Search for the furthest available position */
        while (thiefInPosition(thiefPosition + thiefMovement) && thiefMovement > 0)
          thiefMovement--;
        /* If there is a movement to be made */
        moved = thiefMovement > 0;
      }
      /* If the thief is in the middle of the pack */
      else {
        /* Get thieves behind and ahead */
        int thiefBehind = thiefBehind(ordId);
        int thiefAhead = thiefAhead(ordId);
        // if thief ahead and behind are separated by less than the maximum separation
        if (getThiefPosition(thiefAhead) - getThiefPosition(thiefBehind) <= SimulPar.S) {
          /* Maximum movement allowed to thief */
          thiefMovement = Math.min((getThiefPosition(thiefAhead) - thiefPosition) + SimulPar.S, maxDis);
          /* Search for the furthest available position */
          while (thiefInPosition(thiefPosition + thiefMovement) && thiefMovement > 0)
            thiefMovement--;
          /* If there is a movement to be made */
          moved = thiefMovement > 0;
        } else {
          /* Maximum movement allowed to thief */
          thiefMovement = Math.min(SimulPar.S - (thiefPosition - getThiefPosition(thiefBehind)), maxDis);
          /* Search for the furthest available position */
          while (thiefInPosition(thiefPosition + thiefMovement) && thiefMovement > 0)
            thiefMovement--;
          /* If there is a movement to be made */
          moved = thiefMovement > 0;
        }
      }

      /* update thief next position */
      thiefNextPosition = Math.min(thiefPosition + thiefMovement, targetRoomDistance);
      if (moved) {
        /* Update thief position */
        updateThiefPosition(ordId, thiefNextPosition);
        try {
          reposStub.setAssaultPartyElementPosition(assaultPartyId, getThiefElement(ordId), thiefNextPosition);
        } catch (RemoteException e) {
          GenericIO.writelnString(
              "OrdinaryThief " + ordId + " remote exception on crawlIn - setAssaultPartyElementPosition: "
                  + e.getMessage());
          System.exit(1);
        }
      }

      /* Update moved to check if thief already arrived at room */
      moved = moved && (thiefNextPosition < targetRoomDistance);
    } while (moved);

    /* Update the next thief to move (updates assault party state also) */
    updateNextToMove();

    /* notify all thieves to check if it is time to crawl or proceed operations */
    notifyAll();

    /* Return wether the thief arrived at the room or not */
    if (getThiefPosition(ordId) >= targetRoomDistance) {
      /* Change ordinary thief state */
      ordStates[ordId] = OrdinaryThiefStates.AT_A_ROOM;
      try {
        reposStub.setOrdinaryThiefState(ordId, OrdinaryThiefStates.AT_A_ROOM);
      } catch (RemoteException e) {
        GenericIO.writelnString(
            "OrdinaryThief " + ordId + " remote exception on crawlIn - setOrdinaryThiefState: "
                + e.getMessage());
        System.exit(1);
      }
      return new ReturnBoolean(false, ordStates[ordId]);
    }
    return new ReturnBoolean(true, ordStates[ordId]);
  }

  /**
   * Operation reverse direction.
   *
   * It is called by the ordinary thief to reverse the crawling direction from in
   * to out.
   */
  @Override
  public synchronized int reverseDirection(int ordId) throws RemoteException {
    numberOfReversedThieves++;

    /* If all thieves reversed direction, update assault party state */
    if (numberOfReversedThieves == SimulPar.K) {
      assaultPartyState = CRAWLING_OUT;
      updateNextToMove();
      notifyAll();
    }

    ordStates[ordId] = OrdinaryThiefStates.CRAWLING_OUTWARDS;
    try {
      reposStub.setOrdinaryThiefState(ordId, OrdinaryThiefStates.CRAWLING_OUTWARDS);
    } catch (RemoteException e) {
      GenericIO.writelnString(
          "OrdinaryThief " + ordId + " remote exception on reverseDirection - setOrdinaryThiefState: "
              + e.getMessage());
      System.exit(1);
    }

    return ordStates[ordId];
  }

  /**
   * Operation crawl out.
   *
   * It is called by the ordinary thief to crawl out of the museum.
   *
   * @return true if can continue to crawl out - false otherwise.
   */
  @Override
  public synchronized ReturnBoolean crawlOut(int ordId, int maxDis) throws RemoteException {
    ordStates[ordId] = OrdinaryThiefStates.CRAWLING_OUTWARDS;

    /* Wait until is time to move */
    while (nextToMove != ordId) {
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
      thiefPosition = getThiefPosition(ordId);
      thiefMovement = 0;

      /* If the thieves are at the start */
      if (thiefAtFront() == thiefAtBack()) {
        thiefMovement = Math.min(SimulPar.S, maxDis);
        moved = true;
      }
      /* If thief is at the front of the pack */
      else if (thiefAtFront() == ordId) {
        /* Get thief behind him */
        int thiefBehind = thiefBehind(ordId);
        /*
         * Check if the distance between the 2 is less than the maximum separation limit
         */
        if (getThiefPosition(thiefBehind) - thiefPosition < SimulPar.S) {
          /* Maximum movement allowed to thief */
          thiefMovement = Math.min(SimulPar.S - (getThiefPosition(thiefBehind) - thiefPosition),
              maxDis);
          moved = true;
        }
      }
      /* If thief is at the back of the pack */
      else if (thiefAtBack() == ordId) {
        /* Maximum movement allowed to thief */
        thiefMovement = maxDis;
        /* Search for the furthest available position */
        while (thiefInPosition(thiefPosition - thiefMovement) && thiefMovement > 0)
          thiefMovement--;
        /* If there is a movement to be made */
        moved = thiefMovement > 0;
      }
      /* If the thief is in the middle of the pack */
      else {
        /* Get thieves behind and ahead */
        int thiefBehind = thiefBehind(ordId);
        int thiefAhead = thiefAhead(ordId);
        // if thief ahead and behind are separated by less than the maximum separation
        if (getThiefPosition(thiefBehind) - getThiefPosition(thiefAhead) <= SimulPar.S) {
          /* Maximum movement allowed to thief */
          thiefMovement = Math.min((thiefPosition - getThiefPosition(thiefAhead)) + SimulPar.S, maxDis);
          /* Search for the furthest available position */
          while (thiefInPosition(thiefPosition - thiefMovement) && thiefMovement > 0)
            thiefMovement--;
          /* If there is a movement to be made */
          moved = thiefMovement > 0;
        } else {
          /* Maximum movement allowed to thief */
          thiefMovement = Math.min(SimulPar.S - (getThiefPosition(thiefBehind) - thiefPosition), maxDis);
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
        updateThiefPosition(ordId, thiefNextPosition);
        try {
          reposStub.setAssaultPartyElementPosition(assaultPartyId, getThiefElement(ordId), thiefNextPosition);
        } catch (RemoteException e) {
          GenericIO.writelnString(
              "OrdinaryThief " + ordId
                  + " remote exception on crawlOut - setAssaultPartyElementPosition: "
                  + e.getMessage());
          System.exit(1);
        }
      }

      /* Update moved to check if thief already arrived at room */
      moved = moved && (thiefNextPosition > 0);
    } while (moved);

    /* Update the next thief to move (updates assault party state also) */
    updateNextToMove();

    /* notify all thieves to check if it is time to crawl or proceed operations */
    notifyAll();

    /* Return wether the thief arrived at the controlSite or not */
    if (getThiefPosition(ordId) <= 0) {
      ordStates[ordId] = OrdinaryThiefStates.COLLECTION_SITE;
      try {
        reposStub.setOrdinaryThiefState(ordId, OrdinaryThiefStates.COLLECTION_SITE);
      } catch (RemoteException e) {
        GenericIO.writelnString(
            "OrdinaryThief " + ordId + " remote exception on crawlOut - setOrdinaryThiefState: "
                + e.getMessage());
        System.exit(1);
      }
      return new ReturnBoolean(false, ordStates[ordId]);
    }

    return new ReturnBoolean(true, ordStates[ordId]);
  }

  /**
   * Set the assault party target room for mission.
   *
   * @param targetRoom target room id.
   */
  @Override
  public synchronized void setTargetRoom(int targetRoom) throws RemoteException {
    this.targetRoom = targetRoom;
  }

  /**
   * Get the assault party target room for mission.
   *
   * @return Target room.
   */
  @Override
  public synchronized int getTargetRoom() throws RemoteException {
    return targetRoom;
  }

  /**
   * Set the assault party target room distance for mission.
   *
   * @param targetRoomDistance target room distance.
   */
  @Override
  public synchronized void setTargetRoomDistance(int targetRoomDistance) throws RemoteException {
    this.targetRoomDistance = targetRoomDistance;
  }

  /**
   * Check if the assault party is available.
   *
   * @return true if the assault party is available, false otherwise.
   */
  @Override
  public synchronized boolean isAvailable() throws RemoteException {
    return assaultPartyState == ON_HOLD;
  }

  /**
   * Check if the assault party is full.
   *
   * @return true if the assault party is full, false otherwise.
   */
  @Override
  public synchronized boolean isFull() throws RemoteException {
    return numberOfAssaultPartyMembers == SimulPar.K;
  }

  /**
   * Add new thief to the assault party.
   *
   * @param thiefId thief id.
   */
  @Override
  public synchronized void joinAssaultParty(int thiefId) throws RemoteException {
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
  @Override
  public synchronized void quitAssaultParty(int thiefId) throws RemoteException {
    numberOfAssaultPartyMembers--;
    if (numberOfAssaultPartyMembers == 0) {
      /* Clean assault party data related to mission */
      assaultPartyState = ON_HOLD;
      targetRoom = NOT_A_ROOM;
      nextToMove = NOT_A_THIEF;
      assaultPartyMembers = new int[SimulPar.K][3];
      numberOfAssaultPartyMembers = 0;
      numberOfReversedThieves = 0;
    }
  }

  /**
   * Set thief canvas state.
   *
   * @param thiefId thief id.
   * @param canvas  true if is holding a canvas - false, otherwise.
   */
  public synchronized void setHoldingCanvas(int thiefId, boolean canvas) throws RemoteException {
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
  public synchronized boolean isHoldingCanvas(int thiefId) throws RemoteException {
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
   * Get the element (position inside the assault party) of the thief with the
   * given id.
   *
   * @param thiefId thief id.
   * @return thief element.
   */
  @Override
  public synchronized int getThiefElement(int thiefId) throws RemoteException {
    int thiefElement = -1;
    for (int i = 0; i < SimulPar.K; i++)
      if (assaultPartyMembers[i][0] == thiefId) {
        thiefElement = i;
        break;
      }
    return thiefElement;
  }

  /**
   * Operation server shutdown.
   *
   * New operation.
   */
  @Override
  public synchronized void shutdown() throws RemoteException {
    nEntities += 1;
    if (nEntities >= SimulPar.E)
      ServerHeistToTheMuseumAssaultParty.shutdown();
    notifyAll();
  }

  /**
   * Update the next thief to move.
   */
  private void updateNextToMove() {
    /* If it is the first movement */
    if (nextToMove == NOT_A_THIEF)
      nextToMove = assaultPartyMembers[0][0];
    /* Get the thief right behind in line */
    else {
      int nextThief = getNextThiefInLine();

      /* Update party state if all thieves are already at the room */
      if (getThiefPosition(nextThief) == getThiefPosition(nextToMove)) {
        assaultPartyState = getThiefPosition(nextThief) == 0 ? AT_SITE : AT_ROOM;
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
       * Search for the thief behind the old nextToMove, or, in case he is the last
       * thief, the
       * first thief in line
       */

      /* Get thief if party is crawling in */
      if (assaultPartyState == CRAWLING_IN) {
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
      if (assaultPartyState == CRAWLING_OUT) {
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
   * @param thiefId  thief id.
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
   * Get the thief id that is in front.
   *
   * @return thief id.
   */
  private int thiefAtFront() {
    int thiefId = NOT_A_THIEF;
    int thiefPosition;
    if (assaultPartyState == CRAWLING_IN) {
      thiefPosition = Integer.MIN_VALUE;
      for (int i = 0; i < SimulPar.K; i++)
        if (assaultPartyMembers[i][1] > thiefPosition) {
          thiefId = assaultPartyMembers[i][0];
          thiefPosition = assaultPartyMembers[i][1];
        }
    } else if (assaultPartyState == CRAWLING_OUT) {
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
  private int thiefAtBack() {
    int thiefId = NOT_A_THIEF;
    int thiefPosition;
    if (assaultPartyState == CRAWLING_IN) {
      thiefPosition = Integer.MAX_VALUE;
      for (int i = 0; i < SimulPar.K; i++)
        if (assaultPartyMembers[i][1] < thiefPosition) {
          thiefId = assaultPartyMembers[i][0];
          thiefPosition = assaultPartyMembers[i][1];
        }
    } else if (assaultPartyState == CRAWLING_OUT) {
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
  private int thiefBehind(int thiefId) {
    int thiefBehind = NOT_A_THIEF;
    int thiefBehindPosition;
    if (assaultPartyState == CRAWLING_IN) {
      thiefBehindPosition = Integer.MIN_VALUE;
      for (int i = 0; i < SimulPar.K; i++)
        if (assaultPartyMembers[i][1] < getThiefPosition(thiefId)
            && assaultPartyMembers[i][1] > thiefBehindPosition) {
          thiefBehind = assaultPartyMembers[i][0];
          thiefBehindPosition = assaultPartyMembers[i][1];
        }
    } else if (assaultPartyState == CRAWLING_OUT) {
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
  private int thiefAhead(int thiefId) {
    int thiefAhead = NOT_A_THIEF;
    int thiefAheadPosition;
    if (assaultPartyState == CRAWLING_IN) {
      thiefAheadPosition = Integer.MAX_VALUE;
      for (int i = 0; i < SimulPar.K; i++)
        if (assaultPartyMembers[i][1] > getThiefPosition(thiefId)
            && assaultPartyMembers[i][1] < thiefAheadPosition) {
          thiefAhead = assaultPartyMembers[i][0];
          thiefAheadPosition = assaultPartyMembers[i][1];
        }
    } else if (assaultPartyState == CRAWLING_OUT) {
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
  private boolean thiefInPosition(int position) {
    if (position < 0 || position > targetRoomDistance)
      return true;

    if (position == 0 || position == targetRoomDistance)
      return false;

    for (int i = 0; i < SimulPar.K; i++)
      if (assaultPartyMembers[i][1] == position) {
        return true;
      }
    return false;
  }
}
