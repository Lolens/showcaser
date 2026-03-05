package io.github.lolens.showcaser.registry;

import io.github.lolens.showcaser.api.ClientShareHandler;
import io.github.lolens.showcaser.api.DisplayHandler;
import io.github.lolens.showcaser.api.ServerShareHandler;

public final class ShareHandlerRegistrar {

    public static void register(ClientShareHandler<?> clientShareHandler) {
        ClientHandlerRegistry.addClientShareHandler(clientShareHandler);
    }

    public static void register(DisplayHandler clientShareHandler) {
        ClientHandlerRegistry.addClientDisplayHandler(clientShareHandler);
    }

    public static void register(ServerShareHandler<?> clientShareHandler) {
        ServerShareHandlerRegistry.addServerHandler(clientShareHandler);
    }

}
