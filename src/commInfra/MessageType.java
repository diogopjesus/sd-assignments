package commInfra;

/**
 * Type of the exchanged messages.
 *
 * Implementation of a client-server model of type 2 (server replication). Communication is based on
 * a communication channel under the TCP protocol.
 */

public final class MessageType {
    /**
     * Request send assault party (service request).
     */
    public static final int SEND_ASSAULT_PARTY = 1;

    /**
     * Send assault party was done (reply).
     */
    public static final int SEND_ASSAULT_PARTY_DONE = 2;

    /**
     * Request crawl in (service request).
     */
    public static final int CRAWL_IN = 3;

    /**
     * Crawl in was done (reply).
     */
    public static final int CRAWL_IN_DONE = 4;

    /**
     * Request reverse direction (service request).
     */
    public static final int REVERSE_DIRECTION = 5;

    /**
     * Reverse direction was done (reply).
     */
    public static final int REVERSE_DIRECTION_DONE = 6;

    /**
     * Request crawl out (service request).
     */
    public static final int CRAWL_OUT = 7;

    /**
     * Crawl out was done (reply).
     */
    public static final int CRAWL_OUT_DONE = 8;

    /**
     * Set assault party target room (service request).
     */
    public static final int SET_TARGET_ROOM = 9;

    /**
     * Set assault party target room was done (reply).
     */
    public static final int SET_TARGET_ROOM_DONE = 10;

    /**
     * Get assault party target room (service request).
     */
    public static final int GET_TARGET_ROOM = 11;

    /**
     * Get assault party target room was done (reply).
     */
    public static final int GET_TARGET_ROOM_DONE = 12;

    /**
     * Set assault party target room distance (service request).
     */
    public static final int SET_TARGET_ROOM_DISTANCE = 13;

    /**
     * Set assault party target room distance was done (reply).
     */
    public static final int SET_TARGET_ROOM_DISTANCE_DONE = 14;

    /**
     * Check if assault party is available (service request).
     */
    public static final int IS_AVAILABLE = 15;

    /**
     * Check if assault party is available was done (reply).
     */
    public static final int IS_AVAILABLE_DONE = 16;

    /**
     * Check if assault party is full (service request).
     */
    public static final int IS_FULL = 17;

    /**
     * Check if assault party is full was done (reply).
     */
    public static final int IS_FULL_DONE = 18;

    /**
     * Join assault party (service request).
     */
    public static final int JOIN_ASSAULT_PARTY = 19;

    /**
     * Join assault party was done (reply).
     */
    public static final int JOIN_ASSAULT_PARTY_DONE = 20;

    /**
     * Quit assault party (service request).
     */
    public static final int QUIT_ASSAULT_PARTY = 21;

    /**
     * Quit assault party was done (reply).
     */
    public static final int QUIT_ASSAULT_PARTY_DONE = 22;

    /**
     * Set holding canvas (service request).
     */
    public static final int SET_HOLDING_CANVAS = 23;

    /**
     * Set holding canvas was done (reply).
     */
    public static final int SET_HOLDING_CANVAS_DONE = 24;

    /**
     * Is holding canvas (service request).
     */
    public static final int IS_HOLDING_CANVAS = 25;

    /**
     * Is holding canvas was done (reply).
     */
    public static final int IS_HOLDING_CANVAS_DONE = 26;

    /**
     * Get thief element in assault party (service request).
     */
    public static final int GET_THIEF_ELEMENT = 27;

    /**
     * Get thief element in assault party was done (reply).
     */
    public static final int GET_THIEF_ELEMENT_DONE = 28;

    /**
     * Request am i needed (service request).
     */
    public static final int AM_I_NEEDED = 29;

    /**
     * Am i needed was done (reply).
     */
    public static final int AM_I_NEEDED_DONE = 30;

    /**
     * Request prepare assault party (service request).
     */
    public static final int PREPARE_ASSAULT_PARTY = 31;

    /**
     * Prepare assault party was done (reply).
     */
    public static final int PREPARE_ASSAULT_PARTY_DONE = 32;

    /**
     * Request prepare excursion (service request).
     */
    public static final int PREPARE_EXCURSION = 33;

    /**
     * Prepare excursion was done (reply).
     */
    public static final int PREPARE_EXCURSION_DONE = 34;

    /**
     * Request sum up results (service request).
     */
    public static final int SUM_UP_RESULTS = 35;

