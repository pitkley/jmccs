package de.pitkley.jmccs.monitor;

public class VCPReply {
    private final int minimum = 0;
    private final int current;
    private final int maximum;

    public VCPReply(int current, int maximum) {
        this.current = current;
        this.maximum = maximum;
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
