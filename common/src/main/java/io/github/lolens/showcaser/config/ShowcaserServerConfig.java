package io.github.lolens.showcaser.config;

import io.github.lolens.showcaser.network.message.s2c.ConfigSyncMessage;
import jdk.jfr.Description;

public class ShowcaserServerConfig {

    ShowcaserServerConfig(ConfigSyncMessage message) {
        this.chatSharingCooldown = message.getSharingCooldown();
    }

    ShowcaserServerConfig() {

    }

    @Description("Item sharing cooldown")
    public int chatSharingCooldown = 20;

    @Description(
            "Appends 'verified'/'unverified' message to the tooltip of item shared in chat" +
            "Some mods can reuse the logic that parent handled (by Showcaser) screen has, while changing it," +
            "so that message may not always be true"
    )
    public boolean hideVerifiedTooltipLine = false;


}
