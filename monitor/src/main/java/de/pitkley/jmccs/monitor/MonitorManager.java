package de.pitkley.jmccs.monitor;

import java.util.*;

/**
 * This abstract class is for managing of physical monitors.
 */
public abstract class MonitorManager {
    private static MonitorManager INSTANCE = null;

    /**
     * Retrieves the (operating system specific) singleton-instance of {@link MonitorManager}.
     *
     * @return the (operating system specific) singleton-instance
     */
    public static MonitorManager get() {
        if (INSTANCE == null) {
            loadManagers();
        }
        return INSTANCE;
    }

    private static void loadManagers() {
        Iterator<MonitorManager> managers = ServiceLoader.load(MonitorManager.class).iterator();
        MonitorManager manager = null;
        //noinspection WhileLoopReplaceableByForEach
        while (managers.hasNext()) {
            try {
                MonitorManager curManager = managers.next();
                manager = curManager;
                break;
            } catch (ServiceConfigurationError e) {
                if (!(e.getCause() instanceof UnsupportedOperatingSystemException)) {
                    throw e;
                }
            }
        }

        if (manager == null) {
            // TODO Throw appropriate error if we found no manager
            // For now, we just stop with an error
            throw new Error("no manager found");
        } else {
            INSTANCE = manager;
        }
    }

    /**
     * Returns all monitors recognized by the implementation.
     *
     * @return a list of all monitors recognized by the implementation
     */
    public abstract List<Monitor> getMonitors();

    /**
     * Gets the (first) main monitor it can find among all monitors.
     * <p>
     * If the operating system has no notion of <i>main monitor</i>, the {@link Optional} will be empty. See {@link
     * Monitor#isMainMonitor()} for more information.
     *
     * @return an optional that either contains the (first) main monitor or is empty
     */
    public Optional<Monitor> getMainMonitor() {
        return getMonitors().stream().filter(Monitor::isMainMonitor).findFirst();
    }

    /**
     * Closes all monitors.
     * <p>
     * Closing a monitor generally refers to freeing up any system-resources or clearing any handles an implementation
     * might have. If there is no need for closing monitors, this method can be empty.
     */
    public abstract void closeMonitors();
}
