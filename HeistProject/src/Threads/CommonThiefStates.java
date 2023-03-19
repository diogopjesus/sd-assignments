package Threads;

/**
 *    Definition of the internal states of the CommonThief during his life cycle.
 */

public class CommonThiefStates {
	/**
	 * The ordinary thief is at the concentration site 
	 */
	
	public static final int CONCENTRATION_SITE = 0;	
	
	/**
	 * The ordinary thief starts crawling towards the Museum
	 */
	
	public static final int CRAWLING_INWARDS = 1;
	
	/**
	 * The ordinary thief is at a room 
	 */
	
	public static final int AT_A_ROOM = 2;
	
	/**
	 * The ordinary thief starts crawling back to the collection site
	 */
	
	public static final int CRAWLING_OUTWARDS = 3;
	
	/**
	 * The ordinary thief is at the collection site 
	 */
	
	public static final int COLLECTION_SITE = 4;
	
}
