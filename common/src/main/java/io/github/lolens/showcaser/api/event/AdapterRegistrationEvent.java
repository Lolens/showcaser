package io.github.lolens.showcaser.api.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import io.github.lolens.showcaser.api.adapter.ShareContextAdapter;

public interface AdapterRegistrationEvent {

    Event<AdapterRegistrationEvent> EVENT = EventFactory.createLoop();

    void register(ShareContextAdapter adapter);

}
