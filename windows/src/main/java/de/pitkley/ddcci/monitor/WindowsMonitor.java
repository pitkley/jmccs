package de.pitkley.ddcci.monitor;

import com.sun.jna.platform.win32.Dxva2;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinUser.MONITORINFO;

import java.io.IOException;
import java.util.Optional;

import static com.sun.jna.platform.win32.PhysicalMonitorEnumerationAPI.PHYSICAL_MONITOR;
import static com.sun.jna.platform.win32.WinDef.DWORDByReference;
import static com.sun.jna.platform.win32.WinUser.HMONITOR;
import static com.sun.jna.platform.win32.WinUser.MONITORINFOF_PRIMARY;

public class WindowsMonitor implements Monitor {
    private static final User32 USER32 = User32.INSTANCE;
    private static final Dxva2 DXVA2 = Dxva2.INSTANCE;

    private final PHYSICAL_MONITOR physical_monitor;
    private final HMONITOR hmonitor;

    private final long monitorCapabilities;
    private final long supportedColorTemperatures;

    private int minimumBrightness = -1;
    private int maximumBrightness = -1;

    private boolean closed = false;

    protected WindowsMonitor(PHYSICAL_MONITOR physical_monitor, HMONITOR hmonitor) {
        this.physical_monitor = physical_monitor;
        this.hmonitor = hmonitor;

        DWORDByReference pdwMonitorCapabilities = new DWORDByReference();
        DWORDByReference pdwSupportedColorTemperatures = new DWORDByReference();
        DXVA2.GetMonitorCapabilities(physical_monitor.hPhysicalMonitor, pdwMonitorCapabilities, pdwSupportedColorTemperatures);
        monitorCapabilities = pdwMonitorCapabilities.getValue().longValue();
        supportedColorTemperatures = pdwSupportedColorTemperatures.getValue().longValue();
    }

    @Override
    public boolean isMainMonitor() {
        MONITORINFO monitorinfo = new MONITORINFO();
        USER32.GetMonitorInfo(this.hmonitor, monitorinfo);
        return (monitorinfo.dwFlags & MONITORINFOF_PRIMARY) == 1;
    }

    public boolean isCapabilitySupported(MonitorCapability capability) {
        Optional<WindowsMonitorCapability> windowsMonitorCapability = WindowsMonitorCapability.get(capability);
        return windowsMonitorCapability.isPresent() && (monitorCapabilities & windowsMonitorCapability.get().getMcCapability().getFlag()) != 0;
    }

    @Override
    public int getMinimumBrightness() {
        if (this.minimumBrightness == -1) {
            getCurrentBrightness();
        }
        return minimumBrightness;
    }

    @Override
    public int getCurrentBrightness() {
        if (isClosed()) {
            throw new UnsupportedOperationException("Monitor is already closed");
        }
        if (!isCapabilitySupported(MonitorCapability.BRIGHTNESS)) {
            throw new UnsupportedOperationException("Monitor does not support getting brightness");
        }

        DWORDByReference pdwMinimumBrightness = new DWORDByReference();
        DWORDByReference pdwCurrentBrightness = new DWORDByReference();
        DWORDByReference pdwMaximumBrightness = new DWORDByReference();
        DXVA2.GetMonitorBrightness(physical_monitor.hPhysicalMonitor, pdwMinimumBrightness, pdwCurrentBrightness, pdwMaximumBrightness);

        this.minimumBrightness = pdwMinimumBrightness.getValue().intValue();
        this.maximumBrightness = pdwMaximumBrightness.getValue().intValue();

        return pdwCurrentBrightness.getValue().intValue();
    }

    @Override
    public int getMaximumBrightness() {
        if (this.maximumBrightness == -1) {
            getCurrentBrightness();
        }
        return maximumBrightness;
    }

    @Override
    public void setBrightness(int brightness) {
        if (isClosed()) {
            throw new UnsupportedOperationException("Monitor is already closed");
        }
        if (!isCapabilitySupported(MonitorCapability.BRIGHTNESS)) {
            throw new UnsupportedOperationException("Monitor does not support getting brightness");
        }

        if (brightness < minimumBrightness) {
            throw new IllegalArgumentException("Brightness of '" + brightness + "'was below minimum brightness of '" + minimumBrightness + "'");
        } else if (brightness > maximumBrightness) {
            throw new IllegalArgumentException("Brightness of '" + brightness + "'was above maximum brightness of '" + maximumBrightness + "'");
        }

        DXVA2.SetMonitorBrightness(physical_monitor.hPhysicalMonitor, brightness);
    }

    @Override
    public boolean isClosed() {
        return closed;
    }


    @Override
    public void close() throws IOException {
        DXVA2.DestroyPhysicalMonitor(physical_monitor.hPhysicalMonitor);
        closed = true;
    }
}
