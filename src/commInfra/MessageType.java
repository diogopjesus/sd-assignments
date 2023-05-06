package commInfra;

/**
 * Type of the exchanged messages.
 *
 * Implementation of a client-server model of type 2 (server replication). Communication is based on
 * a communication channel under the TCP protocol.
 */

public final class MessageType {
    /**
     * Request start operations (service request).
     */
    public static final int STAOPE = 1;

    /**
     * Start operations was done (reply).
     */
    public static final int STAOPEDONE = 2;

    /**
     * Request appraise situation (service request).
     */
    public static final int APPSIT = 3;

    /**
     * Appraise situation was done (reply).
     */
    public static final int APPSITDONE = 4;

    /**
     * Request get available assault party (service request).
     */
    public static final int GETAVAASSPAR = 5;

    /**
     * Get available assault party was done (reply).
     */
    public static final int GETAVAASSPARDONE = 6;

    /**
     * Request get available room (service request).
     */
    public static final int GETAVAROO = 7;

    /**
     * Get available room was done (reply).
     */
    public static final int GETAVAROODONE = 8;

    /**
     * Request prepare assault party (service request).
     */
    public static final int PREASSPAR = 9;

    /**
     * Prepare assault party was done (reply).
     */
    public static final int PREASSPARDONE = 10;

    /**
     * Request send assault party (service request).
     */
    public static final int SENASSPAR = 11;

    /**
     * Send assault party was done (reply).
     */
    public static final int SENASSPARDONE = 12;

    /**
     * Request take a rest (service request).
     */
    public static final int TAKARES = 13;

    /**
     * Take a rest was done (reply).
     */
    public static final int TAKARESDONE = 14;

    /**
     * Request collect canvas (service request).
     */
    public static final int COLACAN = 15;

    /**
     * Collect canvas was done (reply).
     */
    public static final int COLACANDONE = 16;

    /**
     * Request sum up results (service request).
     */
    public static final int SUMUPRES = 17;

    /**
     * Sum up results was done (reply).
     */
    public static final int SUMUPRESDONE = 18;

    /**
     * Request am i needed (service request).
     */
    public static final int AMINEE = 19;

    /**
     * Am i needed was done (reply).
     */
    public static final int AMINEEDONE = 20;

    /**
     * Request prepare excursion (service request).
     */
    public static final int PREEXC = 21;

    /**
     * Prepare excursion was done (reply).
     */
    public static final int PREEXCDONE = 22;

    /**
     * Request crawl in (service request).
     */
    public static final int CRAWIN = 23;

    /**
     * Crawl in was done (reply).
     */
    public static final int CRAWINDONE = 24;

    /**
     * Request roll a canvas (service request).
     */
    public static final int ROLACAN = 25;

    /**
     * Roll a canvas was done (reply).
     */
    public static final int ROLACANDONE = 26;

    /**
     * Request reverse direction (service request).
     */
    public static final int REVDIR = 27;

    /**
     * Reverse direction was done (reply).
     */
    public static final int REVDIRDONE = 28;

    /**
     * Request crawl out (service request).
     */
    public static final int CRAWOUT = 29;

    /**
     * Crawl out was done (reply).
     */
    public static final int CRAWOUTDONE = 30;

    /**
     * Request hand a canvas (service request).
     */
    public static final int HANACAN = 31;

    /**
     * Hand a canvas was done (reply).
     */
    public static final int HANACANDONE = 32;

    /**
     * Server shutdown (service request).
     */
    public static final int SHUT = 33;

    /**
     * Server was shutdown (reply).
     */
    public static final int SHUTDONE = 34;

    /**
     * Set assault party target room (service request).
     */
    public static final int SETASSPARTARROO = 41;

    /**
     * Set assault party target room was done (reply).
     */
    public static final int SETASSPARTARROODONE = 42;

    /**
     * Get assault party target room (service request).
     */
    public static final int GETASSPARTARROO = 43;

    /**
     * Get assault party target room was done (reply).
     */
    public static final int GETASSPARTARROODONE = 44;

    /**
     * Set assault party target room distance (service request).
     */
    public static final int SETASSPARTARROODIS = 45;

    /**
     * Set assault party target room distance was done (reply).
     */
    public static final int SETASSPARTARROODISDONE = 46;

    /**
     * Check if assault party is available (service request).
     */
    public static final int ASSPARISAVA = 47;

    /**
     * Check if assault party is available was done (reply).
     */
    public static final int ASSPARISAVADONE = 48;

    /**
     * Check if assault party is full (service request).
     */
    public static final int ASSPARISFUL = 49;

    /**
     * Check if assault party is full was done (reply).
     */
    public static final int ASSPARISFULDONE = 50;

    /**
     * Join assault party (service request).
     */
    public static final int JOIASSPAR = 51;

    /**
     * Join assault party was done (reply).
     */
    public static final int JOIASSPARDONE = 52;

    /**
     * Quit assault party (service request).
     */
    public static final int QUIASSPAR = 53;

    /**
     * Quit assault party was done (reply).
     */
    public static final int QUIASSPARDONE = 54;

    /**
     * Set holding canvas (service request).
     */
    public static final int SETHOLCAN = 55;

    /**
     * Set holding canvas was done (reply).
     */
    public static final int SETHOLCANDONE = 56;

    /**
     * Is holding canvas (service request).
     */
    public static final int ISHOLCAN = 77;

    /**
     * Is holding canvas was done (reply).
     */
    public static final int ISHOLCANDONE = 78;

    /**
     * Get thief element in assault party (service request).
     */
    public static final int GETTHIELEINASSPAR = 57;

    /**
     * Get thief element in assault party was done (reply).
     */
    public static final int GETTHIELEINASSPARDONE = 58;

    /**
     * Set thief to party (service request).
     */
    public static final int SETTHITOPAR = 59;

    /**
     * Set thief to party was done (reply).
     */
    public static final int SETTHITOPARDONE = 60;

    /**
     * Get room distance (service request).
     */
    public static final int GETROODIS = 61;

    /**
     * Get room distance was done (reply).
     */
    public static final int GETROODISDONE = 62;

    /**
     * Set master thief state (service request).
     */
    public static final int SETMASTHISTA = 63;

    /**
     * Set master thief state was done (reply).
     */
    public static final int SETMASTHISTADONE = 64;

    /**
     * Set ordinary thief state (service request).
     */
    public static final int SETORDTHISTA = 65;

    /**
     * Set ordinary thief state was done (reply).
     */
    public static final int SETORDTHISTADONE = 66;

    /**
     * Set assault party room identifier (service request).
     */
    public static final int SETASSPARROOID = 67;

    /**
     * Set assault party room identifier was done (reply).
     */
    public static final int SETASSPARROOIDDONE = 68;

    /**
     * Set assault party element identifier (service request).
     */
    public static final int SETASSPARELEID = 69;

    /**
     * Set assault party element identifier was done (reply).
     */
    public static final int SETASSPARELEIDDONE = 70;

    /**
     * Set assault party element position (service request).
     */
    public static final int SETASSPARELEPOS = 71;

    /**
     * Set assault party element position was done (reply).
     */
    public static final int SETASSPARELEPOSDONE = 72;

    /**
     * Set assault party element canvas status (service request).
     */
    public static final int SETASSPARELECAN = 73;

    /**
     * Set assault party element canvas status was done (reply).
     */
    public static final int SETASSPARELECANDONE = 74;

    /**
     * End assault party element mission (service request).
     */
    public static final int ENDASSPARELEMIS = 75;

    /**
     * End assault party element mission was done (reply).
     */
    public static final int ENDASSPARELEMISDONE = 76;
}
