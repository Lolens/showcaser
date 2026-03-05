package io.github.lolens.showcaser.api.resource;

import io.github.lolens.showcaser.Showcaser;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.List;

public class ShareableItemStack implements ShareableResource {
    // private final Identifier id = Identifier.of(Showcaser.MOD_ID, "item_stack");
    private MinecraftClient client = MinecraftClient.getInstance();

    private final ItemStack itemStack;
    private long amount;

    public ShareableItemStack(ItemStack stack) {
        this.itemStack = stack;
        this.amount = stack.getCount();
    }

    public ShareableItemStack(ItemStack stack, long amount) {
        this.itemStack = stack;
        this.amount = amount;
    }

    public ShareableItemStack(Item item, long amount) {
        this.itemStack = item.getDefaultStack();
        this.amount = amount;
    }

    @Override
    public Object get() {
        return itemStack;
    }

    @Override
    public boolean isEmpty() {
        return itemStack.isEmpty();
    }

    @Override
    public Text getDisplayName() {
        return itemStack.getName();
    }

    @Override
    public long getAmount() {
        return amount;
    }

    public List<Text> getTooltip() {
        return itemStack.getTooltip(client.player, TooltipContext.BASIC);
    }

    @Override
    public void render(DrawContext context, float x, float y, float scale) {
        context.getMatrices().push();
        context.getMatrices().scale(scale, scale, 1.0F);
        context.getMatrices().translate(x / scale, y / scale, 200.0F);
        context.getMatrices().scale(0.5f, 0.5f, 0.5f);
        context.drawItem(itemStack, 0, 0);
        context.getMatrices().pop();
    }


}
