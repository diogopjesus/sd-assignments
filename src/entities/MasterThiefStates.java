package entities;

/**
 * Definition of the internal states of the MasterThief during his life cycle.
 */
public final class MasterThiefStates {
	/**
	 * This is the first state of the Master Thief life cycle, planning how the
	 * heist will be carried out.
	 */
	public static final int PLANNING_THE_HEIST = 0;

	/**
	 * The master Thief decides what is the next operation that will be executed.
	 */
	public static final int DECIDING_WHAT_TO_DO = 1;

	/**
	 * The master Thief prepares an assault party to be sent to the Museum.
	 */
	public static final int ASSEMBLING_A_GROUP = 2;

	/**
	 * The master Thief waits for the arrival of the assault party.
	 */
	public static final int WAITING_FOR_GROUP_ARRIVAL = 3;

	/**
	 * The master Thief declares the end of the heist and presents the results
	 * obtained.
	 */
	public static final int PRESENTING_THE_REPORT = 4;

	/**
	 * It can not be instantiated.
	 */
	private MasterThiefStates() {
	}
}
