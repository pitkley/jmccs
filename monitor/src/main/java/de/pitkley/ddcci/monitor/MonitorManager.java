package de.pitkley.ddcci.monitor;

import java.util.List;
import java.util.Optional;

public interface MonitorManager {

    List<Monitor> getMonitors();

    /**
     * Tries to get the main/primary monitor.
     * This should maybe return a list, as it is bound to HMONITOR under Windows which may not correlate to a
     * physical display.
     * @return
     */
    default Optional<Monitor> getMainMonitor() {
        return getMonitors().stream().filter(Monitor::isMainMonitor).findFirst();
    }

    void closeMonitors();
}
