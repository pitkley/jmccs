package de.pitkley.jmccs.monitor;

/**
 * This class encapsulates the three values a call to {@link Monitor#getVCPFeature(VCPCode)} returns: the minimum, the
 * maxmimum and the current value.
 */
public class VCPReply {
    /**
     * According to the VESA MCCS Standard 3, the minimum of a (continuous) value is always equal to zero.
     */
    @SuppressWarnings("FieldCanBeLocal")
    private final int minimum = 0;
    private final int current;
    private final int maximum;

    /**
     * Create an reply containing the current and maximum value.
     *
     * @param current the current value
     * @param maximum the maximum value
     */
    public VCPReply(int current, int maximum) {
        this.current = current;
        this.maximum = maximum;
    }

    /**
     * @return the minimum value
     */
    public int getMinimum() {
        return minimum;
    }

    /**
     * @return the maximum value
     */
    public int getMaximum() {
        return maximum;
    }

    /**
     * @return the current value
     */
    public int getCurrent() {
        return current;
    }
}
