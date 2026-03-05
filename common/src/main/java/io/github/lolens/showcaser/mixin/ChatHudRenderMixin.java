package io.github.lolens.showcaser.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.lolens.showcaser.client.render.RenderableHoverEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.item.ItemStack;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.OrderedText;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;


@Mixin(ChatHud.class)
public abstract class ChatHudRenderMixin {

    @Shadow @Final private MinecraftClient client;

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/OrderedText;III)I"
            )
    )
    private int renderItemInline(DrawContext context, TextRenderer textRenderer, OrderedText originalText, int x, int y, int color, Operation<Integer> original) {

        int result = original.call(context, textRenderer, originalText, x, y, color);

        showcaser$renderIcons(context, originalText, x, y);

        return result;
    }

    @Unique
    private void showcaser$renderIcons(DrawContext context, OrderedText originalText, int baseX, int baseY) {
        final float[] currentX = {0};
        final float scale = (float) getChatScale();

        originalText.accept((index, style, codePoint) -> {
            if (codePoint == '\uE670') {
                HoverEvent hover = style.getHoverEvent();
                if (hover instanceof RenderableHoverEvent renderableHover) {

                    float renderX = baseX + currentX[0] * scale;
                    float renderY = baseY;

                    renderableHover.getRenderer().render(context, renderX, renderY, scale);
                }
                return true;
            }

            currentX[0] += client.textRenderer.getWidth(String.valueOf((char) codePoint));
            return true;
        });
    }

    @Shadow
    public abstract double getChatScale();
}