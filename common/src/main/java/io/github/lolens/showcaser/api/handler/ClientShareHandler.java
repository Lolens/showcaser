package io.github.lolens.showcaser.api.handler;

import io.github.lolens.showcaser.api.shareContext.ShareContext;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@ApiStatus.OverrideOnly
public interface ClientShareHandler<T extends Screen> extends ShareHandler {

    @NotNull
    HandlerResult createContext(Screen screen, Consumer<ShareContext> contextConsumer);

    @Override
    @Nullable Class<T> getTargetClass();


}
