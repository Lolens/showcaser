package io.github.lolens.showcaser.api.adapter;

import net.minecraft.util.Identifier;

public abstract class BaseShareContextAdapter implements ShareContextAdapter {

    private final Identifier identifier;

    public BaseShareContextAdapter(Identifier identifier) {
        this.identifier = identifier;
    }

    @Override
    public final Identifier getHandlerId() {
        return identifier;
    }
}
