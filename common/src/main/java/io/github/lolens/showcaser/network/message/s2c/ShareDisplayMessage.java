package io.github.lolens.showcaser.network.message.s2c;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import io.github.lolens.showcaser.Showcaser;
import io.github.lolens.showcaser.model.ShareContext;
import io.github.lolens.showcaser.network.Networking;
import io.github.lolens.showcaser.registry.ClientHandlerRegistry;
import net.minecraft.network.PacketByteBuf;

public class ShareDisplayMessage extends BaseS2CMessage {

    ShareContext shareContext;

    public ShareDisplayMessage(PacketByteBuf buf) {
        shareContext = ShareContext.deserialize(buf);
    }

    public ShareDisplayMessage(ShareContext context) {
        this.shareContext = context;
        Showcaser.LOGGER.info("new S2C DisplayMessage: {}", shareContext);
    }

    @Override
    public MessageType getType() {
        return Networking.MessageTypes.S2C.SHARE_DISPLAY;
    }

    @Override
    public void write(PacketByteBuf buf) {
        shareContext.serialize(buf);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        Showcaser.LOGGER.info("Client received ShareDisplayMessage");
        ClientHandlerRegistry.getClientDisplayHandler(shareContext.getId()).display(context.getPlayer(), shareContext);
    }
}
