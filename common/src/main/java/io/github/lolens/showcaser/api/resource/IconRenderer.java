package io.github.lolens.showcaser.api.resource;

import net.minecraft.client.gui.DrawContext;

public interface IconRenderer {

    void render(DrawContext context, float x, float y, float scale);
}
