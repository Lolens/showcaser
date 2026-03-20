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

package io.github.lolens.showcaser.network.message.s2c;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import io.github.lolens.showcaser.Showcaser;
import io.github.lolens.showcaser.api.sharecontext.ShareContext;
import io.github.lolens.showcaser.config.ConfigManager;
import io.github.lolens.showcaser.core.ShareContextImpl;
import io.github.lolens.showcaser.network.Networking;
import io.github.lolens.showcaser.registry.ClientHandlerRegistry;
import net.minecraft.network.PacketByteBuf;

public class ShareDisplayMessage extends BaseS2CMessage {

    String senderName;
    ShareContext shareContext;

    public ShareDisplayMessage(PacketByteBuf buf) {
        this.shareContext = ShareContextImpl.deserialize(buf);
        this.senderName = buf.readString();
    }

    public ShareDisplayMessage(ShareContext context, String senderName) {
        this.shareContext = context;
        this.senderName = senderName;

        Showcaser.LOGGER.debug("new S2C DisplayMessage: {}", shareContext);
    }

    @Override
    public MessageType getType() {
        return Networking.MessageTypes.S2C.SHARE_DISPLAY;
    }

    @Override
    public void write(PacketByteBuf buf) {
        ((ShareContextImpl) shareContext).serialize(buf);
        buf.writeString(senderName);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        Showcaser.LOGGER.debug("Client received ShareDisplayMessage");

        ClientHandlerRegistry.getClientDisplayHandler(shareContext.getId()).display(senderName, shareContext);
    }
}