    /**
     * Sum up results was done (reply).
     */
    public static final int SUM_UP_RESULTS_DONE = 36;

    /**
     * Request start operations (service request).
     */
    public static final int START_OPERATIONS = 37;

    /**
     * Start operations was done (reply).
     */
    public static final int START_OPERATIONS_DONE = 38;

    /**
     * Request appraise situation (service request).
     */
    public static final int APPRAISE_SIT = 39;

    /**
     * Appraise situation was done (reply).
     */
    public static final int APPRAISE_SIT_DONE = 40;

    /**
     * Request take a rest (service request).
     */
    public static final int TAKE_A_REST = 41;

    /**
     * Take a rest was done (reply).
     */
    public static final int TAKE_A_REST_DONE = 42;

    /**
     * Request hand a canvas (service request).
     */
    public static final int HAND_A_CANVAS = 43;

    /**
     * Hand a canvas was done (reply).
     */
    public static final int HAND_A_CANVAS_DONE = 44;

    /**
     * Request collect canvas (service request).
     */
    public static final int COLLECT_A_CANVAS = 45;

    /**
     * Collect canvas was done (reply).
     */
    public static final int COLLECT_A_CANVAS_DONE = 46;

    /**
     * Request get available assault party (service request).
     */
    public static final int GET_AVAILABLE_ASSAULT_PARTY = 47;

    /**
     * Get available assault party was done (reply).
     */
    public static final int GET_AVAILABLE_ASSAULT_PARTY_DONE = 48;

    /**
     * Request get available room (service request).
     */
    public static final int GET_AVAILABLE_ROOM = 49;

    /**
     * Get available room was done (reply).
     */
    public static final int GET_AVAILABLE_ROOM_DONE = 50;

    /**
     * Set thief to party (service request).
     */
    public static final int SET_THIEF_TO_PARTY = 51;

    /**
     * Set thief to party was done (reply).
     */
    public static final int SET_THIEF_TO_PARTY_DONE = 52;

    /**
     * Request roll a canvas (service request).
     */
    public static final int ROLL_A_CANVAS = 53;

    /**
     * Roll a canvas was done (reply).
     */
    public static final int ROLL_A_CANVAS_DONE = 54;

    /**
     * Get room distance (service request).
     */
    public static final int GET_ROOM_DISTANCE = 55;

    /**
     * Get room distance was done (reply).
     */
    public static final int GET_ROOM_DISTANCE_DONE = 56;

    /**
     * Set room information (service request).
     */
    public static final int SET_ROOM_INFO = 64;

    /**
     * Set room information was done (reply).
     */
    public static final int SET_ROOM_INFO_DONE = 72;

    /**
     * Set master thief state (service request).
     */
    public static final int SET_MASTER_THIEF_STATE = 57;

    /**
     * Set ordinary thief state (service request).
     */
    public static final int SET_ORDINARY_THIEF_STATE = 58;

    /**
     * Set assault party room identifier (service request).
     */
    public static final int SET_ASSAULT_PARTY_ROOM_ID = 59;

    /**
     * Set assault party element identifier (service request).
     */
    public static final int SET_ASSAULT_PARTY_ELEMENT_ID = 60;

    /**
     * Set assault party element position (service request).
     */
    public static final int SET_ASSAULT_PARTY_ELEMENT_POSITION = 61;

    /**
     * Set assault party element canvas status (service request).
     */
    public static final int SET_ASSAULT_PARTY_ELEMENT_CANVAS = 62;

    /**
     * End assault party element mission (service request).
     */
    public static final int END_ASSAULT_PARTY_ELEMENT_MISSION = 63;

    /**
     * Setting acknowledged (reply).
     */
    public static final int SET_ACKNOWLEDGE = 65;

    /**
     * Server shutdown (service request).
     */
    public static final int SHUTDOWN = 66;

    /**
     * Server was shutdown (reply).
     */
    public static final int SHUTDOWN_DONE = 67;

    /**
     * End of work - master thief (service request)
     */
    public static final int END_OPERATION = 68;

    /**
     * master thief goes home (reply)
     */
    public static final int END_OPERATION_DONE = 69;

    /**
     * Initialize simulation (service request).
     */
    public static final int INIT_SIMULATION = 70;

    /**
     * Simulation was initialized (reply).
     */
    public static final int INIT_SIMULATION_DONE = 71;

    /**
     * Set ordinary thief (service request).
     */
    public static final int SET_ORDINARY_THIEF = 73;
}
