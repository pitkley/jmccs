package de.pitkley.ddcci.monitor;

import com.sun.jna.platform.win32.Dxva2;
import com.sun.jna.platform.win32.User32;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.sun.jna.platform.win32.PhysicalMonitorEnumerationAPI.*;
import static com.sun.jna.platform.win32.WinDef.DWORDByReference;
import static com.sun.jna.platform.win32.WinUser.HMONITOR;

public class WindowsMonitorManager implements MonitorManager {
    private static final User32 user32 = User32.INSTANCE;
    private static final Dxva2 dxva2 = Dxva2.INSTANCE;

    private final List<Monitor> monitors = new ArrayList<>();

    public WindowsMonitorManager() throws UnsupportedOperatingSystemException {
        OperatingSystemIdentifier operatingSystemIdentifier = new OperatingSystemIdentifier();
        if (!operatingSystemIdentifier.isWindows()) {
            throw new UnsupportedOperatingSystemException("Windows", operatingSystemIdentifier.getOsName());
        }
    }

    @Override
    public List<Monitor> getMonitors() {
        List<HMONITOR> hmonitors = new ArrayList<>();
        user32.EnumDisplayMonitors(null, null, (hMonitor, hdcMonitor, lprcMonitor, dwData) -> {
            hmonitors.add(hMonitor);
            return 1;
        }, null);

        for (HMONITOR hmonitor : hmonitors) {
            DWORDByReference count = new DWORDByReference();
            dxva2.GetNumberOfPhysicalMonitorsFromHMONITOR(hmonitor, count);

            PHYSICAL_MONITOR[] physical_monitors = new PHYSICAL_MONITOR[count.getValue().intValue()];
            dxva2.GetPhysicalMonitorsFromHMONITOR(hmonitor, count.getValue().intValue(), physical_monitors);
            for (PHYSICAL_MONITOR physical_monitor : physical_monitors) {
                monitors.add(new WindowsMonitor(physical_monitor));
            }
        }

        return new ArrayList<>(monitors);
    }

    @Override
    public void closeMonitors() {
        monitors.stream().filter(monitor -> !monitor.isClosed()).forEach(monitor -> {
            try {
                monitor.close();
            } catch (IOException ignored) {
            }
        });
    }
}
