package de.pitkley.jmccs.monitor;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class VCPStringParser {

    private static final Pattern VALUE = Pattern.compile("(?<code>\\w+)(\\((?<values>.*?)\\))?");

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
                values = Optional.of(Arrays.asList(valuesString.split(" "))
                        .stream()
                        .map((e) -> Integer.parseInt(e, 16))
                        .collect(Collectors.toSet()));
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
