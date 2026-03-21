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

package io.github.lolens.showcaser.handler.conditional.forge;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import io.github.lolens.showcaser.api.event.AdapterRegistrationEvent;
import io.github.lolens.showcaser.api.handler.HandlerResult;
import io.github.lolens.showcaser.api.resource.MessageVerification;
import io.github.lolens.showcaser.api.resource.ShareableFluidStack;
import io.github.lolens.showcaser.api.resource.ShareableItemStack;
import io.github.lolens.showcaser.api.resource.ShareableResource;
import io.github.lolens.showcaser.client.adapter.AdapterRegistry;
import io.github.lolens.showcaser.client.adapter.impl.forge.JeiAdapter;
import io.github.lolens.showcaser.forge.command.ForgeServerCommands;
import io.github.lolens.showcaser.forge.network.ForgeNetworking;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.runtime.IJeiRuntime;
import mezz.jei.api.runtime.IRecipesGui;
import mezz.jei.common.Internal;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraftforge.fluids.FluidStack;

import static io.github.lolens.showcaser.Showcaser.MOD_ID;
import static io.github.lolens.showcaser.api.ShowcaserAPI.*;

public class JeiHandlerImpl {

    private static final Identifier ID = Identifier.of(MOD_ID, "jei");

    public static void register() {
        ForgeServerCommands.Conditional.JEI.register();
        ForgeNetworking.Conditional.JEI.register();

        registerServer();

        if (Platform.getEnvironment() == Env.CLIENT) {
            registerClient();
        }
    }

    private static void registerServer() {
        getHandlerBuilderFactory().createServerBuilder(ID)
                .forContainer(null)
                .process((player, context) -> {
                    return context;
                })
                .register();
    }

    private static void registerClient() {
        getHandlerBuilderFactory().createClientBuilder(ID)
                .forScreen(null)
                .priority(200)
                .createContext((screen, contextConsumer) -> {
                    // remapping goes wrong while compiling against api from
                    // suggested repo so curse.maven is used and project is compiled
                    // using full JEI mod and Internal.getJeiRuntime is probably stable enough
                    IJeiRuntime runtime = Internal.getJeiRuntime(); // throws IllegalState if null

                    IRecipesGui recipesGui = runtime.getRecipesGui();

                    // try to get item stack from overlay and then from the recipe gui if it is null
                    ItemStack stack = runtime.getIngredientListOverlay().getIngredientUnderMouse(VanillaTypes.ITEM_STACK);
                    if (stack == null) stack = recipesGui.getIngredientUnderMouse(VanillaTypes.ITEM_STACK).orElse(null);

                    if (stack != null) {
                        NbtCompound entryNbt = new NbtCompound();
                        stack.writeNbt(entryNbt);
                        contextConsumer.accept(
                                getContextFactory().create(ID)
                                        .with("entry", entryNbt)
                                        .with("type", "minecraft:item")
                        );
                        return HandlerResult.SUCCESS;
                    }

                    FluidStack fluidIngredient = runtime.getIngredientListOverlay().getIngredientUnderMouse(ForgeTypes.FLUID_STACK);
                    if (fluidIngredient == null)
                        fluidIngredient = recipesGui.getIngredientUnderMouse(ForgeTypes.FLUID_STACK).orElse(null);

                    if (fluidIngredient != null) {
                        FluidStack fluidStack = new FluidStack(fluidIngredient.getFluid(), 1000);
                        NbtCompound entryNbt = new NbtCompound();
                        fluidStack.writeToNBT(entryNbt);
                        contextConsumer.accept(
                                getContextFactory().create(ID)
                                        .with("entry", entryNbt)
                                        .with("type", "minecraft:fluid")
                        );
                        return HandlerResult.SUCCESS;
                    }
                    return HandlerResult.PASS;
                })
                .display((player, context) -> {
                    ShareableResource resource = AdapterRegistry.adapt(context);

                    String clickEventString;
                    if (resource instanceof ShareableItemStack stack) {
                        clickEventString = String.format("/showcaser jei open item %s", stack.getRegistryId());
                    } else if (resource instanceof ShareableFluidStack stack) {
                        clickEventString = String.format("/showcaser jei open fluid %s", stack.getRegistryId());
                    } else {
                        return;
                    }

                    MutableText text = getMessageBuilderFactory().create(context, player, resource, MessageVerification.NONE)
                            .withWidth(12)
                            .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, clickEventString))
                            .withFormatting(Formatting.UNDERLINE)
                            .withTranslationKey("showcaser.chat.share_message.recipe")
                            .build();

                    MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(text);
                })
                .register();

        AdapterRegistrationEvent.EVENT.invoker().register(new JeiAdapter(ID));
    }


}
