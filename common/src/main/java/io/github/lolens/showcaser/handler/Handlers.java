package io.github.lolens.showcaser.handler;

import io.github.lolens.showcaser.handler.conditional.Ae2Handler;
import io.github.lolens.showcaser.handler.conditional.EmiHandler;
import io.github.lolens.showcaser.handler.conditional.ReiHandler;
import io.github.lolens.showcaser.handler.vanilla.CreativeInventoryHandler;
import io.github.lolens.showcaser.handler.vanilla.FallbackHandler;
import io.github.lolens.showcaser.handler.vanilla.PlayerInventoryHandler;
import io.github.lolens.showcaser.util.PlatformUtils;

public class Handlers {

    public static void registerAll() {

        registerVanilla();

        if (PlatformUtils.isAE2Loaded()) Ae2Handler.register();
        if (PlatformUtils.isREILoaded()) ReiHandler.register();
        if (PlatformUtils.isEMILoaded()) EmiHandler.register();

    }

    public static void registerVanilla() {
        FallbackHandler.register();
        PlayerInventoryHandler.register();
        CreativeInventoryHandler.register();
    }

}
