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

import io.github.lolens.showcaser.api.builder.ServerHandlerBuilder;
import io.github.lolens.showcaser.api.sharecontext.ShareContext;
import io.github.lolens.showcaser.api.event.HandlerRegistrationEvent;
import io.github.lolens.showcaser.api.handler.summary.ServerSummary;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;

import java.util.function.BiFunction;

public class ServerHandlerBuilderImpl<T extends ScreenHandler> implements ServerHandlerBuilder<T> {
    private final Identifier id;
    private Class<T> containerClass;
    private BiFunction<PlayerEntity, ShareContext, ShareContext> processor;

    private ServerHandlerBuilderImpl(Identifier id) {
        this.id = id;
    }

    public static <T extends ScreenHandler> ServerHandlerBuilderImpl<T> create(Identifier id) {
        return new ServerHandlerBuilderImpl<>(id);
    }

    public ServerHandlerBuilderImpl<T> forContainer(Class<T> containerClass) {
        this.containerClass = containerClass;
        return this;
    }

    public ServerHandlerBuilderImpl<T> process(BiFunction<PlayerEntity, ShareContext, ShareContext> processor) {
        this.processor = processor;
        return this;
    }

    public void register() {
        HandlerRegistrationEvent.EVENT.invoker().registerServer(build());
    }

    public ServerSummary<T> build() {
        return new ServerSummary<>(id, containerClass, processor);
    }


}