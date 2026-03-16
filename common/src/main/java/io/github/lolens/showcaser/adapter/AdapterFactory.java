package io.github.lolens.showcaser.adapter;

import dev.architectury.injectables.annotations.ExpectPlatform;
import io.github.lolens.showcaser.api.shareContext.ShareContext;
import io.github.lolens.showcaser.api.resource.ShareableResource;

public class AdapterFactory {

    // todo rewrite as adapter registry someday
    @ExpectPlatform
    public static ShareableResource fromContext(ShareContext context) {
        throw new AssertionError();
    }

}
