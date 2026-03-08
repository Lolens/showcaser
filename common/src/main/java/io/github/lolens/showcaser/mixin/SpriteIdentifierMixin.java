package io.github.lolens.showcaser.mixin;

import io.github.lolens.showcaser.client.render.RenderableHoverEvent;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;

@Mixin(SpriteIdentifier.class)
public class SpriteIdentifierMixin {

    // to make BERs respect alpha
    @Inject(
            method = "getRenderLayer",
            at = @At("HEAD"),
            cancellable = true
    )
    private void overrideLayer(Function<Identifier, RenderLayer> factory,
                               CallbackInfoReturnable<RenderLayer> cir) {

        if (RenderableHoverEvent.currentAlpha != 1f) {
            cir.setReturnValue(RenderLayer.getEntityTranslucentCull(
                    ((SpriteIdentifier) (Object) this).getAtlasId()
            ));
        }
    }
}
