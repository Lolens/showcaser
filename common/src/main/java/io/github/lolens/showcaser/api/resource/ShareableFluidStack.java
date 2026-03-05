package io.github.lolens.showcaser.api.resource;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.architectury.fluid.FluidStack;
import dev.architectury.hooks.fluid.FluidStackHooks;
import dev.architectury.platform.Platform;
import io.github.lolens.showcaser.config.ConfigManager;
import io.github.lolens.showcaser.util.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.texture.Sprite;
import net.minecraft.fluid.Fluid;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class ShareableFluidStack implements ShareableResource {

    // private final Identifier id = Identifier.of(Showcaser.MOD_ID, "fluid_stack");
    private final FluidStack fluidStack;

    public ShareableFluidStack(FluidStack fluidStack) {
        this.fluidStack = fluidStack;
    }

    @Override
    public Object get() {
        return fluidStack;
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


    public static List<Text> buildTooltip(Fluid fluid, long amount, boolean showAmount) {
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

    public static List<Text> buildTooltip(FluidStack fluidStack, boolean showAmount) {
        return buildTooltip(fluidStack.getFluid(), convertToMillibuckets(fluidStack.getAmount()), showAmount);
    }

    public Text buildFluidAmountText(boolean alwaysInMillibuckets) {
        return buildFluidAmountText(convertToMillibuckets(fluidStack.getAmount()), alwaysInMillibuckets);
    }

    // TODO add option to shrink count by thousands/millions etc
    // convert to mBs before using this method
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
            return amount / 81; // droplets in mb
        }
        return amount;
    }

    @Override
    public void render(DrawContext context, float x, float y, float scale) {
        if (fluidStack.isEmpty()) return;
        Sprite sprite = FluidStackHooks.getStillTexture(fluidStack);
        if (sprite == null) return;
        int color = FluidStackHooks.getColor(fluidStack);

        float[] rgb = RenderUtils.intToRGBNormalized(color);

        RenderSystem.setShaderColor(rgb[0], rgb[1], rgb[2], 1);

        context.getMatrices().push();
        context.getMatrices().scale(scale, scale, 1.0F);
        context.getMatrices().translate(x / scale, y / scale, 200.0F);
        context.getMatrices().scale(0.5f, 0.5f, 0.5f);

        // RenderSystem.setShaderTexture(0, sprite.getAtlasId());
        context.drawSprite(
                0,
                0,
                0,
                16,
                16,
                sprite
        );

        RenderSystem.setShaderColor(1f,1f,1f,1f);
        context.getMatrices().pop();

    }
}
