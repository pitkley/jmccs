package de.pitkley.jmccs.monitor;

/**
 * A {@link VCPCode} is associated to a specific <i>control function</i>. The control function defines how the values
 * for the specific code behave.
 * <p>
 * This is according to the VESA Monitor Control Command Set (MCCS) Standard Version 3.
 */
public enum VCPCodeFunction {
    /**
     * Continuous controls are controls that accept any value from zero to a maximum value specific for each control.
     * All continuous controls are read and write enabled.
     * <p>
     * (Taken directly from the VESA MCCS Standard 3, copyright 2003--2006 Video Electronics Standards Association.)
     */
    CONTINOUS,
    /**
     * The non-continuous controls accept only specific values. The valid values of these controls do not need to be
     * continuous in value. Non-continuous controls can be "read and write", "read-only" or "write-only".
     * <p>
     * (Taken directly from the VESA MCCS Standard 3, copyright 2003--2006 Video Electronics Standards Association.)
     */
    NON_CONTINOUS,
    /**
     * These controls are typically associated with a block of data where only the overall structure is explicitly
     * defined and not the contents. Table controls can be "read and write", "read only" or "write only".
     * <p>
     * (Taken directly from the VESA MCCS Standard 3, copyright 2003--2006 Video Electronics Standards Association.)
     */
    TABLE
}
