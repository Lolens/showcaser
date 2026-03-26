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

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.lolens.showcaser.client.render.RenderableHoverEvent;
import io.github.lolens.showcaser.util.PlatformUtils;
import io.github.lolens.showcaser.util.ResourceUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.OrderedText;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;


@Mixin(value = ChatHud.class)
public abstract class ChatHudRenderMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Unique
    float showcaser$currentChatLineAlpha = 1.0f;

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/OrderedText;III)I"
            )
    )
    private int renderItemInline(DrawContext context, TextRenderer textRenderer, OrderedText originalText, int x, int y, int color, Operation<Integer> original) {

        this.showcaser$currentChatLineAlpha = ((color >> 24) & 0xFF) / 255.0f; // 0f to 1f


        int result = original.call(context, textRenderer, originalText, x, y, color);

        RenderableHoverEvent.currentAlpha = showcaser$currentChatLineAlpha;
        showcaser$renderIcons(context, originalText, x, y, showcaser$currentChatLineAlpha);
        RenderableHoverEvent.currentAlpha = 1.0f;


        return result;
    }

    @Unique
    private void showcaser$renderIcons(DrawContext context, OrderedText originalText, int baseX, int baseY, float alpha) {
        final float[] currentX = {0};
        final float scale = (float) getChatScale();

        // render and add renderableHover width or just add char width
        originalText.accept((index, style, codePoint) -> {
            if (codePoint == ResourceUtils.MARKER) {
                HoverEvent hover = style.getHoverEvent();
                if (hover instanceof RenderableHoverEvent renderableHover) {

                    float renderX = baseX + currentX[0] * scale;
                    float renderY = baseY;

                    RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, alpha);
                    renderableHover.getRenderer().render(context, renderX, renderY, scale, alpha);
                    RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

                }
                return true;
            }

            currentX[0] += PlatformUtils.measureText(String.valueOf((char) codePoint));
            return true;

        });
    }

    @Shadow
    public abstract double getChatScale();
}