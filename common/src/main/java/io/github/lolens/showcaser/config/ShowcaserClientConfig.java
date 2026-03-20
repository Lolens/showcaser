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

package io.github.lolens.showcaser.config;

import jdk.jfr.Description;

public class ShowcaserClientConfig {
    // descriptions actually not doing anything

    public static final int CURRENT_VERSION = 1;

    public int version = CURRENT_VERSION;

    @Description("Adds empty paragraph before '(un)verified by server' tooltip text.")
    public boolean addEmptySpaceBeforeVerifiedText = false;

    @Description("Items displayed in chat will use custom name that is set though anvil or any other way")
    public boolean ignoreCustomNames = false;

}
