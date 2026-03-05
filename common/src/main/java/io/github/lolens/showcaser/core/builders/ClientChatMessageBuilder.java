package io.github.lolens.showcaser.core.builders;

import io.github.lolens.showcaser.client.render.RenderableHoverEvent;
import io.github.lolens.showcaser.client.render.icon.IconRenderer;
import io.github.lolens.showcaser.client.render.icon.IconRendererRegistry;
import io.github.lolens.showcaser.client.render.tooltip.TooltipProvider;
import io.github.lolens.showcaser.client.render.tooltip.TooltipProviderRegistry;
import io.github.lolens.showcaser.config.ConfigManager;
import io.github.lolens.showcaser.model.ShareContext;
import io.github.lolens.showcaser.util.FluidUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

public final class ClientChatMessageBuilder {

    public static final Text UNVERIFIED_MESSAGE =
            Text.translatable("showcaser.chat.share_message.tooltip.unverified")
                    .formatted(Formatting.RED, Formatting.BOLD);

    public static final Text VERIFIED_MESSAGE =
            Text.translatable("showcaser.chat.share_message.tooltip.verified")
                    .formatted(Formatting.GREEN, Formatting.BOLD);

    private static final String VERIFIED_KEY = "showcaser_verified";
    private static final char MARKER = '\uE670';

    private final ShareContext context;
    private final PlayerEntity sender;
    private final String renderType;

    private final int iconWidth;
    private final boolean useBrackets;
    private final boolean showAmount;
    private final VerifiedType verifiedType;

    private final Mode mode;
    private final ItemStack stack;
    private final Text customName;
    private final long customAmount;
    private final ClickEvent clickEvent;
    private final boolean isLiquid;

    private final Style contentStyle;
    private final String translationKey;

    private enum Mode {
        STACK,
        CUSTOM
    }

    public enum VerifiedType {
        VERIFIED,
        UNVERIFIED,
        NONE
    }

    private ClientChatMessageBuilder(
            ShareContext context,
            PlayerEntity sender,
            String renderType,
            int iconWidth,
            boolean useBrackets,
            boolean showAmount,
            VerifiedType verifiedType,
            Mode mode,
            ItemStack stack,
            Text customName,
            long customAmount,
            ClickEvent clickEvent,
            boolean isLiquid,
            Style contentStyle,
            String translationKey
    ) {
        this.context = context;
        this.sender = sender;
        this.renderType = renderType;
        this.iconWidth = iconWidth;
        this.useBrackets = useBrackets;
        this.showAmount = showAmount;
        this.verifiedType = verifiedType;
        this.mode = mode;
        this.stack = stack;
        this.customName = customName;
        this.customAmount = customAmount;
        this.clickEvent = clickEvent;
        this.isLiquid = isLiquid;
        this.contentStyle = contentStyle;
        this.translationKey = translationKey;
    }

    public static ClientChatMessageBuilder create(
            ShareContext context,
            PlayerEntity sender,
            String renderType
    ) {
        return new ClientChatMessageBuilder(
                context, sender, renderType,
                12,
                true,
                true,
                VerifiedType.NONE,
                Mode.STACK,
                ItemStack.EMPTY,
                Text.empty(),
                1,
                null,
                false,
                Style.EMPTY,
                "showcaser.chat.share_message"
        );
    }

    // === BUILDER METHODS ===

    public ClientChatMessageBuilder withDisplayStack(ItemStack stack) {
        return new ClientChatMessageBuilder(
                context, sender, renderType,
                iconWidth, useBrackets, showAmount, verifiedType,
                Mode.STACK, stack.copy(), customName, customAmount,
                clickEvent,
                isLiquid,
                contentStyle,
                translationKey
        );
    }

    public ClientChatMessageBuilder withCustomStack(Text name, long amount) {
        return new ClientChatMessageBuilder(
                context, sender, renderType,
                iconWidth, useBrackets, showAmount, verifiedType,
                Mode.CUSTOM, ItemStack.EMPTY, name, amount,
                clickEvent,
                isLiquid,
                contentStyle,
                translationKey
        );
    }

    public ClientChatMessageBuilder setVerified(VerifiedType verifiedType) {
        return new ClientChatMessageBuilder(
                context, sender, renderType,
                iconWidth, useBrackets, showAmount, verifiedType,
                mode, stack, customName, customAmount,
                clickEvent,
                isLiquid,
                contentStyle,
                translationKey
        );
    }

    public ClientChatMessageBuilder withWidth(int width) {
        return new ClientChatMessageBuilder(
                context, sender, renderType,
                width, useBrackets, showAmount, verifiedType,
                mode, stack, customName, customAmount,
                clickEvent,
                isLiquid,
                contentStyle,
                translationKey
        );
    }

    public ClientChatMessageBuilder useBrackets(boolean use) {
        return new ClientChatMessageBuilder(
                context, sender, renderType,
                iconWidth, use, showAmount, verifiedType,
                mode, stack, customName, customAmount,
                clickEvent,
                isLiquid,
                contentStyle,
                translationKey
        );
    }

    public ClientChatMessageBuilder showAmount(boolean show) {
        return new ClientChatMessageBuilder(
                context, sender, renderType,
                iconWidth, useBrackets, show, verifiedType,
                mode, stack, customName, customAmount,
                clickEvent,
                isLiquid,
                contentStyle,
                translationKey
        );
    }

    public ClientChatMessageBuilder withClickEvent(ClickEvent event) {
        return new ClientChatMessageBuilder(
                context, sender, renderType,
                iconWidth, useBrackets, showAmount, verifiedType,
                mode, stack, customName, customAmount,
                event,
                isLiquid,
                contentStyle,
                translationKey
        );
    }

