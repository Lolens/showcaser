package io.github.lolens.showcaser.mixin;

import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.item.ItemGroup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CreativeInventoryScreen.class)
public interface CreativeInventoryScreenMixin {

    @Accessor("selectedTab")
    ItemGroup showcaser$getSelectedTab();

    @Accessor("scrollPosition")
    float showcaser$getScrollPosition();

}
