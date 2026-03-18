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
