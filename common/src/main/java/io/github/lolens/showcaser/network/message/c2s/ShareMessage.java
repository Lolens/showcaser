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

package io.github.lolens.showcaser.network.message.c2s;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import io.github.lolens.showcaser.Showcaser;
import io.github.lolens.showcaser.api.sharecontext.ShareContext;
import io.github.lolens.showcaser.config.ConfigManager;
import io.github.lolens.showcaser.core.ShareContextImpl;
import io.github.lolens.showcaser.handler.ServerShareDispatcher;
import io.github.lolens.showcaser.network.Networking;
import net.minecraft.network.PacketByteBuf;

public class ShareMessage extends BaseC2SMessage {

    private final ShareContext shareContext;

    public ShareMessage(ShareContext shareContext) {
        this.shareContext = shareContext;

        Showcaser.LOGGER.debug("new C2S ShareMessage: {}", shareContext);
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
        Showcaser.LOGGER.debug("Server received ShareMessage: {}", shareContext);

        ServerShareDispatcher.dispatch(this.shareContext, context.getPlayer());
    }
}
