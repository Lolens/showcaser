package io.github.lolens.showcaser.core.builder.handler;

import io.github.lolens.showcaser.api.shareContext.ShareContext;
import io.github.lolens.showcaser.api.event.HandlerRegistrationEvent;
import io.github.lolens.showcaser.api.handler.summary.ServerSummary;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;

import java.util.function.BiFunction;

public class ServerHandlerBuilder<T extends ScreenHandler> {
    private final Identifier id;
    private Class<T> containerClass;
    private BiFunction<PlayerEntity, ShareContext, ShareContext> processor;

    private ServerHandlerBuilder(Identifier id) {
        this.id = id;
    }

    public static <T extends ScreenHandler> ServerHandlerBuilder<T> create(Identifier id) {
        return new ServerHandlerBuilder<>(id);
    }

    public ServerHandlerBuilder<T> forContainer(Class<T> containerClass) {
        this.containerClass = containerClass;
        return this;
    }

    public ServerHandlerBuilder<T> process(BiFunction<PlayerEntity, ShareContext, ShareContext> processor) {
        this.processor = processor;
        return this;
    }

    public void register() {
        HandlerRegistrationEvent.EVENT.invoker().registerServer(build());
    }

    public ServerSummary<T> build() {
        return new ServerSummary<>(id, containerClass, processor);
    }


}