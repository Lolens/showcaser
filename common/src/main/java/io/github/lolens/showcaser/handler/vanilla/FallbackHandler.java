package io.github.lolens.showcaser.handler.vanilla;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import io.github.lolens.showcaser.Showcaser;
import io.github.lolens.showcaser.api.HandlerResult;
import io.github.lolens.showcaser.client.render.icon.ItemStackIconRenderer;
import io.github.lolens.showcaser.core.builders.ClientChatMessageBuilder;
import io.github.lolens.showcaser.core.builders.handler.ClientHandlerBuilder;
import io.github.lolens.showcaser.core.builders.handler.ServerHandlerBuilder;
import io.github.lolens.showcaser.exception.ServerShareProcessingException;
import io.github.lolens.showcaser.mixin.HandledScreenMixin;
import io.github.lolens.showcaser.model.ShareContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.MutableText;
import net.minecraft.util.Identifier;

import static io.github.lolens.showcaser.Showcaser.MOD_ID;
import static io.github.lolens.showcaser.core.builders.ClientChatMessageBuilder.VerifiedType.VERIFIED;
import static io.github.lolens.showcaser.util.HandlerUtils.getHandler;
import static io.github.lolens.showcaser.util.HandlerUtils.isValidSlot;

public class FallbackHandler {
    private static final Identifier ID = Identifier.of(MOD_ID, "fallback");

    public static void register() {
        registerServer();

        if (Platform.getEnvironment() == Env.CLIENT) {
            registerClient();
        }
    }

    private static void registerServer() {
        ServerHandlerBuilder.create(ID)
                .forContainer(ScreenHandler.class)
                .process((player, context) -> {
                    ScreenHandler handler = getHandler(player);
                    int slotIndex = context.getSlotIndex();

                    if (!isValidSlot(slotIndex, handler))
                        throw new ServerShareProcessingException(context, player, ServerShareProcessingException.SLOT_NOT_VALID);

                    ItemStack stack = handler.getSlot(slotIndex).getStack();

                    if (stack.isEmpty()) return null;

                    return ShareContext.of(ID).with("stack", stack);
                })
                .register();
    }

    @SuppressWarnings("rawtypes")
    @Environment(EnvType.CLIENT)
    private static void registerClient() {
        ClientHandlerBuilder.<HandledScreen>create(ID)
                .forScreen(HandledScreen.class)
                .createContext((screen, contextConsumer) -> {
                    Showcaser.LOGGER.info(screen.getClass().getName());

                    Slot slot = ((HandledScreenMixin) screen).showcaser$getFocusedSlot();

                    if (slot == null || !slot.hasStack()) return HandlerResult.PASS;

                    ShareContext shareContext = ShareContext.of(
                                    ID,
                                    screen.getScreenHandler().syncId)
                            .with("slot", slot.id);
                    contextConsumer.accept(shareContext);

                    return HandlerResult.SUCCESS;
                })
                .display((player, context) -> {
                    MutableText text = ClientChatMessageBuilder.create(context, player, "item")
                            // items shared through ME pattern terminal were verified though it is
                            // possible to add them in via REI/EMI etc...
                            // so setting this to true can cause similar "problems"
                            .setVerified(VERIFIED)
                            .withWidth(12)
                            .withDisplayStack(context.getItemStack("stack"))
                            .build();

                    MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(text);
                })
                .withIcon("item", context -> {
                    ItemStack stack = context.getItemStack("stack");
                    return new ItemStackIconRenderer(stack);
                })
                .withTooltip("item", (context, player) -> {
                    ItemStack stack = context.getItemStack("stack");
                    return stack.getTooltip(player, TooltipContext.BASIC);
                })
                .register();
    }
}