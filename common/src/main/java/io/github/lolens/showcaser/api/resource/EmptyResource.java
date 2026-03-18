package io.github.lolens.showcaser.api.resource;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

import java.util.List;

public class EmptyResource implements ShareableResource {

    @Override
    public Object get() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public Text getDisplayName() {
        return Text.empty();
    }

    @Override
    public long getAmount() {
        return 0;
    }

    @Override
    public List<Text> getTooltip() {
        return List.of();
    }

    @Override
    public Text getContent(boolean showAmount, boolean ignoreCustomName, Text forcedName) {
        return forcedName != null ? forcedName : Text.empty();
    }

    @Override
    public void render(DrawContext context, float x, float y, float scale, float alpha) {
        // context.drawItem(Items.BARRIER.getDefaultStack(), (int) x, (int) y);
    }
}
