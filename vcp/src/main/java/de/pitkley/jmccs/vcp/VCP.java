package de.pitkley.jmccs.vcp;

import de.pitkley.jmccs.monitor.MonitorCapability;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VCP {
    public static enum CodeFunction {
        CONTINUOUS,
        NONCONTINUOUS,
        TABLE,
        MANUFACTURER_SPECIFIC;
    }

    public static enum Code {
        BRIGHTNESS(0x10, CodeFunction.CONTINUOUS, MonitorCapability.BRIGHTNESS); // It really should be called LUMINANCE

        private static final Map<Integer, Code> reverseMapCodeNumber = new HashMap<>();
        private static final Map<MonitorCapability, Code> reverseMapMonitorCapability = new HashMap<>();

        static {
            for (Code code : EnumSet.allOf(Code.class)) {
                reverseMapCodeNumber.put(code.getCode(), code);
                reverseMapMonitorCapability.put(code.getMonitorCapability(), code);
            }
        }

        private final int code;
        private final CodeFunction codeFunction;
        private final MonitorCapability monitorCapability;

        Code(int code, CodeFunction codeFunction, MonitorCapability monitorCapability) {
            this.code = code;
            this.codeFunction = codeFunction;
            this.monitorCapability = monitorCapability;
        }

        public int getCode() {
            return code;
        }

        public CodeFunction getCodeFunction() {
            return codeFunction;
        }

        public MonitorCapability getMonitorCapability() {
            return monitorCapability;
        }

        public static Optional<Code> get(int code) {
            return Optional.ofNullable(reverseMapCodeNumber.get(code));
        }

        public static Optional<Code> get(MonitorCapability monitorCapability) {
            return Optional.ofNullable(reverseMapMonitorCapability.get(monitorCapability));
        }
    }

    private static final Pattern VALUE = Pattern.compile("(?<code>\\w+)(\\((?<values>.*?)\\))?");

    private VCP() {
    }

    public static List<Code> getCodeListFromVCPString(String vcpString) throws VCPStringFormatException {
        List<Code> codes = new ArrayList<>();
        Matcher matcher = VALUE.matcher(vcpString);
        while (matcher.find()) {
            Optional<Code> codeOptional = Code.get(Integer.valueOf(matcher.group("code"), 16));
            if (!codeOptional.isPresent()) {
                continue;
            }
            Code code = codeOptional.get();
            if (code.getCodeFunction() == CodeFunction.NONCONTINUOUS && matcher.groupCount() != 2) {
                throw new VCPStringFormatException("Found non-continuous code '" + code.toString() + "' but it had no values associated");
            }
            codes.add(code);
        }

        return codes;
    }
}
