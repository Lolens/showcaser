package io.github.lolens.showcaser.util;

import io.github.lolens.showcaser.Showcaser;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;

public final class HandlerUtils {

    private HandlerUtils() {
    }

    public static ScreenHandler getHandler(PlayerEntity player) {
        return player.currentScreenHandler;
    }

    public static boolean isValidSyncId(PlayerEntity player, int syncId) {
        return player.currentScreenHandler.syncId == syncId;
    }

    public static boolean isValidSlot(int index, ScreenHandler handler) {
        return (handler.slots.size() > index && index >= 0);
    }

}
