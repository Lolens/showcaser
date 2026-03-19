package io.github.lolens.showcaser.core.impl.builder;

import io.github.lolens.showcaser.api.builder.ClientHandlerBuilder;
import io.github.lolens.showcaser.api.builder.HandlerBuilderFactory;
import io.github.lolens.showcaser.api.builder.ServerHandlerBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;

public class HandlerBuilderFactoryImpl implements HandlerBuilderFactory {

    public static final HandlerBuilderFactory INSTANCE = new HandlerBuilderFactoryImpl();

    @Override
    public <T extends ScreenHandler> ServerHandlerBuilder<T> createServerBuilder(Identifier id) {
        return ServerHandlerBuilderImpl.create(id);
    }

    @Override
    public <T extends Screen> ClientHandlerBuilder<T> createClientBuilder(Identifier id) {
        return ClientHandlerBuilderImpl.create(id);
    }
}
