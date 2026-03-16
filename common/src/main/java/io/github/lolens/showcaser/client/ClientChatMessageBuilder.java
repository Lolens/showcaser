package io.github.lolens.showcaser.client;

import io.github.lolens.showcaser.api.shareContext.ShareContext;
import io.github.lolens.showcaser.api.resource.ShareableFluidStack;
import io.github.lolens.showcaser.api.resource.ShareableItemStack;
import io.github.lolens.showcaser.api.resource.ShareableResource;
import io.github.lolens.showcaser.client.render.RenderableHoverEvent;
import io.github.lolens.showcaser.config.ConfigManager;
import net.minecraft.item.ItemStack;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

public final class ClientChatMessageBuilder {

    private static final String VERIFIED_KEY = "showcaser_verified";

    public static final Text UNVERIFIED_MESSAGE =
            Text.translatable("showcaser.chat.share_message.tooltip.unverified")
                    .formatted(Formatting.RED, Formatting.BOLD);

    public static final Text VERIFIED_MESSAGE =
            Text.translatable("showcaser.chat.share_message.tooltip.verified")
                    .formatted(Formatting.GREEN, Formatting.BOLD);

    private static final char MARKER = '\uE670';

    private final ShareContext context;
    private final String senderName;
    private final ShareableResource resource;

    private final int iconWidth;
    private final boolean useBrackets;
    private final boolean showAmount;
    private final VerifiedType verifiedType;
    private final ClickEvent clickEvent;
    private final Style contentStyle;
    private final String translationKey;

    public enum VerifiedType {
        VERIFIED,
        UNVERIFIED,
        NONE
    }

    private ClientChatMessageBuilder(
            ShareContext context,
            String senderName,
            ShareableResource resource,
            int iconWidth,
            boolean useBrackets,
            boolean showAmount,
            VerifiedType verifiedType,
            ClickEvent clickEvent,
            Style contentStyle,
            String translationKey
    ) {
        this.context = context;
        this.senderName = senderName;
        this.resource = resource;
        this.iconWidth = iconWidth;
        this.useBrackets = useBrackets;
        this.showAmount = showAmount;
        this.verifiedType = verifiedType;
        this.clickEvent = clickEvent;
        this.contentStyle = contentStyle;
        this.translationKey = translationKey;
    }

    public static ClientChatMessageBuilder create(
            ShareContext context,
            String sender,
            ShareableResource resource
    ) {
        return new ClientChatMessageBuilder(
                context, sender, resource,
                12,
                true,
                true,
                VerifiedType.NONE,
                null,
                Style.EMPTY,
                "showcaser.chat.share_message"
        );
    }

    // === BUILDER METHODS ===

    public ClientChatMessageBuilder withWidth(int width) {
        return new ClientChatMessageBuilder(
                context, senderName, resource,
                width, useBrackets, showAmount, verifiedType,
                clickEvent, contentStyle, translationKey
        );
    }

    public ClientChatMessageBuilder useBrackets(boolean use) {
        return new ClientChatMessageBuilder(
                context, senderName, resource,
                iconWidth, use, showAmount, verifiedType,
                clickEvent, contentStyle, translationKey
        );
    }

    public ClientChatMessageBuilder showAmount(boolean show) {
        return new ClientChatMessageBuilder(
                context, senderName, resource,
                iconWidth, useBrackets, show, verifiedType,
                clickEvent, contentStyle, translationKey
        );
    }

    public ClientChatMessageBuilder withClickEvent(ClickEvent event) {
        return new ClientChatMessageBuilder(
                context, senderName, resource,
                iconWidth, useBrackets, showAmount, verifiedType,
                event, contentStyle, translationKey
        );
    }

    public ClientChatMessageBuilder withFormatting(Formatting... formatting) {
        return new ClientChatMessageBuilder(
                context, senderName, resource,
                iconWidth, useBrackets, showAmount, verifiedType,
                clickEvent, Style.EMPTY.withFormatting(formatting), translationKey
        );
    }

    public ClientChatMessageBuilder setVerified(VerifiedType verifiedType) {
        return new ClientChatMessageBuilder(
                context, senderName, resource,
                iconWidth, useBrackets, showAmount, verifiedType,
                clickEvent, contentStyle, translationKey
        );
    }

    public ClientChatMessageBuilder withTranslationKey(String key) {
        return new ClientChatMessageBuilder(
                context, senderName, resource,
                iconWidth, useBrackets, showAmount, verifiedType,
                clickEvent, contentStyle, key
        );
    }

