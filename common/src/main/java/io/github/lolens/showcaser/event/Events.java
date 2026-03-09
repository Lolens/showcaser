package io.github.lolens.showcaser.event;

import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.PlayerEvent;
import io.github.lolens.showcaser.Showcaser;
import io.github.lolens.showcaser.api.event.HandlerRegistrationEvent;
import io.github.lolens.showcaser.config.ConfigManager;
import io.github.lolens.showcaser.core.builders.handler.ClientHandlerBuilder;
import io.github.lolens.showcaser.core.builders.handler.ServerHandlerBuilder;
import io.github.lolens.showcaser.network.message.s2c.ConfigSyncMessage;

public class Events {

    public static void register() {

        HandlerRegistrationEvent.EVENT.register(new HandlerRegistrationEvent() {
            @Override
            public void registerClient(ClientHandlerBuilder.ClientSummary<?> summary) {
                summary.registerHandlers();
                Showcaser.LOGGER.info("Registered client handlers: " + summary.id());
            }

            @Override
            public void registerServer(ServerHandlerBuilder.ServerSummary<?> summary) {
                summary.registerHandler();
                Showcaser.LOGGER.info("Registered server handler: " + summary.id());
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
