package de.pitkley.jmccs.monitor;

/**
 * If a VCP-string is malformed, this exception should be thrown.
 */
@SuppressWarnings("JavaDoc")
public class VCPStringFormatException extends Throwable {
    public VCPStringFormatException(String msg) {
        super(msg);
    }
}
