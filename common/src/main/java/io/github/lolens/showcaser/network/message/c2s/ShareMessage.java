package io.github.lolens.showcaser.network.message.c2s;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import io.github.lolens.showcaser.Showcaser;
import io.github.lolens.showcaser.api.shareContext.ShareContext;
import io.github.lolens.showcaser.core.ShareContextImpl;
import io.github.lolens.showcaser.handler.ServerShareDispatcher;
import io.github.lolens.showcaser.network.Networking;
import net.minecraft.network.PacketByteBuf;

public class ShareMessage extends BaseC2SMessage {

    private final ShareContext shareContext;

    public ShareMessage(ShareContext shareContext) {
        this.shareContext = shareContext;
        Showcaser.LOGGER.info("new C2S ShareMessage: {}", shareContext);
    }

    public ShareMessage(PacketByteBuf buf) { // deserializer
        shareContext = ShareContextImpl.deserialize(buf);
    }

    @Override
    public MessageType getType() {
        return Networking.MessageTypes.C2S.SHARE_RESOURCE;
    }

    @Override
    public void write(PacketByteBuf buf) { // serializer
        ((ShareContextImpl) shareContext).serialize(buf);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        ServerShareDispatcher.dispatch(this.shareContext, context.getPlayer());
    }
}
