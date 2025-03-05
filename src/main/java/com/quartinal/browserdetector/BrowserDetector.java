package com.quartinal.browserdetector;

import org.teavm.jso.JSBody;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BrowserDetector {
    private String userAgent;
    private Browser browser;
    private OS os;
    private boolean mobile;
    private boolean tablet;
    
    // Singleton instance
    private static BrowserDetector instance;
    
    /**
     * Gets the singleton instance of BrowserDetector
     * @return The BrowserDetector instance
     */
    public static BrowserDetector getInstance() {
        if (instance == null) {
            instance = new BrowserDetector();
        }
        return instance;
    }
    
    /**
     * Creates a BrowserDetector with a custom user agent string
     * @param customUserAgent The custom user agent string to use
     * @return A BrowserDetector instance with the specified user agent
     */
    public static BrowserDetector createWithCustomUserAgent(String customUserAgent) {
        return new BrowserDetector(customUserAgent);
    }

    /**
     * Default constructor - uses the browser's actual user agent
     */
    private BrowserDetector() {
        this(getBrowserUserAgent().toLowerCase());
    }
    
    @JSBody(script = "return window.navigator.userAgent;")
    private static native String getBrowserUserAgent();

    /**
     * Custom constructor that accepts a specific user agent string
     * @param customUserAgent The user agent string to use
     */
    private BrowserDetector(String customUserAgent) {
        setUserAgent(customUserAgent);
    }
    
    /**
     * Sets a custom user agent and re-runs all detection
     * @param customUserAgent The user agent string to use
     */
    public void setUserAgent(String customUserAgent) {
        this.userAgent = customUserAgent.toLowerCase();
        initializeDetection();
    }
    
    private void initializeDetection() {
        browser = detectBrowser();
        os = detectOS();
        mobile = detectMobile();
        tablet = detectTablet();
    }

    /**
     * Returns information about the detected browser
     * @return Browser information
     */
    public Browser getBrowser() {
        return browser;
    }

    /**
     * Returns information about the detected operating system
     * @return OS information
     */
    public OS getOS() {
        return os;
    }

    /**
     * Checks if the current device is mobile
     * @return true if mobile, false otherwise
     */
    public boolean isMobile() {
        return mobile;
    }

    /**
     * Checks if the current device is a tablet
     * @return true if tablet, false otherwise
     */
    public boolean isTablet() {
        return tablet;
    }

    /**
     * Gets the raw user agent string
     * @return user agent string
     */
    public String getUserAgent() {
        return userAgent;
    }

    // The rest of the detection methods remain unchanged
    private Browser detectBrowser() {
        // Existing implementation
        String name = "unknown";
        String version = "0";
        
        // Chrome detection
        if (containsIgnoreCase(userAgent, "chrome")) {
            name = "chrome";
            version = extractVersion(userAgent, "chrome/([\\d.]+)");
            
            // Edge has Chrome in user agent
            if (containsIgnoreCase(userAgent, "edg")) {
                name = "edge";
                version = extractVersion(userAgent, "edg(?:e)?/([\\d.]+)");
            }
        } 
        // Firefox detection
        else if (containsIgnoreCase(userAgent, "firefox")) {
            name = "firefox";
            version = extractVersion(userAgent, "firefox/([\\d.]+)");
        }
        // Safari detection
        else if (containsIgnoreCase(userAgent, "safari") && !containsIgnoreCase(userAgent, "chrome")) {
            name = "safari";
            version = extractVersion(userAgent, "version/([\\d.]+)");
        }
        // IE detection
        else if (containsIgnoreCase(userAgent, "trident") || containsIgnoreCase(userAgent, "msie")) {
            name = "ie";
            if (containsIgnoreCase(userAgent, "msie")) {
                version = extractVersion(userAgent, "msie ([\\d.]+)");
            } else {
                version = extractVersion(userAgent, "rv:([\\d.]+)");
            }
        }
        // Opera detection
        else if (containsIgnoreCase(userAgent, "opera")) {
            name = "opera";
            version = containsIgnoreCase(userAgent, "version/")
                    ? extractVersion(userAgent, "version/([\\d.]+)")
                    : extractVersion(userAgent, "opera/([\\d.]+)");
        }
        
        return new Browser(name, version);
    }

    private OS detectOS() {
        // Existing implementation
        String name = "unknown";
        String version = "0";
        
        // Windows detection
        if (containsIgnoreCase(userAgent, "windows")) {
            name = "windows";
            if (containsIgnoreCase(userAgent, "windows nt")) {
                String ntVersion = extractVersion(userAgent, "windows nt ([\\d.]+)");
                switch (ntVersion) {
                    case "10.0": version = "10"; break;
                    case "6.3": version = "8.1"; break;
                    case "6.2": version = "8"; break;
                    case "6.1": version = "7"; break;
                    case "6.0": version = "Vista"; break;
                    case "5.2": case "5.1": version = "XP"; break;
                    default: version = ntVersion;
                }
            }
        }
        // macOS detection
        else if (containsIgnoreCase(userAgent, "mac os x")) {
            name = "macos";
            version = extractVersion(userAgent, "mac os x ([\\d_.]+)")
                    .replace("_", ".");
        }
        // iOS detection 
        else if (containsIgnoreCase(userAgent, "iphone") || containsIgnoreCase(userAgent, "ipad") || containsIgnoreCase(userAgent, "ipod")) {
            name = "ios";
            version = extractVersion(userAgent, "os ([\\d_]+)")
                    .replace("_", ".");
        }
        // Android detection
        else if (containsIgnoreCase(userAgent, "android")) {
            name = "android";
            version = extractVersion(userAgent, "android ([\\d.]+)");
        }
        // Linux detection
        else if (containsIgnoreCase(userAgent, "linux")) {
            name = "linux";
        }
        
        return new OS(name, version);
    }

    private boolean detectMobile() {
        return containsIgnoreCase(userAgent, "mobi") || 
               "ios".equals(os.getName()) ||
               ("android".equals(os.getName()) && !containsIgnoreCase(userAgent, "tablet"));
    }

    private boolean detectTablet() {
        return containsIgnoreCase(userAgent, "tablet") ||
               containsIgnoreCase(userAgent, "ipad") || 
               (containsIgnoreCase(userAgent, "android") && !containsIgnoreCase(userAgent, "mobi"));
    }

    private boolean containsIgnoreCase(String source, String target) {
        return source.contains(target);
    }

    private String extractVersion(String source, String pattern) {
        Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(source);
        return m.find() ? m.group(1) : "0";
    }
}