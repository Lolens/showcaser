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

package io.github.lolens.showcaser.client.adapter.impl.fabric;

import dev.architectury.fluid.FluidStack;
import io.github.lolens.showcaser.api.adapter.BaseShareContextAdapter;
import io.github.lolens.showcaser.api.resource.EmptyResource;
import io.github.lolens.showcaser.api.resource.ShareableFluidStack;
import io.github.lolens.showcaser.api.resource.ShareableItemStack;
import io.github.lolens.showcaser.api.resource.ShareableResource;
import io.github.lolens.showcaser.api.sharecontext.ShareContext;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class ReiAdapter extends BaseShareContextAdapter {

    public ReiAdapter(Identifier identifier) {
        super(identifier);
    }

    @Override
    public ShareableResource adapt(ShareContext context) {
        NbtCompound entryNbt = context.getCompound("entry");
        EntryStack<?> entry = EntryStack.read(entryNbt);

        if (entry.getType() == VanillaEntryTypes.ITEM) {
            ItemStack stack = entry.castValue();
            return new ShareableItemStack(stack);
        }
        if (entry.getType() == VanillaEntryTypes.FLUID) {
            FluidStack fluid = entry.castValue();
            return new ShareableFluidStack(fluid);
        }

        return new EmptyResource();
    }
}
