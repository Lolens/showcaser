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

package io.github.lolens.showcaser.fabric.network.message.s2c.conditional.ftbquests;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftbquests.client.ClientQuestFile;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.TeamData;
import io.github.lolens.showcaser.fabric.network.FabricNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class FtbQuestsOpenScreenMessage extends BaseS2CMessage {

    private final long id;

    public FtbQuestsOpenScreenMessage(PacketByteBuf buf) {
        this.id = buf.readLong();
    }

    public FtbQuestsOpenScreenMessage(long id) {
        this.id = id;
    }

    @Override
    public MessageType getType() {
        return FabricNetworking.Conditional.FTBQuests.MessageTypes.S2C.FTBQUESTS_OPEN_SCREEN;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeLong(this.id);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        Quest quest = ClientQuestFile.INSTANCE.getQuest(id);
        if (quest == null) return;

        if (!quest.isVisible(TeamData.get(context.getPlayer()))) {
            MinecraftClient.getInstance().player.sendMessage(
                    Text.translatable("showcaser.chat.share_message.quest.not_visible").formatted(Formatting.RED),
                    true
            );
            return;
        }

        ClientQuestFile.openGui(quest, true);
    }
}
