package io.github.lolens.showcaser.core.impl;

import io.github.lolens.showcaser.api.sharecontext.ShareContext;
import io.github.lolens.showcaser.api.sharecontext.ShareContextFactory;
import io.github.lolens.showcaser.core.ShareContextImpl;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class ShareContextFactoryImpl implements ShareContextFactory {

    public static final ShareContextFactoryImpl INSTANCE = new ShareContextFactoryImpl();

    private ShareContextFactoryImpl() {}

    @Override
    public ShareContext create(Identifier id, int syncId, NbtCompound data) {
        return ShareContextImpl.of(id, syncId, data);
    }

    @Override
    public ShareContext create(Identifier id, int syncId) {
        return ShareContextImpl.of(id, syncId);
    }

    @Override
    public ShareContext create(Identifier id, NbtCompound data) {
        return ShareContextImpl.of(id, data);
    }

    @Override
    public ShareContext create(Identifier id) {
        return ShareContextImpl.of(id);
    }
}
