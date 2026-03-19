package io.github.lolens.showcaser.client.adapter.impl.forge;

import com.google.gson.JsonElement;
import dev.architectury.fluid.FluidStack;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.FluidEmiStack;
import dev.emi.emi.api.stack.ItemEmiStack;
import dev.emi.emi.registry.EmiIngredientSerializers;
import io.github.lolens.showcaser.api.adapter.BaseShareContextAdapter;
import io.github.lolens.showcaser.api.resource.EmptyResource;
import io.github.lolens.showcaser.api.resource.ShareableFluidStack;
import io.github.lolens.showcaser.api.resource.ShareableItemStack;
import io.github.lolens.showcaser.api.resource.ShareableResource;
import io.github.lolens.showcaser.api.sharecontext.ShareContext;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.Identifier;

public class EmiAdapter extends BaseShareContextAdapter {

    public EmiAdapter(Identifier identifier) {
        super(identifier);
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public ShareableResource adapt(ShareContext context) {
        JsonElement json = context.getJsonElement();
        EmiIngredient ingredient = EmiIngredientSerializers.deserialize(json);

        if (ingredient instanceof ItemEmiStack emiStack) {
            return new ShareableItemStack(emiStack.getItemStack());
        }
        if (ingredient instanceof FluidEmiStack emiStack) {
            FluidStack fluidStackArch = FluidStack.create((Fluid) emiStack.getKey(), 1000);
            return new ShareableFluidStack(fluidStackArch);
        }

        return new EmptyResource();
    }
}
