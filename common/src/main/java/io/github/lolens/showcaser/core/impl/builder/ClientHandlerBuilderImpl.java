package io.github.lolens.showcaser.core.impl.builder;

import io.github.lolens.showcaser.api.builder.ClientHandlerBuilder;
import io.github.lolens.showcaser.api.sharecontext.ShareContext;
import io.github.lolens.showcaser.api.event.HandlerRegistrationEvent;
import io.github.lolens.showcaser.api.handler.HandlerResult;
import io.github.lolens.showcaser.api.handler.summary.ClientSummary;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;


public class ClientHandlerBuilderImpl<T extends Screen> implements ClientHandlerBuilder<T> {
    private final Identifier id;
    private Class<T> screenClass;
    private BiFunction<T, Consumer<ShareContext>, HandlerResult> contextCreator;
    private BiConsumer<String, ShareContext> displayHandler;

    private ClientHandlerBuilderImpl(Identifier id) {
        this.id = id;
    }

    public static <T extends Screen> ClientHandlerBuilderImpl<T> create(Identifier id) {
        return new ClientHandlerBuilderImpl<>(id);
    }

    public ClientHandlerBuilderImpl<T> forScreen(Class<T> screenClass) {
        this.screenClass = screenClass;
        return this;
    }

    public ClientHandlerBuilderImpl<T> createContext(BiFunction<T, Consumer<ShareContext>, HandlerResult> contextCreator) {
        this.contextCreator = contextCreator;
        return this;
    }

    public ClientHandlerBuilderImpl<T> display(BiConsumer<String, ShareContext> displayHandler) {
        this.displayHandler = displayHandler;
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
                displayHandler
        );
    }


}