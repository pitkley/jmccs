package de.pitkley.jmccs.monitor;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Dxva2;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.platform.win32.WinUser.MONITORINFO;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.sun.jna.platform.win32.PhysicalMonitorEnumerationAPI.PHYSICAL_MONITOR;
import static com.sun.jna.platform.win32.WinDef.*;
import static com.sun.jna.platform.win32.WinUser.HMONITOR;
import static com.sun.jna.platform.win32.WinUser.MONITORINFOF_PRIMARY;

public class WindowsMonitor implements Monitor {
    private static final User32 USER32 = User32.INSTANCE;
    private static final Dxva2 DXVA2 = Dxva2.INSTANCE;

    private final PHYSICAL_MONITOR physical_monitor;
    private final HMONITOR hmonitor;

    private final Map<VCPCode, Optional<Set<Integer>>> supportedVCPCodes;

    private boolean closed = false;

    protected WindowsMonitor(PHYSICAL_MONITOR physical_monitor, HMONITOR hmonitor) throws VCPStringFormatException {
        this.physical_monitor = physical_monitor;
        this.hmonitor = hmonitor;

        DWORDByReference pdwCapabilitiesStringLengthInCharacters = new DWORDByReference();
        DXVA2.GetCapabilitiesStringLength(physical_monitor.hPhysicalMonitor, pdwCapabilitiesStringLengthInCharacters);

        int capabilitiesStringLengthInCharacters = pdwCapabilitiesStringLengthInCharacters.getValue().intValue();

        Pointer p = new Memory(capabilitiesStringLengthInCharacters);
        WTypes.LPSTR pszASCIICapabilitiesString = new WTypes.LPSTR(p);

        DXVA2.CapabilitiesRequestAndCapabilitiesReply(physical_monitor.hPhysicalMonitor,
                pszASCIICapabilitiesString,
                pdwCapabilitiesStringLengthInCharacters.getValue());

        String capabilityString = Native.toString(p.getByteArray(0, capabilitiesStringLengthInCharacters));
        if (capabilityString == null || capabilityString.isEmpty()) {
            throw new VCPStringFormatException("Unable to get capabilities string");
        }

        String vcpString = CapabilityStringParser.parse(capabilityString).get("vcp");
        if (vcpString == null || vcpString.isEmpty()) {
            throw new VCPStringFormatException("Capabilities string is missing the supported VCP codes");
        }

        supportedVCPCodes = VCPStringParser.parse(vcpString);
    }

    @Override
    public boolean isMainMonitor() {
        if (closed) {
            throw new IllegalStateException("Monitor is already closed");
        }

        MONITORINFO monitorinfo = new MONITORINFO();
        USER32.GetMonitorInfo(this.hmonitor, monitorinfo);
        return (monitorinfo.dwFlags & MONITORINFOF_PRIMARY) == 1;
    }

    @Override
    public boolean isVCPCodeSupported(VCPCode vcpCode) {
        return supportedVCPCodes.containsKey(vcpCode);
    }

    @Override
    public VCPReply getVCPFeature(VCPCode vcpCode) {
        if (closed) {
            throw new IllegalStateException("Monitor is already closed");
        }
        if (!isVCPCodeSupported(vcpCode)) {
            throw new UnsupportedOperationException("The supplied VCP-code is not supported by the monitor");
        }

        DWORDByReference pdwCurrentValue = new DWORDByReference();
        DWORDByReference pdwMaximumValue = new DWORDByReference();
        DXVA2.GetVCPFeatureAndVCPFeatureReply(physical_monitor.hPhysicalMonitor,
                new BYTE(vcpCode.getCode()),
                null,
                pdwCurrentValue,
                pdwMaximumValue);

        VCPReply reply = new VCPReply(pdwCurrentValue.getValue().intValue(), pdwMaximumValue.getValue().intValue());
        return reply;
    }

    @Override
    public boolean setVCPFeature(VCPCode vcpCode, int value) {
        if (closed) {
            throw new IllegalStateException("Monitor is already closed");
        }
        if (!isVCPCodeSupported(vcpCode)) {
            throw new UnsupportedOperationException("The supplied VCP-code is not supported by the monitor");
        }

        DWORD dwNewValue = new DWORD(value);
        BOOL result = DXVA2.SetVCPFeature(physical_monitor.hPhysicalMonitor, new BYTE(vcpCode.getCode()), dwNewValue);

        return result.booleanValue();
    }

    @Override
    public boolean isClosed() {
        return closed;
    }


    @Override
    public void close() throws IOException {
        if (closed) {
            throw new IllegalStateException("Monitor is already closed");
        }

        DXVA2.DestroyPhysicalMonitor(physical_monitor.hPhysicalMonitor);
        closed = true;
    }
}
