package io.github.lolens.showcaser.api.resource;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.List;

public interface ShareableResource extends IconRenderer {

    Object get();

    boolean isEmpty();

    Text getDisplayName();

    long getAmount();

    List<Text> getTooltip();

    void render(DrawContext context, float x, float y, float scale, float alpha);
}
