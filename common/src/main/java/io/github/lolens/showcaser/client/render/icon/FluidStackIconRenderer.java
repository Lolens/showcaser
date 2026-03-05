package io.github.lolens.showcaser.client.render.icon;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.architectury.fluid.FluidStack;
import dev.architectury.hooks.fluid.FluidStackHooks;
import io.github.lolens.showcaser.util.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.texture.Sprite;
import net.minecraft.fluid.Fluid;

public class FluidStackIconRenderer implements IconRenderer {

    FluidStack fluidStack;


    public FluidStackIconRenderer(Fluid fluid) {
        this.fluidStack = FluidStack.create(fluid, 1000);
    }

    public FluidStackIconRenderer(FluidStack fluidStack) {
        this.fluidStack = fluidStack;
    }

    @Override
    public void render(DrawContext context, float x, float y, float scale) {

        if (fluidStack.isEmpty()) return;
        Sprite sprite = FluidStackHooks.getStillTexture(fluidStack);
        if (sprite == null) return;
        int color = FluidStackHooks.getColor(fluidStack);

        float[] rgb = RenderUtils.intToRGBNormalized(color);

        RenderSystem.setShaderColor(rgb[0], rgb[1], rgb[2], 1);

        context.getMatrices().push();
        context.getMatrices().scale(scale, scale, 1.0F);
        context.getMatrices().translate(x / scale, y / scale, 200.0F);
        context.getMatrices().scale(0.5f, 0.5f, 0.5f);

        // RenderSystem.setShaderTexture(0, sprite.getAtlasId());
        context.drawSprite(
                0,
                0,
                0,
                16,
                16,
                sprite
        );

        RenderSystem.setShaderColor(1f,1f,1f,1f);
        context.getMatrices().pop();

    }
}
