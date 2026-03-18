package io.github.lolens.showcaser.command;

import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.context.CommandContext;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import io.github.lolens.showcaser.network.message.s2c.conditional.ftbquests.FtbQuestsOpenScreenMessage;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class ForgeServerCommands {

    public static class Conditional {

        public static class FTBQuests {

            public static void register() {
                CommandRegistrationEvent.EVENT.register((dispatcher, registry, selection) -> {
                    dispatcher.register(
                            CommandManager.literal("showcaser")
                                    .then(CommandManager.literal("ftbquests")
                                            .then(CommandManager.literal("open")
                                                    .then(CommandManager.argument("id", LongArgumentType.longArg())
                                                            .executes(FTBQuests::executeOpen)
                                                    )
                                            )
                                    )
                    );
                });
            }

            private static int executeOpen(CommandContext<ServerCommandSource> context) {
                ServerPlayerEntity player = context.getSource().getPlayer();
                if (player == null) return 1;

                long questId = LongArgumentType.getLong(context, "id");

                new FtbQuestsOpenScreenMessage(questId).sendTo(player);
                return 1;
            }

        }


    }

}
