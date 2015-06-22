package de.pitkley.jmccs.gen;

import org.json.JSONArray;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This class generates a plain-text version of the {@link de.pitkley.jmccs.monitor.VCPCode} entries which in turn can
 * be copied into the Java-file. It is uses the JSON-file <code>vcp.json</code>, which is based on the VESA Monitor
 * Command Control Set (MCCS) Standard Version 3.
 */
public class GenerateEnumEntries {
    private static final Map<String, String> CODE_TYPE_MAP = new HashMap<>();
    private static final Map<String, String> CODE_FUNCTION_MAP = new HashMap<>();
    private static final Map<String, String> IS_LEGAL_MAP = new HashMap<>();

    static {
        CODE_TYPE_MAP.put("RO", "VCPCodeType.READ_ONLY");
        CODE_TYPE_MAP.put("WO", "VCPCodeType.WRITE_ONLY");
        CODE_TYPE_MAP.put("RW", "VCPCodeType.READ_WRITE");

        CODE_FUNCTION_MAP.put("C", "VCPCodeFunction.CONTINOUS");
        CODE_FUNCTION_MAP.put("NC", "VCPCodeFunction.NON_CONTINOUS");
        CODE_FUNCTION_MAP.put("T", "VCPCodeFunction.TABLE");

        IS_LEGAL_MAP.put("POSITIVE", "Helper.POSITIVE");
        IS_LEGAL_MAP.put("TWO_BYTES", "Helper.TWO_BYTES");
        IS_LEGAL_MAP.put("POSITIVE_TWO_BYTES", "Helper.POSITIVE_TWO_BYTES");
    }

    /**
     * This class is supposed to be executed manually. It outputs a plain-text version of Java-code that is meant to be
     * copied into {@link de.pitkley.jmccs.monitor.VCPCode}.
     *
     * @param args the command-line args are ignored
     * @throws URISyntaxException if the file <code>vcp.json</code> is not found
     * @throws IOException        if the file <code>vcp.json</code> is not found
     */
    public static void main(String[] args) throws URISyntaxException, IOException {
        String vcpJsonString = new String(Files.readAllBytes(Paths.get(GenerateEnumEntries.class.getClassLoader()
                .getResource("vcp.json").toURI())));
        JSONArray vcpJson = new JSONArray(vcpJsonString);

        String enumString = IntStream.range(0, vcpJson.length()).mapToObj(vcpJson::getJSONObject).map((j -> {
            // Build javadoc
            StringBuilder sb = new StringBuilder();
            sb.append("/**\n");

            JSONArray description = j.getJSONArray("description");
            IntStream.range(0, description.length()).mapToObj(description::getString).forEach((s -> {
                s = s.replaceAll("\n", "\n * ");
                sb.append(" * ").append(s).append("\n *\n");
            }));
            sb.append(" */\n");

            // Start of enum-definition
            sb.append(transformVCPCodeName(j.getString("vcp_code_name"))).append("(");
            sb.append(transformHexCode(j.getString("code"))).append(", ");
            sb.append(CODE_TYPE_MAP.get(j.getString("type"))).append(", ");
            sb.append(CODE_FUNCTION_MAP.get(j.getString("function"))).append(", ");

            // Values/Is-Legal helper
            JSONArray values = j.optJSONArray("values");
            if (values != null) {
                sb.append("new int[]{");
                String s = IntStream.range(0, values.length()).mapToObj(values::getString).map
                        (GenerateEnumEntries::transformHexCode).collect(Collectors.joining(", "));
                sb.append(s).append("}");
            } else {
                sb.append(IS_LEGAL_MAP.get(j.getString("values")));
            }
            sb.append(")");
            return sb.toString();
        })).collect(Collectors.joining(",\n"));
        enumString += ";";
        System.out.println(enumString);
    }

    protected static String transformVCPCodeName(String vcpCodeName) {
        return vcpCodeName.trim().toUpperCase().replaceAll("[^a-zA-Z0-9\\s]", " ").replaceAll(" +", "_");
    }

    protected static String transformHexCode(String hexCode) {
        return "0x" + hexCode.replace("h", "");
    }
}
