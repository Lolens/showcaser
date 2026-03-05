package io.github.lolens.showcaser.adapter.forge;

import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import com.google.gson.JsonElement;
import dev.architectury.fluid.FluidStack;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.FluidEmiStack;
import dev.emi.emi.api.stack.ItemEmiStack;
import dev.emi.emi.registry.EmiIngredientSerializers;
import io.github.lolens.showcaser.Showcaser;
import io.github.lolens.showcaser.api.resource.EmptyResource;
import io.github.lolens.showcaser.api.resource.ShareableFluidStack;
import io.github.lolens.showcaser.api.resource.ShareableItemStack;
import io.github.lolens.showcaser.api.resource.ShareableResource;
import io.github.lolens.showcaser.model.ShareContext;
import io.github.lolens.showcaser.util.PlatformUtils;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class AdapterFactoryImpl {

    public static ShareableResource fromContext(ShareContext context) {

        if (context == null) return new EmptyResource();

        String id = context.getId().getPath();

        return switch (id) {

            case "fallback", "player_inventory", "creative_inventory" -> createItemAdapter(context);
            case "ae2" -> createAe2Adapter(context);
            case "rei" -> createReiAdapter(context);
            case "emi" -> createEmiAdapter(context);

            default -> new EmptyResource();
        };
    }

    private static ShareableResource createItemAdapter(ShareContext context) {
        ItemStack stack = context.getItemStack("stack");
        return new ShareableItemStack(stack);
    }

    private static ShareableResource createAe2Adapter(ShareContext context) {
        if (!PlatformUtils.isAE2Loaded()) return new EmptyResource();

        try {
            AEKey key = AEKey.fromTagGeneric(context.getCompound("key"));
            long amount = context.getAmount();

            if (key instanceof AEItemKey itemKey) {
                return new ShareableItemStack(itemKey.toStack());
            }
            if (key instanceof AEFluidKey fluidKey) {
                FluidStack fluidStack = FluidStack.create(fluidKey.getFluid(), amount);
                return new ShareableFluidStack(fluidStack);
            }
        } catch (Exception e) {
            Showcaser.LOGGER.error("Failed to create AE2 adapter", e);
        }

        return new EmptyResource();
    }

    private static ShareableResource createReiAdapter(ShareContext context) {
        if (!PlatformUtils.isREILoaded()) return new EmptyResource();

        try {
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
        } catch (Exception e) {
            Showcaser.LOGGER.error("Failed to create REI adapter", e);
        }

        return new EmptyResource();
    }

    @SuppressWarnings("UnstableApiUsage")
    private static ShareableResource createEmiAdapter(ShareContext context) {
        if (!PlatformUtils.isEMILoaded()) return new EmptyResource();

        try {
            JsonElement json = context.getJsonElement();
            EmiIngredient ingredient = EmiIngredientSerializers.deserialize(json);

            if (ingredient instanceof ItemEmiStack emiStack) {
                return new ShareableItemStack(emiStack.getItemStack());
            }
            if (ingredient instanceof FluidEmiStack emiStack) {
                FluidStack fluidStackArch = FluidStack.create((Fluid) emiStack.getKey(), 1000);
                return new ShareableFluidStack(fluidStackArch);
            }
        } catch (Exception e) {
            Showcaser.LOGGER.error("Failed to create EMI adapter", e);
        }

        return new EmptyResource();
    }
}