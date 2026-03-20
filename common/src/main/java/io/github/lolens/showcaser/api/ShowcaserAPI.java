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

package io.github.lolens.showcaser.api;

import io.github.lolens.showcaser.api.builder.ChatMessageBuilderFactory;
import io.github.lolens.showcaser.api.builder.HandlerBuilderFactory;
import io.github.lolens.showcaser.api.sharecontext.ShareContextFactory;
import io.github.lolens.showcaser.client.impl.messagebuilder.ChatMessageBuilderFactoryImpl;
import io.github.lolens.showcaser.core.impl.ShareContextFactoryImpl;
import io.github.lolens.showcaser.core.impl.builder.HandlerBuilderFactoryImpl;

import java.util.Objects;

public class ShowcaserAPI {

    private static final String NOT_INITIALIZED_MESSAGE = "Showcaser API not initialized";

    private static ShareContextFactory contextFactory;
    private static HandlerBuilderFactory handlerBuilderFactory;
    private static ChatMessageBuilderFactory messageBuilderFactory;

    public static void init() {
        contextFactory = ShareContextFactoryImpl.INSTANCE;
        handlerBuilderFactory = HandlerBuilderFactoryImpl.INSTANCE;
        messageBuilderFactory = ChatMessageBuilderFactoryImpl.INSTANCE;
    }

    public static ShareContextFactory getContextFactory() {
        return Objects.requireNonNull(contextFactory, NOT_INITIALIZED_MESSAGE);
    }

    public static HandlerBuilderFactory getHandlerBuilderFactory() {
        return Objects.requireNonNull(handlerBuilderFactory, NOT_INITIALIZED_MESSAGE);
    }

    public static ChatMessageBuilderFactory getMessageBuilderFactory() {
        return Objects.requireNonNull(messageBuilderFactory, NOT_INITIALIZED_MESSAGE);
    }
}
