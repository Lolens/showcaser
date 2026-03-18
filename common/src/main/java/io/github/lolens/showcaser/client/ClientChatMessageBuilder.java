package io.github.lolens.showcaser.client;

import io.github.lolens.showcaser.api.resource.MessageVerification;
import io.github.lolens.showcaser.api.resource.ShareableItemStack;
import io.github.lolens.showcaser.api.shareContext.ShareContext;
import io.github.lolens.showcaser.api.resource.ShareableFluidStack;
import io.github.lolens.showcaser.api.resource.ShareableResource;
import io.github.lolens.showcaser.client.render.RenderableHoverEvent;
import io.github.lolens.showcaser.config.ConfigManager;
import io.github.lolens.showcaser.util.ResourceUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

import java.util.List;

import static io.github.lolens.showcaser.util.ResourceUtils.*;

public final class ClientChatMessageBuilder {

    private final ShareContext context;
    private final String senderName;
    private final ShareableResource resource;

    private final int iconWidth;
    private final boolean useBrackets;
    private final boolean showAmount;
    private final MessageVerification messageVerification;
    private final ClickEvent clickEvent;
    private final Style contentStyle;
    private final String translationKey;
    private final Text forcedName;

    private ClientChatMessageBuilder(
            ShareContext context,
            String senderName,
            ShareableResource resource,
            int iconWidth,
            boolean useBrackets,
            boolean showAmount,
            MessageVerification messageVerification,
            ClickEvent clickEvent,
            Style contentStyle,
            String translationKey,
            Text forcedName
    ) {
        this.context = context;
        this.senderName = senderName;
        this.resource = resource;
        this.iconWidth = iconWidth;
        this.useBrackets = useBrackets;
        this.showAmount = showAmount;
        this.messageVerification = messageVerification;
        this.clickEvent = clickEvent;
        this.contentStyle = contentStyle;
        this.translationKey = translationKey;
        this.forcedName = forcedName;
    }

    public static ClientChatMessageBuilder create(
            ShareContext context,
            String sender,
            ShareableResource resource,
            MessageVerification verified
    ) {
        return new ClientChatMessageBuilder(
                context, sender, resource,
                12,
                true,
                true,
                verified,
                null,
                Style.EMPTY,
                "showcaser.chat.share_message",
                null
        );
    }

    // === BUILDER METHODS ===

    public ClientChatMessageBuilder withWidth(int width) {
        return new ClientChatMessageBuilder(
                context, senderName, resource,
                width, useBrackets, showAmount, messageVerification,
                clickEvent, contentStyle, translationKey, forcedName
        );
    }

    public ClientChatMessageBuilder useBrackets(boolean use) {
        return new ClientChatMessageBuilder(
                context, senderName, resource,
                iconWidth, use, showAmount, messageVerification,
                clickEvent, contentStyle, translationKey, forcedName
        );
    }

    public ClientChatMessageBuilder showAmount(boolean show) {
        return new ClientChatMessageBuilder(
                context, senderName, resource,
                iconWidth, useBrackets, show, messageVerification,
                clickEvent, contentStyle, translationKey, forcedName
        );
    }

    public ClientChatMessageBuilder withClickEvent(ClickEvent event) {
        return new ClientChatMessageBuilder(
                context, senderName, resource,
                iconWidth, useBrackets, showAmount, messageVerification,
                event, contentStyle, translationKey, forcedName
        );
    }

    public ClientChatMessageBuilder withFormatting(Formatting... formatting) {
        return new ClientChatMessageBuilder(
                context, senderName, resource,
                iconWidth, useBrackets, showAmount, messageVerification,
                clickEvent, Style.EMPTY.withFormatting(formatting),
                translationKey, forcedName
        );
    }

    public ClientChatMessageBuilder withTranslationKey(String key) {
        return new ClientChatMessageBuilder(
                context, senderName, resource,
                iconWidth, useBrackets, showAmount, messageVerification,
                clickEvent, contentStyle, key, forcedName
        );
    }

    public ClientChatMessageBuilder withForcedDisplayName(Text name) {
        return new ClientChatMessageBuilder(
                context, senderName, resource,
                iconWidth, useBrackets, showAmount, messageVerification,
                clickEvent, contentStyle, translationKey, name
        );
    }

    public MutableText build() {

        RenderableHoverEvent hoverEvent = buildHoverEvent();

        MutableText marker = Text.literal(String.valueOf(ResourceUtils.MARKER))
                .styled(style -> style
                        .withClickEvent(clickEvent)
                        .withHoverEvent(hoverEvent)
                        // for items with rarity use rarity even for brackets
                        .withFormatting(resource instanceof ShareableItemStack itemStack
                                ? itemStack.getRarity().formatting
                                : Formatting.WHITE)
                );

        MutableText content = resource.getContent(
                showAmount,
                ConfigManager.getClientConfig().ignoreCustomNames,
                forcedName
        ).copy();

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

        // use marked default stack tooltip for stacks and custom text for everything else
        if (resource instanceof ShareableItemStack itemStack) {
            ItemStack copy = ResourceUtils.markStackVerified(itemStack.getStack(), messageVerification);

            if (ConfigManager.getClientConfig().ignoreCustomNames) copy.setCustomName(null);

            return new RenderableHoverEvent(
                    iconWidth,
                    resource,
                    copy
            );
        } else {
            Text fullTooltip = markTooltip(resource, messageVerification);
            return new RenderableHoverEvent(
                    iconWidth,
                    resource,
                    fullTooltip
            );
        }
    }

}