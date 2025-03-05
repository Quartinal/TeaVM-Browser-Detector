package com.quartinal.browserdetector;

import org.teavm.jso.JSBody;

public class FeatureDetector {

    /**
     * Checks if the browser supports touch events
     * @return true if touch events are supported
     */
    public static boolean hasTouch() {
        return detectTouch();
    }

    /**
     * Checks if the browser supports WebGL
     * @return true if WebGL is supported
     */
    public static boolean hasWebGL() {
        return detectWebGL();
    }

    /**
     * Checks if the browser supports LocalStorage
     * @return true if LocalStorage is supported
     */
    public static boolean hasLocalStorage() {
        return detectLocalStorage();
    }

    /**
     * Checks if the browser supports the Fetch API
     * @return true if Fetch API is supported
     */
    public static boolean hasFetch() {
        return detectFetch();
    }

    /**
     * Checks if the browser supports Web Workers
     * @return true if Web Workers are supported
     */
    public static boolean hasWebWorkers() {
        return detectWebWorkers();
    }

    @JSBody(script = 
        "return ('ontouchstart' in window) || " +
        "(navigator.maxTouchPoints > 0) || " +
        "(navigator.msMaxTouchPoints > 0);")
    private static native boolean detectTouch();

    @JSBody(script = 
        "try {" +
        "    var canvas = document.createElement('canvas');" +
        "    return !!(window.WebGLRenderingContext && " +
        "        (canvas.getContext('webgl') || canvas.getContext('experimental-webgl')));" +
        "} catch(e) {" +
        "    return false;" +
        "}")
    private static native boolean detectWebGL();

    @JSBody(script = 
        "try {" +
        "    return 'localStorage' in window && window['localStorage'] !== null;" +
        "} catch (e) {" +
        "    return false;" +
        "}")
    private static native boolean detectLocalStorage();

    @JSBody(script = "return 'fetch' in window;")
    private static native boolean detectFetch();

    @JSBody(script = "return 'Worker' in window;")
    private static native boolean detectWebWorkers();
}