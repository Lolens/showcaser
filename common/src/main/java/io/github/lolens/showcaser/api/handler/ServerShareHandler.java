package io.github.lolens.showcaser.api.handler;

import io.github.lolens.showcaser.api.sharecontext.ShareContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;
import org.jetbrains.annotations.Nullable;


public interface ServerShareHandler<T extends ScreenHandler> extends ShareHandler {

    ShareContext handle(PlayerEntity player, ShareContext context);

    @Override
    @Nullable Class<T> getTargetClass();

}
