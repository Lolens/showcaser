package io.github.lolens.showcaser.api.handler.summary;

import io.github.lolens.showcaser.api.sharecontext.ShareContext;
import io.github.lolens.showcaser.api.handler.HandlerResult;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public record ClientSummary<T extends Screen>(
        Identifier id,
        @Nullable Class<T> screenClass,
        @Nullable BiFunction<T, Consumer<ShareContext>, HandlerResult> contextCreator,
        @Nullable BiConsumer<String, ShareContext> display
) {}
