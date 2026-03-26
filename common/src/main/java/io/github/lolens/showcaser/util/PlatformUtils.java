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

package io.github.lolens.showcaser.util;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.platform.Platform;
import net.minecraft.client.MinecraftClient;

public final class PlatformUtils {

    private PlatformUtils() {
    }

    public static boolean isREILoaded() {
        return Platform.isModLoaded("roughlyenoughitems");
    }

    public static boolean isAE2Loaded() {
        return Platform.isModLoaded("ae2");
    }

    public static boolean isEMILoaded() {
        return Platform.isModLoaded("emi");
    }

    public static boolean isFTBQuestsLoaded() {
        return Platform.isModLoaded("ftbquests");
    }

    public static boolean isJEILoaded() {
        return Platform.isModLoaded("jei");
    }

    public static boolean isModernUILoaded() {
        return Platform.isModLoaded("modernui");
    }


    public static float measureText(String text) {
        if (isMUIModernTextEngineActive()) {
            return modernui$measureText(text);
        }
        return MinecraftClient.getInstance().textRenderer.getWidth(text);
    }

    public static boolean isMUIModernTextEngineActive() {
        return isModernUILoaded() ? modernui$isMUIModernTextEngineActive() : false;
    }

    @ExpectPlatform
    public static boolean modernui$isMUIModernTextEngineActive() {
        throw new AssertionError();
    }

    // fallbacks to vanilla if modern ui is present and modern text engine is disabled
    @ExpectPlatform
    public static float modernui$measureText(String text) {
        throw new AssertionError();
    }


}
