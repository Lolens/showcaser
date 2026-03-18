package io.github.lolens.showcaser.util;

import io.github.lolens.showcaser.api.resource.MessageVerification;
import io.github.lolens.showcaser.api.resource.ShareableResource;
import io.github.lolens.showcaser.config.ConfigManager;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Formatting;

public class ResourceUtils {

    private ResourceUtils() {

    }

    public static final String VERIFIED_KEY = "showcaser_verified";

    public static final Text UNVERIFIED_MESSAGE =
            Text.translatable("showcaser.chat.share_message.tooltip.unverified")
                    .formatted(Formatting.RED, Formatting.BOLD);

    public static final Text VERIFIED_MESSAGE =
            Text.translatable("showcaser.chat.share_message.tooltip.verified")
                    .formatted(Formatting.GREEN, Formatting.BOLD);

    public static final char MARKER = '\uE670';

    /**
     * Appends specific marker to stack nbt to display it as verified
     * @param stack to be marked as verified
     * @param verified verification type
     * @return verified copy of the stack
     */
    public static ItemStack markStackVerified(ItemStack stack, MessageVerification verified) {
        ItemStack copy = stack.copy();

        if (ConfigManager.getSyncedConfig().hideVerifiedTooltipLine) return copy;

        switch (verified) {
            case VERIFIED -> copy.getOrCreateNbt().putBoolean(VERIFIED_KEY, true);
            case UNVERIFIED -> copy.getOrCreateNbt().putBoolean(VERIFIED_KEY, false);
        }

        return copy;
    }


    public static Text markTooltip(ShareableResource resource, MessageVerification verification) {
        Text tooltip = Texts.join(
                resource.getTooltip(),
                Text.literal("\n")
        );
        return markTooltip(tooltip, verification);
    }

    public static Text markTooltip(Text tooltip, MessageVerification verified) {
        if (ConfigManager.getSyncedConfig().hideVerifiedTooltipLine) return tooltip;

        if (verified == MessageVerification.VERIFIED) {
            return markAsVerified(tooltip);
        }
        if (verified == MessageVerification.UNVERIFIED) {
            return markAsUnverified(tooltip);
        }
        return tooltip;
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
