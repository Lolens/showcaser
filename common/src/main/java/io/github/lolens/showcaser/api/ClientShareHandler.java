package io.github.lolens.showcaser.api;

import io.github.lolens.showcaser.model.ShareContext;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface ClientShareHandler<T extends Screen> extends ShareHandler {

    @NotNull
    HandlerResult createContext(Screen screen, Consumer<ShareContext> contextConsumer);

    @Override
    @Nullable Class<T> getTargetClass();


}
