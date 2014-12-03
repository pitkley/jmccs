package de.pitkley.jmccs.monitor;

import com.sun.jna.Platform;
import com.sun.jna.platform.win32.Dxva2;
import com.sun.jna.platform.win32.User32;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.sun.jna.platform.win32.PhysicalMonitorEnumerationAPI.PHYSICAL_MONITOR;
import static com.sun.jna.platform.win32.WinDef.DWORDByReference;
import static com.sun.jna.platform.win32.WinUser.HMONITOR;

public class WindowsMonitorManager implements MonitorManager {
    private final User32 USER32;
    private final Dxva2 DXVA2;

    private final List<Monitor> monitors = new ArrayList<>();

    public WindowsMonitorManager() throws UnsupportedOperatingSystemException {
        if (!Platform.isWindows()) {
            throw new UnsupportedOperatingSystemException("Windows", System.getProperty("os.name"));
        }

        USER32 = User32.INSTANCE;
        DXVA2 = Dxva2.INSTANCE;
    }

    @Override
    public List<Monitor> getMonitors() {
        if (monitors.size() == 0) {
            List<HMONITOR> hmonitors = new ArrayList<>();
            USER32.EnumDisplayMonitors(null, null, (hMonitor, hdcMonitor, lprcMonitor, dwData) -> {
                hmonitors.add(hMonitor);
                return 1;
            }, null);

            for (HMONITOR hmonitor : hmonitors) {
                DWORDByReference count = new DWORDByReference();
                DXVA2.GetNumberOfPhysicalMonitorsFromHMONITOR(hmonitor, count);

                PHYSICAL_MONITOR[] physical_monitors = new PHYSICAL_MONITOR[count.getValue().intValue()];
                DXVA2.GetPhysicalMonitorsFromHMONITOR(hmonitor, count.getValue().intValue(), physical_monitors);
                for (PHYSICAL_MONITOR physical_monitor : physical_monitors) {
                    monitors.add(new WindowsMonitor(physical_monitor, hmonitor));
                }
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
