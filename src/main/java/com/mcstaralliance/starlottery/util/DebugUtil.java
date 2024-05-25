package com.mcstaralliance.starlottery.util;

public class DebugUtil {
    private static boolean debugMode = false;

    public static boolean getDebugMode() {
        return debugMode;
    }

    public static void setDebugMode(boolean debugMode) {
        DebugUtil.debugMode = debugMode;
    }
}
