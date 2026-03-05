package io.github.lolens.showcaser.handler.conditional.fabric;

import dev.architectury.fluid.FluidStack;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import dev.emi.emi.api.EmiApi;
import dev.emi.emi.api.stack.*;
import dev.emi.emi.registry.EmiIngredientSerializers;
import dev.emi.emi.screen.RecipeScreen;
import io.github.lolens.showcaser.Showcaser;
import io.github.lolens.showcaser.api.HandlerResult;
import io.github.lolens.showcaser.client.render.icon.FluidStackIconRenderer;
import io.github.lolens.showcaser.client.render.icon.ItemStackIconRenderer;
import io.github.lolens.showcaser.command.ServerCommands;
import io.github.lolens.showcaser.core.builders.ClientChatMessageBuilder;
import io.github.lolens.showcaser.core.builders.handler.ClientHandlerBuilder;
import io.github.lolens.showcaser.core.builders.handler.ServerHandlerBuilder;
import io.github.lolens.showcaser.model.ShareContext;
import io.github.lolens.showcaser.network.Networking;
import io.github.lolens.showcaser.util.FluidUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import static io.github.lolens.showcaser.Showcaser.MOD_ID;
import static io.github.lolens.showcaser.core.builders.ClientChatMessageBuilder.VerifiedType.NONE;

@SuppressWarnings("UnstableApiUsage")
public class EmiHandlerImpl {
    private static final Identifier ID = Identifier.of(MOD_ID, "emi");

    public static void register() {
        ServerCommands.Conditional.EMI.register();
        Networking.Conditional.EMI.register();

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

    @Environment(EnvType.CLIENT)
    private static void registerClient() {
        ClientHandlerBuilder.create(ID)
                .forScreen(null)
                .createContext((screen, contextConsumer) -> {
                    EmiStackInteraction interaction = EmiApi.getHoveredStack(false);
                    EmiIngredient ingredient = EmiStack.EMPTY;
                    Identifier recipeId = null;

                    if (!interaction.isEmpty()) {
                        // ID may be null if used on items like cable anchors from ae2.
                        // In these and other cases send resource to find all recipes for it
                        if (interaction.getRecipeContext() != null && interaction.getRecipeContext().getId() != null) {
                            // handle favorite recipes and recipe outputs in RecipeScreen
                            recipeId = interaction.getRecipeContext().getId();
                        }
                        ingredient = interaction.getStack();
                    }

                    if (screen instanceof RecipeScreen recipeScreen) {
                        EmiIngredient screnIngredient = recipeScreen.getHoveredStack();
                        if (!screnIngredient.isEmpty() && ingredient.isEmpty()) {
                            ingredient = screnIngredient;
                        }
                    }

                    ingredient = ingredient.getEmiStacks().get(0);

                    if (ingredient instanceof FluidEmiStack ||
                            ingredient instanceof ItemEmiStack
                    ) {
                        if (recipeId == null) {
                            contextConsumer.accept(ShareContext.of(ID)
                                    .withJsonElement(EmiIngredientSerializers.serialize(ingredient))
                            );
                            return HandlerResult.SUCCESS;
                        }
                        contextConsumer.accept(ShareContext.of(ID)
                                .withJsonElement(EmiIngredientSerializers.serialize(ingredient))
                                .withIdentifier(recipeId)
                        );
                        return HandlerResult.SUCCESS;
                    }

                    return HandlerResult.PASS;
                })
                .display((player, context) -> {
                    EmiIngredient emiIngredient = EmiIngredientSerializers.deserialize(context.getJsonElement());
                    String clickEventString;

                    if (context.hasIdentifier()) {
                        Identifier recipeId = context.getIdentifier();
                        clickEventString = String.format("/showcaser emi open %s", recipeId.toString());
                    } else {
                        clickEventString = String.format("/showcaser emi open %s", context.getJsonAsNbt());
                    }

                    MutableText text = Text.empty();

                    if (emiIngredient instanceof ItemEmiStack itemEmiStack) {

                        text = ClientChatMessageBuilder.create(context, player, "item")
                                .withDisplayStack(itemEmiStack.getItemStack())
                                .showAmount(false)
                                .withWidth(12)
                                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, clickEventString))
                                .withFormatting(Formatting.UNDERLINE)
                                .setVerified(NONE)
                                .withTranslationKey("showcaser.chat.share_message.recipe")
                                .build();
                    }

                    if (emiIngredient instanceof FluidEmiStack fluidEmiStack) {
                        FluidStack fluidStack = FluidStack.create((Fluid) fluidEmiStack.getKey(), 1000);
                        text = ClientChatMessageBuilder.create(context, player, "fluid")
                                .withCustomStack(fluidStack.getName(), 1000) // amount does not matter probably
                                .withWidth(12)
                                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, clickEventString))
                                .withFormatting(Formatting.UNDERLINE)
                                .showAmount(false)
                                .setVerified(NONE)
                                .withTranslationKey("showcaser.chat.share_message.recipe")
                                .build();
                    }

                    MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(text);
                })
                .withIcon("item", context -> {
                    ItemEmiStack itemEmiStack = (ItemEmiStack) EmiIngredientSerializers.deserialize(context.getJsonElement());
                    return new ItemStackIconRenderer(itemEmiStack.getItemStack());
                })
                .withTooltip("item", (context, player) -> {
                    ItemEmiStack fluidEmiStack = (ItemEmiStack) EmiIngredientSerializers.deserialize(context.getJsonElement());
                    return fluidEmiStack.getItemStack().getTooltip(player, TooltipContext.BASIC);
                })
                .withIcon("fluid", context -> {
                    FluidEmiStack fluidEmiStack = (FluidEmiStack) EmiIngredientSerializers.deserialize(context.getJsonElement());
                    FluidStack fluidStack = (FluidStack.create((Fluid) fluidEmiStack.getKey(), 1000));
                    return new FluidStackIconRenderer(fluidStack);
                })
                .withTooltip("fluid", (context, player) -> {
                    FluidEmiStack fluidEmiStack = (FluidEmiStack) EmiIngredientSerializers.deserialize(context.getJsonElement());
                    FluidStack fluidStack = (FluidStack.create((Fluid) fluidEmiStack.getKey(), 1000));
                    return FluidUtils.buildTooltip(fluidStack, player, false);
                })
                .register();
    }
}