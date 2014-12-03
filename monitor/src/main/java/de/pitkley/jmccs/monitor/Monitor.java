package de.pitkley.jmccs.monitor;

import java.io.Closeable;

public interface Monitor extends Closeable {

   boolean isMainMonitor();

   boolean isCapabilitySupported(MonitorCapability capability);
   boolean isClosed();

   int getMinimumBrightness();
   int getCurrentBrightness();
   int getMaximumBrightness();
   void setBrightness(int brightness);
}
