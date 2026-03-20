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

package io.github.lolens.showcaser.fabric.mixin;

import io.github.lolens.showcaser.client.render.RenderableHoverEvent;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.TexturedRenderLayers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(RenderLayers.class)
public class RenderLayersMixin {
    @Inject(method = "getEntityBlockLayer", at = @At("HEAD"), cancellable = true)
    private static void overrideEntityBlockRenderLayer(BlockState state, boolean direct, CallbackInfoReturnable<RenderLayer> cir) {
        if (RenderableHoverEvent.currentAlpha != 1) {
            if (!MinecraftClient.isFabulousGraphicsOrBetter()) {
                cir.setReturnValue(TexturedRenderLayers.getEntityTranslucentCull());
            } else {
                cir.setReturnValue(direct ? TexturedRenderLayers.getEntityTranslucentCull() : TexturedRenderLayers.getItemEntityTranslucentCull());
            }
        }
    }

}
