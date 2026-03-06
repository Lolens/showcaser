package io.github.lolens.showcaser.core.builders.handler;

import io.github.lolens.showcaser.Showcaser;
import io.github.lolens.showcaser.api.ServerShareHandler;
import io.github.lolens.showcaser.api.event.HandlerRegistrationEvent;
import io.github.lolens.showcaser.model.ShareContext;
import io.github.lolens.showcaser.registry.ShareHandlerRegistrar;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

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

    public record ServerSummary<T extends ScreenHandler>(
            Identifier id,
            Class<T> containerClass,
            BiFunction<PlayerEntity, ShareContext, ShareContext> processor
    ) {

        public void registerHandler() {
            ShareHandlerRegistrar.register(new ServerShareHandler<T>() {
                @Override
                public ShareContext handle(PlayerEntity player, ShareContext context) {
                    try {
                        return processor.apply(player, context);
                    } catch (RuntimeException e) {
                        Showcaser.LOGGER.error("Encountered error while processing ShareContext from {}. Context Id: {}", player.getName(), context.getId(), e);
                    }
                    return null;
                }

                @Override
                public @NotNull Identifier getIdentifier() {
                    return id;
                }

                @Override
                public Class<T> getTargetClass() {
                    return containerClass;
                }
            });
        }
    }
}