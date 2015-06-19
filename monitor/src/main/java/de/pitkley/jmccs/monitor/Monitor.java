package de.pitkley.jmccs.monitor;

import de.pitkley.jmccs.vcp.VCPCode;
import de.pitkley.jmccs.vcp.VCPReply;

import java.io.Closeable;

interface Monitor extends Closeable {

    boolean isMainMonitor();

    boolean isVCPCodeSupported(VCPCode vcpCode);

    VCPReply getVCPFeature(VCPCode vcpCode);

    boolean setVCPFeature(VCPCode vcpCode, int value);

    boolean isClosed();
}
