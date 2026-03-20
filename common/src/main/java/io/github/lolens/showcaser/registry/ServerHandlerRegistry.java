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

package io.github.lolens.showcaser.registry;

import io.github.lolens.showcaser.Showcaser;
import io.github.lolens.showcaser.api.handler.ServerShareHandler;
import io.github.lolens.showcaser.client.ClientHandlerCache;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;


public final class ServerHandlerRegistry {

    private static final Map<Identifier, ServerShareHandler<? extends ScreenHandler>> SERVER_HANDLERS = new HashMap<>();


    static void addServerHandler(@NotNull ServerShareHandler<? extends ScreenHandler> handler) {
        Objects.requireNonNull(handler);
        if (SERVER_HANDLERS.containsKey(handler.getIdentifier())) {
            Showcaser.LOGGER.warn("Tried registering ServerHandler with ID which is already registered: {}. It was omitted", handler.getIdentifier());
        }

        SERVER_HANDLERS.put(handler.getIdentifier(), handler);

        ClientHandlerCache.invalidate();
    }

    public static Collection<ServerShareHandler<? extends ScreenHandler>> getServerHandlers() {
        return Collections.unmodifiableCollection(SERVER_HANDLERS.values());
    }

    @Nullable
    public static ServerShareHandler<? extends ScreenHandler> getServerHandler(Identifier id) {
        return SERVER_HANDLERS.get(id);
    }

}