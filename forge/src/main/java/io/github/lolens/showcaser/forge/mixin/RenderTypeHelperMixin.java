package io.github.lolens.showcaser.forge.mixin;

import io.github.lolens.showcaser.client.render.RenderableHoverEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.RenderTypeHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderTypeHelper.class)
public class RenderTypeHelperMixin {

    @Inject(method = "getFallbackItemRenderType", at = @At("HEAD"), cancellable = true)
    private static void overrideRenderType(ItemStack stack, BakedModel model, boolean cull, CallbackInfoReturnable<RenderLayer> cir) {
        if (RenderableHoverEvent.currentAlpha != 1) {
            if (cull) {
                cir.setReturnValue(TexturedRenderLayers.getEntityTranslucentCull());
            } else {
                cir.setReturnValue(TexturedRenderLayers.getItemEntityTranslucentCull());
            }
        }
    }

}
