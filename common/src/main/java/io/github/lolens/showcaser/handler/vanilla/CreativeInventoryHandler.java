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

package io.github.lolens.showcaser.handler.vanilla;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import io.github.lolens.showcaser.api.event.AdapterRegistrationEvent;
import io.github.lolens.showcaser.api.handler.HandlerResult;
import io.github.lolens.showcaser.api.resource.MessageVerification;
import io.github.lolens.showcaser.api.resource.ShareableResource;
import io.github.lolens.showcaser.api.sharecontext.ShareContext;
import io.github.lolens.showcaser.client.adapter.AdapterRegistry;
import io.github.lolens.showcaser.client.adapter.impl.ItemStackAdapter;
import io.github.lolens.showcaser.mixin.CreativeInventoryScreenHandlerMixin;
import io.github.lolens.showcaser.mixin.CreativeInventoryScreenMixin;
import io.github.lolens.showcaser.mixin.HandledScreenMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.MutableText;
import net.minecraft.util.Identifier;

import static io.github.lolens.showcaser.Showcaser.MOD_ID;
import static io.github.lolens.showcaser.api.ShowcaserAPI.*;

public class CreativeInventoryHandler {
    private static final Identifier ID = Identifier.of(MOD_ID, "creative_inventory");

    public static void register() {
        registerServer();

        if (Platform.getEnvironment() == Env.CLIENT) {
            registerClient();
        }
    }

    private static void registerServer() {
        getHandlerBuilderFactory().createServerBuilder(ID)
                .forContainer(null) // does not exist at server
                .process((player, context) -> {
                    if (!player.isCreative()) return null;

                    return getContextFactory().create(ID)
                            .withItemStack(context.getItemStack());
                })
                .register();
    }

    private static void registerClient() {
        getHandlerBuilderFactory().<CreativeInventoryScreen>createClientBuilder(ID)
                .forScreen(CreativeInventoryScreen.class)
                .createContext((screen, contextConsumer) -> {
                    CreativeInventoryScreen.CreativeScreenHandler handler = screen.getScreenHandler();

                    ItemGroup.Type type = ((CreativeInventoryScreenMixin) screen).showcaser$getSelectedTab().getType();

                    Slot slot = ((HandledScreenMixin) screen).showcaser$getFocusedSlot();
                    if (slot == null) return HandlerResult.PASS;

                    switch (type) {
                        case INVENTORY -> {
                            if (slot.hasStack()) {
                                ShareContext shareContext = getContextFactory().create(
                                                ID,
                                                screen.getScreenHandler().syncId)
                                        .withItemStack(slot.getStack());
                                contextConsumer.accept(shareContext);
                                return HandlerResult.SUCCESS;
                            }
                        }

                        case CATEGORY -> {
                            // hotbar slots

                            if (slot.id >= 45 && slot.id <= 53) {

                                // fixes sharing empty hotbar slots while in category
                                if (!slot.hasStack()) return HandlerResult.STOP;

                                ShareContext shareContext = getContextFactory().create(
                                                ID,
                                                screen.getScreenHandler().syncId)
                                        .withItemStack(slot.getStack());
                                contextConsumer.accept(shareContext);
                                return HandlerResult.SUCCESS;
                            }

                            float scrollPosition = ((CreativeInventoryScreenMixin) screen).showcaser$getScrollPosition();
                            int row = ((CreativeInventoryScreenHandlerMixin) screen.getScreenHandler()).showcaser$getRow(scrollPosition);
                            int listId = slot.id + (9 * row);

                            // do not allow fallback handler handle this
                            if (listId >= handler.itemList.size()) return HandlerResult.STOP;

                            ItemStack stack = screen.getScreenHandler().itemList.get(slot.id + (9 * row));
                            ShareContext shareContext = getContextFactory().create(
                                            ID,
                                            screen.getScreenHandler().syncId)
                                    .withItemStack(stack);

                            contextConsumer.accept(shareContext);
                            return HandlerResult.SUCCESS;
                        }
                    }

                    return HandlerResult.STOP;
                })
                .display((player, context) -> {
                    ShareableResource resource = AdapterRegistry.adapt(context);

                    MutableText text = getMessageBuilderFactory().create(context, player, resource, MessageVerification.VERIFIED)
                            .withWidth(12)
                            .build();

                    MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(text);
                })
                .register();

        AdapterRegistrationEvent.EVENT.invoker().register(new ItemStackAdapter(ID));
    }
}