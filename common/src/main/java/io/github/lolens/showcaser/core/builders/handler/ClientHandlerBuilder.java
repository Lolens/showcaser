package io.github.lolens.showcaser.core.builders.handler;

import io.github.lolens.showcaser.Showcaser;
import io.github.lolens.showcaser.api.ClientShareHandler;
import io.github.lolens.showcaser.api.DisplayHandler;
import io.github.lolens.showcaser.api.HandlerResult;
import io.github.lolens.showcaser.api.event.HandlerRegistrationEvent;
import io.github.lolens.showcaser.api.resource.IconRenderer;
import io.github.lolens.showcaser.model.ShareContext;
import io.github.lolens.showcaser.registry.ShareHandlerRegistrar;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;


public class ClientHandlerBuilder<T extends Screen> {
    private final Identifier id;
    private Class<T> screenClass;
    private BiFunction<T, Consumer<ShareContext>, HandlerResult> contextCreator;
    private BiConsumer<String, ShareContext> display;
    private final Map<String, Function<ShareContext, IconRenderer>> iconRenderers = new HashMap<>();

    private ClientHandlerBuilder(Identifier id) {
        this.id = id;
    }

    public static <T extends Screen> ClientHandlerBuilder<T> create(Identifier id) {
        return new ClientHandlerBuilder<>(id);
    }

    public ClientHandlerBuilder<T> forScreen(Class<T> screenClass) {
        this.screenClass = screenClass;
        return this;
    }

    public ClientHandlerBuilder<T> createContext(BiFunction<T, Consumer<ShareContext>, HandlerResult> contextCreator) {
        this.contextCreator = contextCreator;
        return this;
    }

    public ClientHandlerBuilder<T> display(BiConsumer<String, ShareContext> display) {
        this.display = display;
        return this;
    }

    public void register() {
        HandlerRegistrationEvent.EVENT.invoker().registerClient(build());
    }

    public ClientSummary<T> build() {
        return new ClientSummary<>(
                id,
                screenClass,
                contextCreator,
                display,
                new HashMap<>(iconRenderers)
        );
    }

    public record ClientSummary<T extends Screen>(
            Identifier id,
            @Nullable Class<T> screenClass,
            @Nullable BiFunction<T, Consumer<ShareContext>, HandlerResult> contextCreator,
            @Nullable BiConsumer<String, ShareContext> display,
            Map<String, Function<ShareContext, IconRenderer>> iconRenderers
    ) {

        public void registerHandlers() {
            if (contextCreator != null) {
                ShareHandlerRegistrar.register(new ClientShareHandler<T>() {
                    @Override
                    public @NotNull HandlerResult createContext(Screen screen, Consumer<ShareContext> contextConsumer) {
                        try {
                            return contextCreator.apply((T) screen, contextConsumer);
                        } catch (Exception e) {
                            Showcaser.LOGGER.error("Encountered error while creating ShareContext for screen {}", screen.getClass().getName(), e);
                        }
                        return HandlerResult.PASS;
                    }

                    @Override
                    public @NotNull Identifier getIdentifier() {
                        return id;
                    }

                    @Override
                    public @Nullable Class<T> getTargetClass() {
                        return screenClass;
                    }
                });
            }

            if (display != null) {
                ShareHandlerRegistrar.register(new DisplayHandler() {
                    @Override
                    public Identifier getIdentifier() {
                        return id;
                    }

                    @Override
                    public void display(String senderName, ShareContext context) {
                        try {
                            display.accept(senderName, context);
                        } catch (Exception e) {
                            Showcaser.LOGGER.error("Encountered error while displaying ShareContext from {}. Context Id: {}", senderName, context.getId(), e);
                        }
                    }
                });
            }
            
        }
    }
}