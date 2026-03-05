package io.github.lolens.showcaser.mixin;

import io.github.lolens.showcaser.Showcaser;
import io.github.lolens.showcaser.client.render.RenderableHoverEvent;
import net.minecraft.text.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.client.font.TextRenderer$Drawer")
public class TextRendererDrawerMixin {
    @Shadow
    float x;

    @Inject(method = "accept", at = @At("HEAD"), cancellable = true)
    private void onAccept(int index, Style style, int codePoint, CallbackInfoReturnable<Boolean> cir) {
        if (codePoint != '\uE670') return;

        if (style.getHoverEvent() instanceof RenderableHoverEvent renderableHoverEvent) {
            this.x += renderableHoverEvent.getWidth();
        }

        cir.setReturnValue(true);
    }
}