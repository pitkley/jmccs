package de.pitkley.ddcci.monitor;

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
            } catch (ServiceConfigurationError ignored) {
                /**
                 * To be fair, the exception should only be ignored, if it isn't an instance of
                 * UnsupportedOperatingSystemException.
                 *
                 * TODO Throw appropriate error/exception if classloading failed for a not expected reason
                 */
            }
        }

        if (manager == null) {
            // TODO Throw appropriate error if we found no manager
            // For now, we just stop with a error
            throw new Error("no manager found");
        } else {
            monitorManager = manager;
        }
    }

    public static MonitorManager getMonitorManager() {
        return monitorManager;
    }
}
