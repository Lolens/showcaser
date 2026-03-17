package io.github.lolens.showcaser.util;

import io.github.lolens.showcaser.api.handler.ClientShareHandler;
import io.github.lolens.showcaser.api.handler.ServerShareHandler;
import io.github.lolens.showcaser.core.PriorityCalculator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;

import java.util.Comparator;

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
