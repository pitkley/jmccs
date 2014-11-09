package de.pitkley.ddcci.monitor;

public class OperatingSystemIdentifier {
    private final String osName;

    public OperatingSystemIdentifier() {
        osName = System.getProperty("os.name").toLowerCase();
    }

    public String getOsName() {
        return osName;
    }

    public boolean isWindows() {
        return osName.contains("win");
    }

    public boolean isOSX() {
        return osName.contains("mac");
    }

    public boolean isUnix() {
        return osName.contains("nix") || osName.contains("nux");
    }
}
