package io.github.lolens.showcaser.fabric.network.message.s2c.conditional.patchouli;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import io.github.lolens.showcaser.fabric.network.FabricNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import vazkii.patchouli.api.PatchouliAPI;

public class PatchouliOpenScreenMessage extends BaseS2CMessage {

    private Identifier book;
    private Identifier entry;
    private int page;
    private TargetType type;

    public enum TargetType {
        BOOK,
        ENTRY
    }


    public PatchouliOpenScreenMessage(Identifier book, Identifier entry, int page) {
        this.book = book;
        this.entry = entry;
        this.page = page;
        this.type = TargetType.ENTRY;
    }

    public PatchouliOpenScreenMessage(Identifier book) {
        this.book = book;
        this.type = TargetType.BOOK;
    }

    public PatchouliOpenScreenMessage(PacketByteBuf buf) {
        this.type = buf.readEnumConstant(TargetType.class);

        switch (type) {
            case BOOK -> {
                this.book = buf.readIdentifier();
            }
            case ENTRY -> {
                this.book = buf.readIdentifier();
                this.entry = buf.readIdentifier();
                this.page = buf.readVarInt();
            }
        }
    }

    @Override
    public MessageType getType() {
        return FabricNetworking.Conditional.Patchouli.MessageTypes.S2C.PATCHOULI_OPEN_SCREEN;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeEnumConstant(type);
        switch (type) {
            case BOOK -> {
                buf.writeIdentifier(book);
            }
            case ENTRY -> {
                buf.writeIdentifier(book);
                buf.writeIdentifier(entry);
                buf.writeVarInt(page);
            }
        }
    }

    @Override
    public void handle(NetworkManager.PacketContext packetContext) {
        switch (type) {
            case BOOK -> {
                PatchouliAPI.get().openBookGUI(book);
            }
            case ENTRY -> {
                PatchouliAPI.get().openBookEntry(book, entry, page);
            }
        }
    }
}
