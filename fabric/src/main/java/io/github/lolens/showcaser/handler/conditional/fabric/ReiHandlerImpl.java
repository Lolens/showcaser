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

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import io.github.lolens.showcaser.Showcaser;
import io.github.lolens.showcaser.api.event.AdapterRegistrationEvent;
import io.github.lolens.showcaser.api.handler.HandlerResult;
import io.github.lolens.showcaser.api.resource.MessageVerification;
import io.github.lolens.showcaser.api.resource.ShareableResource;
import io.github.lolens.showcaser.client.adapter.AdapterRegistry;
import io.github.lolens.showcaser.client.adapter.impl.fabric.ReiAdapter;
import io.github.lolens.showcaser.command.ServerCommands;
import io.github.lolens.showcaser.network.Networking;
import me.shedaniel.rei.api.client.REIRuntime;
import me.shedaniel.rei.api.client.overlay.ScreenOverlay;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.EntryType;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.Optional;

import static io.github.lolens.showcaser.Showcaser.MOD_ID;
import static io.github.lolens.showcaser.api.ShowcaserAPI.*;
public class ReiHandlerImpl {

    private static final Identifier ID = Identifier.of(MOD_ID, "rei");

    public static void register() {
        ServerCommands.Conditional.REI.register();
        Networking.Conditional.REI.register();

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
                .createContext((screen, contextConsumer) -> {
                    Optional<ScreenOverlay> opt = REIRuntime.getInstance().getOverlay();

                    if (opt.isEmpty()) return HandlerResult.PASS;

                    EntryStack<?> entryStack = opt.get().getEntryList().getFocusedStack();

                    if (entryStack.isEmpty()) return HandlerResult.PASS;

                    EntryType<?> entryType = entryStack.getType();
                    NbtCompound entryNbt = entryStack.saveStack();

                    if (entryNbt == null) {
                        Showcaser.LOGGER.warn("Tried creating context for stack that cannot be saved to nbt. Screen: {}",
                                screen.getClass().getName());
                        return HandlerResult.PASS;
                    }

                    if (entryType == VanillaEntryTypes.ITEM) {
                        entryNbt.putString("type", "minecraft:item");
                        contextConsumer.accept(getContextFactory().create(ID)
                                .with("entry", entryNbt));
                        return HandlerResult.SUCCESS;
                    }
                    if (entryType == VanillaEntryTypes.FLUID) {
                        entryNbt.putString("type", "minecraft:fluid");
                        contextConsumer.accept(getContextFactory().create(ID)
                                .with("entry", entryNbt));
                        return HandlerResult.SUCCESS;
                    }

                    return HandlerResult.PASS;
                })
                .display((player, context) -> {
                    ShareableResource resource = AdapterRegistry.adapt(context);

                    NbtCompound entryNbt = context.getCompound("entry");
                    EntryStack<?> entryStack = EntryStack.read(entryNbt);
                    EntryType<?> entryType = entryStack.getType();
                    MutableText text = Text.empty();

                    if (entryType == VanillaEntryTypes.ITEM) {
                        text = getMessageBuilderFactory().create(context, player, resource, MessageVerification.NONE)
                                .withWidth(12)
                                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                                        String.format("/showcaser rei open %s", entryStack.wildcard().saveStack())))
                                .withFormatting(Formatting.UNDERLINE)
                                .withTranslationKey("showcaser.chat.share_message.recipe")
                                .build();
                    }
                    if (entryType == VanillaEntryTypes.FLUID) {
                        text = getMessageBuilderFactory().create(context, player, resource, MessageVerification.NONE)
                                .showAmount(false)
                                .withWidth(12)
                                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                                        String.format("/showcaser rei open %s", entryStack.wildcard().saveStack())))
                                .withFormatting(Formatting.UNDERLINE)
                                .withTranslationKey("showcaser.chat.share_message.recipe")
                                .build();
                    }

                    MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(text);
                })
                .register();

        AdapterRegistrationEvent.EVENT.invoker().register(new ReiAdapter(ID));
    }
}
