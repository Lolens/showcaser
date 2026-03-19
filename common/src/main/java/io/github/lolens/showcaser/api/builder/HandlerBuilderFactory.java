package io.github.lolens.showcaser.api.builder;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;

public interface HandlerBuilderFactory {

    <T extends ScreenHandler> ServerHandlerBuilder<T> createServerBuilder(Identifier id);

    <T extends Screen> ClientHandlerBuilder<T> createClientBuilder(Identifier id);

}
