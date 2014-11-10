package de.pitkley.ddcci.main;

import de.pitkley.ddcci.monitor.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
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
