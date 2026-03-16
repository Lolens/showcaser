package io.github.lolens.showcaser.core.builder.handler;

import io.github.lolens.showcaser.api.handler.HandlerResult;
import io.github.lolens.showcaser.api.event.HandlerRegistrationEvent;
import io.github.lolens.showcaser.api.handler.summary.ClientSummary;
import io.github.lolens.showcaser.core.ShareContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;


public class ClientHandlerBuilder<T extends Screen> {
    private final Identifier id;
    private Class<T> screenClass;
    private BiFunction<T, Consumer<ShareContext>, HandlerResult> contextCreator;
    private BiConsumer<String, ShareContext> display;

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
                display
        );
    }


}