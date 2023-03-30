package main;

/**
 * Definition of the simulation parameters.
 */

public final class SimulPar
{
    /**
     * Number of Thieves.
     */

    public static final int M = 7;

    /**
     * Number of Rooms.
     */

    public static final int N = 5;

    /**
     * Maximum value possible to be assigned to maximum displacement.
     */

    public static final int MD = 6;

    /**
     * Minimum value possible to be assigned to displacement.
     */
    public static final int md = 2;

    /**
     * Maximum number of paitings hanging in each room.
     */

    public static final int P = 16;

    /**
     * Minimum number of paitings hanging in each room.
     */

    public static final int p = 8;

    /**
     * Maximum distance from outside concentretion site to each room.
     */

    public static final int D = 30;

    /**
     * Minimum distance from outside concentretion site to each room.
     */

    public static final int d = 15;

    /**
     * Number of assault parties elements.
     */ 

     public static final int K = 3;

    /**
     * Maximum separation limit between thieves crawling in line.
     */

    public static final int S = 3;

    /**
     * It can not be instantiated.
     */
 
    private SimulPar ()
    { }
}
