package io.github.lolens.showcaser.handler.vanilla;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import io.github.lolens.showcaser.Showcaser;
import io.github.lolens.showcaser.adapter.AdapterFactory;
import io.github.lolens.showcaser.api.shareContext.ShareContext;
import io.github.lolens.showcaser.api.handler.HandlerResult;
import io.github.lolens.showcaser.api.resource.ShareableResource;
import io.github.lolens.showcaser.client.ClientChatMessageBuilder;
import io.github.lolens.showcaser.core.builder.handler.ClientHandlerBuilder;
import io.github.lolens.showcaser.core.builder.handler.ServerHandlerBuilder;
import io.github.lolens.showcaser.exception.ServerShareProcessingException;
import io.github.lolens.showcaser.mixin.HandledScreenMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.MutableText;
import net.minecraft.util.Identifier;

import static io.github.lolens.showcaser.Showcaser.MOD_ID;
import static io.github.lolens.showcaser.api.ShowcaserAPI.getContextFactory;
import static io.github.lolens.showcaser.client.ClientChatMessageBuilder.VerifiedType.VERIFIED;
import static io.github.lolens.showcaser.util.HandlerUtils.getHandler;
import static io.github.lolens.showcaser.util.HandlerUtils.isValidSlot;

public class PlayerInventoryHandler {
    private static final Identifier ID = Identifier.of(MOD_ID, "player_inventory");

    public static void register() {
        registerServer();

        if (Platform.getEnvironment() == Env.CLIENT) {
            registerClient();
        }
    }

    private static void registerServer() {
        ServerHandlerBuilder.<PlayerScreenHandler>create(ID)
                .forContainer(PlayerScreenHandler.class)
                .process((player, context) -> {
                    ScreenHandler handler = getHandler(player);
                    int slotIndex = context.getSlotIndex();

                    if (!isValidSlot(slotIndex, handler)) {
                        throw new ServerShareProcessingException(context, player, ServerShareProcessingException.SLOT_NOT_VALID);
                    }

                    ItemStack stack = handler.getSlot(slotIndex).getStack();

                    if (stack.isEmpty()) return null;

                    return getContextFactory().create(ID).with("stack", stack);
                })
                .register();
    }

    private static void registerClient() {
        ClientHandlerBuilder.<InventoryScreen>create(ID)
                .forScreen(InventoryScreen.class)
                .createContext((screen, contextConsumer) -> {
                    Showcaser.LOGGER.info(screen.getClass().getName());

                    Slot slot = ((HandledScreenMixin) screen).showcaser$getFocusedSlot();

                    if (slot instanceof CraftingResultSlot) return HandlerResult.STOP;

                    if (slot == null || !slot.hasStack()) return HandlerResult.PASS;

                    ShareContext shareContext = getContextFactory().create(
                                    ID,
                                    screen.getScreenHandler().syncId)
                            .withSlotIndex(slot.id);

                    contextConsumer.accept(shareContext);
                    return HandlerResult.SUCCESS;
                })
                .display((player, context) -> {
                    ShareableResource resource = AdapterFactory.fromContext(context);

                    MutableText text = ClientChatMessageBuilder.create(context, player, resource)
                            .setVerified(VERIFIED)
                            .withWidth(12)
                            .build();

                    MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(text);
                })
                .register();
    }
}