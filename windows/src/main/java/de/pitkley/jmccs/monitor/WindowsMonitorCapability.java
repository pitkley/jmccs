package de.pitkley.jmccs.monitor;

import com.sun.jna.platform.win32.HighLevelMonitorConfigurationAPI.MC_CAPS;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum WindowsMonitorCapability {
    BRIGHTNESS(MonitorCapability.BRIGHTNESS, MC_CAPS.MC_CAPS_BRIGHTNESS);

    private static final Map<MonitorCapability, WindowsMonitorCapability> reverseMap = new HashMap<>();

    static {
        for (WindowsMonitorCapability windowsMonitorCapability : EnumSet.allOf(WindowsMonitorCapability.class)) {
            reverseMap.put(windowsMonitorCapability.getMonitorCapability(), windowsMonitorCapability);
        }
    }

    private final MonitorCapability monitorCapability;
    private final MC_CAPS mcCapability;

    WindowsMonitorCapability(MonitorCapability monitorCapability, MC_CAPS mcCapability) {
        this.monitorCapability = monitorCapability;
        this.mcCapability = mcCapability;
    }

    public MonitorCapability getMonitorCapability() {
        return monitorCapability;
    }

    public MC_CAPS getMcCapability() {
        return mcCapability;
    }

    public static Optional<WindowsMonitorCapability> get(MonitorCapability monitorCapability) {
        return Optional.ofNullable(reverseMap.get(monitorCapability));
    }
}
