package io.github.lolens.showcaser.util;

import dev.architectury.platform.Platform;

public final class PlatformUtils {

    private PlatformUtils() {}

    public static boolean isREILoaded() {
        return Platform.isModLoaded("roughlyenoughitems");
    }
    public static boolean isAE2Loaded() {
        return Platform.isModLoaded("ae2");
    }
    public static boolean isEMILoaded() {
        return Platform.isModLoaded("emi");
    }

}
