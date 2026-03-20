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

package io.github.lolens.showcaser.api.handler.summary;

import io.github.lolens.showcaser.api.sharecontext.ShareContext;
import io.github.lolens.showcaser.api.handler.HandlerResult;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public record ClientSummary<T extends Screen>(
        Identifier id,
        @Nullable Class<T> screenClass,
        @Nullable BiFunction<T, Consumer<ShareContext>, HandlerResult> contextCreator,
        @Nullable BiConsumer<String, ShareContext> display,
        int priority
) {}
