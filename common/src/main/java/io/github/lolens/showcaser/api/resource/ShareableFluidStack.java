package io.github.lolens.showcaser.api.resource;

import dev.architectury.fluid.FluidStack;
import dev.architectury.hooks.fluid.FluidStackHooks;
import dev.architectury.platform.Platform;
import io.github.lolens.showcaser.util.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.texture.Sprite;
import net.minecraft.fluid.Fluid;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class ShareableFluidStack implements ShareableResource {

    private final FluidStack fluidStack;

    public ShareableFluidStack(FluidStack fluidStack) {
        this.fluidStack = fluidStack;
    }

    @Override
    public Object get() {
        return fluidStack;
    }

    public FluidStack getFluidStack() {
        return this.fluidStack;
    }

    public Identifier getRegistryId() {
        return this.fluidStack.getFluid().arch$registryName();
    }

    @Override
    public boolean isEmpty() {
        return fluidStack.isEmpty();
    }

    @Override
    public Text getDisplayName() {
        return fluidStack.getName();
    }

    @Override
    public long getAmount() {
        return fluidStack.getAmount();
    }

    @Override
    public List<Text> getTooltip() {
        return buildTooltip(fluidStack, false);
    }

    public List<Text> getTooltip(boolean displayAmount) {
        return buildTooltip(fluidStack, displayAmount);
    }

    @Override
    public Text getContent(boolean showAmount, boolean ignoreCustomName, Text forcedName) {
        long amount = getAmount();
        if (amount <= 1 || !showAmount) {
            return getDisplayName().copy();
        }

        return getFluidAmountText()
                .copy()
                .append(" ")
                .append(getDisplayName());
    }

    public static List<Text> buildTooltip(Fluid fluid, long amount, boolean showAmount) {
        // fluidStack is used only for name because it is created with x81 amount on fabric cuz droplets
        FluidStack fluidStack = FluidStack.create(fluid, amount);

        List<Text> tooltip = new ArrayList<>();

        MutableText nameText = fluidStack.getName().copy();
        tooltip.add(nameText);

        if (showAmount) {
            tooltip.add(getFluidAmountText(amount));
        }

        if (fluidStack.hasTag()) {
            tooltip.add(Text.literal(fluidStack.getTag().toString()).formatted(Formatting.GRAY));
        }
        return tooltip;
    }


    public static List<Text> buildTooltip(FluidStack fluidStack, boolean showAmount) {
        return buildTooltip(fluidStack.getFluid(), convertToMillibuckets(fluidStack.getAmount()), showAmount);
    }

    public Text getFluidAmountText() {
        return getFluidAmountText(convertToMillibuckets(fluidStack.getAmount()));
    }

    // convert to mBs before using this method
    public static Text getFluidAmountText(long mbAmount) {
        return Text.of(formatFluid(mbAmount));
    }

    private static final String[] UNITS = {
            " mB", " B", "K B", "M B", "G B", "T B"
    };

    public static String formatFluid(long mbAmount) {
        long value = mbAmount;
        long divisor = 1;
        int unit = 0;

        while (value >= 1000 && unit < UNITS.length - 1) {
            value /= 1000;
            divisor *= 1000;
            unit++;
        }

        if (value < 10 && unit > 0) {

            long whole = mbAmount / divisor;
            long decimal = (mbAmount % divisor) * 10 / divisor;

            if (decimal > 0) {
                return whole + "." + decimal + UNITS[unit];
            }

            return whole + UNITS[unit];
        }

        return value + UNITS[unit];
    }



    public static long convertToMillibuckets(long amount) {
        if (Platform.isFabric()) {
            return amount / 81; // droplets in mb
        }
        return amount;
    }

    @Override
    public void render(DrawContext context, float x, float y, float scale, float alpha) {
        if (fluidStack.isEmpty()) return;
        Sprite sprite = FluidStackHooks.getStillTexture(fluidStack);
        if (sprite == null) return;
        int color = FluidStackHooks.getColor(fluidStack);

        float[] fluidRbg = RenderUtils.intToRGBNormalized(color);
                context.getMatrices().push();
        context.getMatrices().scale(scale, scale, 1.0F);
        context.getMatrices().translate(x / scale, y / scale, 200.0F);
        context.getMatrices().scale(0.5f, 0.5f, 0.5f);

        context.drawSprite(
                0,
                0,
                0,
                16,
                16,
                sprite,
                fluidRbg[0],
                fluidRbg[1],
                fluidRbg[2],
                alpha
        );

        context.getMatrices().pop();

    }
}
