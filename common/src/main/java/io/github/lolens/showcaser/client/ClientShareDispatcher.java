package io.github.lolens.showcaser.client;

import io.github.lolens.showcaser.Showcaser;
import io.github.lolens.showcaser.api.handler.ClientShareHandler;
import io.github.lolens.showcaser.api.handler.HandlerResult;
import io.github.lolens.showcaser.config.ConfigManager;
import io.github.lolens.showcaser.network.message.c2s.ShareMessage;
import io.github.lolens.showcaser.client.ClientHandlerCache;
import net.minecraft.client.gui.screen.Screen;

public class ClientShareDispatcher {

    private static long lastSendAt = 0;

    public static void onKeyPress(Screen screen) {
        int cooldown = ConfigManager.getSyncedConfig().chatSharingCooldown;

        if (ConfigManager.getClientConfig().logScreenClass) {
            Showcaser.LOGGER.info("Pressed key on screen: {}", screen.getClass().getName());
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastSendAt < cooldown) return;

        var handlers = ClientHandlerCache.getClientHandlersFor(screen);

        for (ClientShareHandler<? extends Screen> handler : handlers) {

            HandlerResult result = handler.createContext(screen, context -> {
                Showcaser.LOGGER.info("Handler with target {} created context for {}",
                        handler.getTargetClass(), context.getId());

                new ShareMessage(context).sendToServer();
                lastSendAt = currentTime;
            });

            if (result != HandlerResult.PASS) return; // proceed only if passes

        }

        Showcaser.LOGGER.warn("No valid handler found for screen: {}", screen.getClass().getSimpleName());
    }
}