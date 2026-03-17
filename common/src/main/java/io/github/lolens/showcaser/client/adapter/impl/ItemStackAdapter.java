package io.github.lolens.showcaser.client.adapter.impl;

import io.github.lolens.showcaser.api.adapter.BaseShareContextAdapter;
import io.github.lolens.showcaser.api.resource.EmptyResource;
import io.github.lolens.showcaser.api.resource.ShareableItemStack;
import io.github.lolens.showcaser.api.resource.ShareableResource;
import io.github.lolens.showcaser.api.shareContext.ShareContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ItemStackAdapter extends BaseShareContextAdapter {

    public ItemStackAdapter(Identifier identifier) {
        super(identifier);
    }

    @Override
    public ShareableResource adapt(ShareContext context) {
        ItemStack stack = context.getItemStack("stack");
        if (stack == null) return new EmptyResource();
        return new ShareableItemStack(stack);
    }
}
