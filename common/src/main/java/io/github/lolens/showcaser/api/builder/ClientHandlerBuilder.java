package io.github.lolens.showcaser.api.builder;

import io.github.lolens.showcaser.api.handler.HandlerResult;
import io.github.lolens.showcaser.api.sharecontext.ShareContext;
import net.minecraft.client.gui.screen.Screen;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public interface ClientHandlerBuilder<T extends Screen> {

    ClientHandlerBuilder<T> forScreen(Class<T> screenClass);

    ClientHandlerBuilder<T> createContext(BiFunction<T, Consumer<ShareContext>, HandlerResult> contextCreator);

    ClientHandlerBuilder<T> display(BiConsumer<String, ShareContext> displayHandler);

    void register();

}
