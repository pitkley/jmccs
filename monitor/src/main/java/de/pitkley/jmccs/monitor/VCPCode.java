package de.pitkley.jmccs.monitor;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * This class (currently) holds a selection of VCP-codes with associated code-types, code-functions and allowed values.
 */
public enum VCPCode {
    /**
     * Restore all factory presets including luminance / contrast, geometry, color and TV defaults.
     * <p>
     * Any non-zero value causes defaults to be restored. A value of zero must be ignored
     */
    RESTORE_FACTORY_DEFAULTS(0x04, VCPCodeType.WRITE_ONLY, VCPCodeFunction.NON_CONTINOUS, Helper.POSITIVE_TWO_BYTES),
    /**
     * Restore factory defaults for luminance and contrast adjustments.
     * <p>
     * Any non-zero value causes defaults to be restored. A value of zero must be ignored
     */
    RESTORE_FACTORY_LUMINANCE_CONTRAST_DEFAULTS(0x05, VCPCodeType.WRITE_ONLY, VCPCodeFunction.NON_CONTINOUS, Helper
            .POSITIVE_TWO_BYTES),
    /**
     * Restore factory defaults for geometry adjustments.
     * <p>
     * Any non-zero value causes defaults to be restored. A value of zero must be ignored
     */
    RESTORE_FACTORY_GEOMETRY_DEFAULTS(0x06, VCPCodeType.WRITE_ONLY, VCPCodeFunction.NON_CONTINOUS, Helper
            .POSITIVE_TWO_BYTES),
    /**
     * Restore factory defaults for color settings.
     * <p>
     * Any non-zero value causes defaults to be restored. A value of zero must be ignored
     */
    RESTORE_FACTORY_COLOR_DEFAULTS(0x08, VCPCodeType.WRITE_ONLY, VCPCodeFunction.NON_CONTINOUS, Helper
            .POSITIVE_TWO_BYTES),
    /**
     * Restore factory defaults for TV functions.
     * <p>
     * Any non-zero value causes defaults to be restored. A value of zero must be ignored
     */
    RESTORE_FACTORY_TV_DEFAULTS(0x0A, VCPCodeType.WRITE_ONLY, VCPCodeFunction.NON_CONTINOUS, Helper.POSITIVE_TWO_BYTES),
    /**
     * Store / Restore the user saved values for current mode.
     * <p>
     * <table> <thead><tr><th>Byte: SL</th><th>Description</th></tr></thead> <tbody> <tr><td>01h</td><td>Store current
     * settings in the monitor.</td></tr> <tr><td>02h</td><td>Restore factory defaults for current mode. If no factory
     * defaults then restore user values for current mode.</td></tr> </tbody></table>
     * <p>
     * All other values are reserved and must be ignored.
     */
    SETTINGS(0xB0, VCPCodeType.WRITE_ONLY, VCPCodeFunction.NON_CONTINOUS, new int[]{0x01, 0x02}),
    /**
     * Allows the display to specify the minimum increment in which it can adjust the color temperature.
     * <p>
     * This will be used in conjunction with VCP code 0Ch, Color temperature request.
     * <p>
     * Values of 0 and &gt; 5000 are invalid and must be ignored.
     */
    COLOR_TEMPERATURE_INCREMENT(0x0B, VCPCodeType.READ_ONLY, VCPCodeFunction.NON_CONTINOUS, Helper.POSITIVE_TWO_BYTES),
    /**
     * Allows a specified color temperature (in K) to be requested. If display is unable to achieve requested color
     * temperature, then it should move to the closest possible temperature.
     * <p>
     * A value of 0 must be treated as a request for a color temperature of 3000 K. Values greater than 0 must be used
     * as a multiplier of the color temperature increment (read using VCP 0Bh) and the result added to the base value of
     * 3000 K
     * <p>
     * <u>Example:</u>
     * <p>
     * If VCP 0Bh returns a value of 50 K and VCP code 0Ch sends a value of 50 (decimal) then the display must interpret
     * this as a request to adjust the color temperature to 5500 K
     * <p>
     * (3000 + (50 * 50)) K = 5500 K
     * <p>
     * <b><u>Notes:</u></b>
     * <p>
     * <ol> <li>Applications using this function are recommended to read the actual color temperature after using this
     * command and taking appropriate action.</li> <li>This control is only recommended if the display can produce a
     * continuously (at defined increment, see VCP code 0Bh) variable color temperature.</li> </ol>
     */
    COLOR_TEMPERATURE_REQUEST(0x0C, VCPCodeType.READ_WRITE, VCPCodeFunction.CONTINOUS, Helper.POSITIVE_TWO_BYTES),
    /**
     * Increasing (decreasing) this value will increase (decrease) the video sampling clock frequency
     */
    CLOCK(0x0E, VCPCodeType.READ_WRITE, VCPCodeFunction.CONTINOUS, Helper.POSITIVE_TWO_BYTES),
    /**
     * Increasing (decreasing) this value will increase (decrease) the Luminance of the image.
     */
    LUMINANCE(0x10, VCPCodeType.READ_WRITE, VCPCodeFunction.CONTINOUS, Helper.POSITIVE_TWO_BYTES),
    /**
     * Increasing (decreasing) this value will increase (decrease) the Contrast of the image.
     * <p>
     * <b><u>Notes:</u></b>
     * <p>
     * <ol> <li>The actual range of contrast over which this control applies is defined by the manufacturer.</li>
     * <li>Care should be taken to avoid the situation where the contrast ratio approaches 0 â€¦ this may be
     * non-recoverable since user will not be able to see the image.</li> </ol>
     */
    CONTRAST(0x12, VCPCodeType.READ_WRITE, VCPCodeFunction.CONTINOUS, Helper.POSITIVE_TWO_BYTES),
    /**
     * Increasing (decreasing) this value will increase (decrease) the luminance of red pixels.
     * <p>
     * The value returned must be an indication of the actual red gain at the current color temperature and not be
     * normalized.
     */
    VIDEO_GAIN_DRIVE_RED(0x16, VCPCodeType.READ_WRITE, VCPCodeFunction.CONTINOUS, Helper.POSITIVE_TWO_BYTES),
    /**
     * Increasing (decreasing) this value will increase (decrease) the degree of compensation.
     * <p>
     * <b><u>Note:</u></b>
     * <p>
     * This is intended to help user suffering from the form of color deficiency in which red colors are poorly seen.
     */
    USER_COLOR_VISION_COMPENSATION(0x17, VCPCodeType.READ_WRITE, VCPCodeFunction.CONTINOUS, Helper.POSITIVE_TWO_BYTES),
    /**
     * Increasing (decreasing) this value will increase (decrease) the luminance of green pixels.
     * <p>
     * The value returned must be an indication of the actual green gain at the current color temperature and not be
     * normalized.
     */
    VIDEO_GAIN_DRIVE_GREEN(0x18, VCPCodeType.READ_WRITE, VCPCodeFunction.CONTINOUS, Helper.POSITIVE_TWO_BYTES),
    /**
     * Increasing (decreasing) this value will increase (decrease) the luminance of blue pixels.
     * <p>
     * The value returned must be an indication of the actual blue gain at the current color temperature and not be
     * normalized.
     */
    VIDEO_GAIN_DRIVE_BLUE(0x1A, VCPCodeType.READ_WRITE, VCPCodeFunction.CONTINOUS, Helper.POSITIVE_TWO_BYTES),
    /**
     * Increasing (decreasing) this value will adjust the focus of the image.
     */
    FOCUS(0x1C, VCPCodeType.READ_WRITE, VCPCodeFunction.CONTINOUS, Helper.POSITIVE_TWO_BYTES),
    /**
     * Perform auto setup function (H/V position, clock, clock phase, A/D converter, etc)
     * <p>
     * <table> <thead><tr><th>Byte: SL</th><th>Description</th></tr></thead> <tbody> <tr><td>00h</td><td>Auto setup is
     * not active</td></tr> <tr><td>01h</td><td>Perform / performing auto setup</td></tr> <tr><td>02h</td><td>Enable
     * continuous / periodic auto setup</td></tr> <tr><td>&gt;= 03h</td><td>Reserved, must be ignored</td></tr>
     * </tbody></table>
     * <p>
     * <b><u>Note:</u></b>
     * <p>
     * A value of 02h (when supported) must cause the display to either continuously or periodically (event or timer
     * driven) perform an auto setup. Cancel by writing a value of either 01h or 00h.
     */
    AUTO_SETUP(0x1E, VCPCodeType.READ_WRITE, VCPCodeFunction.NON_CONTINOUS, new int[]{0x00, 0x01, 0x02}),
    /**
     * Perform auto color setup function (R / G / B gain and offset, A/D setup, etc.)
     * <p>
     * <table> <thead><tr><th>Byte: SL</th><th>Description</th></tr></thead> <tbody> <tr><td>00h</td><td>Auto color
     * setup is not active</td></tr> <tr><td>01h</td><td>Perform / performing auto color setup</td></tr>
     * <tr><td>02h</td><td>Enable continuous / periodic auto color setup</td></tr> <tr><td>&gt;= 03h</td><td>Reserved,
     * must be ignored</td></tr> </tbody></table>
     * <p>
     * <b><u>Note:</u></b>
     * <p>
     * A value of 02h (when supported) must cause the display to either continuously or periodically (event or timer
     * driven) perform an auto color setup. Cancel by writing a value of either 01h or 00h.
     */
    AUTO_COLOR_SETUP(0x1F, VCPCodeType.READ_WRITE, VCPCodeFunction.NON_CONTINOUS, new int[]{0x00, 0x01, 0x02}),
    /**
     * Increasing (decreasing) this value will increase (decrease) the phase shift of the sampling clock.
     */
    CLOCK_PHASE(0x3E, VCPCodeType.READ_WRITE, VCPCodeFunction.CONTINOUS, Helper.POSITIVE_TWO_BYTES),
    /**
     * Increasing (decreasing) this value controls the horizontal picture moirÃ© cancellation.
     */
    HORIZONTAL_MOIRE(0x56, VCPCodeType.READ_WRITE, VCPCodeFunction.CONTINOUS, Helper.POSITIVE_TWO_BYTES),
    /**
     * Increasing (decreasing) this value controls the vertical picture moirÃ© cancellation.
     */
    VERTICAL_MOIRE(0x58, VCPCodeType.READ_WRITE, VCPCodeFunction.CONTINOUS, Helper.POSITIVE_TWO_BYTES),
    /**
     * Adjust the red saturation for 6-axis color
     * <p>
     * <table> <thead><tr><th>Byte: SL</th><th>Description</th></tr></thead> <tbody> <tr><td>&gt; 7Fh</td><td>Causes an
     * increase in red saturation</td></tr> <tr><td>7Fh</td><td>The nominal (default) value</td></tr> <tr><td>&lt;
     * 7Fh</td><td>Causes a decrease in red saturation</td></tr> </tbody></table>
     * <p>
     * If set = 7fh then display must make no change to the red saturation of the incoming signal. If set != 7Fh, then
     * writing a value = 7Fh must cause the display to return to its nominal (default) setting for red saturation. The
     * +- 7Fh range must be linearly mapped to the actual adjustment range.
     */
    SIX_AXIS_SATURATION_CONTROL_RED(0x59, VCPCodeType.READ_WRITE, VCPCodeFunction.CONTINOUS, Helper.POSITIVE_TWO_BYTES),
    /**
     * Adjust the yellow saturation for 6-axis color
     * <p>
     * <table> <thead><tr><th>Byte: SL</th><th>Description</th></tr></thead> <tbody> <tr><td>&gt; 7Fh</td><td>Causes an
     * increase in yellow saturation</td></tr> <tr><td>7Fh</td><td>The nominal (default) value</td></tr> <tr><td>&lt;
     * 7Fh</td><td>Causes a decrease in yellow saturation</td></tr> </tbody></table>
     * <p>
     * If set = 7fh then display must make no change to the yellow saturation of the incoming signal. If set != 7Fh,
     * then writing a value = 7Fh must cause the display to return to its nominal (default) setting for yellow
     * saturation. The +- 7Fh range must be linearly mapped to the actual adjustment range.
     */
    SIX_AXIS_SATURATION_CONTROL_YELLOW(0x5A, VCPCodeType.READ_WRITE, VCPCodeFunction.CONTINOUS, Helper
            .POSITIVE_TWO_BYTES),
    /**
     * Adjust the green saturation for 6-axis color
     * <p>
     * <table> <thead><tr><th>Byte: SL</th><th>Description</th></tr></thead> <tbody> <tr><td>&gt; 7Fh</td><td>Causes an
     * increase in green saturation</td></tr> <tr><td>7Fh</td><td>The nominal (default) value</td></tr> <tr><td>&lt;
     * 7Fh</td><td>Causes a decrease in green saturation</td></tr> </tbody></table>
     * <p>
     * If set = 7fh then display must make no change to the green saturation of the incoming signal. If set != 7Fh, then
     * writing a value = 7Fh must cause the display to return to its nominal (default) setting for green saturation. The
     * +- 7Fh range must be linearly mapped to the actual adjustment range.
     */
    SIX_AXIS_SATURATION_CONTROL_GREEN(0x5B, VCPCodeType.READ_WRITE, VCPCodeFunction.CONTINOUS, Helper
            .POSITIVE_TWO_BYTES),
    /**
     * Adjust the cyan saturation for 6-axis color
     * <p>
     * <table> <thead><tr><th>Byte: SL</th><th>Description</th></tr></thead> <tbody> <tr><td>&gt; 7Fh</td><td>Causes an
     * increase in cyan saturation</td></tr> <tr><td>7Fh</td><td>The nominal (default) value</td></tr> <tr><td>&lt;
     * 7Fh</td><td>Causes a decrease in cyan saturation</td></tr> </tbody></table>
     * <p>
     * If set = 7fh then display must make no change to the cyan saturation of the incoming signal. If set != 7Fh, then
     * writing a value = 7Fh must cause the display to return to its nominal (default) setting for cyan saturation. The
     * +- 7Fh range must be linearly mapped to the actual adjustment range.
     */
    SIX_AXIS_SATURATION_CONTROL_CYAN(0x5C, VCPCodeType.READ_WRITE, VCPCodeFunction.CONTINOUS, Helper
            .POSITIVE_TWO_BYTES),
    /**
     * Adjust the blue saturation for 6-axis color
     * <p>
     * <table> <thead><tr><th>Byte: SL</th><th>Description</th></tr></thead> <tbody> <tr><td>&gt; 7Fh</td><td>Causes an
     * increase in blue saturation</td></tr> <tr><td>7Fh</td><td>The nominal (default) value</td></tr> <tr><td>&lt;
     * 7Fh</td><td>Causes a decrease in blue saturation</td></tr> </tbody></table>
     * <p>
     * If set = 7fh then display must make no change to the blue saturation of the incoming signal. If set != 7Fh, then
     * writing a value = 7Fh must cause the display to return to its nominal (default) setting for blue saturation. The
     * +- 7Fh range must be linearly mapped to the actual adjustment range.
     */
    SIX_AXIS_SATURATION_CONTROL_BLUE(0x5D, VCPCodeType.READ_WRITE, VCPCodeFunction.CONTINOUS, Helper
            .POSITIVE_TWO_BYTES),
    /**
     * Adjust the magenta saturation for 6-axis color
     * <p>
     * <table> <thead><tr><th>Byte: SL</th><th>Description</th></tr></thead> <tbody> <tr><td>&gt; 7Fh</td><td>Causes an
     * increase in magenta saturation</td></tr> <tr><td>7Fh</td><td>The nominal (default) value</td></tr> <tr><td>&lt;
     * 7Fh</td><td>Causes a decrease in magenta saturation</td></tr> </tbody></table>
     * <p>
     * If set = 7fh then display must make no change to the magenta saturation of the incoming signal. If set != 7Fh,
     * then writing a value = 7Fh must cause the display to return to its nominal (default) setting for magenta
     * saturation. The +- 7Fh range must be linearly mapped to the actual adjustment range.
     */
    SIX_AXIS_SATURATION_CONTROL_MAGENTA(0x5E, VCPCodeType.READ_WRITE, VCPCodeFunction.CONTINOUS, Helper
            .POSITIVE_TWO_BYTES),
    /**
     * Increasing (decreasing) this value will increase (decrease) the black level of the red video.
     */
    VIDEO_BLACK_LEVEL_RED(0x6C, VCPCodeType.READ_WRITE, VCPCodeFunction.CONTINOUS, Helper.POSITIVE_TWO_BYTES),
    /**
     * Increasing (decreasing) this value will increase (decrease) the black level of the green video.
     */
    VIDEO_BLACK_LEVEL_GREEN(0x6E, VCPCodeType.READ_WRITE, VCPCodeFunction.CONTINOUS, Helper.POSITIVE_TWO_BYTES),
    /**
     * Increasing (decreasing) this value will increase (decrease) the black level of the blue video.
     */
    VIDEO_BLACK_LEVEL_BLUE(0x70, VCPCodeType.READ_WRITE, VCPCodeFunction.CONTINOUS, Helper.POSITIVE_TWO_BYTES),
    /**
     * Increasing (decreasing) this value will increase (decrease) the zoom function of the projection lens.
     */
    ADJUST_ZOOM(0x7C, VCPCodeType.READ_WRITE, VCPCodeFunction.CONTINOUS, Helper.POSITIVE_TWO_BYTES),
    /**
     * Allows one of a range of algorithms to be selected to suit the type of image being displayed and/or personal
     * preference.
     * <p>
     * Increasing (decreasing) the value must increase (decrease) the edge sharpness of image features.
     */
    SHARPNESS(0x87, VCPCodeType.READ_WRITE, VCPCodeFunction.CONTINOUS, Helper.POSITIVE_TWO_BYTES),
    /**
     * Increasing (decreasing) this value will increase (decrease) the velocity modulation of the horizontal scan as a
     * function of a change in the luminance level.
     */
    VELOCITY_SCAN_MODULATION(0x88, VCPCodeType.READ_WRITE, VCPCodeFunction.CONTINOUS, Helper.POSITIVE_TWO_BYTES),
    /**
     * Increasing this control increases the amplitude of the color difference components of the video signal.
     * <p>
     * The result is an increase in the amount of pure color relative to white in the video. This control applies to the
     * currently active interface.
     */
    COLOR_SATURATION(0x8A, VCPCodeType.READ_WRITE, VCPCodeFunction.CONTINOUS, Helper.POSITIVE_TWO_BYTES),
    /**
     * Increasing this control increases the amplitude of the high frequency components of the video signal.
     * <p>
     * This allows fine details to be accentuated. This control does not affect the RGB input, only the TV video
     * inputs.
     */
    TV_SHARPNESS(0x8C, VCPCodeType.READ_WRITE, VCPCodeFunction.CONTINOUS, Helper.POSITIVE_TWO_BYTES),
    /**
     * Increasing (decreasing) this control increases (decreases) the ratio between whites and blacks in the video.
     * <p>
     * This control does not affect the RGB input, only the TV video inputs.
     */
    TV_CONTRAST(0x8E, VCPCodeType.READ_WRITE, VCPCodeFunction.CONTINOUS, Helper.POSITIVE_TWO_BYTES),
    /**
     * Also known as tint
     * <p>
     * Increasing (decreasing) this control increases (decreases) the wavelength of the color component of the video
     * signal.
     * <p>
     * The result is a shift towards red (blue) in the hue of all colors. This control applies to the currently active
     * interface.
     */
    HUE(0x90, VCPCodeType.READ_WRITE, VCPCodeFunction.CONTINOUS, Helper.POSITIVE_TWO_BYTES),
    /**
     * Increasing this control increases the black level of the video, resulting in an increase of the luminance level
     * of the video.
     * <p>
     * A value of zero represents the darkest level possible.
     * <p>
     * This control does not affect the RGB input, only the TV video inputs.
     */
    TV_BLACK_LEVEL_LUMINANCE(0x92, VCPCodeType.READ_WRITE, VCPCodeFunction.CONTINOUS, Helper.POSITIVE_TWO_BYTES),
    /**
     * Changes the contrast ratio between the area of the window and the rest of the desktop
     * <p>
     * Lower (higher) values will cause the desktop luminance to decrease (increase)
     * <p>
     * <b><u>Notes:</u></b>
     * <p>
     * <ol> <li>This VCP code should be used in conjunction with VCP 99h</li> <li>This command structure is not
     * recommended for new designs, see VCP A5h for alternate.</li> </ol>
     */
    WINDOW_BACKGROUND(0x9A, VCPCodeType.READ_WRITE, VCPCodeFunction.CONTINOUS, Helper.POSITIVE_TWO_BYTES),
    /**
     * Adjust the red hue for 6-axis color
     * <p>
     * <table> <thead><tr><th>Byte: SL</th><th>Description</th></tr></thead> <tbody> <tr><td>&gt; 7Fh</td><td>Causes an
     * increase in red hue</td></tr> <tr><td>7Fh</td><td>The nominal (default) value</td></tr> <tr><td>&lt;
     * 7Fh</td><td>Causes a decrease in red hue</td></tr> </tbody></table>
     * <p>
     * If set = 7fh then display must make no change to the red hue of the incoming signal. If set != 7Fh, then writing
     * a value = 7Fh must cause the display to return to its nominal (default) setting for red hue. The +- 7Fh range
     * must be linearly mapped to the actual adjustment range.
     */
    SIX_AXIS_HUE_CONTROL_RED(0x9B, VCPCodeType.READ_WRITE, VCPCodeFunction.CONTINOUS, Helper.POSITIVE_TWO_BYTES),
    /**
     * Adjust the yellow hue for 6-axis color
     * <p>
     * <table> <thead><tr><th>Byte: SL</th><th>Description</th></tr></thead> <tbody> <tr><td>&gt; 7Fh</td><td>Causes an
     * increase in yellow hue</td></tr> <tr><td>7Fh</td><td>The nominal (default) value</td></tr> <tr><td>&lt;
     * 7Fh</td><td>Causes a decrease in yellow hue</td></tr> </tbody></table>
     * <p>
     * If set = 7fh then display must make no change to the yellow hue of the incoming signal. If set != 7Fh, then
     * writing a value = 7Fh must cause the display to return to its nominal (default) setting for yellow hue. The +-
     * 7Fh range must be linearly mapped to the actual adjustment range.
     */
    SIX_AXIS_HUE_CONTROL_YELLOW(0x9C, VCPCodeType.READ_WRITE, VCPCodeFunction.CONTINOUS, Helper.POSITIVE_TWO_BYTES),
    /**
     * Adjust the green hue for 6-axis color
     * <p>
     * <table> <thead><tr><th>Byte: SL</th><th>Description</th></tr></thead> <tbody> <tr><td>&gt; 7Fh</td><td>Causes an
     * increase in green hue</td></tr> <tr><td>7Fh</td><td>The nominal (default) value</td></tr> <tr><td>&lt;
     * 7Fh</td><td>Causes a decrease in green hue</td></tr> </tbody></table>
     * <p>
     * If set = 7fh then display must make no change to the green hue of the incoming signal. If set != 7Fh, then
     * writing a value = 7Fh must cause the display to return to its nominal (default) setting for green hue. The +- 7Fh
     * range must be linearly mapped to the actual adjustment range.
     */
    SIX_AXIS_HUE_CONTROL_GREEN(0x9D, VCPCodeType.READ_WRITE, VCPCodeFunction.CONTINOUS, Helper.POSITIVE_TWO_BYTES),
    /**
     * Adjust the cyan hue for 6-axis color
     * <p>
     * <table> <thead><tr><th>Byte: SL</th><th>Description</th></tr></thead> <tbody> <tr><td>&gt; 7Fh</td><td>Causes an
     * increase in cyan hue</td></tr> <tr><td>7Fh</td><td>The nominal (default) value</td></tr> <tr><td>&lt;
     * 7Fh</td><td>Causes a decrease in cyan hue</td></tr> </tbody></table>
     * <p>
     * If set = 7fh then display must make no change to the cyan hue of the incoming signal. If set != 7Fh, then writing
     * a value = 7Fh must cause the display to return to its nominal (default) setting for cyan hue. The +- 7Fh range
     * must be linearly mapped to the actual adjustment range.
     */
    SIX_AXIS_HUE_CONTROL_CYAN(0x9E, VCPCodeType.READ_WRITE, VCPCodeFunction.CONTINOUS, Helper.POSITIVE_TWO_BYTES),
    /**
     * Adjust the blue hue for 6-axis color
     * <p>
     * <table> <thead><tr><th>Byte: SL</th><th>Description</th></tr></thead> <tbody> <tr><td>&gt; 7Fh</td><td>Causes an
     * increase in blue hue</td></tr> <tr><td>7Fh</td><td>The nominal (default) value</td></tr> <tr><td>&lt;
     * 7Fh</td><td>Causes a decrease in blue hue</td></tr> </tbody></table>
     * <p>
     * If set = 7fh then display must make no change to the blue hue of the incoming signal. If set != 7Fh, then writing
     * a value = 7Fh must cause the display to return to its nominal (default) setting for blue hue. The +- 7Fh range
     * must be linearly mapped to the actual adjustment range.
     */
    SIX_AXIS_HUE_CONTROL_BLUE(0x9F, VCPCodeType.READ_WRITE, VCPCodeFunction.CONTINOUS, Helper.POSITIVE_TWO_BYTES),
    /**
     * Adjust the magenta hue for 6-axis color
     * <p>
     * <table> <thead><tr><th>Byte: SL</th><th>Description</th></tr></thead> <tbody> <tr><td>&gt; 7Fh</td><td>Causes an
     * increase in magenta hue</td></tr> <tr><td>7Fh</td><td>The nominal (default) value</td></tr> <tr><td>&lt;
     * 7Fh</td><td>Causes a decrease in magenta hue</td></tr> </tbody></table>
     * <p>
     * If set = 7fh then display must make no change to the magenta hue of the incoming signal. If set != 7Fh, then
     * writing a value = 7Fh must cause the display to return to its nominal (default) setting for magenta hue. The +-
     * 7Fh range must be linearly mapped to the actual adjustment range.
     */
    SIX_AXIS_HUE_CONTROL_MAGENTA(0xA0, VCPCodeType.READ_WRITE, VCPCodeFunction.CONTINOUS, Helper.POSITIVE_TWO_BYTES),
    /**
     * Turn on / off the auto setup function (periodic or event driven).
     * <p>
     * <table> <thead><tr><th>Byte: SL</th><th>Description</th></tr></thead> <tbody> <tr><td>00h</td><td>Reserved, must
     * be ignored</td></tr> <tr><td>01h</td><td>Turn auto setup off</td></tr> <tr><td>02h</td><td>Turn auto setup
     * on</td></tr> <tr><td>&gt;= 03h</td><td>Reserved, must be ignored</td></tr> </tbody></table>
     */
    AUTO_SETUP_ON_OFF(0xA2, VCPCodeType.WRITE_ONLY, VCPCodeFunction.NON_CONTINOUS, Helper.POSITIVE_TWO_BYTES),
    /**
     * Indicates the orientation of the screen
     * <p>
     * <table> <thead><tr><th>Byte: SL</th><th>Description</th></tr></thead> <tbody>
     * <tr><td>00h</td><td>Reserved</td><td>Shall be ignored</td></tr> <tr><td>01h</td><td>0 degrees</td><td>The normal
     * landscape mode</td></tr> <tr><td>02h</td><td>90 degrees</td><td>Portrait mode achieved by clockwise rotation of
     * the display 90 degrees.</td></tr> <tr><td>03h</td><td>180 degrees</td><td>Landscape mode achieved by rotation of
     * the display 180 degrees.</td></tr> <tr><td>04h</td><td>270 degrees</td><td>Portrait mode achieved by clockwise
     * rotation of the display 270 degrees.</td></tr> <tr><td>05h to FEh</td><td>Reserved</td><td>Shall be
     * ignored</td></tr> <tr><td>FFh</td><td>Not applicable</td><td>Indicates that the display cannot supply the current
     * orientation</td></tr> </tbody></table>
     */
    SCREEN_ORIENTATION(0xAA, VCPCodeType.READ_ONLY, VCPCodeFunction.NON_CONTINOUS, Helper.POSITIVE_TWO_BYTES),
    /**
     * Permits the selection of a preset optimized by manufacturer for an application type or the selection of a user
     * defined setting.
     * <p>
     * <table> <thead><tr><th>Byte: SL</th><th>Description</th></tr></thead> <tbody> <tr><td>00h</td><td>Stand / default
     * mode</td></tr> <tr><td>01h</td><td>Productivity (e.g. office applications)</td></tr> <tr><td>02h</td><td>Mixed
     * (e.g. internet with mix of text and images)</td></tr> <tr><td>03h</td><td>Movie</td></tr>
     * <tr><td>04h</td><td>User defined</td></tr> <tr><td>05h</td><td>Games (e.g. games console / PC game)</td></tr>
     * <tr><td>06h</td><td>Sports (e.g. fast action)</td></tr> <tr><td>07h</td><td>Professional (all signal processing
     * disabled)</td></tr> <tr><td>08h</td><td>Standard / default mode with intermediate power consumption</td></tr>
     * <tr><td>09h</td><td>Standard / default mode with low power consumption</td></tr>
     * <tr><td>0Ah</td><td>Demonstration (used for high visual impact in retail etc)</td></tr> <tr><td>0Bh -
     * EFh</td><td>Reserved, must be ignored</td></tr> <tr><td>F0h</td><td>Dynamic contrast</td></tr> <tr><td>&gt;=
     * F1h</td><td>Reserved, must be ignored</td></tr> </tbody></table>
     */
    DISPLAY_APPLICATION(0xDC, VCPCodeType.READ_WRITE, VCPCodeFunction.NON_CONTINOUS, new int[]{0x00, 0x01, 0x02,
            0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0xF0});

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

    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * @return the code type
     */
    public VCPCodeType getCodeType() {
        return codeType;
    }

    /**
     * @return the code function
     */
    public VCPCodeFunction getCodeFunction() {
        return codeFunction;
    }

    /**
     * Checks if the supplied value is legal. This is meant to be used before transmitting a value to a monitor and have
     * it fail.
     *
     * @param value the value to check
     * @return <code>true</code> if the value is legal/accepted, otherwise <code>false</code>
     */
    public boolean isValueLegal(int value) {
        return isLegal.test(value);
    }

    /**
     * Get a {@link VCPCode} by the integer-value of the code.
     *
     * @param code the code
     * @return an optional that either contains the {@link VCPCode} if it was found, or that is empty
     */
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
