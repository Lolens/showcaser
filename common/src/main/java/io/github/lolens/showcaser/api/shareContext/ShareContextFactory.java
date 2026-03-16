package io.github.lolens.showcaser.api.shareContext;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public interface ShareContextFactory {

    ShareContext create(Identifier id, int syncId, NbtCompound data);

    ShareContext create(Identifier id, int syncId);

    ShareContext create(Identifier id, NbtCompound data);

    ShareContext create(Identifier id);

}
