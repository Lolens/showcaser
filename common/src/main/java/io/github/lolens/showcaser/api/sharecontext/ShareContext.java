package io.github.lolens.showcaser.api.sharecontext;

import com.google.gson.JsonElement;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.Identifier;

public interface ShareContext {

    Identifier getId();
    int getSyncId();
    boolean hasTrustedSyncId();

    String getString(String key);
    int getInt(String key);
    long getLong(String key);
    boolean has(String key);
    NbtCompound getCompound(String key);
    ItemStack getItemStack(String key);

    ShareContext withItemStack(ItemStack stack);
    ItemStack getItemStack();
    boolean hasItemStack();

    ShareContext withJsonElement(JsonElement jsonElement);
    JsonElement getJsonElement();
    boolean hasJsonElement();

    ShareContext withSlotIndex(int value);
    int getSlotIndex();
    boolean hasSlotIndex();

    ShareContext withAmount(long value);
    long getAmount();
    boolean hasAmount();

    ShareContext withIdentifier(Identifier identifier);
    Identifier getIdentifier();
    boolean hasIdentifier();

    ShareContext with(String key, String value);
    ShareContext with(String key, int value);
    ShareContext with(String key, long value);
    ShareContext with(String key, ItemStack stack);
    ShareContext with(String key, NbtElement compound);


}
