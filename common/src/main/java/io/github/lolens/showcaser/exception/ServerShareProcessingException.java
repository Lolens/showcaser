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

package io.github.lolens.showcaser.exception;

import io.github.lolens.showcaser.api.sharecontext.ShareContext;
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
