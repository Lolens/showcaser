package io.github.lolens.showcaser.api.builder;

import io.github.lolens.showcaser.api.sharecontext.ShareContext;
import io.github.lolens.showcaser.core.impl.builder.ServerHandlerBuilderImpl;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;

import java.util.function.BiFunction;

public interface ServerHandlerBuilder<T extends ScreenHandler> {

    ServerHandlerBuilderImpl<T> forContainer(Class<T> containerClass);

    ServerHandlerBuilderImpl<T> process(BiFunction<PlayerEntity, ShareContext, ShareContext> processor);

    void register();


}
