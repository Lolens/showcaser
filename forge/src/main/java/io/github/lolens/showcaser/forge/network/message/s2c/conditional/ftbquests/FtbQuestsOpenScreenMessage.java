package io.github.lolens.showcaser.forge.network.message.s2c.conditional.ftbquests;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftbquests.client.ClientQuestFile;
import dev.ftb.mods.ftbquests.quest.Quest;
import io.github.lolens.showcaser.forge.network.ForgeNetworking;
import net.minecraft.network.PacketByteBuf;

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
        return ForgeNetworking.Conditional.FTBQuests.MessageTypes.S2C.FTBQUESTS_OPEN_SCREEN;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeLong(this.id);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        Quest quest = ClientQuestFile.INSTANCE.getQuest(id);
        if (quest == null) return;

        ClientQuestFile.openGui(quest, true);
    }
}
