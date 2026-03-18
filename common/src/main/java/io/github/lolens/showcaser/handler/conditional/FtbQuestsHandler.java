package io.github.lolens.showcaser.handler.conditional;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class FtbQuestsHandler {
    @ExpectPlatform
    public static void register() {
        throw new AssertionError();
    }
}