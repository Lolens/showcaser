package io.github.lolens.showcaser.mixin;

import io.github.lolens.showcaser.Showcaser;
import io.github.lolens.showcaser.client.render.RenderableHoverEvent;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderLayers.class)
public class RenderLayersMixin {
    @Inject(method = "getEntityBlockLayer", at = @At("HEAD"), cancellable = true)
    private static void overrideRenderType(BlockState state, boolean direct, CallbackInfoReturnable<RenderLayer> cir) {
        if (RenderableHoverEvent.currentAlpha != 1) {
            if (!MinecraftClient.isFabulousGraphicsOrBetter()) {
                cir.setReturnValue(TexturedRenderLayers.getEntityTranslucentCull());
            } else {
                cir.setReturnValue(direct ? TexturedRenderLayers.getEntityTranslucentCull() : TexturedRenderLayers.getItemEntityTranslucentCull());
            }
        }
    }

}
