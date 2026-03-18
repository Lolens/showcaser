package io.github.lolens.showcaser.handler.conditional.forge;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import dev.ftb.mods.ftbquests.client.ClientQuestFile;
import dev.ftb.mods.ftbquests.client.gui.quests.QuestScreen;
import dev.ftb.mods.ftbquests.quest.Quest;
import io.github.lolens.showcaser.api.ShowcaserAPI;
import io.github.lolens.showcaser.api.handler.HandlerResult;
import io.github.lolens.showcaser.api.resource.EmptyResource;
import io.github.lolens.showcaser.api.resource.MessageVerification;
import io.github.lolens.showcaser.client.ClientChatMessageBuilder;
import io.github.lolens.showcaser.forge.command.ForgeServerCommands;
import io.github.lolens.showcaser.core.builder.handler.ClientHandlerBuilder;
import io.github.lolens.showcaser.core.builder.handler.ServerHandlerBuilder;
import io.github.lolens.showcaser.forge.network.ForgeNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.Optional;

import static io.github.lolens.showcaser.Showcaser.MOD_ID;

public class FtbQuestsHandlerImpl {
    private static final Identifier ID = Identifier.of(MOD_ID, "ftbquests");

    public static void register() {
        ForgeServerCommands.Conditional.FTBQuests.register();
        ForgeNetworking.Conditional.FTBQuests.register();

        registerServer();

        if (Platform.getEnvironment() == Env.CLIENT) {
            registerClient();
        }
    }

    private static void registerServer() {
        ServerHandlerBuilder.create(ID)
                .forContainer(null)
                .process((player, context) -> {
                    return context;
                })
                .register();
    }

    private static void registerClient() {
        ClientHandlerBuilder.create(ID)
                .forScreen(null)
                .createContext((screen, contextConsumer) -> {

                    Optional<QuestScreen> opt = ClientQuestFile.INSTANCE.getQuestScreen();

                    if (opt.isPresent()) {

                        QuestScreen questScreen = opt.get();

                        if (questScreen.getViewedQuest() == null) return HandlerResult.PASS;
                        long questId = questScreen.getViewedQuest().getId();

                        contextConsumer.accept(ShowcaserAPI.getContextFactory().create(ID)
                                .with("questId", questId)
                        );

                        return HandlerResult.SUCCESS;
                    }

                    // no other handlers should
                    return HandlerResult.PASS;
                })
                .display((player, context) -> {
                    long questId = context.getLong("questId");
                    Quest quest = ClientQuestFile.INSTANCE.getQuest(questId);
                    if (quest == null) return;

                    String clickEventString = String.format("/showcaser ftbquests open %s", questId);

                    MutableText text = ClientChatMessageBuilder.create(context, player, new EmptyResource(), MessageVerification.NONE)
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