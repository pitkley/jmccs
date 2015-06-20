package de.pitkley.jmccs.monitor;

import java.io.Closeable;

public interface Monitor extends Closeable {

    boolean isMainMonitor();

    boolean isVCPCodeSupported(VCPCode vcpCode);

    VCPReply getVCPFeature(VCPCode vcpCode);

    boolean setVCPFeature(VCPCode vcpCode, int value);

    boolean isClosed();
}
