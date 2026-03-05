package io.github.lolens.showcaser.registry;

import io.github.lolens.showcaser.Showcaser;
import io.github.lolens.showcaser.api.ClientShareHandler;
import io.github.lolens.showcaser.api.DisplayHandler;
import io.github.lolens.showcaser.registry.CachedPriorityRegistry;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class ClientHandlerRegistry {

    private static final Map<Identifier, ClientShareHandler<? extends Screen>> CLIENT_SHARE_HANDLERS = new HashMap<>();
    private static final Map<Identifier, ClientShareHandler<? extends Screen>> CLIENT_OVERLAY_SHARE_HANDLERS = new HashMap<>();


    private static final Map<Identifier, DisplayHandler> CLIENT_DISPLAY_HANDLERS = new HashMap<>();

    static void addClientShareHandler(@NotNull ClientShareHandler<? extends Screen> handler) {
        Objects.requireNonNull(handler);

        if (handler.getTargetClass() == null) {

            if (CLIENT_OVERLAY_SHARE_HANDLERS.containsKey(handler.getIdentifier())) {
                throw new IllegalStateException("Tried registering ClientHandler for overlay with ID which is already registered: " + handler.getIdentifier());
            }
            CLIENT_OVERLAY_SHARE_HANDLERS.put(handler.getIdentifier(), handler);

        } else {

            if (CLIENT_SHARE_HANDLERS.containsKey(handler.getIdentifier())) {
                throw new IllegalStateException("Tried registering ClientHandler with ID which is already registered: " + handler.getIdentifier());
            }
            CLIENT_SHARE_HANDLERS.put(handler.getIdentifier(), handler);
        }

        CachedPriorityRegistry.invalidate();
    }

    static void addClientDisplayHandler(@NotNull DisplayHandler handler) {
        Objects.requireNonNull(handler);
        if (CLIENT_DISPLAY_HANDLERS.containsKey(handler.getIdentifier())) {
            Showcaser.LOGGER.warn("Tried registering DisplayHandler with ID which is already registered: {}. It was omitted", handler.getIdentifier());
        }

        CLIENT_DISPLAY_HANDLERS.put(handler.getIdentifier(), handler);

        CachedPriorityRegistry.invalidate();
    }

    public static Collection<ClientShareHandler<? extends Screen>> getClientShareHandlers() {
        return Collections.unmodifiableCollection(CLIENT_SHARE_HANDLERS.values());
    }

    public static Collection<ClientShareHandler<? extends Screen>> getClientOverlayShareHandlers() {
        return Collections.unmodifiableCollection(CLIENT_OVERLAY_SHARE_HANDLERS.values());
    }

    public static Collection<DisplayHandler> getClientDisplayHandlers() {
        return Collections.unmodifiableCollection(CLIENT_DISPLAY_HANDLERS.values());
    }


    @Nullable
    public static DisplayHandler getClientDisplayHandler(Identifier id) {
        return CLIENT_DISPLAY_HANDLERS.get(id);
    }

    @Nullable
    public static ClientShareHandler<? extends Screen> getClientShareHandler(Identifier id) {
        return CLIENT_SHARE_HANDLERS.get(id);
    }

    public static void clear() {
        CLIENT_SHARE_HANDLERS.clear();
        CLIENT_DISPLAY_HANDLERS.clear();
    }

}
