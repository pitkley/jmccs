package de.pitkley.ddcci.main;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import de.pitkley.ddcci.monitor.*;

import java.util.List;

public class Main {

    private static void testMonitorManager() {
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
                msg("Getting current brightness...");
                int currentBrightness = monitor.getCurrentBrightness();
                msg("Response: " + currentBrightness);
                if (currentBrightness == 50) {
                    msg("Setting brightness to 90...");
                    monitor.setBrightness(90);
                } else {
                    msg("Setting brightness to 50...");
                    monitor.setBrightness(50);
                }
            }
        }
        msg("Closing monitors");
        monitorManager.closeMonitors();
    }

    private static void testLibOSX() {
        int displayCount = LibOSX.INSTANCE.getDisplayCount(5);
        System.out.println("displayCount: " + displayCount);

        Pointer onlineDisplays = new Memory(5 * Native.getNativeSize(Integer.TYPE));
        IntByReference ptrDisplayCount = new IntByReference();
        LibOSX.INSTANCE.getOnlineDisplayList(5, onlineDisplays, ptrDisplayCount);
        System.out.println(onlineDisplays);
        System.out.println(ptrDisplayCount.getValue());
        for (int i = 0; i < ptrDisplayCount.getValue(); i++) {
            System.out.println(onlineDisplays.getInt(i * Native.getNativeSize(Integer.TYPE)));
        }
    }

    private static void testCoreGraphics() {
        int maxDisplays = 5;

        Pointer onlineDisplays = new Memory(maxDisplays * Native.getNativeSize(Integer.TYPE));
        IntByReference ptrDisplayCount = new IntByReference();

        CoreGraphics.INSTANCE.CGGetOnlineDisplayList(maxDisplays, onlineDisplays, ptrDisplayCount);
        msg("onlineDisplays: "+onlineDisplays);
        msg("displayCount: "+ptrDisplayCount.getValue());
        for (int i = 0; i < ptrDisplayCount.getValue(); i++) {
            msg("Display " + i + ": " + onlineDisplays.getInt(i * Native.getNativeSize(Integer.TYPE)));
        }
    }

    public static void main(String[] args) {
        testCoreGraphics();
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
