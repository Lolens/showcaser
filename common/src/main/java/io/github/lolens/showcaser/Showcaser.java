package io.github.lolens.showcaser;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import io.github.lolens.showcaser.client.KeyMappings;
import io.github.lolens.showcaser.client.event.ClientEvents;
import io.github.lolens.showcaser.command.ServerCommands;
import io.github.lolens.showcaser.event.Events;
import io.github.lolens.showcaser.handler.Handlers;
import io.github.lolens.showcaser.network.Networking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Showcaser {

    public static final Logger LOGGER = LoggerFactory.getLogger(Showcaser.class);
    public static final String MOD_ID = "showcaser";

    public static void init() {

        Events.register();

        Handlers.registerAll();

        Networking.register();

        ServerCommands.register();

        if (Platform.getEnvironment() == Env.CLIENT) {
            KeyMappings.register();
            ClientEvents.register();
        }
    }
}
