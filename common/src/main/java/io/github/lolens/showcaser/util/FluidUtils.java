package io.github.lolens.showcaser.util;

import dev.architectury.fluid.FluidStack;
import dev.architectury.hooks.fluid.FluidStackHooks;
import dev.architectury.platform.Platform;
import io.github.lolens.showcaser.config.ConfigManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class FluidUtils {

    private static final long DROPLETS_IN_MB = 81;

    // amount should be already converted to mB
    public static List<Text> buildTooltip(Fluid fluid, long amount, PlayerEntity player, boolean showAmount) {
        // fluidStack is used only for name because it is created with x81 amount on fabric cuz droplets
        FluidStack fluidStack = FluidStack.create(fluid, amount);

        List<Text> tooltip = new ArrayList<>();

        MutableText nameText = fluidStack.getName().copy();
        tooltip.add(nameText);

        if (showAmount) {
            tooltip.add(buildFluidAmountText(
                    amount,
                    false
            ));
        }

        if (fluidStack.hasTag()) {
            tooltip.add(Text.literal(fluidStack.getTag().toString()).formatted(Formatting.GRAY));
        }
        return tooltip;
    }


    public static List<Text> buildTooltip(FluidStack fluidStack, PlayerEntity player, boolean showAmount) {
        return buildTooltip(fluidStack.getFluid(), convertToMillibuckets(fluidStack.getAmount()), player, showAmount);
    }

    public static Text buildFluidAmountText(long mbAmount, boolean alwaysInMillibuckets) {
        String blankSpace = ConfigManager.getConfig().addEmptySpaceAfterFluidAmount ? " " : "";

        if (mbAmount < 1000 || alwaysInMillibuckets) {
            return Text.of(String.format("%d%smB", mbAmount, blankSpace));
        } else {
            return Text.of(String.format("%d%sB", mbAmount / 1000, blankSpace));
        }
    }

    public static long convertToMillibuckets(long amount) {
        if (Platform.isFabric()) {
            return amount / 81;
        }
        return amount;
    }

}
