package io.github.lolens.showcaser.client.event;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.event.events.client.ClientScreenInputEvent;
import io.github.lolens.showcaser.api.event.AdapterRegistrationEvent;
import io.github.lolens.showcaser.api.event.client.ClientConfigSyncEvent;
import io.github.lolens.showcaser.client.adapter.AdapterRegistry;
import io.github.lolens.showcaser.client.ClientShareDispatcher;
import io.github.lolens.showcaser.config.ConfigManager;
import io.github.lolens.showcaser.client.ClientHandlerCache;
import net.minecraft.client.gui.screen.Screen;

import static io.github.lolens.showcaser.client.KeyMappings.SHARE_ITEM_IN_CHAT;

public class ClientEvents {

    public static void register() {

        // share on key press
        ClientScreenInputEvent.KEY_PRESSED_PRE.register((client, screen, keyCode, scanCode, modifiers) -> {
            if (keyCode == SHARE_ITEM_IN_CHAT.getDefaultKey().getCode() && Screen.hasShiftDown()) {
                ClientShareDispatcher.onKeyPress(screen);
                return EventResult.interruptTrue();
            }
            return EventResult.pass();
        });

        AdapterRegistrationEvent.EVENT.register(AdapterRegistry::register);

        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(player -> {
            ConfigManager.loadClient();
        });

        // prevents clients from creating context on screens that are banned on server
        ClientConfigSyncEvent.EVENT.register(config -> {

            ClientHandlerCache.clearConfigBlacklistedClasses();

            for (String className : config.blacklistedClassesWithInheritors) {
                ClientHandlerCache.blacklistWithInheritors(className);
            }
            for (String className : config.blacklistedClassesExact) {
                ClientHandlerCache.blacklistExact(className);
            }

            ClientHandlerCache.prewarmCache();
        });

    }
}
