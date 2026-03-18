package io.github.lolens.showcaser.handler;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import io.github.lolens.showcaser.handler.conditional.Ae2Handler;
import io.github.lolens.showcaser.handler.conditional.EmiHandler;
import io.github.lolens.showcaser.handler.conditional.FtbQuestsHandler;
import io.github.lolens.showcaser.handler.conditional.ReiHandler;
import io.github.lolens.showcaser.handler.vanilla.CreativeInventoryHandler;
import io.github.lolens.showcaser.handler.vanilla.FallbackHandler;
import io.github.lolens.showcaser.handler.vanilla.PlayerInventoryHandler;
import io.github.lolens.showcaser.client.ClientHandlerCache;
import io.github.lolens.showcaser.util.PlatformUtils;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;

public class Handlers {


    public static void registerAll() {

        registerVanilla();

        if (PlatformUtils.isAE2Loaded()) Ae2Handler.register();
        if (PlatformUtils.isREILoaded()) ReiHandler.register();
        if (PlatformUtils.isEMILoaded()) EmiHandler.register();
        if (PlatformUtils.isFTBQuestsLoaded()) FtbQuestsHandler.register();

        if (Platform.getEnvironment() == Env.CLIENT) {
            // prevents sharing constantly renamed output slot item
            ClientHandlerCache.blacklistExact(AnvilScreen.class);
        }

    }

    public static void registerVanilla() {
        FallbackHandler.register();
        PlayerInventoryHandler.register();
        CreativeInventoryHandler.register();
    }

}
