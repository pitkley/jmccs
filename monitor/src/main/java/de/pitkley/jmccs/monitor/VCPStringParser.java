package de.pitkley.jmccs.monitor;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * This class consists exclusively of static methods that operate on a <i>VCP string</i> as defined by the VESA Monitor
 * Control Command Set (MCCS) Standard Version 3.
 * <p>
 * It allows you to parse the VCP-string, that is part of the capability string (see {@link CapabilityStringParser}),
 * into a more friendly format.
 */
public class VCPStringParser {

    private static final Pattern VALUE = Pattern.compile("(?<code>\\w+)(\\((?<values>.*?)\\))?");

    /**
     * Converts the given {@link String} to a more friendly {@link Map}, mapping the supported VCP-codes to a {@link
     * Set} of values (if there are any).
     *
     * @param vcpString the VCP-string
     * @return a {@link Map} mapping the supported VCP-codes to a {@link Optional} {@link Set} of values (if there are
     * any)
     */
    public static Map<VCPCode, Optional<Set<Integer>>> parse(String vcpString) {
        Map<VCPCode, Optional<Set<Integer>>> codes = new HashMap<>();
        Matcher matcher = VALUE.matcher(vcpString);
        while (matcher.find()) {
            Optional<VCPCode> vcpCodeOptional = VCPCode.get(Integer.valueOf(matcher.group("code"), 16));
            if (!vcpCodeOptional.isPresent()) {
                continue;
            }
            VCPCode vcpCode = vcpCodeOptional.get();

            String valuesString = matcher.group("values");
            Optional<Set<Integer>> values;
            if (valuesString != null) {
                values = Optional.of(Arrays.asList(valuesString.split(" ")).stream().map((e) -> Integer.parseInt(e,
                        16)).collect(Collectors.toSet()));
            } else {
                values = Optional.empty();
//                values = vcpCode.getValues();
                // TODO refactor this to work with `.isLegal`
            }

            codes.put(vcpCode, values);
        }

        return codes;
    }
}
