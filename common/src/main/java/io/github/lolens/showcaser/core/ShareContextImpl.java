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

package io.github.lolens.showcaser.core;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import io.github.lolens.showcaser.api.sharecontext.ShareContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class ShareContextImpl implements ShareContext {

    protected final Identifier id;
    protected int syncId; // -1 if not trusted

    protected final NbtCompound data;

    protected ShareContextImpl(Identifier id, int syncId, NbtCompound data) {
        this.id = id;
        this.syncId = syncId;
        this.data = data;
    }

    public static ShareContext of(Identifier id, int syncId, NbtCompound data) {
        return new ShareContextImpl(id, syncId, data);
    }

    public static ShareContext of(Identifier id, NbtCompound data) {
        return new ShareContextImpl(id, -1, data);
    }

    public static ShareContext of(Identifier id, int syncId) {
        return new ShareContextImpl(id, syncId, new NbtCompound());
    }

    public static ShareContext of(Identifier id) {
        return new ShareContextImpl(id, -1, new NbtCompound());
    }

    public ShareContext with(String key, String value) {
        data.putString(key, value);
        return this;
    }

    public ShareContext with(String key, long value) {
        data.putLong(key, value);
        return this;
    }

    public ShareContext with(String key, int value) {
        data.putInt(key, value);
        return this;
    }

    public ShareContext with(String key, ItemStack stack) {
        NbtCompound stackNbt = new NbtCompound();
        stack.writeNbt(stackNbt);
        // overwrite byte value with int count to work with inventories that have higher max stack count
        stackNbt.putInt("Count", stack.getCount());
        data.put(key, stackNbt);
        return this;
    }

    public ShareContext with(String key, NbtElement compound) {
        data.put(key, compound);
        return this;
    }

    public Identifier getId() {
        return id;
    }

    public boolean hasTrustedSyncId() {
        return syncId != -1;
    }

    public int getSyncId() {
        return syncId;
    }

    public String getString(String key) {
        return data.getString(key);
    }

    public int getInt(String key) {
        return data.getInt(key);
    }

    public long getLong(String key) {
        return data.getLong(key);
    }

    public NbtCompound getCompound(String key) {
        return data.getCompound(key);
    }

    public ItemStack getItemStack(String key) {
        NbtCompound stackNbt = data.getCompound(key);
        ItemStack stack = ItemStack.fromNbt(stackNbt);
        int actualCount = stackNbt.getInt("Count");
        stack.setCount(actualCount);
        return stack;
    }

    public boolean has(String key) {
        return data.contains(key);
    }

    public void serialize(PacketByteBuf buf) {
        buf.writeInt(syncId);
        buf.writeIdentifier(id);
        buf.writeNbt(data);
    }

    public static ShareContext deserialize(PacketByteBuf buf) {
        int syncId = buf.readInt();
        Identifier id = buf.readIdentifier();
        NbtCompound data = buf.readNbt();
        return new ShareContextImpl(id, syncId, data);
    }

    @Override
    public String toString() {
        return "ShareContextImpl{" +
                "id=" + id +
                ", syncId=" + syncId +
                ", data=" + data +
                '}';
    }

    /// things

    public int getSlotIndex() {
        return data.getInt("slot");
    }

    public ShareContext withSlotIndex(int value) {
        data.putInt("slot", value);
        return this;
    }

    public boolean hasSlotIndex() {
        return has("slot");
    }

    public long getAmount() {
        return data.getLong("amount");
    }

    public ShareContext withAmount(long value) {
        data.putLong("amount", value);
        return this;
    }

    public boolean hasAmount() {
        return has("amount");
    }

    public ShareContext withItemStack(ItemStack stack) {
        return with("stack", stack);
    }

    public ItemStack getItemStack() {
        return getItemStack("stack");
    }

    public boolean hasItemStack() {
        return has("stack");
    }

    public ShareContext withIdentifier(Identifier id) {
        data.putString("id-namespace", id.getNamespace());
        data.putString("id-path", id.getPath());
        return this;
    }

    public ShareContext withIdentifier(String key, Identifier id) {
        data.putString(String.format("id-%s-namespace", key), id.getNamespace());
        data.putString(String.format("id-%s-path", key), id.getPath());
        return this;
    }

    public Identifier getIdentifier() {
        return Identifier.of(data.getString("id-namespace"), data.getString("id-path"));
    }

    public Identifier getIdentifier(String key) {
        return Identifier.of(
                data.getString(String.format("id-%s-namespace", key)),
                data.getString(String.format("id-%s-path", key))
        );
    }

    public boolean hasIdentifier() {
        return !data.getString("id-namespace").isEmpty();
    }

    public boolean hasIdentifier(String key) {
        return !data.getString(String.format("id-%s-namespace", key)).isEmpty();
    }

    public ShareContext withJsonElement(JsonElement jsonElement) {
        NbtElement element = JsonOps.INSTANCE.convertTo(NbtOps.INSTANCE, jsonElement);
        return with("json", element);
    }

    public boolean hasJsonElement() {
        return has("json");
    }

    public JsonElement getJsonElement() {
        return NbtOps.INSTANCE.convertTo(JsonOps.INSTANCE, data.get("json"));
    }

}
