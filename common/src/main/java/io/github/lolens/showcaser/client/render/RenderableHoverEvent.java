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

package io.github.lolens.showcaser.client.render;

import io.github.lolens.showcaser.Showcaser;
import io.github.lolens.showcaser.api.resource.IconRenderer;
import io.github.lolens.showcaser.util.PlatformUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;

import static io.github.lolens.showcaser.util.ResourceUtils.INV_CHAR_STR;
import static io.github.lolens.showcaser.util.ResourceUtils.SPACE_STR;

public class RenderableHoverEvent extends HoverEvent {

    public static float currentAlpha = 1.0f;

    private final IconRenderer iconRenderer;
    private final float rawWidth;

    public RenderableHoverEvent(float width, IconRenderer iconRenderer, Text text) {
        super(Action.SHOW_TEXT, text);
        this.rawWidth = width;
        this.iconRenderer = iconRenderer;
    }

    public RenderableHoverEvent(float width, IconRenderer iconRenderer, ItemStack stack) {
        super(Action.SHOW_ITEM, new ItemStackContent(stack));
        this.rawWidth = width;
        this.iconRenderer = iconRenderer;
    }

    public int getSpacesToFill() {
        float spaceWidth = PlatformUtils.measureText(INV_CHAR_STR);
        Showcaser.LOGGER.error("space width: {}", spaceWidth);
        return (int) (rawWidth / spaceWidth);
    }

    public float getLeftoverWidth() {
        float spaceWidth = PlatformUtils.measureText(INV_CHAR_STR);
        int spaces = (int) (rawWidth / spaceWidth);
        return rawWidth - (spaceWidth * spaces);
    }

    public IconRenderer getRenderer() {
        return iconRenderer;
    }

    public float getRawWidth() {
        return this.rawWidth;
    }

}