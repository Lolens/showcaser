package io.github.lolens.showcaser.handler.vanilla;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import io.github.lolens.showcaser.adapter.AdapterFactory;
import io.github.lolens.showcaser.api.handler.HandlerResult;
import io.github.lolens.showcaser.api.resource.ShareableResource;
import io.github.lolens.showcaser.client.ClientChatMessageBuilder;
import io.github.lolens.showcaser.core.builder.handler.ClientHandlerBuilder;
import io.github.lolens.showcaser.core.builder.handler.ServerHandlerBuilder;
import io.github.lolens.showcaser.mixin.CreativeInventoryScreenHandlerMixin;
import io.github.lolens.showcaser.mixin.CreativeInventoryScreenMixin;
import io.github.lolens.showcaser.mixin.HandledScreenMixin;
import io.github.lolens.showcaser.core.ShareContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.MutableText;
import net.minecraft.util.Identifier;

import static io.github.lolens.showcaser.Showcaser.MOD_ID;
import static io.github.lolens.showcaser.client.ClientChatMessageBuilder.VerifiedType.VERIFIED;

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

    private static void registerClient() {
        ClientHandlerBuilder.<CreativeInventoryScreen>create(ID)
                .forScreen(CreativeInventoryScreen.class)
                .createContext((screen, contextConsumer) -> {
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

                            if (slot.id >= 45 && slot.id <= 53) {

                                // fixes sharing empty hotbar slots while in category
                                if (!slot.hasStack()) return HandlerResult.STOP;

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