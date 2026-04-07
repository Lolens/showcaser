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

import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import dev.architectury.fluid.FluidStack;
import io.github.lolens.showcaser.api.adapter.BaseShareContextAdapter;
import io.github.lolens.showcaser.api.resource.EmptyResource;
import io.github.lolens.showcaser.api.resource.ShareableFluidStack;
import io.github.lolens.showcaser.api.resource.ShareableItemStack;
import io.github.lolens.showcaser.api.resource.ShareableResource;
import io.github.lolens.showcaser.api.sharecontext.ShareContext;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class Ae2Adapter extends BaseShareContextAdapter {

    public Ae2Adapter(Identifier identifier) {
        super(identifier);
    }

    @Override
    public Optional<ShareableResource> adapt(ShareContext context) {
        AEKey key = AEKey.fromTagGeneric(context.getCompound("key"));
        long amount = context.getAmount();

        if (key instanceof AEItemKey itemKey) {
            return Optional.of(new ShareableItemStack(itemKey.toStack(), amount));
        }
        if (key instanceof AEFluidKey fluidKey) {
            FluidStack fluidStack = FluidStack.create(fluidKey.getFluid(), amount);
            return Optional.of(new ShareableFluidStack(fluidStack));
        }

        return Optional.empty();
    }
}
