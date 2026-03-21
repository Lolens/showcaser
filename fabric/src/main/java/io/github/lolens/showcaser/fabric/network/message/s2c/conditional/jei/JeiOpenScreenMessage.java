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

package io.github.lolens.showcaser.fabric.network.message.s2c.conditional.jei;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.emi.emi.api.EmiApi;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.stack.EmiStack;
import io.github.lolens.showcaser.fabric.network.FabricNetworking;
import io.github.lolens.showcaser.network.Networking;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.fabric.constants.FabricTypes;
import mezz.jei.api.fabric.ingredients.fluids.IJeiFluidIngredient;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.common.Internal;
import mezz.jei.fabric.ingredients.fluid.JeiFluidIngredient;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class JeiOpenScreenMessage extends BaseS2CMessage {
    private final Identifier target;
    private final TargetType type;

    public enum TargetType {
        ITEM,
        FLUID
    }

    public JeiOpenScreenMessage(Identifier resourceId, TargetType type) {
        this.type = type;
        this.target = resourceId;
    }

    public JeiOpenScreenMessage(PacketByteBuf buf) {
        this.type = buf.readEnumConstant(TargetType.class);
        this.target = buf.readIdentifier();
    }

    @Override
    public MessageType getType() {
        return FabricNetworking.Conditional.JEI.MessageTypes.S2C.JEI_OPEN_SCREEN;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeEnumConstant(type);
        buf.writeIdentifier(target);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        switch (type) {
            case ITEM -> {
                Item item = Registries.ITEM.get(target);
                IFocus<ItemStack> focus = Internal.getJeiRuntime().getJeiHelpers().getFocusFactory().createFocus(
                        RecipeIngredientRole.OUTPUT,
                        VanillaTypes.ITEM_STACK,
                        item.getDefaultStack()
                );
                Internal.getJeiRuntime().getRecipesGui().show(focus);
            }
            case FLUID -> {
                Fluid fluid = Registries.FLUID.get(target);
                IFocus<IJeiFluidIngredient> focus = Internal.getJeiRuntime().getJeiHelpers().getFocusFactory().createFocus(
                        RecipeIngredientRole.OUTPUT,
                        FabricTypes.FLUID_STACK,
                        new JeiFluidIngredient(fluid, 1000)
                );
                Internal.getJeiRuntime().getRecipesGui().show(focus);
            }
        }

    }
}