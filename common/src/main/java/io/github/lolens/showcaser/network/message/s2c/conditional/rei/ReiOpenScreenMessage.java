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

package io.github.lolens.showcaser.network.message.s2c.conditional.rei;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import io.github.lolens.showcaser.network.Networking;
import me.shedaniel.rei.api.client.ClientHelper;
import me.shedaniel.rei.api.client.view.ViewSearchBuilder;
import me.shedaniel.rei.api.common.entry.EntryStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

public class ReiOpenScreenMessage extends BaseS2CMessage {

    private final NbtCompound entry;

    public ReiOpenScreenMessage(PacketByteBuf buf) {
        this.entry = buf.readNbt();
    }

    public ReiOpenScreenMessage(NbtCompound nbtCompound) {
        this.entry = nbtCompound;
    }

    @Override
    public MessageType getType() {
        return Networking.Conditional.REI.MessageTypes.S2C.REI_OPEN_SCREEN;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeNbt(this.entry);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        ViewSearchBuilder viewSearchBuilder = ViewSearchBuilder.builder();
        viewSearchBuilder.getRecipesFor().add(EntryStack.read(entry));

        ClientHelper.getInstance().openView(viewSearchBuilder);
    }
}
