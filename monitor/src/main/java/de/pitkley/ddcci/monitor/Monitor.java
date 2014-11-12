package de.pitkley.ddcci.monitor;

import java.io.Closeable;

public interface Monitor extends Closeable {

   boolean isCapabilitySupported(MonitorCapability capability);
   boolean isClosed();

   int getMinimumBrightness();
   int getCurrentBrightness();
   int getMaximumBrightness();
   void setBrightness(int brightness);
}
