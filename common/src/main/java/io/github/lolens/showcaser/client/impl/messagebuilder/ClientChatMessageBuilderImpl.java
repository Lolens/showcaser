/*
 * Showcaser - <https://github.com/Lolens/showcaser>
 * Copyright (C) 2026-present Lolens <https://github.com/Lolens>
 *
 * This file is part of Showcaser.
 *
 * Showcaser is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 3 of the License,
 * or (at your option) any later version.
 *
 * Showcaser is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along Showcaser.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.lolens.showcaser.client.impl.messagebuilder;

import io.github.lolens.showcaser.api.builder.ChatMessageBuilder;
import io.github.lolens.showcaser.api.resource.MessageVerification;
import io.github.lolens.showcaser.api.resource.ShareableItemStack;
import io.github.lolens.showcaser.api.sharecontext.ShareContext;
import io.github.lolens.showcaser.api.resource.ShareableResource;
import io.github.lolens.showcaser.client.render.RenderableHoverEvent;
import io.github.lolens.showcaser.config.ConfigManager;
import io.github.lolens.showcaser.util.ResourceUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

import static io.github.lolens.showcaser.util.ResourceUtils.*;

public final class ClientChatMessageBuilderImpl implements ChatMessageBuilder {

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

    private ClientChatMessageBuilderImpl(
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

    public static ClientChatMessageBuilderImpl create(
            ShareContext context,
            String sender,
            ShareableResource resource,
            MessageVerification verified
    ) {
        return new ClientChatMessageBuilderImpl(
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

    public ClientChatMessageBuilderImpl withWidth(int width) {
        return new ClientChatMessageBuilderImpl(
                context, senderName, resource,
                width, useBrackets, showAmount, messageVerification,
                clickEvent, contentStyle, translationKey, forcedName
        );
    }

    public ClientChatMessageBuilderImpl useBrackets(boolean use) {
        return new ClientChatMessageBuilderImpl(
                context, senderName, resource,
                iconWidth, use, showAmount, messageVerification,
                clickEvent, contentStyle, translationKey, forcedName
        );
    }

    public ClientChatMessageBuilderImpl showAmount(boolean show) {
        return new ClientChatMessageBuilderImpl(
                context, senderName, resource,
                iconWidth, useBrackets, show, messageVerification,
                clickEvent, contentStyle, translationKey, forcedName
        );
    }

    public ClientChatMessageBuilderImpl withClickEvent(ClickEvent event) {
        return new ClientChatMessageBuilderImpl(
                context, senderName, resource,
                iconWidth, useBrackets, showAmount, messageVerification,
                event, contentStyle, translationKey, forcedName
        );
    }

    public ClientChatMessageBuilderImpl withFormatting(Formatting... formatting) {
        return new ClientChatMessageBuilderImpl(
                context, senderName, resource,
                iconWidth, useBrackets, showAmount, messageVerification,
                clickEvent, Style.EMPTY.withFormatting(formatting),
                translationKey, forcedName
        );
    }

    public ClientChatMessageBuilderImpl withTranslationKey(String key) {
        return new ClientChatMessageBuilderImpl(
                context, senderName, resource,
                iconWidth, useBrackets, showAmount, messageVerification,
                clickEvent, contentStyle, key, forcedName
        );
    }

    public ClientChatMessageBuilderImpl withForcedDisplayName(Text name) {
        return new ClientChatMessageBuilderImpl(
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