package io.github.lolens.showcaser.handler.conditional;

import dev.architectury.fluid.FluidStack;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
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
import me.shedaniel.rei.api.client.REIRuntime;
import me.shedaniel.rei.api.client.overlay.ScreenOverlay;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.EntryType;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.Optional;

import static io.github.lolens.showcaser.Showcaser.MOD_ID;
import static io.github.lolens.showcaser.core.builders.ClientChatMessageBuilder.VerifiedType.NONE;

public class ReiHandler {
    private static final Identifier ID = Identifier.of(MOD_ID, "rei");

    public static void register() {
        ServerCommands.Conditional.REI.register();
        Networking.Conditional.REI.register();

        registerServer();

        if (Platform.getEnvironment() == Env.CLIENT) {
            registerClient();
        }
    }

    private static void registerServer() {
        ServerHandlerBuilder.create(ID)
                .forContainer(null)
                .process((player, context) -> {
                    return context; // todo some validation ???
                })
                .register();
    }

    @Environment(EnvType.CLIENT)
    private static void registerClient() {
        ClientHandlerBuilder.<Screen>create(ID)
                .forScreen(null)
                .createContext((screen, contextConsumer) -> {
                    Optional<ScreenOverlay> opt = REIRuntime.getInstance().getOverlay();

                    if (opt.isEmpty()) return HandlerResult.PASS;

                    EntryStack<?> entryStack = opt.get().getEntryList().getFocusedStack();

                    if (entryStack.isEmpty()) return HandlerResult.PASS;

                    EntryType<?> entryType = entryStack.getType();
                    NbtCompound entryNbt = entryStack.saveStack();

                    if (entryType == VanillaEntryTypes.ITEM) {
                        entryNbt.putString("type", "minecraft:item");
                        contextConsumer.accept(ShareContext.of(ID)
                                .with("entry", entryNbt));
                        return HandlerResult.SUCCESS;
                    }
                    if (entryType == VanillaEntryTypes.FLUID) {
                        entryNbt.putString("type", "minecraft:fluid");
                        contextConsumer.accept(ShareContext.of(ID)
                                .with("entry", entryNbt));
                        return HandlerResult.SUCCESS;
                    }

                    return HandlerResult.PASS;
                })
                .display((player, context) -> {
                    NbtCompound entryNbt = context.getCompound("entry");
                    EntryStack<?> entryStack = EntryStack.read(entryNbt);
                    EntryType<?> entryType = entryStack.getType();
                    MutableText text = Text.empty();

                    if (entryType == VanillaEntryTypes.ITEM) {
                        ItemStack itemStack = entryStack.castValue();
                        text = ClientChatMessageBuilder.create(context, player, "item")
                                .withDisplayStack(itemStack)
                                .withWidth(12)
                                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                                        String.format("/showcaser rei open %s", entryStack.wildcard().saveStack())))
                                .withFormatting(Formatting.UNDERLINE)
                                .setVerified(NONE)
                                .withTranslationKey("showcaser.chat.share_message.recipe")
                                .build();
                    }
                    if (entryType == VanillaEntryTypes.FLUID) {
                        FluidStack fluidStack = entryStack.castValue();
                        text = ClientChatMessageBuilder.create(context, player, "fluid")
                                .withCustomStack(fluidStack.getName(), 0)
                                .showAmount(false)
                                .withWidth(12)
                                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                                        String.format("/showcaser rei open %s", entryStack.wildcard().saveStack())))
                                .withFormatting(Formatting.UNDERLINE)
                                .setVerified(NONE)
                                .withTranslationKey("showcaser.chat.share_message.recipe")
                                .build();
                    }

                    MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(text);
                })
                .withIcon("item", context -> {
                    NbtCompound entryNbt = context.getCompound("entry");
                    EntryStack<?> entryStack = EntryStack.read(entryNbt);
                    return new ItemStackIconRenderer((ItemStack) entryStack.castValue());
                })
                .withTooltip("item", (context, player) -> {
                    NbtCompound entryNbt = context.getCompound("entry");
                    EntryStack<?> entryStack = EntryStack.read(entryNbt);
                    return ((ItemStack) entryStack.castValue()).getTooltip(player, TooltipContext.BASIC);
                })
                .withIcon("fluid", context -> {
                    NbtCompound entryNbt = context.getCompound("entry");
                    EntryStack<?> entryStack = EntryStack.read(entryNbt);
                    return new FluidStackIconRenderer((FluidStack) entryStack.castValue());
                })
                .withTooltip("fluid", (context, player) -> {
                    NbtCompound entryNbt = context.getCompound("entry");
                    EntryStack<?> entryStack = EntryStack.read(entryNbt);
                    return FluidUtils.buildTooltip(entryStack.castValue(), player, false);
                })
                .register();
    }
}