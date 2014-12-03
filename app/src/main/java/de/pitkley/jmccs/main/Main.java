package de.pitkley.jmccs.main;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import de.pitkley.jmccs.monitor.*;

import java.util.List;
import java.util.Optional;

public class Main {

    private static void testMonitorManager(int brightness) {
        testMonitorManager(brightness, brightness);
    }

    private static void testMonitorManager(int brightness, int brightnessMain) {
        msg("Getting monitor manager");
        MonitorManager monitorManager = Monitors.getMonitorManager();
        msg("Getting monitors");
        List<Monitor> monitors = monitorManager.getMonitors();
        msg("Monitor count: " + monitors.size());

        msg("Looping through monitors...");
        for (Monitor monitor : monitors) {
            msg("Checking if brightness is supported");
            boolean brightnessSupported = monitor.isCapabilitySupported(MonitorCapability.BRIGHTNESS);
            msg("Response: " + brightnessSupported);

            if (brightnessSupported) {
                int setBrightness = brightness;

                Optional<Monitor> mainMonitor = monitorManager.getMainMonitor();
                if (mainMonitor.isPresent() && mainMonitor.get().equals(monitor)) {
                    setBrightness = brightnessMain;
                }

                msg("Getting current brightness...");
                int currentBrightness = monitor.getCurrentBrightness();
                msg("Response: " + currentBrightness);
                if (currentBrightness != setBrightness) {
                    msg("Setting brightness to " + setBrightness + "...");
                    monitor.setBrightness(setBrightness);
                }
            }
        }
        msg("Closing monitors");
        monitorManager.closeMonitors();
    }

    private static void testLibMCCSread() {
        LibMCCS mccs = LibMCCS.INSTANCE;
        LibMCCS.MCCSReadCommand.ByReference mccsRead = new LibMCCS.MCCSReadCommand.ByReference();
        mccsRead.control_id = 0x10;
        mccs.MCCSRead(CoreGraphics.INSTANCE.CGMainDisplayID(), mccsRead);
        msg("current: " + mccsRead.current_value);
        msg("max: " + mccsRead.max_value);
    }

    private static void testLibMCCSCapString() {
        LibMCCS mccs = LibMCCS.INSTANCE;
        PointerByReference ptrRef = new PointerByReference();
        mccs.MCCSGetCapabilityString(CoreGraphics.INSTANCE.CGMainDisplayID(), ptrRef);
        String val = ptrRef.getValue().getString(0);
        msg("val: " + val);
        mccs.cleanup_pointer(ptrRef.getValue());
    }

    private static void testCoreGraphicsMainDisplayID() {
        System.out.println(CoreGraphics.INSTANCE.CGMainDisplayID().intValue());
    }

    private static void testCoreGraphicsGetOnlineDisplayList() {
        int maxDisplays = 5;

        Pointer onlineDisplays = new Memory(maxDisplays * Native.getNativeSize(Integer.TYPE));
        IntByReference ptrDisplayCount = new IntByReference();

        CoreGraphics.INSTANCE.CGGetOnlineDisplayList(maxDisplays, onlineDisplays, ptrDisplayCount);
        msg("onlineDisplays: " + onlineDisplays);
        msg("displayCount: " + ptrDisplayCount.getValue());
        for (int i = 0; i < ptrDisplayCount.getValue(); i++) {
            msg("Display " + i + ": " + onlineDisplays.getInt(i * Native.getNativeSize(Integer.TYPE)));
        }
    }

    public static void main(String[] args) {
        testMonitorManager(1, 10);
    }

    private static long lastMsg = 0L;

    public static void msg(String msg) {
        long now = System.currentTimeMillis();
        if (lastMsg == 0L) {
            lastMsg = now;
        }
        long delta = now - lastMsg;
        lastMsg = now;
        System.out.println(String.format("%d] %4d] %s", now, delta, msg));
    }
}
