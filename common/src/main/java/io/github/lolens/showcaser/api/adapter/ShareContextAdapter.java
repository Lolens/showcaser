package io.github.lolens.showcaser.api.adapter;

import io.github.lolens.showcaser.api.resource.ShareableResource;
import io.github.lolens.showcaser.api.sharecontext.ShareContext;
import net.minecraft.util.Identifier;

public interface ShareContextAdapter {

    Identifier getHandlerId();

    ShareableResource adapt(ShareContext context);

    default int getPriority() {
        return 100;
    }

}