    public MutableText build() {

        RenderableHoverEvent hoverEvent = buildHoverEvent();

        MutableText marker = Text.literal(String.valueOf(MARKER))
                .styled(style -> style
                        .withClickEvent(clickEvent)
                        .withHoverEvent(hoverEvent)
                        // full message should be colored or it looks bad
                        .withFormatting(resource instanceof ShareableItemStack stack
                                ? stack.getRarity().formatting
                                : Formatting.WHITE)
                );


        MutableText content = buildContent();

        if (!contentStyle.isEmpty()) {
            content = content.styled(style -> style.withParent(contentStyle));
        }

        MutableText displayText = useBrackets ? Texts.bracketed(content) : content;

        return Text.translatable(
                translationKey,
                senderName,
                marker.append(displayText)
        );
    }

    // === HELPERS ===

    private RenderableHoverEvent buildHoverEvent() {

        if (resource instanceof ShareableItemStack shareableItemStack) {

            // item stack uses its default tooltip
            ItemStack stack = prepareRenderStack(shareableItemStack);
            return new RenderableHoverEvent(
                    iconWidth,
                    resource,
                    stack
            );
        }

        if (resource instanceof  ShareableFluidStack) {
            // tooltip for fluidStack is fully custom
            Text fullTooltip = buildTooltip();
            return new RenderableHoverEvent(
                    iconWidth,
                    resource,
                    fullTooltip
            );
        }

        throw new IllegalStateException("Tried building hover event for unknown resource");
    }

    private MutableText buildContent() {
        long amount = resource.getAmount();

        Text name;

        if (resource instanceof ShareableFluidStack fluidStack) {

            if (amount <= 1 || !showAmount) {
                return fluidStack.getDisplayName().copy();
            }

            return fluidStack.getFluidAmountText()
                    .copy()
                    .append(" ")
                    .append(fluidStack.getDisplayName());
        }

        if (resource instanceof ShareableItemStack itemStack) {

            ItemStack copy = itemStack.getCopy();

            if (ConfigManager.getClientConfig().ignoreCustomNames) {
                copy.setCustomName(null);
            }

            if (copy.hasCustomName()) {
                name = copy.getName().copy().formatted(Formatting.ITALIC);
            } else {
                name = copy.getName();
            }

            if (amount <= 1 || !showAmount) {
                return name.copy();
            }

            return Text.literal("x")
                    .append(String.valueOf(amount))
                    .append(" ")
                    .append(name);
        }

        throw new IllegalStateException("Tried building content for unknown resource");
    }



    private Text buildTooltip() {
        Text tooltip = Texts.join(
                resource.getTooltip(),
                Text.literal("\n")
        );

        if (ConfigManager.getSyncedConfig().hideVerifiedTooltipLine) return tooltip;

        if (verifiedType == VerifiedType.VERIFIED) {
            return markAsVerified(tooltip);
        }
        if (verifiedType == VerifiedType.UNVERIFIED) {
            return markAsUnverified(tooltip);
        }
        return tooltip;
    }

    // modifies resource's itemStack's copy to contain verified marker in nbt
    private ItemStack prepareRenderStack(ShareableItemStack resource) {
        ItemStack copy = ((ItemStack) resource.get()).copy();

        if (ConfigManager.getClientConfig().ignoreCustomNames) copy.setCustomName(null);

        if (ConfigManager.getSyncedConfig().hideVerifiedTooltipLine) return copy;

        if (verifiedType == VerifiedType.VERIFIED) {
            copy.getOrCreateNbt().putBoolean(VERIFIED_KEY, true);
        }
        if (verifiedType == VerifiedType.UNVERIFIED) {
            copy.getOrCreateNbt().putBoolean(VERIFIED_KEY, false);
        }

        return copy;
    }

    public static Text markAsVerified(Text text) {
        MutableText mutable = text.copy();
        mutable.append("\n");
        // can cause verified message indentation desync on client upon reload but who cares
        if (ConfigManager.getClientConfig().addEmptySpaceBeforeVerifiedText) mutable.append("\n");
        mutable.append(VERIFIED_MESSAGE);
        return mutable;
    }

    public static Text markAsUnverified(Text text) {
        MutableText mutable = text.copy();
        mutable.append("\n");
        if (ConfigManager.getClientConfig().addEmptySpaceBeforeVerifiedText) mutable.append("\n");
        mutable.append(UNVERIFIED_MESSAGE);
        return mutable;
    }
}