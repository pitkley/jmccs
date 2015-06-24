package de.pitkley.jmccs.gen;

import com.google.common.io.CharStreams;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import de.pitkley.jmccs.monitor.Monitor;
import de.pitkley.jmccs.monitor.VCPCode;
import de.pitkley.jmccs.monitor.VCPCodeType;
import de.pitkley.jmccs.monitor.VCPReply;
import org.json.JSONArray;

import javax.lang.model.element.Modifier;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;

/**
 * This class generates the convenience- or helper-class <code>MonitorHelper</code>. It makes use of the pre-supplied
 * JSON-file <code>vcp.json</code> which contains the definitions for the VCP-codes including descriptions. The contents
 * of the JSON-file are taken from the VESA Monitor Control Command Set (MCCS) Standard Version 3.
 */
public class GenerateConvenienceClass {

    private static void generateMonitorHelper(Path path) throws IOException, URISyntaxException {
        TypeSpec.Builder monitorHelperBuilder = TypeSpec.classBuilder("MonitorHelper").addModifiers(Modifier.PUBLIC)
                .addAnnotation(AnnotationSpec.builder(SuppressWarnings.class).addMember("value", "$S", "unused")
                        .build()).addSuperinterface(Monitor.class).addField(Monitor.class, "monitor", Modifier
                        .PRIVATE, Modifier.FINAL);

        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addParameter(Monitor.class, "monitor")
                .addStatement("this.$L = $L", "monitor", "monitor")
                .addModifiers(Modifier.PUBLIC)
                .build();
        monitorHelperBuilder.addMethod(constructor);

        List<MethodSpec> interfaceMethods = generateInterfaceMethods();
        interfaceMethods.forEach(monitorHelperBuilder::addMethod);

        InputStream vcpStream = GenerateConvenienceClass.class.getClassLoader().getResourceAsStream("vcp.json");
        try (InputStreamReader isr = new InputStreamReader(vcpStream);
             BufferedReader br = new BufferedReader(isr)) {
            String vcpJsonString = CharStreams.toString(br);
            JSONArray vcpJson = new JSONArray(vcpJsonString);
            IntStream.range(0, vcpJson.length())
                    .mapToObj(vcpJson::getJSONObject)
                    .forEach((j -> {
                        String vcpCodeName = j.getString("vcp_code_name");
                        String transformedName = transformVCPCodeName(vcpCodeName);

                        VCPCode c = VCPCode.valueOf(GenerateEnumEntries.transformVCPCodeName(vcpCodeName));
                        List<MethodSpec> methods = generateMethodsForVCPCode(transformedName, c, j.getJSONArray
                                ("description"));
                        methods.forEach(monitorHelperBuilder::addMethod);
                    }));
        }

        JavaFile javaFile = JavaFile.builder("de.pitkley.jmccs.monitor", monitorHelperBuilder.build()).build();
        javaFile.writeTo(path);
    }

    private static List<MethodSpec> generateInterfaceMethods() {
        List<MethodSpec> methods = new ArrayList<>();

        methods.add(MethodSpec.methodBuilder("isMainMonitor")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(boolean.class)
                .addStatement("return $L.$L()", "monitor", "isMainMonitor")
                .build());
        methods.add(MethodSpec.methodBuilder("isVCPCodeSupported")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(boolean.class)
                .addParameter(VCPCode.class, "vcpCode")
                .addStatement("return $L.$L($L)", "monitor", "isVCPCodeSupported", "vcpCode")
                .build());
        methods.add(MethodSpec.methodBuilder("getVCPFeature")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(VCPReply.class)
                .addParameter(VCPCode.class, "vcpCode")
                .addStatement("return $L.$L($L)", "monitor", "getVCPFeature", "vcpCode")
                .build());
        methods.add(MethodSpec.methodBuilder("setVCPFeature")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(boolean.class)
                .addParameter(VCPCode.class, "vcpCode")
                .addParameter(int.class, "value")
                .addStatement("return $L.$L($L, $L)", "monitor", "setVCPFeature", "vcpCode", "value")
                .build());
        methods.add(MethodSpec.methodBuilder("isClosed")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(boolean.class)
                .addStatement("return $L.$L()", "monitor", "isClosed")
                .build());
        methods.add(MethodSpec.methodBuilder("close")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addException(IOException.class)
                .addStatement("$L.$L()", "monitor", "close")
                .build());

        return methods;
    }

    private static List<MethodSpec> generateMethodsForVCPCode(String name, VCPCode vcpCode, JSONArray description) {
        List<MethodSpec> methods = new ArrayList<>();
        if (vcpCode.getCodeType() == VCPCodeType.READ_ONLY || vcpCode.getCodeType() == VCPCodeType.READ_WRITE) {
            MethodSpec.Builder spec = MethodSpec.methodBuilder("get" + name);

            String javaDoc = IntStream.range(0, description.length())
                    .mapToObj(description::getString)
                    .collect(Collectors.joining("\n<p>\n"));
            spec.addJavadoc(javaDoc);

            spec.addJavadoc("\n\n@return the reply to the sent command\n");

            spec.addModifiers(Modifier.PUBLIC)
                    .returns(VCPReply.class)
                    .addStatement("return $L.$L($T.$L)", "monitor", "getVCPFeature", vcpCode.getClass(), vcpCode);

            methods.add(spec.build());
        }
        if (vcpCode.getCodeType() == VCPCodeType.WRITE_ONLY || vcpCode.getCodeType() == VCPCodeType.READ_WRITE) {
            MethodSpec.Builder spec = MethodSpec.methodBuilder("set" + name);

            String javaDoc = IntStream.range(0, description.length())
                    .mapToObj(description::getString)
                    .collect(Collectors.joining("\n<p>\n"));
            spec.addJavadoc(javaDoc);

            spec.addJavadoc("\n\n@param value the value to set the VCP-code to\n")
                    .addJavadoc("@return <code>true</code> if the command succeeded, otherwise <code>false</code>\n");

            spec.addModifiers(Modifier.PUBLIC)
                    .addParameter(int.class, "value")
                    .returns(boolean.class)
                    .addStatement("$T $L = $T.$L", vcpCode.getClass(), "c", vcpCode.getClass(), vcpCode)
                    .beginControlFlow("if (!$L" + ".$L($L))", "c", "isValueLegal", "value")
                    .addStatement("throw new $T(\"The value '\" + value + \"' is illegal\")",
                            IllegalStateException.class)
                    .endControlFlow()
                    .addStatement("return $L.$L($L, $L)", "monitor", "setVCPFeature", "c", "value");
            methods.add(spec.build());
        }

        return methods;
    }

    /**
     * This class is meant to be executed as a part of the maven build-process. It expects one argument, which should be
     * a path to where the generated sources should be saved to.
     *
     * @param args command line arguments, should be one argument that is the path to where the generated sources should
     *             be saved to
     * @throws URISyntaxException if the file <code>vcp.json</code> is not found
     * @throws IOException        if the file <code>vcp.json</code> is not found
     */
    public static void main(String[] args) throws URISyntaxException, IOException {
        if (args.length != 1) {
            throw new IllegalArgumentException("Missing output-path");
        }
        generateMonitorHelper(Paths.get(args[0]));
    }

    private static String transformVCPCodeName(String vcpCodeName) {
        return LOWER_UNDERSCORE.to(UPPER_CAMEL,
                vcpCodeName.trim().replaceAll("[^a-zA-Z0-9\\s]", " ").replaceAll(" +", "_").toLowerCase());
    }
}
