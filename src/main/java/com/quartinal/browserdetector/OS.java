package com.quartinal.browserdetector;

public class OS {
    private final String name;
    private final String version;
    private final String majorVersion;

    public OS(String name, String version) {
        this.name = name;
        this.version = version;
        this.majorVersion = extractMajorVersion(version);
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getMajorVersion() {
        return majorVersion;
    }
    
    public boolean is(String osName) {
        return name.equalsIgnoreCase(osName);
    }

    private String extractMajorVersion(String version) {
        int dotIndex = version.indexOf('.');
        return dotIndex == -1 ? version : version.substring(0, dotIndex);
    }

    @Override
    public String toString() {
        return name + " " + version;
    }
}