package interfaces;

import java.io.Serializable;

/**
 * Data type to return both an character and an integer state value.
 *
 * Used in calls on remote objects.
 */
public class ReturnChar implements Serializable {
    /**
     * Serialization key.
     */
    public static final long serialVersionUID = 2021L;

    /**
     * Character value.
     */
    private char val;

    /**
     * Integer state value.
     */
    private int state;

    /**
     * ReturnChar instantiation.
     *
     * @param val   character value
     * @param state integer state value
     */
    public ReturnChar(char val, int state) {
        this.val = val;
        this.state = state;
    }

    /**
     * Getting character value.
     *
     * @return character value
     */
    public char getCharVal() {
        return (val);
    }

    /**
     * Getting integer state value.
     *
     * @return integer state value
     */
    public int getIntStateVal() {
        return (state);
    }
}
