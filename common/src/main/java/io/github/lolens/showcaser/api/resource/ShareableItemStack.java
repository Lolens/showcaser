/*
 * Showcaser - <https://github.com/Lolens/showcaser>
 * Copyright (C) 2026-present Lolens <https://github.com/Lolens>
 *
 * This file is part of Showcaser.
 *
 * Showcaser is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 3 of the License,
 * or (at your option) any later version.
 *
 * Showcaser is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along Showcaser.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.lolens.showcaser.api.resource;

import io.github.lolens.showcaser.client.render.RenderableHoverEvent;
import io.github.lolens.showcaser.config.ConfigManager;
import io.github.lolens.showcaser.util.ResourceUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
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

    public Identifier getRegistryId() {
        return this.itemStack.getItem().arch$registryName();
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

    @Override
    public Text getContent(boolean showAmount, boolean ignoreCustomName, Text forcedName) {
        Text text;
        ItemStack stack = getCopy();

        if (forcedName == null) {

            if (ConfigManager.getClientConfig().ignoreCustomNames) {
                stack.setCustomName(null);
            }

            if (stack.hasCustomName()) {
                text = stack.getName().copy().formatted(Formatting.ITALIC);
            } else {
                text = stack.getName();
            }

        } else {
            text = forcedName.copy();
        }

        if (amount <= 1 || !showAmount) {
            return text.copy();
        }

        return Text.literal("x")
                .append(String.valueOf(amount))
                .append(" ")
                .append(text);
    }

    public Rarity getRarity() {
        return itemStack.getRarity();
    }

    public ItemStack getStack() {
        return this.itemStack;
    }

    @Override
    public void render(DrawContext context, float x, float y, float scale, float alpha) {
        context.getMatrices().push();
        context.getMatrices().scale(scale, scale, 1.0F);
        context.getMatrices().translate(x / scale, y / scale, 200.0F);
        context.getMatrices().scale(0.5f, 0.5f, 0.5f);
        context.drawItem(itemStack, 0, 0);
        context.getMatrices().pop();
    }

}
