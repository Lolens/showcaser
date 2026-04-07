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
import io.github.lolens.showcaser.api.sharecontext.ShareContext;
import io.github.lolens.showcaser.api.handler.ClientShareHandler;
import io.github.lolens.showcaser.api.handler.DisplayHandler;
import io.github.lolens.showcaser.api.handler.HandlerResult;
import io.github.lolens.showcaser.api.handler.ServerShareHandler;
import io.github.lolens.showcaser.api.handler.summary.ClientSummary;
import io.github.lolens.showcaser.api.handler.summary.ServerSummary;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public final class ShareHandlerRegistrar {

    public static void register(ClientShareHandler<?> clientShareHandler) {
        ClientHandlerRegistry.addClientShareHandler(clientShareHandler);
        Showcaser.LOGGER.debug("Registered client share handler with id {}", clientShareHandler.getIdentifier());
    }

    public static void register(DisplayHandler clientDisplayHandler) {
        ClientHandlerRegistry.addClientDisplayHandler(clientDisplayHandler);
        Showcaser.LOGGER.debug("Registered client display handler with id {}", clientDisplayHandler.getIdentifier());
    }

    public static void register(ServerShareHandler<?> serverShareHandler) {
        ServerHandlerRegistry.addServerHandler(serverShareHandler);
        Showcaser.LOGGER.debug("Registered server share handler with id {}", serverShareHandler.getIdentifier());
    }

    public static <T extends Screen> void registerClientSummary(ClientSummary<T> summary) {

        if (summary.contextCreator() != null) {
            ShareHandlerRegistrar.register(new ClientShareHandler<T>() {
                @Override
                public @NotNull HandlerResult createContext(Screen screen, Consumer<ShareContext> contextConsumer) {
                    try {
                        return summary.contextCreator().apply((T) screen, contextConsumer);
                    } catch (RuntimeException e) {
                        Showcaser.LOGGER.error("Encountered error while creating ShareContext for screen {}", screen.getClass().getName(), e);
                    }
                    return HandlerResult.PASS;
                }

                @Override
                public @NotNull Identifier getIdentifier() {
                    return summary.id();
                }

                @Override
                public @Nullable Class<T> getTargetClass() {
                    return summary.screenClass();
                }

                @Override
                public int getPriority() {
                    return summary.priority();
                }
            });
        }

        if (summary.display() != null) {
            ShareHandlerRegistrar.register(new DisplayHandler() {
                @Override
                public Identifier getIdentifier() {
                    return summary.id();
                }

                @Override
                public void display(String senderName, ShareContext context) {
                    try {
                        summary.display().accept(senderName, context);
                    } catch (RuntimeException e) {
                        Showcaser.LOGGER.error("Encountered error while displaying ShareContext from {}. Context Id: {}", senderName, context.getId(), e);

                    }
                }
            });
        }

    }

    public static <T extends ScreenHandler> void registerServerSummary(ServerSummary<T> summary) {
        ShareHandlerRegistrar.register(new ServerShareHandler<T>() {
            @Override
            public ShareContext handle(PlayerEntity player, ShareContext context) {
                try {
                    return summary.processor().apply(player, context);
                } catch (RuntimeException e) {
                    Showcaser.LOGGER.error("Encountered error while processing ShareContext from {}. Context Id: {}", player.getName(), context.getId(), e);
                }
                return null;
            }

            @Override
            public @NotNull Identifier getIdentifier() {
                return summary.id();
            }

            @Override
            public Class<T> getTargetClass() {
                return summary.containerClass();
            }
        });
    }

}
