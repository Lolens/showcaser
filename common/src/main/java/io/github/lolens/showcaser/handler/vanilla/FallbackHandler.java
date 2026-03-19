package io.github.lolens.showcaser.handler.vanilla;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import io.github.lolens.showcaser.api.event.AdapterRegistrationEvent;
import io.github.lolens.showcaser.api.handler.HandlerResult;
import io.github.lolens.showcaser.api.resource.MessageVerification;
import io.github.lolens.showcaser.api.resource.ShareableResource;
import io.github.lolens.showcaser.api.sharecontext.ShareContext;
import io.github.lolens.showcaser.client.adapter.AdapterRegistry;
import io.github.lolens.showcaser.client.adapter.impl.ItemStackAdapter;
import io.github.lolens.showcaser.exception.ServerShareProcessingException;
import io.github.lolens.showcaser.mixin.HandledScreenMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.MutableText;
import net.minecraft.util.Identifier;

import static io.github.lolens.showcaser.Showcaser.MOD_ID;
import static io.github.lolens.showcaser.api.ShowcaserAPI.*;
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
        getHandlerBuilderFactory().createServerBuilder(ID)
                .forContainer(ScreenHandler.class)
                .process((player, context) -> {
                    ScreenHandler handler = getHandler(player);
                    int slotIndex = context.getSlotIndex();

                    if (!isValidSlot(slotIndex, handler))
                        throw new ServerShareProcessingException(context, player, ServerShareProcessingException.SLOT_NOT_VALID);

                    ItemStack stack = handler.getSlot(slotIndex).getStack();

                    if (stack.isEmpty()) return null;

                    return getContextFactory().create(ID).with("stack", stack);
                })
                .register();
    }

    @SuppressWarnings("rawtypes")
    private static void registerClient() {
        getHandlerBuilderFactory().<HandledScreen>createClientBuilder(ID)
                .forScreen(HandledScreen.class)
                .createContext((screen, contextConsumer) -> {
                    Slot slot = ((HandledScreenMixin) screen).showcaser$getFocusedSlot();

                    if (slot == null || !slot.hasStack()) return HandlerResult.PASS;

                    if (slot instanceof CraftingResultSlot) return HandlerResult.STOP;

                    ShareContext shareContext = getContextFactory().create(
                                    ID,
                                    screen.getScreenHandler().syncId)
                            .with("slot", slot.id);
                    contextConsumer.accept(shareContext);

                    return HandlerResult.SUCCESS;
                })
                .display((player, context) -> {
                    ShareableResource resource = AdapterRegistry.adapt(context);

                    MutableText text = getMessageBuilderFactory().create(context, player, resource, MessageVerification.VERIFIED)
                            .withWidth(12)
                            .build();

                    MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(text);
                })
                .register();

        AdapterRegistrationEvent.EVENT.invoker().register(new ItemStackAdapter(ID));
    }
}