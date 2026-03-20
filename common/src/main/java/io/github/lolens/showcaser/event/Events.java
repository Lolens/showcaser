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

package io.github.lolens.showcaser.event;

import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.PlayerEvent;
import io.github.lolens.showcaser.api.event.HandlerRegistrationEvent;
import io.github.lolens.showcaser.api.handler.ClientShareHandler;
import io.github.lolens.showcaser.api.handler.DisplayHandler;
import io.github.lolens.showcaser.api.handler.ServerShareHandler;
import io.github.lolens.showcaser.api.handler.summary.ClientSummary;
import io.github.lolens.showcaser.api.handler.summary.ServerSummary;
import io.github.lolens.showcaser.config.ConfigManager;
import io.github.lolens.showcaser.network.message.s2c.ConfigSyncMessage;
import io.github.lolens.showcaser.registry.ShareHandlerRegistrar;

public class Events {

    public static void register() {

        HandlerRegistrationEvent.EVENT.register(new HandlerRegistrationEvent() {
            @Override
            public void registerClient(ClientSummary<?> summary) {
                ShareHandlerRegistrar.registerClientSummary(summary);
            }

            @Override
            public void registerServer(ServerSummary<?> summary) {
                ShareHandlerRegistrar.registerServerSummary(summary);
            }

            @Override
            public void register(DisplayHandler displayHandler) {
                ShareHandlerRegistrar.register(displayHandler);
            }

            @Override
            public void register(ClientShareHandler<?> shareHandler) {
                ShareHandlerRegistrar.register(shareHandler);
            }

            @Override
            public void register(ServerShareHandler<?> shareHandler) {
                ShareHandlerRegistrar.register(shareHandler);
            }
        });

        LifecycleEvent.SERVER_STARTING.register(instance -> {
            ConfigManager.loadServer();
        });

        PlayerEvent.PLAYER_JOIN.register(player -> {
            new ConfigSyncMessage(ConfigManager.getServerConfig()).sendTo(player);
        });

    }

}
