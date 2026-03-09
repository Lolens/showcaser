package io.github.lolens.showcaser.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import io.github.lolens.showcaser.config.ConfigManager;
import io.github.lolens.showcaser.network.message.s2c.conditional.emi.EmiOpenScreenMessage;
import io.github.lolens.showcaser.network.message.s2c.conditional.emi.EmiOpenScreenMessage.TargetType;
import io.github.lolens.showcaser.network.message.s2c.conditional.rei.ReiOpenScreenMessage;
import net.minecraft.command.argument.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.function.Predicate;

public class ServerCommands {

    public static void register() {

        Predicate<ServerCommandSource> spOrOP = source -> source.getServer().isSingleplayer() || source.hasPermissionLevel(2);

        CommandRegistrationEvent.EVENT.register((dispatcher, registry, selection) -> {
            dispatcher.register(
                    CommandManager.literal("showcaser")
                            .requires(spOrOP)
                            .then(CommandManager.literal("ban")
                                    .then(CommandManager.argument("player", EntityArgumentType.player())
                                            .suggests((context, builder) -> EntityArgumentType.players().listSuggestions(context, builder))
                                            .executes(ServerCommands::executeBan)
                                    )
                            )
                            .then(CommandManager.literal("unban")
                                    .then(CommandManager.argument("player", EntityArgumentType.player())
                                            .suggests((context, builder) -> EntityArgumentType.players().listSuggestions(context, builder))
                                            .executes(ServerCommands::executeUnban)
                                    )
                            )
                            .then(CommandManager.literal("reload")
                                    .executes(ServerCommands::reloadConfigs))
            );
        });
    }

    private static int reloadConfigs(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ConfigManager.loadServer();

        context.getSource().sendMessage(Text.translatable("showcaser.commands.reload").formatted(Formatting.GREEN));
        return 1;
    }

    private static int executeBan(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        PlayerEntity player = EntityArgumentType.getPlayer(context, "player");

        ConfigManager.getStorage().shareBannedPlayer.put(
                player.getUuid(),
                player.getDisplayName().getString()
        );
        ConfigManager.saveStorage();

        context.getSource().sendMessage(Text.translatable("showcaser.commands.sharing.ban", player.getDisplayName()).formatted(Formatting.GREEN));
        return 1;
    }

    private static int executeUnban(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        PlayerEntity player = EntityArgumentType.getPlayer(context, "player");

        ConfigManager.getStorage().shareBannedPlayer.remove(
                player.getUuid()
        );
        ConfigManager.saveStorage();

        context.getSource().sendMessage(Text.translatable("showcaser.commands.sharing.unban", player.getDisplayName()).formatted(Formatting.GREEN));
        return 1;
    }

    public static class Conditional {

        public static class EMI {

            public static void register() {
                CommandRegistrationEvent.EVENT.register((dispatcher, registry, selection) -> {
                    dispatcher.register(
                            CommandManager.literal("showcaser")
                                    .then(CommandManager.literal("emi")
                                            .then(CommandManager.literal("open")
                                                    .then(CommandManager.argument("type", StringArgumentType.string())
                                                            .then(CommandManager.argument("id", IdentifierArgumentType.identifier())
                                                                    .executes(EMI::executeOpen)
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
                    case "recipe" -> TargetType.RECIPE;
                    case "item" -> TargetType.ITEM;
                    case "fluid" -> TargetType.FLUID;
                    default -> throw new IllegalArgumentException("Unexpected value: " + type);
                };
                new EmiOpenScreenMessage(id, targetType).sendTo(player);
                return 1;
            }

        } // end static class EMI


        public static class REI {

            public static void register() {


                CommandRegistrationEvent.EVENT.register((dispatcher, registry, selection) -> {
                    dispatcher.register(
                            CommandManager.literal("showcaser")
                                    .then(CommandManager.literal("rei")
                                            .then(CommandManager.literal("open")
                                                    .then(CommandManager.argument("nbt", NbtCompoundArgumentType.nbtCompound())
                                                            .executes(REI::executeOpenNbt)
                                                    )
                                            )
                                    )
                    );
                });

            } // end register REI

            private static int executeOpenNbt(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
                ServerPlayerEntity player = context.getSource().getPlayer();
                if (player == null) return 1;
                NbtCompound entryStack = NbtCompoundArgumentType.getNbtCompound(context, "nbt");

                new ReiOpenScreenMessage(entryStack).sendTo(player);
                return 1;
            }

        } // end static class REI


    }

}
