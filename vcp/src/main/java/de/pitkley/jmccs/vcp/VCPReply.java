package de.pitkley.jmccs.vcp;

public class VCPReply {
    private final int minimum = 0;
    private final int maximum;
    private final int current;

    public VCPReply(int maximum, int current) {
        this.maximum = maximum;
        this.current = current;
    }

    public int getMinimum() {
        return minimum;
    }

    public int getMaximum() {
        return maximum;
    }

    public int getCurrent() {
        return current;
    }
}
