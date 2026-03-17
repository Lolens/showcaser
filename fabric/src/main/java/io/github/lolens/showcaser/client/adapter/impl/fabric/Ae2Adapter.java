package io.github.lolens.showcaser.client.adapter.impl.fabric;

import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import dev.architectury.fluid.FluidStack;
import io.github.lolens.showcaser.api.adapter.BaseShareContextAdapter;
import io.github.lolens.showcaser.api.resource.EmptyResource;
import io.github.lolens.showcaser.api.resource.ShareableFluidStack;
import io.github.lolens.showcaser.api.resource.ShareableItemStack;
import io.github.lolens.showcaser.api.resource.ShareableResource;
import io.github.lolens.showcaser.api.shareContext.ShareContext;
import net.minecraft.util.Identifier;

public class Ae2Adapter extends BaseShareContextAdapter {

    public Ae2Adapter(Identifier identifier) {
        super(identifier);
    }

    @Override
    public ShareableResource adapt(ShareContext context) {
        AEKey key = AEKey.fromTagGeneric(context.getCompound("key"));
        long amount = context.getAmount();

        if (key instanceof AEItemKey itemKey) {
            return new ShareableItemStack(itemKey.toStack(), amount);
        }
        if (key instanceof AEFluidKey fluidKey) {
            FluidStack fluidStack = FluidStack.create(fluidKey.getFluid(), amount);
            return new ShareableFluidStack(fluidStack);
        }

        return new EmptyResource();
    }
}
