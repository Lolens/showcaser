package io.github.lolens.showcaser.forge;

import dev.architectury.platform.forge.EventBuses;
import io.github.lolens.showcaser.util.PlatformUtils;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import io.github.lolens.showcaser.Showcaser;
import org.spongepowered.asm.mixin.Mixins;

@Mod(Showcaser.MOD_ID)
public final class ShowcaserForge {
    public ShowcaserForge(FMLJavaModLoadingContext context) {
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(Showcaser.MOD_ID, context.getModEventBus());

        // Run our common setup.
        Showcaser.init();

        if (PlatformUtils.isEMILoaded()) {
            Mixins.addConfiguration("showcaser-emi.mixins.json");
        }
    }
}
