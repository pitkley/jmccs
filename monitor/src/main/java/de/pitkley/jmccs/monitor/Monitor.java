package de.pitkley.jmccs.monitor;

import java.io.Closeable;

/**
 * This interface is for interacting with a physical monitor and should be implemented by system-specific classes.
 */
public interface Monitor extends Closeable {

    /**
     * Checks if this monitor is the main monitor.
     * <p>
     * Many operating systems have the notion of a <i>main monitor</i>. This is the monitor that e.g. the taskbar is
     * associated to.
     *
     * @return <code>true</code> if the current monitor is the main monitor, <code>false</code> if it isn't or if the
     * operating system doesn't have the notion of <i>main monitor</i>
     */
    boolean isMainMonitor();

    /**
     * Checks if the supplied {@link VCPCode} is supported by the monitor.
     *
     * @param vcpCode the VCP-code to check
     * @return <code>true</code> if the code is supported, otherwise <code>false</code>
     */
    boolean isVCPCodeSupported(VCPCode vcpCode);

    /**
     * Tries to get the value that is associated to the given {@link VCPCode} from the monitor.
     *
     * @param vcpCode the VCP-code to retrieve the value for
     * @return a {@link VCPReply} containing the minimum, maxmimum and current value for the VCP-code
     */
    VCPReply getVCPFeature(VCPCode vcpCode);

    /**
     * Tries to set the value for the given {@link VCPCode} to the monitor.
     *
     * @param vcpCode the VCP-code to set the value for
     * @param value   the value to set the VCP-code to
     * @return <code>true</code> if setting the value succeeded, otherwise <code>false</code>
     */
    boolean setVCPFeature(VCPCode vcpCode, int value);

    /**
     * Checks if the monitor has been closed.
     * <p>
     * Closing a monitor generally refers to freeing up any system-resources or clearing any handles an implementation
     * might have. If there is no need for closing monitors, this method including {@link Closeable#close()} can be
     * ignored.
     *
     * @return <code>true</code> if the monitor has already been closed, otherwise <code>false</code>
     */
    boolean isClosed();
}
