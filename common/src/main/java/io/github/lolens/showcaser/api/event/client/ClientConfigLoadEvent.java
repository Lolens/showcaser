package io.github.lolens.showcaser.api.event.client;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import io.github.lolens.showcaser.config.ShowcaserClientConfig;

public interface ClientConfigLoadEvent {

    Event<ClientConfigLoadEvent> EVENT = EventFactory.createLoop();

    void onLoad(ShowcaserClientConfig clientConfig);

}
