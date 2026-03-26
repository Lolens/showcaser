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

package io.github.lolens.showcaser.handler.conditional.fabric;

import appeng.api.config.Actionable;
import appeng.api.stacks.AEKey;
import appeng.client.gui.implementations.UpgradeableScreen;
import appeng.client.gui.me.common.MEStorageScreen;
import appeng.client.gui.me.common.RepoSlot;
import appeng.menu.AEBaseMenu;
import appeng.menu.me.common.GridInventoryEntry;
import appeng.menu.me.common.MEStorageMenu;
import appeng.menu.slot.FakeSlot;
import appeng.menu.slot.PatternTermSlot;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import io.github.lolens.showcaser.api.event.AdapterRegistrationEvent;
import io.github.lolens.showcaser.api.handler.HandlerResult;
import io.github.lolens.showcaser.api.resource.MessageVerification;
import io.github.lolens.showcaser.api.resource.ShareableResource;
import io.github.lolens.showcaser.api.sharecontext.ShareContext;
import io.github.lolens.showcaser.client.ClientHandlerCache;
import io.github.lolens.showcaser.client.adapter.AdapterRegistry;
import io.github.lolens.showcaser.client.adapter.impl.fabric.Ae2Adapter;
import io.github.lolens.showcaser.exception.ServerShareProcessingException;
import io.github.lolens.showcaser.fabric.mixin.MEStorageMenuInvoker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.MutableText;
import net.minecraft.util.Identifier;

import static io.github.lolens.showcaser.Showcaser.MOD_ID;
import static io.github.lolens.showcaser.api.ShowcaserAPI.*;
import static io.github.lolens.showcaser.util.HandlerUtils.getHandler;
import static io.github.lolens.showcaser.util.HandlerUtils.isValidSyncId;

@SuppressWarnings("rawtypes")
public class Ae2HandlerImpl {
    private static final Identifier ID = Identifier.of(MOD_ID, "ae2");

    public static void register() {

        registerServer();

        if (Platform.getEnvironment() == Env.CLIENT) {
            ClientHandlerCache.blacklistWithInheritors(UpgradeableScreen.class);
            registerClient();
        }
    }

    private static void registerServer() {
        getHandlerBuilderFactory().<AEBaseMenu>createServerBuilder(ID)
                .forContainer(AEBaseMenu.class)
                .process((player, context) -> {
                    if (!isValidSyncId(player, context.getSyncId()))
                        throw new ServerShareProcessingException(context, player, ServerShareProcessingException.SYNC_ID_NOT_VALID);

                    MEStorageMenu menu = (MEStorageMenu) getHandler(player);

                    long serial = context.getLong("serial");

                    AEKey key = ((MEStorageMenuInvoker) menu).showcaser$getStackBySerial(serial);

                    long amount = menu.getHost().getInventory().extract(
                            key,
                            Long.MAX_VALUE,
                            Actionable.SIMULATE,
                            menu.getActionSource()
                    );

                    NbtCompound keyCompound = key.toTagGeneric();
                    return getContextFactory().create(ID)
                            .with("key", keyCompound)
                            .withAmount(amount); // amount in droplets on fabric. Gets converted at display
                })
                .register();
    }

    private static void registerClient() {
        getHandlerBuilderFactory().<MEStorageScreen>createClientBuilder(ID)
                .forScreen(MEStorageScreen.class)
                .createContext((screen, contextConsumer) -> {
                    Slot slot = screen.getSlotUnderMouse();

                    // prevent fallback handler handling for slots that can be filled with ghost items
                    if (slot instanceof FakeSlot || slot instanceof PatternTermSlot) return HandlerResult.STOP;

                    if (slot instanceof RepoSlot repoSlot && repoSlot.hasStack()) {

                        GridInventoryEntry entry = repoSlot.getEntry();

                        // for cases when "something" is 0 and displayed for crafting availability reason
                        if (entry.getStoredAmount() == 0) return HandlerResult.STOP;

                        ShareContext context = getContextFactory().create(
                                ID,
                                screen.getScreenHandler().syncId
                        ).with("serial", entry.getSerial());

                        contextConsumer.accept(context);
                        return HandlerResult.SUCCESS;
                    }
                    return HandlerResult.PASS;
                })
                .display((player, context) -> {
                    ShareableResource resource = AdapterRegistry.adapt(context);

                    MutableText text = getMessageBuilderFactory().create(context, player, resource, MessageVerification.VERIFIED)
                            .showAmount(true)
                            .build();

                    MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(text);
                })
                .register();

        AdapterRegistrationEvent.EVENT.invoker().register(new Ae2Adapter(ID));
    }
}