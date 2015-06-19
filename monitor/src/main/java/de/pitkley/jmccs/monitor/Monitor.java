package de.pitkley.jmccs.monitor;

import de.pitkley.jmccs.vcp.VCPCode;
import de.pitkley.jmccs.vcp.VCPReply;

import java.io.Closeable;

interface Monitor extends Closeable {

    boolean isMainMonitor();

    boolean isClosed();

    boolean isVCPCodeSupported(VCPCode capability);

    VCPReply getVCPFeature(VCPCode vcpCode);

    void setVCPFeature(VCPCode vcpCode, int value);
}
