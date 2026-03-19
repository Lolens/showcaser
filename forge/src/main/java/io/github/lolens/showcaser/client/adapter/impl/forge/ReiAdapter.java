package io.github.lolens.showcaser.client.adapter.impl.forge;

import dev.architectury.fluid.FluidStack;
import io.github.lolens.showcaser.api.adapter.BaseShareContextAdapter;
import io.github.lolens.showcaser.api.resource.EmptyResource;
import io.github.lolens.showcaser.api.resource.ShareableFluidStack;
import io.github.lolens.showcaser.api.resource.ShareableItemStack;
import io.github.lolens.showcaser.api.resource.ShareableResource;
import io.github.lolens.showcaser.api.sharecontext.ShareContext;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class ReiAdapter extends BaseShareContextAdapter {

    public ReiAdapter(Identifier identifier) {
        super(identifier);
    }

    @Override
    public ShareableResource adapt(ShareContext context) {
        NbtCompound entryNbt = context.getCompound("entry");
        EntryStack<?> entry = EntryStack.read(entryNbt);

        if (entry.getType() == VanillaEntryTypes.ITEM) {
            ItemStack stack = entry.castValue();
            return new ShareableItemStack(stack);
        }
        if (entry.getType() == VanillaEntryTypes.FLUID) {
            FluidStack fluid = entry.castValue();
            return new ShareableFluidStack(fluid);
        }

        return new EmptyResource();
    }
}
