package de.pitkley.jmccs.monitor;

/**
 * Instantiating a {@link MonitorManager} that is not for the running operating system throws this exception.
 */
@SuppressWarnings("JavaDoc")
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
