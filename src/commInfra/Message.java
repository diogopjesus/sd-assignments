package commInfra;

import java.io.*;
import genclass.GenericIO;

/**
 * Internal structure of the exchanged messages.
 *
 * Implementation of a client-server model of type 2 (server replication). Communication is based on
 * a communication channel under the TCP protocol.
 */

public class Message implements Serializable {
    /**
     * Serialization key.
     */
    private static final long serialVersionUID = 2021L;

    /**
     * Message type.
     */
    private int msgType = -1;

    /**
     * Master thief state.
     */
    private int mtState = -1;

    /**
     * Ordinary thief identification.
     */
    private int otId = -1;

    /**
     * Ordinary thief maximum displacement.
     */
    private int maxDis = -1;

    /**
     * Ordinary thief state.
     */
    private int otState = -1;

    /**
     * Assault party identification.
     */
    private int assPartId = -1;

    /**
     * Room identification.
     */
    private int roomId = -2;

    /**
     * Room distance.
     */
    private int distance = -1;

    /**
     * Assault party is available.
     */
    private boolean isAvailable = false;

    /**
     * Assault party is full.
     */
    private boolean isFull = false;

    /**
     * Ordinary thief is holding canvas.
     */
    private boolean canvas = false;

    /**
     * Element identification on assault party.
     */
    private int elemId = -1;

    /**
     * Ordinary thief is needed.
     */
    private boolean isNeeded = false;

    /**
     * Appraise situation operation.
     */
    private char oper = 0x00;

    /**
     * Ordinary thief position.
     */
    private int pos = -1;

    /**
     * Continue crawling.
     */
    private boolean contCrawl = false;

    /**
     * Number of paintings per room.
     */
    private int[] numPaint = null;

    /**
     * Distances to rooms.
     */
    private int[] roomDist = null;

    /**
     * Array of maximum displacements.
     */
    private int[] maxDisArray = null;

    /**
     * Name of the logging file.
     */
    private String fName = null;

    /**
     * Message instantiation (form 1).
     *
     * @param type message type
     */
    public Message(int type) {
        this.msgType = type;
    }

    /**
     * Message instantiation (form 2).
     *
     * @param type message type
     * @param value
     */
    public Message(int type, int value) {
        this.msgType = type;
        if ((msgType == MessageType.SEND_ASSAULT_PARTY_DONE)
                || (msgType == MessageType.PREPARE_ASSAULT_PARTY_DONE)
                || (msgType == MessageType.SUM_UP_RESULTS_DONE)
                || (msgType == MessageType.START_OPERATIONS_DONE)
                || (msgType == MessageType.TAKE_A_REST_DONE)
                || (msgType == MessageType.COLLECT_A_CANVAS_DONE)
                || (msgType == MessageType.SET_MASTER_THIEF_STATE)) {
            mtState = value;
        } else if ((msgType == MessageType.REVERSE_DIRECTION)
                || (msgType == MessageType.JOIN_ASSAULT_PARTY)
                || (msgType == MessageType.QUIT_ASSAULT_PARTY)
                || (msgType == MessageType.IS_HOLDING_CANVAS)
                || (msgType == MessageType.GET_THIEF_ELEMENT)
                || (msgType == MessageType.PREPARE_EXCURSION)
                || (msgType == MessageType.HAND_A_CANVAS_DONE)
                || (msgType == MessageType.ROLL_A_CANVAS_DONE)) {
            otId = value;
        } else if ((msgType == MessageType.SET_TARGET_ROOM)
                || (msgType == MessageType.GET_TARGET_ROOM_DONE)
                || (msgType == MessageType.GET_AVAILABLE_ROOM_DONE)
                || (msgType == MessageType.GET_ROOM_DISTANCE)) {
            roomId = value;
        } else if ((msgType == MessageType.SET_TARGET_ROOM_DISTANCE)
                || (msgType == MessageType.GET_ROOM_DISTANCE_DONE)) {
            distance = value;
        } else if ((msgType == MessageType.GET_THIEF_ELEMENT_DONE)) {
            elemId = value;
        } else if ((msgType == MessageType.GET_AVAILABLE_ASSAULT_PARTY_DONE)) {
            assPartId = value;
        } else {
            GenericIO.writelnString(
                    "Message type = " + msgType + ": non-implemented instantiation!");
            System.exit(1);
        }
    }

