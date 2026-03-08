package io.github.lolens.showcaser.mixin;

import io.github.lolens.showcaser.client.render.WrappedVertexConsumerProvider;
import io.github.lolens.showcaser.client.render.RenderableHoverEvent;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {

    @ModifyVariable(
            method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
            at = @At("HEAD"),
            index = 5,
            argsOnly = true
    )
    public VertexConsumerProvider wrapVertexConsumerProviderWithAlpha(VertexConsumerProvider value) {
        if (RenderableHoverEvent.currentAlpha != 1.0f) {
            return new WrappedVertexConsumerProvider(value);
        }
        return value;
    }


}
