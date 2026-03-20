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

package io.github.lolens.showcaser.api.resource;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.List;

public interface ShareableResource extends IconRenderer {

    Object get();

    boolean isEmpty();

    Text getDisplayName();

    long getAmount();

    List<Text> getTooltip();

    // content is the "x2 stone" or "1 B Water" text
    Text getContent(boolean showAmount, boolean ignoreCustomName, Text forcedName);

    void render(DrawContext context, float x, float y, float scale, float alpha);
}
