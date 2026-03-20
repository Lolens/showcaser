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

package io.github.lolens.showcaser.core.impl.builder;

import io.github.lolens.showcaser.api.builder.ClientHandlerBuilder;
import io.github.lolens.showcaser.api.sharecontext.ShareContext;
import io.github.lolens.showcaser.api.event.HandlerRegistrationEvent;
import io.github.lolens.showcaser.api.handler.HandlerResult;
import io.github.lolens.showcaser.api.handler.summary.ClientSummary;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;


public class ClientHandlerBuilderImpl<T extends Screen> implements ClientHandlerBuilder<T> {
    private final Identifier id;
    private Class<T> screenClass;
    private BiFunction<T, Consumer<ShareContext>, HandlerResult> contextCreator;
    private BiConsumer<String, ShareContext> displayHandler;
    private int priority = 100;

    private ClientHandlerBuilderImpl(Identifier id) {
        this.id = id;
    }

    public static <T extends Screen> ClientHandlerBuilderImpl<T> create(Identifier id) {
        return new ClientHandlerBuilderImpl<>(id);
    }

    public ClientHandlerBuilderImpl<T> forScreen(Class<T> screenClass) {
        this.screenClass = screenClass;
        return this;
    }

    public ClientHandlerBuilderImpl<T> createContext(BiFunction<T, Consumer<ShareContext>, HandlerResult> contextCreator) {
        this.contextCreator = contextCreator;
        return this;
    }

    public ClientHandlerBuilderImpl<T> display(BiConsumer<String, ShareContext> displayHandler) {
        this.displayHandler = displayHandler;
        return this;
    }

    public ClientHandlerBuilderImpl<T> priority(int priority) {
        this.priority = priority;
        return this;
    }

    public void register() {
        HandlerRegistrationEvent.EVENT.invoker().registerClient(build());
    }

    public ClientSummary<T> build() {
        return new ClientSummary<>(
                id,
                screenClass,
                contextCreator,
                displayHandler,
                priority
        );
    }


}