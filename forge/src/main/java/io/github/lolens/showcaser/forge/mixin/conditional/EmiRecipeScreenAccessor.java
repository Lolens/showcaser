package io.github.lolens.showcaser.forge.mixin.conditional;

import dev.emi.emi.api.widget.Widget;
import dev.emi.emi.screen.RecipeScreen;
import dev.emi.emi.screen.WidgetGroup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(value = RecipeScreen.class, remap = false)
public interface EmiRecipeScreenAccessor {

    @Accessor("hoveredWidget")
    Widget showcaser$getHoveredWidget();

    @Accessor("currentPage")
    List<WidgetGroup> showcaser$getCurrentPage();

}
