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
import io.github.lolens.showcaser.api.handler.HandlerResult;
import io.github.lolens.showcaser.api.resource.MessageVerification;
import io.github.lolens.showcaser.api.resource.ShareableItemStack;
import io.github.lolens.showcaser.api.sharecontext.ShareContext;
import io.github.lolens.showcaser.forge.command.ForgeServerCommands;
import io.github.lolens.showcaser.forge.network.ForgeNetworking;
import io.github.lolens.showcaser.forge.network.message.s2c.conditional.patchouli.PatchouliOpenScreenMessage;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.gui.GuiBookEntry;
import vazkii.patchouli.client.book.gui.GuiBookLanding;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.book.BookRegistry;

import static io.github.lolens.showcaser.Showcaser.MOD_ID;
import static io.github.lolens.showcaser.api.ShowcaserAPI.*;
import static io.github.lolens.showcaser.forge.network.message.s2c.conditional.patchouli.PatchouliOpenScreenMessage.TargetType.BOOK;
import static io.github.lolens.showcaser.forge.network.message.s2c.conditional.patchouli.PatchouliOpenScreenMessage.TargetType.ENTRY;

public class PatchouliHandlerImpl {
    private static final Identifier ID = Identifier.of(MOD_ID, "patchouli");

    public static void register() {
        ForgeServerCommands.Conditional.Patchouli.register();
        ForgeNetworking.Conditional.Patchouli.register();

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
        getHandlerBuilderFactory().<GuiBook>createClientBuilder(ID)
                .forScreen(GuiBook.class)
                .createContext((screen, contextConsumer) -> {

                    // works only for book's main page and entries
                    if (!(screen instanceof GuiBookEntry || screen instanceof GuiBookLanding)) return HandlerResult.STOP;

                    Identifier book = screen.book.id;
                    Identifier entry = screen instanceof GuiBookEntry ent ? ent.getEntry().getId() : null;
                    int page = screen.getSpread();

                    ShareContext context = getContextFactory().create(ID)
                            .withIdentifier("book", book)
                            .with("page", page);
                    if (entry != null) context.withIdentifier("entry", entry);

                    contextConsumer.accept(context);
                    return HandlerResult.SUCCESS;
                })
                .display((player, context) -> {
                    Identifier bookId = context.getIdentifier("book");
                    Identifier entryId = context.hasIdentifier("entry") ? context.getIdentifier("entry") : null;
                    int page = context.getInt("page");

                    PatchouliOpenScreenMessage.TargetType type = entryId != null ? ENTRY : BOOK;

                    ItemStack bookStack = PatchouliAPI.get().getBookStack(bookId);

                    String clickEventString = type == ENTRY ?
                            String.format("/showcaser patchouli open %s %s %s", bookId, entryId, page) :
                            String.format("/showcaser patchouli open %s", bookId);

                    Book book = BookRegistry.INSTANCE.books.get(bookId);

                    Text displayName = type == ENTRY ? book.getContents().entries.get(entryId).getName() : bookStack.getName();

                    MutableText text = getMessageBuilderFactory().create(context, player, new ShareableItemStack(bookStack), MessageVerification.NONE)
                            .withForcedDisplayName(displayName)
                            .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, clickEventString))
                            .withFormatting(Formatting.UNDERLINE)
                            .withTranslationKey("showcaser.chat.share_message.patchouli_book" + (type == ENTRY ? "_entry" : ""))
                            .build();

                    MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(text);
                })
                .register();
    }
}