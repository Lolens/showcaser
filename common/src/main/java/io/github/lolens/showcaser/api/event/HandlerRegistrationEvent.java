package io.github.lolens.showcaser.api.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import io.github.lolens.showcaser.api.handler.ClientShareHandler;
import io.github.lolens.showcaser.api.handler.DisplayHandler;
import io.github.lolens.showcaser.api.handler.ServerShareHandler;
import io.github.lolens.showcaser.api.handler.summary.ClientSummary;
import io.github.lolens.showcaser.api.handler.summary.ServerSummary;
import io.github.lolens.showcaser.core.builder.handler.ClientHandlerBuilder;
import io.github.lolens.showcaser.core.builder.handler.ServerHandlerBuilder;


public interface HandlerRegistrationEvent {

    Event<HandlerRegistrationEvent> EVENT = EventFactory.createLoop();

    void registerClient(ClientSummary<?> summary);
    void registerServer(ServerSummary<?> summary);

    void register(DisplayHandler displayHandler);
    void register(ClientShareHandler<?> shareHandler);
    void register(ServerShareHandler<?> shareHandler);
}
