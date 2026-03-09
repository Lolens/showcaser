package io.github.lolens.showcaser.mixin;

import io.github.lolens.showcaser.config.ConfigManager;
import io.github.lolens.showcaser.client.ClientChatMessageBuilder;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    // lowering priority helps with ae2 "hold g to open guidebook" interaction
    @Inject(method = "getTooltip", at = @At("RETURN"), cancellable = true, order = 10000)
    private void showcaser$addVerifiedText(@Nullable PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir) {
        List<Text> tooltip = cir.getReturnValue();

        ItemStack stack = (ItemStack) (Object) this;

        if (stack.hasNbt() && stack.getNbt().contains("showcaser_verified")) {
            boolean isVerified = stack.getNbt().getBoolean("showcaser_verified");

            Text verifiedText = isVerified
                    ? ClientChatMessageBuilder.VERIFIED_MESSAGE
                    : ClientChatMessageBuilder.UNVERIFIED_MESSAGE;

            // tooltip.add(ScreenTexts.EMPTY);

            if (ConfigManager.getClientConfig().addEmptySpaceBeforeVerifiedText) tooltip.add(ScreenTexts.EMPTY);
            tooltip.add(verifiedText);

            cir.setReturnValue(tooltip);
        }
    }

}
