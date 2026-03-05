package io.github.lolens.showcaser.api;

import io.github.lolens.showcaser.model.ShareContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;
import org.jetbrains.annotations.Nullable;


public interface ServerShareHandler<T extends ScreenHandler> extends ShareHandler {

    ShareContext handle(PlayerEntity player, ShareContext context);

    @Override
    @Nullable Class<T> getTargetClass();

}
