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

package io.github.lolens.showcaser.fabric.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import io.github.lolens.showcaser.fabric.network.message.s2c.conditional.ftbquests.FtbQuestsOpenScreenMessage;
import io.github.lolens.showcaser.fabric.network.message.s2c.conditional.jei.JeiOpenScreenMessage;
import io.github.lolens.showcaser.fabric.network.message.s2c.conditional.jei.JeiOpenScreenMessage.TargetType;
import io.github.lolens.showcaser.network.message.s2c.conditional.emi.EmiOpenScreenMessage;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class FabricServerCommands {

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

        public static class JEI {

            public static void register() {
                CommandRegistrationEvent.EVENT.register((dispatcher, registry, selection) -> {
                    dispatcher.register(
                            CommandManager.literal("showcaser")
                                    .then(CommandManager.literal("jei")
                                            .then(CommandManager.literal("open")
                                                    .then(CommandManager.argument("type", StringArgumentType.string())
                                                            .then(CommandManager.argument("id", IdentifierArgumentType.identifier())
                                                                    .executes(JEI::executeOpen)
                                                            )
                                                    )
                                            )
                                    )
                    );
                });
            }

            private static int executeOpen(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
                ServerPlayerEntity player = context.getSource().getPlayer();
                if (player == null) return 1;

                String type = StringArgumentType.getString(context, "type");
                Identifier id = IdentifierArgumentType.getIdentifier(context, "id");
                TargetType targetType = switch (type) {
                    case "item" -> TargetType.ITEM;
                    case "fluid" -> TargetType.FLUID;
                    default -> throw new IllegalArgumentException("Unexpected value: " + type);
                };
                new JeiOpenScreenMessage(id, targetType).sendTo(player);
                return 1;
            }

        }


    }

}
