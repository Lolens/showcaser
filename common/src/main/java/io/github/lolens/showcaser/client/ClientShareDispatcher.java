package io.github.lolens.showcaser.client;

import io.github.lolens.showcaser.Showcaser;
import io.github.lolens.showcaser.api.handler.ClientShareHandler;
import io.github.lolens.showcaser.api.handler.HandlerResult;
import io.github.lolens.showcaser.config.ConfigManager;
import io.github.lolens.showcaser.network.message.c2s.ShareMessage;
import net.minecraft.client.gui.screen.Screen;

public class ClientShareDispatcher {

    public static final ClientCooldownManager COOLDOWN_MANAGER = new ClientCooldownManager(
            ConfigManager.getSyncedConfig().chatSharingCooldown
    );

    public static void onKeyPress(Screen screen) {

        Showcaser.LOGGER.debug("Pressed key on screen: {}", screen.getClass().getName());

        if (COOLDOWN_MANAGER.isOnCooldown()) {
            Showcaser.LOGGER.debug("Tried sharing resource while on cooldown");
            return;
        }

        var handlers = ClientHandlerCache.getClientHandlersFor(screen);

        for (ClientShareHandler<? extends Screen> handler : handlers) {

            HandlerResult result = handler.createContext(screen, context -> {

                Showcaser.LOGGER.debug("Handler with target {} created context for {}",
                        handler.getTargetClass(), context.getId());

                new ShareMessage(context).sendToServer();
                COOLDOWN_MANAGER.updateNow();
            });

            if (result != HandlerResult.PASS) return; // proceed only if passes

        }

        Showcaser.LOGGER.warn("No valid handler found for screen: {}", screen.getClass().getSimpleName());
    }

    public static class ClientCooldownManager {
        private long lastSend = 0;
        private int cooldownMs;

        ClientCooldownManager(int cooldownTicks) {
            setCooldown(cooldownTicks);
        }

        public void updateNow() {
            this.lastSend = System.currentTimeMillis();
        }

        public boolean isOnCooldown() {
            return System.currentTimeMillis() - lastSend < cooldownMs;
        }

        public void setCooldown(int cooldownTicks) {
            this.cooldownMs = cooldownTicks * 50;
        }

    }

}