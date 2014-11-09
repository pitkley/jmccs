package de.pitkley.ddcci.monitor;

import java.util.List;

public interface MonitorManager {

    List<Monitor> getMonitors();

    void closeMonitors();
}
