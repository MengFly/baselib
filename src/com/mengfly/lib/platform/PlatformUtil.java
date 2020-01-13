package com.mengfly.lib.platform;

import java.util.Locale;

/**
 * @author wangp
 */
public class PlatformUtil {

    public static enum PlatformEnum {
        /**
         * Platform of Microsoft Windows
         */
        WINDOWS,
        /**
         * Platform of linux
         */
        LINUX,
        /**
         * Platform of macOS (OS X)
         */
        MACOSX,
        /**
         * Known platform
         */
        UNKNOWN
    }

    private static PlatformEnum platformEnum;

    public static PlatformEnum getPlatform() {
        if (platformEnum != null) {
            return platformEnum;
        }

        String systemName = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
        if (systemName.startsWith("linux")) {
            platformEnum = PlatformEnum.LINUX;
        } else if (systemName.startsWith("mac") || systemName.startsWith("darwin")) {
            platformEnum = PlatformEnum.MACOSX;
        } else if (systemName.startsWith("windows")) {
            platformEnum = PlatformEnum.WINDOWS;
        } else {
            platformEnum = PlatformEnum.UNKNOWN;
        }
        return platformEnum;
    }

    public static boolean isWindows() {
        return getPlatform() == PlatformEnum.WINDOWS;
    }

    public static boolean isLinux() {
        return getPlatform() == PlatformEnum.LINUX;
    }

    public static boolean isMac() {
        return getPlatform() == PlatformEnum.MACOSX;
    }
}
