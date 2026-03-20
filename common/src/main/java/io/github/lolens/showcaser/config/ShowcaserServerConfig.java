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

import io.github.lolens.showcaser.network.message.s2c.ConfigSyncMessage;
import jdk.jfr.Description;

import java.util.ArrayList;
import java.util.List;

public class ShowcaserServerConfig {

    ShowcaserServerConfig(ConfigSyncMessage message) {
        this.chatSharingCooldown = message.getSharingCooldown();
        this.hideVerifiedTooltipLine = message.isShouldHideVerificationMessage();
        this.blacklistedClassesExact = message.getBlacklistExact();
        this.blacklistedClassesWithInheritors = message.getBlacklistWithInheritors();
    }

    ShowcaserServerConfig() {

    }

    public static final int CURRENT_VERSION = 1;

    public int version = CURRENT_VERSION;

    @Description("Item sharing cooldown")
    public int chatSharingCooldown = 20;

    @Description(
            "Appends 'verified'/'unverified' message to the tooltip of item shared in chat" +
            "Some mods can reuse the logic that parent handled (by Showcaser) screen has, while changing it," +
            "so that message may not always be true"
    )
    public boolean hideVerifiedTooltipLine = false;

    @Description("Disables context creation on specified screen")
    public List<String> blacklistedClassesExact = new ArrayList<>();

    @Description("Disables context creation on specified screen and it's inheritors")
    public List<String> blacklistedClassesWithInheritors = new ArrayList<>();

}
