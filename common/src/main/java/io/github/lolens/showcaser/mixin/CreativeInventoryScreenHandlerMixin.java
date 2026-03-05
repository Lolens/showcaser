package io.github.lolens.showcaser.mixin;

import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CreativeInventoryScreen.CreativeScreenHandler.class)
public interface CreativeInventoryScreenHandlerMixin {

    @Invoker("getRow")
    int showcaser$getRow(float x);

}
