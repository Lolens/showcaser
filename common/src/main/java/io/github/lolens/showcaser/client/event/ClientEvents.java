package io.github.lolens.showcaser.client.event;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.event.events.client.ClientScreenInputEvent;
import io.github.lolens.showcaser.client.handler.ClientShareDispatcher;
import io.github.lolens.showcaser.config.ConfigManager;
import io.github.lolens.showcaser.registry.CachedPriorityRegistry;
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

        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(player -> {
            ConfigManager.loadClient();
            CachedPriorityRegistry.prewarmCache();
        });

    }
}
