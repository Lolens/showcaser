package io.github.lolens.showcaser.api.handler.summary;

import io.github.lolens.showcaser.api.shareContext.ShareContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;

import java.util.function.BiFunction;

public record ServerSummary<T extends ScreenHandler>(
        Identifier id,
        Class<T> containerClass,
        BiFunction<PlayerEntity, ShareContext, ShareContext> processor
) {}
