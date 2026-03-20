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