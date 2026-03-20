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

package io.github.lolens.showcaser.network.message.s2c;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import io.github.lolens.showcaser.config.ConfigManager;
import io.github.lolens.showcaser.config.ShowcaserServerConfig;
import io.github.lolens.showcaser.network.Networking;
import net.minecraft.network.PacketByteBuf;

import java.util.ArrayList;
import java.util.List;

public class ConfigSyncMessage extends BaseS2CMessage {

    private final int sharingCooldown;
    private final boolean shouldHideVerificationMessage;

    private final List<String> blacklistExact;
    private final List<String> blacklistWithInheritors;

    public ConfigSyncMessage(ShowcaserServerConfig config) {
        this.sharingCooldown = config.chatSharingCooldown;
        this.shouldHideVerificationMessage = config.hideVerifiedTooltipLine;

        this.blacklistExact = config.blacklistedClassesExact;
        this.blacklistWithInheritors = config.blacklistedClassesWithInheritors;
    }

    public ConfigSyncMessage(PacketByteBuf buf) {
        this.sharingCooldown = buf.readVarInt();
        this.shouldHideVerificationMessage = buf.readBoolean();
        this.blacklistExact = new ArrayList<>();
        this.blacklistWithInheritors = new ArrayList<>();

        int size1 = buf.readVarInt();
        for (int i = 0; i < size1; i++) {
            blacklistExact.add(buf.readString());
        }

        int size2 = buf.readVarInt();
        for (int i = 0; i < size2; i++) {
            blacklistWithInheritors.add(buf.readString());
        }

    }

    @Override
    public MessageType getType() {
        return Networking.MessageTypes.S2C.CONFIG_SYNC;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeVarInt(sharingCooldown);
        buf.writeBoolean(shouldHideVerificationMessage);

        int size1 = blacklistExact.size();
        buf.writeVarInt(size1);
        for (int i = 0; i < size1; i++) {
            buf.writeString(blacklistExact.get(i));
        }

        int size2 = blacklistWithInheritors.size();
        buf.writeVarInt(size2);
        for (int i = 0; i < size2; i++) {
            buf.writeString(blacklistWithInheritors.get(i));
        }

    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        ConfigManager.applySyncMessage(this);
    }

    public int getSharingCooldown() {
        return sharingCooldown;
    }

    public boolean isShouldHideVerificationMessage() {
        return shouldHideVerificationMessage;
    }

    public List<String> getBlacklistExact() {
        return blacklistExact;
    }

    public List<String> getBlacklistWithInheritors() {
        return blacklistWithInheritors;
    }

}