    /**
     * Message instantiation (form 3).
     *
     * @param type message type
     * @param value isAvailable, isFull or canvas
     */
    public Message(int type, boolean value) {
        this.msgType = type;
        if ((msgType == MessageType.IS_AVAILABLE_DONE)) {
            isAvailable = value;
        } else if ((msgType == MessageType.IS_FULL_DONE)) {
            isFull = value;
        } else if ((msgType == MessageType.IS_HOLDING_CANVAS_DONE)) {
            canvas = value;
        } else {
            GenericIO.writelnString(
                    "Message type = " + msgType + ": non-implemented instantiation!");
            System.exit(1);
        }
    }

    /**
     * Message instantiation (form 4).
     *
     * @param type message type
     * @param oper appraise sit operation
     */
    public Message(int type, char oper) {
        this.msgType = type;
        if ((msgType == MessageType.APPRAISE_SIT_DONE)) {
            this.oper = oper;
        } else {
            GenericIO.writelnString(
                    "Message type = " + msgType + ": non-implemented instantiation!");
            System.exit(1);
        }
    }

    /**
     * Message instantiation (form 5).
     *
     * @param type message type
     * @param value1
     * @param value2
     */
    public Message(int type, int value1, int value2) {
        this.msgType = type;
        if ((msgType == MessageType.CRAWL_IN) || (msgType == MessageType.CRAWL_OUT)) {
            otId = value1;
            maxDis = value2;
        } else if ((msgType == MessageType.REVERSE_DIRECTION_DONE)
                || (msgType == MessageType.AM_I_NEEDED)
                || (msgType == MessageType.SET_ORDINARY_THIEF_STATE)) {
            otId = value1;
            otState = value2;
        } else if ((msgType == MessageType.PREPARE_ASSAULT_PARTY)
                || (msgType == MessageType.SET_ASSAULT_PARTY_ROOM_ID)) {
            assPartId = value1;
            roomId = value2;
        } else if ((msgType == MessageType.HAND_A_CANVAS)
                || (msgType == MessageType.SET_THIEF_TO_PARTY)
                || (msgType == MessageType.ROLL_A_CANVAS)) {
            otId = value1;
            assPartId = value2;
        } else if ((msgType == MessageType.END_ASSAULT_PARTY_ELEMENT_MISSION)) {
            assPartId = value1;
            elemId = value2;
        } else {
            GenericIO.writelnString(
                    "Message type = " + msgType + ": non-implemented instantiation!");
            System.exit(1);
        }
    }

    /**
     * Message instantiation (form 6).
     *
     * @param type message type
     * @param value1
     * @param value2
     */
    public Message(int type, int value1, boolean value2) {
        this.msgType = type;
        if ((msgType == MessageType.SET_HOLDING_CANVAS)) {
            otId = value1;
            canvas = value2;
        } else {
            GenericIO.writelnString(
                    "Message type = " + msgType + ": non-implemented instantiation!");
            System.exit(1);
        }
    }


    /**
     * Message instantiation (form 7).
     *
     * @param type message type
     * @param value1
     * @param value2
     * @param value3
     */
    public Message(int type, int value1, int value2, int value3) {
        this.msgType = type;
        if ((msgType == MessageType.PREPARE_EXCURSION_DONE)) {
            otId = value1;
            otState = value2;
            assPartId = value3;
        } else if ((msgType == MessageType.SET_ASSAULT_PARTY_ELEMENT_ID)) {
            assPartId = value1;
            elemId = value2;
            otId = value3;
        } else if ((msgType == MessageType.SET_ASSAULT_PARTY_ELEMENT_POSITION)) {
            assPartId = value1;
            elemId = value2;
            pos = value3;
        } else {
            GenericIO.writelnString(
                    "Message type = " + msgType + ": non-implemented instantiation!");
            System.exit(1);
        }
    }

    /**
     * Message instantiation (form 8).
     *
     * @param type message type
     * @param value1
     * @param value2
     * @param value3
     */
    public Message(int type, int value1, int value2, boolean value3) {
        this.msgType = type;
        if ((msgType == MessageType.CRAWL_IN_DONE) || (msgType == MessageType.CRAWL_OUT_DONE)) {
            otId = value1;
            otState = value2;
            contCrawl = value3;
        } else if ((msgType == MessageType.AM_I_NEEDED_DONE)) {
            otId = value1;
            otState = value2;
            isNeeded = value3;
        } else if ((msgType == MessageType.SET_ASSAULT_PARTY_ELEMENT_CANVAS)) {
            assPartId = value1;
            elemId = value2;
            canvas = value3;
        } else {
            GenericIO.writelnString(
                    "Message type = " + msgType + ": non-implemented instantiation!");
            System.exit(1);
        }
    }

