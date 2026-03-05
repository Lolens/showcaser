package io.github.lolens.showcaser.exception;

import io.github.lolens.showcaser.model.ShareContext;
import net.minecraft.entity.player.PlayerEntity;

public class ServerShareProcessingException extends RuntimeException {

    public static String SLOT_NOT_VALID = "Requested slot is not in handler slot bounds";

    public static String SYNC_ID_NOT_VALID = "Player requested share for not his current container";

    private ShareContext context;
    private PlayerEntity player;

    public ServerShareProcessingException(String message) {
        super(message);
    }

    public ServerShareProcessingException(ShareContext context, PlayerEntity player) {
        this.context = context;
        this.player = player;
    }


    public ServerShareProcessingException(ShareContext context, PlayerEntity player, String message) {
        super(message);
        this.context = context;
        this.player = player;
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    public ShareContext getContext() {
        return context;
    }

}
