package io.github.lolens.showcaser.registry;

import io.github.lolens.showcaser.Showcaser;
import io.github.lolens.showcaser.api.handler.ServerShareHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;


public final class ServerShareHandlerRegistry {

    private static final Map<Identifier, ServerShareHandler<? extends ScreenHandler>> SERVER_HANDLERS = new HashMap<>();


    static void addServerHandler(@NotNull ServerShareHandler<? extends ScreenHandler> handler) {
        Objects.requireNonNull(handler);
        if (SERVER_HANDLERS.containsKey(handler.getIdentifier())) {
            Showcaser.LOGGER.warn("Tried registering ServerHandler with ID which is already registered: {}. It was omitted", handler.getIdentifier());
        }

        SERVER_HANDLERS.put(handler.getIdentifier(), handler);

        CachedPriorityRegistry.invalidate();
    }

    public static Collection<ServerShareHandler<? extends ScreenHandler>> getServerHandlers() {
        return Collections.unmodifiableCollection(SERVER_HANDLERS.values());
    }

    @Nullable
    public static ServerShareHandler<? extends ScreenHandler> getServerHandler(Identifier id) {
        return SERVER_HANDLERS.get(id);
    }

}