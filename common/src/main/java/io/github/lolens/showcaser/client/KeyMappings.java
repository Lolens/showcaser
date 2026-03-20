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

package io.github.lolens.showcaser.client;

import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import io.github.lolens.showcaser.Showcaser;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class KeyMappings {

    // maybe should be rewritten to make use of forge key binding modifier and use Screen.isShiftDown() on fabric
    public static final KeyBinding SHARE_ITEM_IN_CHAT = new KeyBinding(
            "key.showcaser.share_item_in_chat",
            InputUtil.Type.KEYSYM,
            InputUtil.GLFW_KEY_X,
            "key.showcaser.category"
    );

    public static void register() {
        KeyMappingRegistry.register(SHARE_ITEM_IN_CHAT);
    }

}
