package io.github.lolens.showcaser.event;

import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.PlayerEvent;
import io.github.lolens.showcaser.api.event.HandlerRegistrationEvent;
import io.github.lolens.showcaser.api.handler.ClientShareHandler;
import io.github.lolens.showcaser.api.handler.DisplayHandler;
import io.github.lolens.showcaser.api.handler.ServerShareHandler;
import io.github.lolens.showcaser.api.handler.summary.ClientSummary;
import io.github.lolens.showcaser.api.handler.summary.ServerSummary;
import io.github.lolens.showcaser.config.ConfigManager;
import io.github.lolens.showcaser.network.message.s2c.ConfigSyncMessage;
import io.github.lolens.showcaser.registry.ShareHandlerRegistrar;

public class Events {

    public static void register() {

        HandlerRegistrationEvent.EVENT.register(new HandlerRegistrationEvent() {
            @Override
            public void registerClient(ClientSummary<?> summary) {
                ShareHandlerRegistrar.registerClientSummary(summary);
            }

            @Override
            public void registerServer(ServerSummary<?> summary) {
                ShareHandlerRegistrar.registerServerSummary(summary);
            }

            @Override
            public void register(DisplayHandler displayHandler) {
                ShareHandlerRegistrar.register(displayHandler);
            }

            @Override
            public void register(ClientShareHandler<?> shareHandler) {
                ShareHandlerRegistrar.register(shareHandler);
            }

            @Override
            public void register(ServerShareHandler<?> shareHandler) {
                ShareHandlerRegistrar.register(shareHandler);
            }
        });

        LifecycleEvent.SERVER_STARTING.register(instance -> {
            ConfigManager.loadServer();
        });

        PlayerEvent.PLAYER_JOIN.register(player -> {
            new ConfigSyncMessage(ConfigManager.getServerConfig()).sendTo(player);
        });

    }

}
