package io.github.lolens.showcaser.client.render.icon;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;

public class ItemStackIconRenderer implements IconRenderer {

    private final ItemStack stack;

    public ItemStackIconRenderer(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public void render(DrawContext context, float x, float y, float scale) {
        context.getMatrices().push();
        context.getMatrices().scale(scale, scale, 1.0F);
        context.getMatrices().translate(x / scale, y / scale, 200.0F);
        context.getMatrices().scale(0.5f, 0.5f, 0.5f);
        context.drawItem(stack, 0, 0);
        context.getMatrices().pop();
    }
}
