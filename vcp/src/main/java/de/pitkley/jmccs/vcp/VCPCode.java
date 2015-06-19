package de.pitkley.jmccs.vcp;

import java.util.*;

public enum VCPCode {
    /**
     * <p>Restore all factory presets including luminance / contrast, geometry, color and TV defaults.</p>
     * <p>
     * <p>Any non-zero value causes defaults to be restored. A value of zero must be ignored.</p>
     */
    RESTORE_FACTORY_DEFAULTS(0x04, VCPCodeType.WRITE_ONLY, VCPCodeFunction.NON_CONTINOUS),
    /**
     * <p>Restore factory defaults for luminance and contrast adjustments.</p>
     * <p>
     * <p>Any non-zero value causes defaults to be restored. A value of zero must be ignored.</p>
     */
    RESTORE_FACTORY_LUMINANCE_CONTRAST_DEFAULTS(0x05, VCPCodeType.WRITE_ONLY, VCPCodeFunction.NON_CONTINOUS),
    /**
     * <p>Permits the selection of a preset optimized by manufacturer for an application type or the selection of
     * a user defined setting.</p>
     * <p>
     * <table>
     * <thead>
     * <tr><th>Byte: SL</th><th>Description</th></tr>
     * </thead>
     * <tbody>
     * <tr><td>00h</td><td>Stand / default mode</td></tr>
     * <tr><td>01h</td><td>Productivity (e.g. office applications)</td></tr>
     * <tr><td>02h</td><td>Mixed (e.g. internet with mix of text and images)</td></tr>
     * </tbody>
     * </table>
     */
    DISPLAY_APPLICATION(0xDC, VCPCodeType.READ_WRITE, VCPCodeFunction.NON_CONTINOUS, new int[]{0x00, 0x01, 0x02}),
    /**
     * Increasing (decreasing) this value will increase (decrease) the Luminance of the image.
     */
    LUMINANCE(0x10, VCPCodeType.READ_WRITE, VCPCodeFunction.CONTINOUS);

    private static final Map<Integer, VCPCode> mapCodeToVCPCode = new HashMap<>();

    static {
        for (VCPCode vcpCode : EnumSet.allOf(VCPCode.class)) {
            mapCodeToVCPCode.put(vcpCode.getCode(), vcpCode);
        }
    }

    private final int code;
    private final VCPCodeType codeType;
    private final VCPCodeFunction codeFunction;
    private final Optional<Set<Integer>> values;

    VCPCode(int code, VCPCodeType codeType, VCPCodeFunction codeFunction) {
        this.code = code;
        this.codeType = codeType;
        this.codeFunction = codeFunction;
        this.values = Optional.empty();
    }

    VCPCode(int code, VCPCodeType codeType, VCPCodeFunction codeFunction, int[] values) {
        this.code = code;
        this.codeType = codeType;
        this.codeFunction = codeFunction;

        Set<Integer> v = new HashSet<>();
        this.values = Optional.of(v);

        for (int value : values) {
            v.add(value);
        }
    }

    public int getCode() {
        return code;
    }

    public VCPCodeType getCodeType() {
        return codeType;
    }

    public VCPCodeFunction getCodeFunction() {
        return codeFunction;
    }

    public Optional<Set<Integer>> getValues() {
        return values;
    }

    public static Optional<VCPCode> get(int code) {
        return Optional.ofNullable(mapCodeToVCPCode.get(code));
    }
}
