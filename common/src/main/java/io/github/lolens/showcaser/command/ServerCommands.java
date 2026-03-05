package io.github.lolens.showcaser.command;

import com.google.gson.JsonElement;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.JsonOps;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.emi.emi.api.EmiApi;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.serializer.EmiIngredientSerializer;
import io.github.lolens.showcaser.Showcaser;
import io.github.lolens.showcaser.config.ConfigManager;
import io.github.lolens.showcaser.network.message.s2c.conditional.emi.EmiOpenScreenMessage;
import io.github.lolens.showcaser.network.message.s2c.conditional.rei.ReiOpenScreenMessage;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.command.argument.NbtCompoundArgumentType;
import net.minecraft.command.argument.NbtElementArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
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
        ConfigManager.loadAll();

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
                                                    .then(CommandManager.argument("recipe_id", IdentifierArgumentType.identifier())
                                                            .executes(EMI::executeOpenRecipe)
                                                    )

                                                    .then(CommandManager.argument("output_resource", NbtElementArgumentType.nbtElement())
                                                            .executes(EMI::executeOpenResource)
                                                    )
                                            )
                                    )
                    );
                });
            }

            private static int executeOpenRecipe(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
                ServerPlayerEntity player = context.getSource().getPlayer();
                if (player == null) return 1;
                Identifier recipeId = IdentifierArgumentType.getIdentifier(context, "recipe_id");

                new EmiOpenScreenMessage(recipeId).sendTo(player);
                return 1;
            }

            private static int executeOpenResource(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
                ServerPlayerEntity player = context.getSource().getPlayer();
                if (player == null) return 1;

                NbtElement nbtElement = NbtElementArgumentType.getNbtElement(context, "output_resource");

                new EmiOpenScreenMessage(nbtElement).sendTo(player);
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
