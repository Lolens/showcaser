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

package io.github.lolens.showcaser.handler;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import io.github.lolens.showcaser.handler.conditional.*;
import io.github.lolens.showcaser.handler.vanilla.CreativeInventoryHandler;
import io.github.lolens.showcaser.handler.vanilla.FallbackHandler;
import io.github.lolens.showcaser.handler.vanilla.PlayerInventoryHandler;
import io.github.lolens.showcaser.client.ClientHandlerCache;
import io.github.lolens.showcaser.util.PlatformUtils;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;

public class Handlers {


    public static void registerAll() {

        registerVanilla();

        if (PlatformUtils.isAE2Loaded()) Ae2Handler.register();
        if (PlatformUtils.isREILoaded()) ReiHandler.register();
        if (PlatformUtils.isEMILoaded()) EmiHandler.register();
        if (PlatformUtils.isFTBQuestsLoaded()) FtbQuestsHandler.register();
        if (PlatformUtils.isJEILoaded()) JeiHandler.register();

        if (Platform.getEnvironment() == Env.CLIENT) {
            // prevents sharing constantly renamed output slot item
            ClientHandlerCache.blacklistExact(AnvilScreen.class);
        }

    }

    public static void registerVanilla() {
        FallbackHandler.register();
        PlayerInventoryHandler.register();
        CreativeInventoryHandler.register();
    }

}
