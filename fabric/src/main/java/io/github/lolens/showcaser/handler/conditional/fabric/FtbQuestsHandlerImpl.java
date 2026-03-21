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
import dev.ftb.mods.ftblibrary.ui.ScreenWrapper;
import dev.ftb.mods.ftbquests.client.ClientQuestFile;
import dev.ftb.mods.ftbquests.client.gui.quests.QuestScreen;
import dev.ftb.mods.ftbquests.quest.Quest;
import io.github.lolens.showcaser.api.handler.HandlerResult;
import io.github.lolens.showcaser.api.resource.EmptyResource;
import io.github.lolens.showcaser.api.resource.MessageVerification;
import io.github.lolens.showcaser.fabric.command.FabricServerCommands;
import io.github.lolens.showcaser.fabric.network.FabricNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.Optional;

import static io.github.lolens.showcaser.Showcaser.MOD_ID;
import static io.github.lolens.showcaser.api.ShowcaserAPI.*;

public class FtbQuestsHandlerImpl {
    private static final Identifier ID = Identifier.of(MOD_ID, "ftbquests");

    public static void register() {
        FabricServerCommands.Conditional.FTBQuests.register();
        FabricNetworking.Conditional.FTBQuests.register();

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
        getHandlerBuilderFactory().<ScreenWrapper>createClientBuilder(ID)
                .forScreen(ScreenWrapper.class) // workaround to handle ftblib screens
                .createContext((screen, contextConsumer) -> {
                    if (screen.getGui() instanceof QuestScreen questScreen) {

                        // true if quest book is opened and no quest selected
                        if (questScreen.getViewedQuest() == null) return HandlerResult.STOP;

                        long questId = questScreen.getViewedQuest().getId();

                        contextConsumer.accept(getContextFactory().create(ID)
                                .with("questId", questId)
                        );

                        return HandlerResult.SUCCESS;
                    }

                    return HandlerResult.PASS;
                })
                .display((player, context) -> {
                    long questId = context.getLong("questId");
                    Quest quest = ClientQuestFile.INSTANCE.getQuest(questId);
                    if (quest == null) return;

                    String clickEventString = String.format("/showcaser ftbquests open %s", questId);

                    MutableText text = getMessageBuilderFactory().create(context, player, new EmptyResource(), MessageVerification.NONE)
                            .withWidth(0)
                            .withForcedDisplayName(quest.getTitle())
                            .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, clickEventString))
                            .withFormatting(Formatting.UNDERLINE)
                            .withTranslationKey("showcaser.chat.share_message.quest")
                            .build();

                    MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(text);
                })
                .register();
    }
}