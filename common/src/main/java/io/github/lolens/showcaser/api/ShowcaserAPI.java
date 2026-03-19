package io.github.lolens.showcaser.api;

import io.github.lolens.showcaser.api.builder.ChatMessageBuilderFactory;
import io.github.lolens.showcaser.api.builder.HandlerBuilderFactory;
import io.github.lolens.showcaser.api.sharecontext.ShareContextFactory;
import io.github.lolens.showcaser.client.impl.messagebuilder.ChatMessageBuilderFactoryImpl;
import io.github.lolens.showcaser.core.impl.ShareContextFactoryImpl;
import io.github.lolens.showcaser.core.impl.builder.HandlerBuilderFactoryImpl;

import java.util.Objects;

public class ShowcaserAPI {

    private static final String NOT_INITIALIZED_MESSAGE = "Showcaser API not initialized";

    private static ShareContextFactory contextFactory;
    private static HandlerBuilderFactory handlerBuilderFactory;
    private static ChatMessageBuilderFactory messageBuilderFactory;

    public static void init() {
        contextFactory = ShareContextFactoryImpl.INSTANCE;
        handlerBuilderFactory = HandlerBuilderFactoryImpl.INSTANCE;
        messageBuilderFactory = ChatMessageBuilderFactoryImpl.INSTANCE;
    }

    public static ShareContextFactory getContextFactory() {
        return Objects.requireNonNull(contextFactory, NOT_INITIALIZED_MESSAGE);
    }

    public static HandlerBuilderFactory getHandlerBuilderFactory() {
        return Objects.requireNonNull(handlerBuilderFactory, NOT_INITIALIZED_MESSAGE);
    }

    public static ChatMessageBuilderFactory getMessageBuilderFactory() {
        return Objects.requireNonNull(messageBuilderFactory, NOT_INITIALIZED_MESSAGE);
    }
}
