package io.github.lolens.showcaser.client.render.icon;

import net.minecraft.client.gui.DrawContext;

public interface IconRenderer {

    void render(DrawContext context, float x, float y, float scale);
}
