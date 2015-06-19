package de.pitkley.jmccs.monitor;

import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

public class Monitors {
    private static MonitorManager monitorManager = null;

    static {
        loadManagers();
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
            monitorManager = manager;
        }
    }

    public static MonitorManager getMonitorManager() {
        return monitorManager;
    }
}
