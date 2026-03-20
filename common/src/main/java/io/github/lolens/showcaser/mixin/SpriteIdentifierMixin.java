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
