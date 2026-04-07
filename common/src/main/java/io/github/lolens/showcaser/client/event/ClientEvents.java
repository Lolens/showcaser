/*
 * Showcaser - <https://github.com/Lolens/showcaser>
 * Copyright (C) 2026-present Lolens <https://github.com/Lolens>
 *
 * This file is part of Showcaser.
 *
 * Showcaser is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 3 of the License,
 * or (at your option) any later version.
 *
 * Showcaser is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along Showcaser.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.lolens.showcaser.client.event;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.event.events.client.ClientRawInputEvent;
import dev.architectury.event.events.client.ClientScreenInputEvent;
import io.github.lolens.showcaser.api.event.AdapterRegistrationEvent;
import io.github.lolens.showcaser.api.event.client.ClientConfigLoadEvent;
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
            if (client.player == null) return EventResult.pass(); // do not dispatch when not in the world
            if (keyCode == SHARE_ITEM_IN_CHAT.getDefaultKey().getCode() && Screen.hasShiftDown()) {
                ClientShareDispatcher.onKeyPress(screen);
                return EventResult.interruptDefault();
            }
            return EventResult.pass();
        });

        AdapterRegistrationEvent.EVENT.register(AdapterRegistry::register);

        // Configs are loaded twice (on game startup and upon world join)
        // to prevent client from using share keybind before joining the world and crashing itself
        ClientLifecycleEvent.CLIENT_STARTED.register(instance -> {
            ConfigManager.loadClient();
        });

        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(player -> {
            ConfigManager.loadClient();
        });

        ClientConfigSyncEvent.EVENT.register(config -> {

            // update cooldown on client
            ClientShareDispatcher.COOLDOWN_MANAGER.setCooldown(config.chatSharingCooldown);

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
