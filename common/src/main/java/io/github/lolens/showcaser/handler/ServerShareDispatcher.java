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

package io.github.lolens.showcaser.handler;


import dev.architectury.utils.GameInstance;
import io.github.lolens.showcaser.Showcaser;
import io.github.lolens.showcaser.api.sharecontext.ShareContext;
import io.github.lolens.showcaser.config.ConfigManager;
import io.github.lolens.showcaser.network.message.s2c.ShareDisplayMessage;
import io.github.lolens.showcaser.registry.ServerHandlerRegistry;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.UUID;

import static io.github.lolens.showcaser.Showcaser.LOGGER;

public class ServerShareDispatcher {

    private static final Object2LongOpenHashMap<UUID> lastSendTimes = new Object2LongOpenHashMap<>();

    public static void dispatch(ShareContext context, PlayerEntity player) {

        if (checkBanned(player)) return;
        if (checkCooldown(player)) {
            LOGGER.debug("Player {} tried sending share while on cooldown", player.getName().getString());
            return;
        }

        ShareContext out;
        if (context.hasTrustedSyncId()) {
            out = dispatchTrusted(context, player);
        } else {
            out = dispatchUntrusted(context, player);
        }

        if (out != null) {
            new ShareDisplayMessage(out, player.getDisplayName().getString()).sendToAll(GameInstance.getServer());
        } else {
            // can happen when screens override vanilla slot logic and client thinks that slot is not empty and sends this slot id to the server
            // but server cant get slot by handler.getSlot(slotIndex) or gets wrong slot
            //
            // Latency is also a reason why it can happen. Even in singleplayer if item is taken from ME network
            // simultaneously with sending a packet in will result in Items.AIR or null
            LOGGER.warn("Player {} requested dispatch for context with id {} but it resulted in null", player.getName(), context.getId());
        }
    }

    private static ShareContext dispatchUntrusted(ShareContext context, PlayerEntity player) {
        var handler = ServerHandlerRegistry.getServerHandler(context.getId());
        if (handler != null) {
            return handler.handle(player, context);
        }

        LOGGER.error("No container handler found for ID: {} in container: {}",
                context.getId(), player.currentScreenHandler.getClass().getSimpleName());
        return null;
    }

    private static ShareContext dispatchTrusted(ShareContext context, PlayerEntity player) {
        if (player.currentScreenHandler == null) {
            LOGGER.warn("Player {} requested sharing for context with id {} but dont have screen opened",
                    player.getName(), context.getId());
            return null;
        }

        if (player.currentScreenHandler.syncId != context.getSyncId()) {
            LOGGER.warn("Sync id mismatch for player {}. expected: {}, actual: {}",
                    player.getName(), context.getSyncId(), player.currentScreenHandler.syncId);
            return null;
        }

        var handler = ServerHandlerRegistry.getServerHandler(context.getId());
        if (handler != null) {
            LOGGER.info("Handler found! Id: {}", handler.getIdentifier());
            return handler.handle(player, context);
        }

        LOGGER.error("No container handler is found to handle {} request for ID: {} in container: {}",
                player.getName(), context.getId(), player.currentScreenHandler.getClass().getSimpleName());
        return null;
    }

    private static boolean checkBanned(PlayerEntity player) {
        if (ConfigManager.getStorage().shareBannedPlayer.containsKey(player.getUuid())) {
            player.sendMessage(Text.translatable("showcaser.chat.banned").formatted(Formatting.RED));
            return true; // true -> return from dispatch
        }
        return false;
    }

    private static boolean checkCooldown(PlayerEntity player) {
        // caching cooldowns as static field and updating via ConfigUpdateEvent is overhead probably...
        int cooldown = ConfigManager.getServerConfig().chatSharingCooldown;

        // dispatch should only be called only after server is loaded
        long time = GameInstance.getServer().getOverworld().getTime();

        UUID uuid = player.getUuid();

        long lastTime = lastSendTimes.getLong(uuid);
        long sinceSend = time - lastTime;

        if (sinceSend <= cooldown) {
            return true;
        }

        lastSendTimes.put(uuid, time);
        return false;
    }

}