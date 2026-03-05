package io.github.lolens.showcaser.model;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import io.github.lolens.showcaser.Showcaser;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class ShareContext {

    protected final Identifier id;
    protected int syncId; // -1 if not trusted

    protected final NbtCompound data;

    protected ShareContext(Identifier id, int syncId, NbtCompound data) {
        this.id = id;
        this.syncId = syncId;
        this.data = data;
    }

    protected ShareContext(Identifier id, NbtCompound data) {
        this(id, -1, data);
    }

    public static ShareContext of(Identifier id, int syncId, NbtCompound data) {
        return new ShareContext(id, syncId, data);
    }
    public static ShareContext of(Identifier id, NbtCompound data) {
        return new ShareContext(id, -1, data);
    }

    public static ShareContext of(Identifier id, int syncId) {
        return new ShareContext(id, syncId, new NbtCompound());
    }

    public static ShareContext of(Identifier id) {
        return new ShareContext(id, -1, new NbtCompound());
    }

    public ShareContext with(String key, String value) {
        data.putString(key, value);
        return this;
    }

    public ShareContext with(String key, Long value) {
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

    public boolean hasValidSyncId() {
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
        return ItemStack.fromNbt(data.getCompound(key));
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
        return new ShareContext(id, syncId, data);
    }

    @Override
    public String toString() {
        return "ShareContext{" +
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

    public long getAmount() {
        return data.getLong("amount");
    }

    public ShareContext withAmount(long value) {
        data.putLong("amount", value);
        return this;
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
        Showcaser.LOGGER.info("With identifier {}", id.toString());
        data.putString("id-namespace", id.getNamespace());
        data.putString("id-path", id.getPath());
        return this;
    }

    public Identifier getIdentifier() {
        return Identifier.of(data.getString("id-namespace"), data.getString("id-path"));
    }

    public boolean hasIdentifier() {
        return !data.getString("id-namespace").isEmpty();
    }

    public ShareContext withJsonElement(JsonElement jsonElement) {
        NbtElement element = JsonOps.INSTANCE.convertTo(NbtOps.INSTANCE, jsonElement);
        return with("json", element);
    }

    public JsonElement getJsonElement() {
        return NbtOps.INSTANCE.convertTo(JsonOps.INSTANCE, data.get("json"));
    }

    public NbtElement getJsonAsNbt() {
        return data.get("json");
    }

    public ShareContext withType(String type) {
        data.putString("type", type);
        return this;
    }

    public String getType() {
        return data.getString("type");
    }

}
