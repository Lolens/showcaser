package io.github.lolens.showcaser.api.resource;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Rarity;

import java.util.List;

public class ShareableItemStack implements ShareableResource {
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

    public ItemStack getCopy() {
        return itemStack.copy();
    }

    @Override
    public boolean isEmpty() {
        return itemStack.isEmpty();
    }

    @Override
    public Text getDisplayName() {
        return itemStack.getName();
    }

    public ItemStack setCustomName(Text name) {
        return itemStack.setCustomName(name);
    }

    @Override
    public long getAmount() {
        return amount;
    }

    public boolean hasCustomName() {
        return itemStack.hasCustomName();
    }

    public List<Text> getTooltip() {
        return itemStack.getTooltip(client.player, TooltipContext.BASIC);
    }

    public Rarity getRarity() {
        return itemStack.getRarity();
    }

    @Override
    public void render(DrawContext context, float x, float y, float scale, float alpha) {

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, alpha);
        context.getMatrices().push();
        context.getMatrices().scale(scale, scale, 1.0F);
        context.getMatrices().translate(x / scale, y / scale, 200.0F);
        context.getMatrices().scale(0.5f, 0.5f, 0.5f);
        context.drawItem(itemStack, 0, 0);
        context.getMatrices().pop();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

    }


}
