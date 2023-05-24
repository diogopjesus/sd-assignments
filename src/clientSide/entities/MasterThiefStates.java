package clientSide.entities;

/**
 * Definition of the internal states of the MasterThief during his life cycle.
 */
public final class MasterThiefStates {
  /**
   * The master thief plans how the heist will be carried out.
   */
  public static final int PLANNING_THE_HEIST = 0;

  /**
   * The master thief decides what is the next operation that will be executed.
   */
  public static final int DECIDING_WHAT_TO_DO = 1;

  /**
   * The master thief prepares an assault party to be sent to the Museum.
   */
  public static final int ASSEMBLING_A_GROUP = 2;

  /**
   * The master thief waits for the arrival of the assault party.
   */
  public static final int WAITING_FOR_GROUP_ARRIVAL = 3;

  /**
   * The master thief signals the end of the heist and presents the results.
   */
  public static final int PRESENTING_THE_REPORT = 4;

  /**
   * It can not be instantiated.
   */
  private MasterThiefStates() {
  }
}
