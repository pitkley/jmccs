package de.pitkley.jmccs.monitor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class consists exclusively of static methods that operate on a <i>capability string</i> as defined by the VESA
 * Monitor Control Command Set (MCCS) Standard Version 3.
 * <p>
 * It allows you to parse a capability string returned by a monitor into a more friendly format.
 */
public class CapabilityStringParser {

    private static final Pattern HEADER = Pattern.compile("(?<header>\\w+)\\((?<value>.+?)\\)(?=$|[\\w\\)])");

    /**
     * Converts the given {@link File} to a {@link String} and executes {@link CapabilityStringParser#parse(String)}.
     *
     * @param f a file that contains the capability string
     * @return a {@link Map} mapping from a header to one or multiple values (that are still formatted as
     * space-separated strings
     * @throws IOException if the file was not found
     */
    public static Map<String, String> parse(File f) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(f));
        StringBuilder capabilityString = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            capabilityString.append(line);
        }
        return parse(capabilityString.toString());
    }

    /**
     * Converts the given {@link String} to a more friendly {@link Map}, mapping the capability-headers to values (which
     * are in turn space-separated strings).
     *
     * @param s the capability-string
     * @return a {@link Map} mapping from a header to one or multiple values (that are still formatted as
     * space-separated strings)
     */
    public static Map<String, String> parse(String s) {
        Map<String, String> values = new HashMap<>();
        Matcher m = HEADER.matcher(s);
        while (m.find()) {
            values.put(m.group("header"), m.group("value"));
        }
        return values;
    }
}
