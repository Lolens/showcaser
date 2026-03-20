package io.github.lolens.showcaser.network.message.s2c;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import io.github.lolens.showcaser.Showcaser;
import io.github.lolens.showcaser.config.ConfigManager;
import io.github.lolens.showcaser.config.ShowcaserServerConfig;
import io.github.lolens.showcaser.network.Networking;
import net.minecraft.network.PacketByteBuf;

import java.util.ArrayList;
import java.util.List;

public class ConfigSyncMessage extends BaseS2CMessage {

    private final int sharingCooldown;
    private final boolean shouldHideVerificationMessage;

    private final List<String> blacklistExact;
    private final List<String> blacklistWithInheritors;

    public ConfigSyncMessage(ShowcaserServerConfig config) {
        this.sharingCooldown = config.chatSharingCooldown;
        this.shouldHideVerificationMessage = config.hideVerifiedTooltipLine;

        this.blacklistExact = config.blacklistedClassesExact;
        this.blacklistWithInheritors = config.blacklistedClassesWithInheritors;
    }

    public ConfigSyncMessage(PacketByteBuf buf) {
        this.sharingCooldown = buf.readVarInt();
        this.shouldHideVerificationMessage = buf.readBoolean();
        this.blacklistExact = new ArrayList<>();
        this.blacklistWithInheritors = new ArrayList<>();

        int size1 = buf.readVarInt();
        for (int i = 0; i < size1; i++) {
            blacklistExact.add(buf.readString());
        }

        int size2 = buf.readVarInt();
        for (int i = 0; i < size2; i++) {
            blacklistWithInheritors.add(buf.readString());
        }

    }

    @Override
    public MessageType getType() {
        return Networking.MessageTypes.S2C.CONFIG_SYNC;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeVarInt(sharingCooldown);
        buf.writeBoolean(shouldHideVerificationMessage);

        int size1 = blacklistExact.size();
        buf.writeVarInt(size1);
        for (int i = 0; i < size1; i++) {
            buf.writeString(blacklistExact.get(i));
        }

        int size2 = blacklistWithInheritors.size();
        buf.writeVarInt(size2);
        for (int i = 0; i < size2; i++) {
            buf.writeString(blacklistWithInheritors.get(i));
        }

    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        ConfigManager.sync(this);
    }

    public int getSharingCooldown() {
        return sharingCooldown;
    }

    public boolean isShouldHideVerificationMessage() {
        return shouldHideVerificationMessage;
    }

    public List<String> getBlacklistExact() {
        return blacklistExact;
    }

    public List<String> getBlacklistWithInheritors() {
        return blacklistWithInheritors;
    }

}
