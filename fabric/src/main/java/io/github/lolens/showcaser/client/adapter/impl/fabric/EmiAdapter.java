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

import com.google.gson.JsonElement;
import dev.architectury.fluid.FluidStack;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.FluidEmiStack;
import dev.emi.emi.api.stack.ItemEmiStack;
import dev.emi.emi.api.stack.serializer.EmiIngredientSerializer;
import io.github.lolens.showcaser.api.adapter.BaseShareContextAdapter;
import io.github.lolens.showcaser.api.resource.EmptyResource;
import io.github.lolens.showcaser.api.resource.ShareableFluidStack;
import io.github.lolens.showcaser.api.resource.ShareableItemStack;
import io.github.lolens.showcaser.api.resource.ShareableResource;
import io.github.lolens.showcaser.api.sharecontext.ShareContext;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class EmiAdapter extends BaseShareContextAdapter {

    public EmiAdapter(Identifier identifier) {
        super(identifier);
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public Optional<ShareableResource> adapt(ShareContext context) {
        JsonElement json = context.getJsonElement();
        EmiIngredient ingredient = EmiIngredientSerializer.getDeserialized(json);

        if (ingredient instanceof ItemEmiStack emiStack) {
            return Optional.of(new ShareableItemStack(emiStack.getItemStack()));
        }
        if (ingredient instanceof FluidEmiStack emiStack) {
            FluidStack fluidStackArch = FluidStack.create((Fluid) emiStack.getKey(), 1000);
            return Optional.of(new ShareableFluidStack(fluidStackArch));
        }

        return Optional.empty();
    }
}
