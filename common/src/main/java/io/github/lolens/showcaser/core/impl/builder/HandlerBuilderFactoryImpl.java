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
import io.github.lolens.showcaser.api.builder.HandlerBuilderFactory;
import io.github.lolens.showcaser.api.builder.ServerHandlerBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;

public class HandlerBuilderFactoryImpl implements HandlerBuilderFactory {

    public static final HandlerBuilderFactory INSTANCE = new HandlerBuilderFactoryImpl();

    @Override
    public <T extends ScreenHandler> ServerHandlerBuilder<T> createServerBuilder(Identifier id) {
        return ServerHandlerBuilderImpl.create(id);
    }

    @Override
    public <T extends Screen> ClientHandlerBuilder<T> createClientBuilder(Identifier id) {
        return ClientHandlerBuilderImpl.create(id);
    }
}
