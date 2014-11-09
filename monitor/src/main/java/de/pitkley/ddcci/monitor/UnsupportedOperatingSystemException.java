package de.pitkley.ddcci.monitor;

public class UnsupportedOperatingSystemException extends Exception {

    public UnsupportedOperatingSystemException(String expected, String actual) {
        this("Expected: " + expected + ", actual: " + actual);
    }

    public UnsupportedOperatingSystemException() {}

    public UnsupportedOperatingSystemException(String message) {
        super(message);
    }

    public UnsupportedOperatingSystemException(Throwable cause) {
        super(cause);
    }

    public UnsupportedOperatingSystemException(String message, Throwable cause) {
        super(message, cause);
    }
}
