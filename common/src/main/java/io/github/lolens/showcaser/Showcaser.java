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

package io.github.lolens.showcaser;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import io.github.lolens.showcaser.api.ShowcaserAPI;
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

        ShowcaserAPI.init();

        Events.register();

        if (Platform.getEnvironment() == Env.CLIENT) {
            KeyMappings.register();
            ClientEvents.register();
        }

        Handlers.registerAll();

        Networking.register();

        ServerCommands.register();

    }
}
