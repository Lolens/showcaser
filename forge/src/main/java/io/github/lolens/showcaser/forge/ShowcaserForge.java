/*
 * Showcaser - <https://github.com/Lolens/showcaser>
 * Copyright (C) 2026-present Lolens <https://github.com/Lolens>
 *
 * This file is part of Showcaser.
 *
 * Showcaser is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 3 of the License,
 * or (at your option) any later version.
 *
 * Showcaser is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along Showcaser.  If not, see <http://www.gnu.org/licenses/>.
 */

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
    }
}
