package de.pitkley.jmccs.monitor;

/**
 * A {@link VCPCode} is of a specific <i>type</i>. The type defines if it can be read, written or both.
 * <p>
 * This is according to the VESA Monitor Control Command Set (MCCS) Standard Version 3.
 */
public enum VCPCodeType {
    /**
     * Write-only codes can only be written to, not read from.
     */
    WRITE_ONLY,
    /**
     * Read-only codes can only be read from, not written to.
     */
    READ_ONLY,
    /**
     * Read-write codes can both be read from and written to.
     */
    READ_WRITE
}
