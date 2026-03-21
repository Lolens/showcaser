package io.github.lolens.showcaser.client.adapter.impl.fabric;

import dev.architectury.fluid.FluidStack;
import io.github.lolens.showcaser.api.adapter.BaseShareContextAdapter;
import io.github.lolens.showcaser.api.resource.EmptyResource;
import io.github.lolens.showcaser.api.resource.ShareableFluidStack;
import io.github.lolens.showcaser.api.resource.ShareableItemStack;
import io.github.lolens.showcaser.api.resource.ShareableResource;
import io.github.lolens.showcaser.api.sharecontext.ShareContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class JeiAdapter extends BaseShareContextAdapter {

    public JeiAdapter(Identifier identifier) {
        super(identifier);
    }

    @Override
    public ShareableResource adapt(ShareContext context) {
        NbtCompound entryNbt = context.getCompound("entry");

        String type = context.getString("type");

        switch (type) {
            case "minecraft:item" -> {
                ItemStack stack = ItemStack.fromNbt(entryNbt);
                return new ShareableItemStack(stack);
            }
            case "minecraft:fluid" -> {
                FluidStack stack = FluidStack.read(entryNbt);
                return new ShareableFluidStack(stack);
            }
        }
        return new EmptyResource();
    }
}
