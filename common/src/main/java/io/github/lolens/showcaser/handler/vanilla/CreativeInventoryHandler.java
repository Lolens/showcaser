package io.github.lolens.showcaser.handler.vanilla;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import io.github.lolens.showcaser.Showcaser;
import io.github.lolens.showcaser.api.HandlerResult;
import io.github.lolens.showcaser.client.render.icon.ItemStackIconRenderer;
import io.github.lolens.showcaser.core.builders.ClientChatMessageBuilder;
import io.github.lolens.showcaser.core.builders.handler.ClientHandlerBuilder;
import io.github.lolens.showcaser.core.builders.handler.ServerHandlerBuilder;
import io.github.lolens.showcaser.mixin.CreativeInventoryScreenHandlerMixin;
import io.github.lolens.showcaser.mixin.CreativeInventoryScreenMixin;
import io.github.lolens.showcaser.mixin.HandledScreenMixin;
import io.github.lolens.showcaser.model.ShareContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.MutableText;
import net.minecraft.util.Identifier;

import static io.github.lolens.showcaser.Showcaser.MOD_ID;
import static io.github.lolens.showcaser.core.builders.ClientChatMessageBuilder.VerifiedType.VERIFIED;

public class CreativeInventoryHandler {
    private static final Identifier ID = Identifier.of(MOD_ID, "creative_inventory");

    public static void register() {
        registerServer();

        if (Platform.getEnvironment() == Env.CLIENT) {
            registerClient();
        }
    }

    private static void registerServer() {
        ServerHandlerBuilder.create(ID)
                .forContainer(null) // does not exist at server
                .process((player, context) -> {
                    if (!player.isCreative()) return null;

                    return ShareContext.of(ID)
                            .withItemStack(context.getItemStack());
                })
                .register();
    }

    @Environment(EnvType.CLIENT)
    private static void registerClient() {
        ClientHandlerBuilder.<CreativeInventoryScreen>create(ID)
                .forScreen(CreativeInventoryScreen.class)
                .createContext((screen, contextConsumer) -> {

                    Showcaser.LOGGER.info(screen.getClass().getName());

                    CreativeInventoryScreen.CreativeScreenHandler handler = screen.getScreenHandler();

                    ItemGroup.Type type = ((CreativeInventoryScreenMixin) screen).showcaser$getSelectedTab().getType();

                    Slot slot = ((HandledScreenMixin) screen).showcaser$getFocusedSlot();
                    if (slot == null) return HandlerResult.PASS;

                    switch (type) {
                        case INVENTORY -> {
                            if (slot.hasStack()) {
                                ShareContext shareContext = ShareContext.of(
                                                ID,
                                                screen.getScreenHandler().syncId)
                                        .withItemStack(slot.getStack());
                                contextConsumer.accept(shareContext);
                                return HandlerResult.SUCCESS;
                            }
                        }

                        case CATEGORY -> {
                            // hotbar slots
                            if (slot.id >= 45 && slot.id <= 53 && slot.hasStack()) {
                                ShareContext shareContext = ShareContext.of(
                                                ID,
                                                screen.getScreenHandler().syncId)
                                        .withItemStack(slot.getStack());
                                contextConsumer.accept(shareContext);
                                return HandlerResult.SUCCESS;
                            }

                            float scrollPosition = ((CreativeInventoryScreenMixin) screen).showcaser$getScrollPosition();
                            int row = ((CreativeInventoryScreenHandlerMixin) screen.getScreenHandler()).showcaser$getRow(scrollPosition);
                            int listId = slot.id + (9 * row);

                            // do not allow fallback handler handle this
                            if (listId >= handler.itemList.size()) return HandlerResult.STOP;

                            ItemStack stack = screen.getScreenHandler().itemList.get(slot.id + (9 * row));
                            ShareContext shareContext = ShareContext.of(
                                            ID,
                                            screen.getScreenHandler().syncId)
                                    .withItemStack(stack);

                            contextConsumer.accept(shareContext);
                            return HandlerResult.SUCCESS;
                        }
                    }

                    return HandlerResult.STOP;
                })
                .display((player, context) -> {
                    MutableText text = ClientChatMessageBuilder.create(context, player, "item")
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