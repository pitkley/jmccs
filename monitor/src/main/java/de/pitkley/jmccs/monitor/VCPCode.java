package de.pitkley.jmccs.monitor;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public enum VCPCode {
    /**
     * Restore all factory presets including luminance / contrast, geometry, color and TV defaults.
     * <p>
     * Any non-zero value causes defaults to be restored. A value of zero must be ignored.
     */
    RESTORE_FACTORY_DEFAULTS(0x04, VCPCodeType.WRITE_ONLY, VCPCodeFunction.NON_CONTINOUS, Helper.POSITIVE_TWO_BYTES),
    /**
     * Restore factory defaults for luminance and contrast adjustments.
     * <p>
     * Any non-zero value causes defaults to be restored. A value of zero must be ignored.
     */
    RESTORE_FACTORY_LUMINANCE_CONTRAST_DEFAULTS(0x05, VCPCodeType.WRITE_ONLY, VCPCodeFunction.NON_CONTINOUS, Helper.POSITIVE_TWO_BYTES),
    /**
     * Restore factory defaults for geometry adjustments.
     * <p>
     * Any non-zero value causes defaults to be restored. A value of zero must be ignored.
     */
    RESTORE_FACTORY_GEOMETRY_DEFAULTS(0x06, VCPCodeType.WRITE_ONLY, VCPCodeFunction.NON_CONTINOUS, Helper.POSITIVE_TWO_BYTES),
    /**
     * Restore factory defaults for color settings.
     * <p>
     * Any non-zero value causes defaults to be restored. A value of zero must be ignored.
     */
    RESTORE_FACTORY_COLOR_DEFAULTS(0x08, VCPCodeType.WRITE_ONLY, VCPCodeFunction.NON_CONTINOUS, Helper.POSITIVE_TWO_BYTES),
    /**
     * Restore factory defaults for TV functions.
     * <p>
     * Any non-zero value causes defaults to be restored. A value of zero must be ignored.
     */
    RESTORE_FACTORY_TV_DEFAULTS(0x0A, VCPCodeType.WRITE_ONLY, VCPCodeFunction.NON_CONTINOUS, Helper.POSITIVE_TWO_BYTES),
    /**
     * Store / Restore the user saved values for current mode.
     * <p>
     * <table>
     * <thead><tr><th>Byte: SL</th><th>Description</th></tr></thead>
     * <tbody>
     * <tr><td>01h</td><td>Store current settings in the monitor.</td></tr>
     * <tr><td>02h</td><td>Restore factory defaults for current mode. If no factory defaults then restore user values for current mode.</td></tr>
     * </tbody>
     * </table>
     * <p>
     * All other values are reserved and must be ignored.
     */
    SETTINGS(0xB0, VCPCodeType.WRITE_ONLY, VCPCodeFunction.NON_CONTINOUS, new int[]{0x01, 0x02}),
    /**
     * Permits the selection of a preset optimized by manufacturer for an application type or the selection of
     * a user defined setting.
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
    private final Predicate<Integer> isLegal;

    VCPCode(int code, VCPCodeType codeType, VCPCodeFunction codeFunction) {
        this(code, codeType, codeFunction, (i -> true));
    }

    VCPCode(int code, VCPCodeType codeType, VCPCodeFunction codeFunction, int[] values) {
        this.code = code;
        this.codeType = codeType;
        this.codeFunction = codeFunction;

        Set<Integer> v = new HashSet<>();
        for (int value : values) {
            v.add(value);
        }
        this.isLegal = Helper.MAP_VALUES.apply(v);
    }

    VCPCode(int code, VCPCodeType codeType, VCPCodeFunction codeFunction, Predicate<Integer> isLegal) {
        this.code = code;
        this.codeType = codeType;
        this.codeFunction = codeFunction;
        this.isLegal = isLegal;
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

    public boolean isValueLegal(int value) {
        return isLegal.test(value);
    }

    public static Optional<VCPCode> get(int code) {
        return Optional.ofNullable(mapCodeToVCPCode.get(code));
    }
}

class Helper {
    protected static final Predicate<Integer> POSITIVE = (i) -> i >= 0;
    protected static final Predicate<Integer> TWO_BYTES = (i) -> i < Math.pow(2, 2 * 8);

    protected static final Predicate<Integer> POSITIVE_TWO_BYTES = POSITIVE.and(TWO_BYTES);

    protected static final Function<Set<Integer>, Predicate<Integer>> MAP_VALUES = (v) -> v::contains;
}
