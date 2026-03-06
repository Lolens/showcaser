package io.github.lolens.showcaser.client.render;

import io.github.lolens.showcaser.api.resource.IconRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;

public class RenderableHoverEvent extends HoverEvent {

    public static float currentAlpha = 1.0f;

    private final IconRenderer iconRenderer;
    private final int width;

    public RenderableHoverEvent(int width, IconRenderer iconRenderer, Text text) {
        super(Action.SHOW_TEXT, text);
        this.width = width;
        this.iconRenderer = iconRenderer;
    }

    public RenderableHoverEvent(int width, IconRenderer iconRenderer, ItemStack stack) {
        super(Action.SHOW_ITEM, new ItemStackContent(stack));
        this.width = width;
        this.iconRenderer = iconRenderer;
    }

    public IconRenderer getRenderer() {
        return iconRenderer;
    }
    public int getWidth() {
        return this.width;
    }

}