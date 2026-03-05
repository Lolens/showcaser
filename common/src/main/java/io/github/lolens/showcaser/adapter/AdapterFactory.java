package io.github.lolens.showcaser.adapter;

import dev.architectury.injectables.annotations.ExpectPlatform;
import io.github.lolens.showcaser.api.resource.ShareableResource;
import io.github.lolens.showcaser.model.ShareContext;

public class AdapterFactory {

    @ExpectPlatform
    public static ShareableResource fromContext(ShareContext context) {
        throw new AssertionError();
    }

}
