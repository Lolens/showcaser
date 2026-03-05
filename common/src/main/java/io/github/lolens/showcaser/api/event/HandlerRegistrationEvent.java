package io.github.lolens.showcaser.api.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import io.github.lolens.showcaser.core.builders.handler.ClientHandlerBuilder;
import io.github.lolens.showcaser.core.builders.handler.ServerHandlerBuilder;


public interface HandlerRegistrationEvent {

    Event<HandlerRegistrationEvent> EVENT = EventFactory.createLoop();

    void registerClient(ClientHandlerBuilder.ClientSummary<?> summary);
    void registerServer(ServerHandlerBuilder.ServerSummary<?> summary);

}
