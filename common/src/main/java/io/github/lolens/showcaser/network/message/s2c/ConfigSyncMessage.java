package io.github.lolens.showcaser.network.message.s2c;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import io.github.lolens.showcaser.config.ConfigManager;
import io.github.lolens.showcaser.config.ShowcaserServerConfig;
import io.github.lolens.showcaser.network.Networking;
import net.minecraft.network.PacketByteBuf;

public class ConfigSyncMessage extends BaseS2CMessage {

    private final int sharingCooldown;
    private final boolean shouldHideVerificationMessage;

    public ConfigSyncMessage(ShowcaserServerConfig config) {
        this.sharingCooldown = config.chatSharingCooldown;
        this.shouldHideVerificationMessage = config.hideVerifiedTooltipLine;
    }

    public ConfigSyncMessage(PacketByteBuf buf) {
        this.sharingCooldown = buf.readVarInt();
        this.shouldHideVerificationMessage = buf.readBoolean();
    }

    @Override
    public MessageType getType() {
        return Networking.MessageTypes.S2C.CONFIG_SYNC;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeVarInt(sharingCooldown);
        buf.writeBoolean(shouldHideVerificationMessage);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        ConfigManager.sync(this);
    }

    public int getSharingCooldown() {
        return sharingCooldown;
    }
}
