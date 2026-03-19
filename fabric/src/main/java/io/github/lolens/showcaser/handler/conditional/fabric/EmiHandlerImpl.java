package io.github.lolens.showcaser.handler.conditional.fabric;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import dev.emi.emi.api.EmiApi;
import dev.emi.emi.api.stack.*;
import dev.emi.emi.registry.EmiIngredientSerializers;
import dev.emi.emi.screen.RecipeScreen;
import io.github.lolens.showcaser.api.event.AdapterRegistrationEvent;
import io.github.lolens.showcaser.api.handler.HandlerResult;
import io.github.lolens.showcaser.api.resource.MessageVerification;
import io.github.lolens.showcaser.api.resource.ShareableFluidStack;
import io.github.lolens.showcaser.api.resource.ShareableItemStack;
import io.github.lolens.showcaser.api.resource.ShareableResource;
import io.github.lolens.showcaser.client.adapter.AdapterRegistry;
import io.github.lolens.showcaser.client.adapter.impl.fabric.EmiAdapter;
import io.github.lolens.showcaser.command.ServerCommands;
import io.github.lolens.showcaser.network.Networking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import static io.github.lolens.showcaser.Showcaser.MOD_ID;
import static io.github.lolens.showcaser.api.ShowcaserAPI.*;
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
                            contextConsumer.accept(getContextFactory().create(ID)
                                    .withJsonElement(EmiIngredientSerializers.serialize(ingredient))
                            );
                            return HandlerResult.SUCCESS;
                        }
                        contextConsumer.accept(getContextFactory().create(ID)
                                .withJsonElement(EmiIngredientSerializers.serialize(ingredient))
                                .withIdentifier(recipeId)
                        );
                        return HandlerResult.SUCCESS;
                    }

                    return HandlerResult.PASS;
                })
                .display((player, context) -> {
                    ShareableResource resource = AdapterRegistry.adapt(context);

                    String clickEventString;

                    if (context.hasIdentifier()) {
                        clickEventString = String.format("/showcaser emi open recipe %s", context.getIdentifier());
                    } else {
                        if (resource instanceof ShareableItemStack itemStack) {
                            clickEventString = String.format("/showcaser emi open item %s", itemStack.getRegistryId());
                        } else if (resource instanceof ShareableFluidStack fluidStack) {
                            clickEventString = String.format("/showcaser emi open fluid %s", fluidStack.getRegistryId());
                        } else {
                            return;
                        }
                    }

                    MutableText text = getMessageBuilderFactory().create(context, player, resource, MessageVerification.NONE)
                            .withWidth(12)
                            .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, clickEventString))
                            .withFormatting(Formatting.UNDERLINE)
                            .showAmount(false)
                            .withTranslationKey("showcaser.chat.share_message.recipe")
                            .build();

                    MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(text);
                })
                .register();

        AdapterRegistrationEvent.EVENT.invoker().register(new EmiAdapter(ID));
    }
}