    /**
     * Message instantiation (form 9).
     *
     * @param type message type
     * @param numPaint number of paintings
     * @param roomDist room distances
     */
    public Message(int type, int[] numPaint, int[] roomDist) {
        this.msgType = type;
        if ((msgType == MessageType.SET_ROOM_INFO)) {
            this.numPaint = numPaint;
            this.roomDist = roomDist;
        } else {
            GenericIO.writelnString(
                    "Message type = " + msgType + ": non-implemented instantiation!");
            System.exit(1);
        }
    }

    /**
     * Message instantiation (form 10).
     *
     * @param type message type
     * @param logFileName name of the logging file
     * @param maxDisArray maximum displacement array
     */
    public Message(int type, String logFileName, int[] maxDisArray) {
        this.msgType = type;
        if ((msgType == MessageType.INIT_SIMULATION)) {
            this.fName = logFileName;
            this.maxDisArray = maxDisArray;
        } else {
            GenericIO.writelnString(
                    "Message type = " + msgType + ": non-implemented instantiation!");
            System.exit(1);
        }
    }

    /**
     * Get message type.
     *
     * @return message type
     */
    public int getMsgType() {
        return msgType;
    }

    /**
     * Get master thief state.
     *
     * @return master thief state
     */
    public int getMtState() {
        return mtState;
    }

    /**
     * Get ordinary thief identification.
     *
     * @return ordinary thief identification
     */
    public int getOtId() {
        return otId;
    }

    /**
     * Get ordinary thief maximum displacement.
     *
     * @return ordinary thief maximum displacement
     */
    public int getMaxDis() {
        return maxDis;
    }

    /**
     * Get ordinary thief state.
     *
     * @return ordinary thief state
     */
    public int getOtState() {
        return otState;
    }

    /**
     * Get assault party identification.
     *
     * @return assault party identification
     */
    public int getAssPartId() {
        return assPartId;
    }

    /**
     * Get room identification.
     *
     * @return room identification
     */
    public int getRoomId() {
        return roomId;
    }

    /**
     * Get room distance.
     *
     * @return room distance
     */
    public int getDistance() {
        return distance;
    }

    /**
     * Get assault party is available.
     *
     * @return assault party is available
     */
    public boolean isAvailable() {
        return isAvailable;
    }

    /**
     * Get assault party is full.
     *
     * @return assault party is full
     */
    public boolean isFull() {
        return isFull;
    }

    /**
     * Get ordinary thief is holding canvas.
     *
     * @return ordinary thief is holding canvas
     */
    public boolean isCanvas() {
        return canvas;
    }

    /**
     * Get element identification on assault party.
     *
     * @return element identification on assault party
     */
    public int getElemId() {
        return elemId;
    }

    /**
     * Get ordinary thief is needed.
     *
     * @return ordinary thief is needed
     */
    public boolean isNeeded() {
        return isNeeded;
    }

    /**
     * Get appraise situation operation.
     *
     * @return appraise situation operation
     */
    public char getOper() {
        return oper;
    }

    /**
     * Get ordinary thief position.
     *
     * @return ordinary thief position
     */
    public int getPos() {
        return pos;
    }

    /**
     * Get continue crawling.
     *
     * @return
     */
    public boolean isContCrawl() {
        return contCrawl;
    }

    /**
     * Get number of paintings per room.
     *
     * @return number of paintings per room
     */
    public int[] getNumPaint() {
        return numPaint;
    }

    /**
     * Get room distances.
     *
     * @return room distances
     */
    public int[] getRoomDist() {
        return roomDist;
    }

    /**
     * Get maximum displacement array.
     *
     * @return maximum displacement array
     */
    public int[] getMaxDisArray() {
        return maxDisArray;
    }

    /**
     * Get name of the logging file.
     *
     * @return name of the logging file
     */
    public String getfName() {
        return fName;
    }

    /**
     * Printing the values of the internal fields.
     *
     * It is used for debugging purposes.
     *
     * @return string containing, in separate lines, the pair field name - field value
     */
    @Override
    public String toString() {
        return ("Message Type =" + msgType + "\nMaster Thief State=" + mtState
                + "\nOrdinary Thief Identification =" + otId
                + "\nOrdinary Thief Maximum Displacement =" + maxDis + "\nOrdinary Thief State ="
                + otState + "\nAssault Party Identification =" + assPartId
                + "\nRoom Identification =" + roomId + "\nRoom Distance =" + distance
                + "\nIs Available =" + isAvailable + "\nIs Full =" + isFull + "\nCanvas =" + canvas
                + "\nElement Identification =" + elemId + "\nIs Needed =" + isNeeded
                + "\nOperation =" + oper + "\nPosition=" + pos + "\nFile Name=" + fName);
    }
}
