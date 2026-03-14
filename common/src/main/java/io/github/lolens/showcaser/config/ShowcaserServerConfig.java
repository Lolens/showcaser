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
