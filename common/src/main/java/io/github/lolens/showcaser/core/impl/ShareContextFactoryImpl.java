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

package io.github.lolens.showcaser.core.impl;

import io.github.lolens.showcaser.api.sharecontext.ShareContext;
import io.github.lolens.showcaser.api.sharecontext.ShareContextFactory;
import io.github.lolens.showcaser.core.ShareContextImpl;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class ShareContextFactoryImpl implements ShareContextFactory {

    public static final ShareContextFactoryImpl INSTANCE = new ShareContextFactoryImpl();

    private ShareContextFactoryImpl() {}

    @Override
    public ShareContext create(Identifier id, int syncId, NbtCompound data) {
        return ShareContextImpl.of(id, syncId, data);
    }

    @Override
    public ShareContext create(Identifier id, int syncId) {
        return ShareContextImpl.of(id, syncId);
    }

    @Override
    public ShareContext create(Identifier id, NbtCompound data) {
        return ShareContextImpl.of(id, data);
    }

    @Override
    public ShareContext create(Identifier id) {
        return ShareContextImpl.of(id);
    }
}
