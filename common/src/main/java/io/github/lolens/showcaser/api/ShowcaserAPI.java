package io.github.lolens.showcaser.api;

import io.github.lolens.showcaser.api.shareContext.ShareContextFactory;
import io.github.lolens.showcaser.core.ShareContextFactoryImpl;
import org.jetbrains.annotations.ApiStatus;

import java.util.Objects;

public class ShowcaserAPI {

    private static ShareContextFactory contextFactory;

    @ApiStatus.Internal
    public static void setContextFactory(ShareContextFactory factory) {
        contextFactory = factory;
    }

    public static void init() {
        contextFactory = ShareContextFactoryImpl.INSTANCE;
    }

    public static ShareContextFactory getContextFactory() {
        return Objects.requireNonNull(contextFactory, "Showcaser API not initialized yet");
    }

}
