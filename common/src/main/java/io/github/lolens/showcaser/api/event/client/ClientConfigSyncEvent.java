package io.github.lolens.showcaser.api.event.client;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import io.github.lolens.showcaser.config.ShowcaserServerConfig;

public interface ClientConfigSyncEvent {

    Event<ClientConfigSyncEvent> EVENT = EventFactory.createLoop();

    void onConfigSync(ShowcaserServerConfig config);

}
