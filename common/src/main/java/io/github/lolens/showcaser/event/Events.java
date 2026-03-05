package io.github.lolens.showcaser.event;

import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import io.github.lolens.showcaser.api.event.HandlerRegistrationEvent;
import io.github.lolens.showcaser.config.ConfigManager;
import io.github.lolens.showcaser.core.builders.handler.ClientHandlerBuilder;
import io.github.lolens.showcaser.core.builders.handler.ServerHandlerBuilder;
import io.github.lolens.showcaser.registry.CachedPriorityRegistry;

public class Events {

    public static void register() {

        HandlerRegistrationEvent.EVENT.register(new HandlerRegistrationEvent() {
            @Override
            public void registerClient(ClientHandlerBuilder.ClientSummary<?> summary) {
                summary.registerHandlers();
                System.out.println("Registered client handlers: " + summary.id());
            }

            @Override
            public void registerServer(ServerHandlerBuilder.ServerSummary<?> summary) {
                summary.registerHandler();
                System.out.println("Registered server handler: " + summary.id());
            }
        });

        LifecycleEvent.SERVER_STARTING.register(instance -> {
            ConfigManager.loadAll();
        });

    }

}
