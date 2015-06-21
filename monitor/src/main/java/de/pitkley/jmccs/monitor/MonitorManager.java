package de.pitkley.jmccs.monitor;

import java.util.*;

public abstract class MonitorManager {
    private static MonitorManager INSTANCE = null;

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

    public abstract List<Monitor> getMonitors();

    /**
     * Tries to get the main/primary monitor.
     * This should maybe return a list, as it is bound to HMONITOR under Windows which may not correlate to a
     * physical display.
     *
     * @return
     */
    public Optional<Monitor> getMainMonitor() {
        return getMonitors().stream().filter(Monitor::isMainMonitor).findFirst();
    }

    public abstract void closeMonitors();
}
