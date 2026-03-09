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


}
