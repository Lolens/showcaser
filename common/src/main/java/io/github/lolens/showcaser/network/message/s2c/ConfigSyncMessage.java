package io.github.lolens.showcaser.network.message.s2c;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import io.github.lolens.showcaser.config.ConfigManager;
import io.github.lolens.showcaser.config.ShowcaserServerConfig;
import io.github.lolens.showcaser.network.Networking;
import net.minecraft.network.PacketByteBuf;

public class ConfigSyncMessage extends BaseS2CMessage {

    private int sharingCooldown = 20;

    public ConfigSyncMessage(ShowcaserServerConfig config) {
        this.sharingCooldown = config.chatSharingCooldown;
    }

    public ConfigSyncMessage(PacketByteBuf buf) {
        this.sharingCooldown = buf.readVarInt();
    }

    @Override
    public MessageType getType() {
        return Networking.MessageTypes.S2C.CONFIG_SYNC;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeVarInt(sharingCooldown);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        ConfigManager.sync(this);
    }

    public int getSharingCooldown() {
        return sharingCooldown;
    }
}
