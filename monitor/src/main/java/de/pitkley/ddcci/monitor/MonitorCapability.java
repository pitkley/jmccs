package de.pitkley.ddcci.monitor;

public enum MonitorCapability {
    BRIGHTNESS(0x10);

    private final int code;

    MonitorCapability(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
