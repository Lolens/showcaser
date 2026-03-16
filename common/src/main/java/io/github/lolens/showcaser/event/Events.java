package io.github.lolens.showcaser.event;

import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.PlayerEvent;
import io.github.lolens.showcaser.Showcaser;
import io.github.lolens.showcaser.api.event.HandlerRegistrationEvent;
import io.github.lolens.showcaser.api.handler.summary.ClientSummary;
import io.github.lolens.showcaser.api.handler.summary.ServerSummary;
import io.github.lolens.showcaser.config.ConfigManager;
import io.github.lolens.showcaser.core.builder.handler.ClientHandlerBuilder;
import io.github.lolens.showcaser.core.builder.handler.ServerHandlerBuilder;
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
        });

        LifecycleEvent.SERVER_STARTING.register(instance -> {
            ConfigManager.loadServer();
        });

        PlayerEvent.PLAYER_JOIN.register(player -> {
            new ConfigSyncMessage(ConfigManager.getServerConfig()).sendTo(player);
        });

    }

}
