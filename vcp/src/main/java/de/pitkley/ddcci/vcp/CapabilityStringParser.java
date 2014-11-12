package de.pitkley.ddcci.vcp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CapabilityStringParser {

    private static final Pattern HEADER = Pattern.compile("(?<header>\\w+)\\((?<value>.+?)\\)(?=$|[\\w\\)])");

    public static Map<String, String> parse(File f) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(f));
        StringBuilder capabilityString = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            capabilityString.append(line);
        }
        return parse(capabilityString.toString());
    }

    public static Map<String, String> parse(String s) {
        Map<String, String> values = new HashMap<>();
        Matcher m = HEADER.matcher(s);
        while (m.find()) {
            values.put(m.group("header"), m.group("value"));
        }
        return values;
    }
}