    public ClientChatMessageBuilder withContentStyle(Style style) {
        return new ClientChatMessageBuilder(
                context, sender, renderType,
                iconWidth, useBrackets, showAmount, verifiedType,
                mode, stack, customName, customAmount,
                clickEvent,
                isLiquid,
                style,
                translationKey
        );
    }

    public ClientChatMessageBuilder withFormatting(Formatting... formatting) {
        return withContentStyle(Style.EMPTY.withFormatting(formatting));
    }

    public ClientChatMessageBuilder withTranslationKey(String key) {
        return new ClientChatMessageBuilder(
                context, sender, renderType,
                iconWidth, useBrackets, showAmount, verifiedType,
                mode, stack, customName, customAmount,
                clickEvent,
                isLiquid,
                contentStyle,
                key
        );
    }

    /**
     * Changes how message content will be displayed
     * by default content displays resource amount as raw count
     * for example "[x2000 Water]"
     * after applying it will be displayed as "[2B Water]"
     * @return builder
     */
    public ClientChatMessageBuilder displayAsFluid(boolean isLiquid) {
        return new ClientChatMessageBuilder(
                context, sender, renderType,
                iconWidth, useBrackets, showAmount, verifiedType,
                mode, stack, customName, customAmount,
                clickEvent,
                isLiquid,
                contentStyle,
                translationKey
        );
    }

    public MutableText build() {

        IconRenderer iconRenderer = IconRendererRegistry
                .getRendererFactory(context.getId(), renderType)
                .create(context);

        TooltipProvider tooltipProvider = TooltipProviderRegistry
                .getRenderer(context.getId(), renderType);

        ItemStack renderStack = prepareRenderStack();

        RenderableHoverEvent hoverEvent = mode == Mode.STACK
                ? new RenderableHoverEvent(iconWidth, iconRenderer, renderStack)
                : new RenderableHoverEvent(iconWidth, iconRenderer, buildCustomTooltip(tooltipProvider));

        MutableText marker = Text.literal(String.valueOf(MARKER))
                .styled(style -> style
                        .withClickEvent(clickEvent)
                        .withHoverEvent(hoverEvent)
                        .withFormatting(mode == Mode.STACK
                                ? renderStack.getRarity().formatting
                                : Formatting.WHITE)
                );

        MutableText content = mode == Mode.STACK
                ? buildStackContent(renderStack)
                : buildCustomContent();

        if (!contentStyle.isEmpty()) {
            content = content.styled(style -> style.withParent(contentStyle));
        }

        MutableText displayText = useBrackets
                ? Texts.bracketed(content)
                : content;

        return Text.translatable(
                translationKey,
                sender.getDisplayName(),
                marker.append(displayText)
        );
    }

    // === HELPERS ===

    // although stack is basically a representational layer object it is cleaner to
    // return a copy in each method that can mutate that stack
    private ItemStack prepareRenderStack() {
        if (mode != Mode.STACK || stack.isEmpty()) return stack.copy();

        ItemStack copy = stack.copy();

        if (ConfigManager.getConfig().ignoreCustomNames) copy.setCustomName(null);

        if (verifiedType == VerifiedType.VERIFIED) {
            copy.getOrCreateNbt().putBoolean(VERIFIED_KEY, true);
        }
        if (verifiedType == VerifiedType.UNVERIFIED) {
            copy.getOrCreateNbt().putBoolean(VERIFIED_KEY, false);
        }

        return copy;
    }

    private MutableText buildCustomContent() {
        MutableText text = customName.copy();
        if (showAmount) {

            if (isLiquid) {
                text = FluidUtils.buildFluidAmountText(customAmount, false).copy().append(" ").append(text);
            } else {

                // TODO add option to shrink count by thousands
                text = Text.literal("x" + customAmount + " ").append(text);
            }
        }
        return text;
    }


    // at this moment 'allowCustomNames' config value is already applied
    private MutableText buildStackContent(ItemStack stack) {
        MutableText text = stack.hasCustomName()
                ? stack.getName().copy().formatted(Formatting.ITALIC)
                : stack.getName().copy();

        if (showAmount) {
            int count = stack.getCount();
            if (count > 1) {
                text = Text.literal("x" + count + " ").append(text);
            }
        }
        return text;
    }

    private Text buildCustomTooltip(TooltipProvider tooltipProvider) {
        Text tooltip = Texts.join(
                tooltipProvider.buildTooltip(context, sender),
                Text.literal("\n")
        );

        if (verifiedType == VerifiedType.VERIFIED) {
            return markAsVerified(tooltip);
        }
        if (verifiedType == VerifiedType.UNVERIFIED) {
            return markAsUnverified(tooltip);
        }
        return tooltip;
    }

    // === UTIL ===

    public static Text markAsVerified(Text text) {
        MutableText mutable = text.copy();
        mutable.append("\n");
        // can cause verified message indentation desync on client upon reload but who cares
        if (ConfigManager.getConfig().addEmptySpaceBeforeVerifiedText) mutable.append("\n");
        mutable.append(VERIFIED_MESSAGE);
        return mutable;
    }

    public static Text markAsUnverified(Text text) {
        MutableText mutable = text.copy();
        mutable.append("\n");
        if (ConfigManager.getConfig().addEmptySpaceBeforeVerifiedText) mutable.append("\n");
        mutable.append(UNVERIFIED_MESSAGE);
        return mutable;
    }
}
