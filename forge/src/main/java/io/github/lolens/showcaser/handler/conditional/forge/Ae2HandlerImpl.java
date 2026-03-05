package io.github.lolens.showcaser.handler.conditional.forge;

import appeng.api.config.Actionable;
import appeng.api.stacks.AEKey;
import appeng.client.gui.implementations.UpgradeableScreen;
import appeng.client.gui.me.common.MEStorageScreen;
import appeng.client.gui.me.common.RepoSlot;
import appeng.menu.AEBaseMenu;
import appeng.menu.me.common.GridInventoryEntry;
import appeng.menu.me.common.MEStorageMenu;
import appeng.menu.slot.FakeSlot;
import appeng.menu.slot.PatternTermSlot;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import io.github.lolens.showcaser.adapter.AdapterFactory;
import io.github.lolens.showcaser.api.HandlerResult;
import io.github.lolens.showcaser.api.resource.ShareableResource;
import io.github.lolens.showcaser.core.builders.ClientChatMessageBuilder;
import io.github.lolens.showcaser.core.builders.handler.ClientHandlerBuilder;
import io.github.lolens.showcaser.core.builders.handler.ServerHandlerBuilder;
import io.github.lolens.showcaser.exception.ServerShareProcessingException;
import io.github.lolens.showcaser.forge.mixin.MEStorageMenuInvoker;
import io.github.lolens.showcaser.model.ShareContext;
import io.github.lolens.showcaser.registry.CachedPriorityRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.MutableText;
import net.minecraft.util.Identifier;

import static io.github.lolens.showcaser.Showcaser.MOD_ID;
import static io.github.lolens.showcaser.core.builders.ClientChatMessageBuilder.VerifiedType.VERIFIED;
import static io.github.lolens.showcaser.util.HandlerUtils.getHandler;
import static io.github.lolens.showcaser.util.HandlerUtils.isValidSyncId;

@SuppressWarnings("rawtypes")
public class Ae2HandlerImpl {
    private static final Identifier ID = Identifier.of(MOD_ID, "ae2");

    public static void register() {

        registerServer();

        if (Platform.getEnvironment() == Env.CLIENT) {
            CachedPriorityRegistry.blacklistWithInheritors(UpgradeableScreen.class);
            registerClient();
        }
    }

    private static void registerServer() {
        ServerHandlerBuilder.<AEBaseMenu>create(ID)
                .forContainer(AEBaseMenu.class)
                .process((player, context) -> {
                    if (!isValidSyncId(player, context.getSyncId()))
                        throw new ServerShareProcessingException(context, player, ServerShareProcessingException.SYNC_ID_NOT_VALID);

                    MEStorageMenu menu = (MEStorageMenu) getHandler(player);

                    long serial = context.getLong("serial");

                    AEKey key = ((MEStorageMenuInvoker) menu).showcaser$getStackBySerial(serial);

                    long amount = menu.getHost().getInventory().extract(
                            key,
                            Long.MAX_VALUE,
                            Actionable.SIMULATE,
                            menu.getActionSource()
                    );

                    NbtCompound keyCompound = key.toTagGeneric();
                    return ShareContext.of(ID)
                            .with("key", keyCompound)
                            .withAmount(amount); // amount in droplets on fabric. Gets converted at display
                })
                .register();
    }

    private static void registerClient() {
        ClientHandlerBuilder.<MEStorageScreen>create(ID)
                .forScreen(MEStorageScreen.class)
                .createContext((screen, contextConsumer) -> {
                    Slot slot = screen.getSlotUnderMouse();

                    // prevent fallback handler handling for slots that can be filled with ghost items
                    if (slot instanceof FakeSlot || slot instanceof PatternTermSlot) return HandlerResult.STOP;

                    if (slot instanceof RepoSlot repoSlot && repoSlot.hasStack()) {

                        GridInventoryEntry entry = repoSlot.getEntry();

                        // for cases when "something" is 0 and displayed for crafting availability reason
                        if (entry.getStoredAmount() == 0) return HandlerResult.STOP;

                        ShareContext context = ShareContext.of(
                                ID,
                                screen.getScreenHandler().syncId
                        ).with("serial", entry.getSerial());

                        contextConsumer.accept(context);
                        return HandlerResult.SUCCESS;
                    }
                    return HandlerResult.PASS;
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