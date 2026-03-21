package io.github.lolens.showcaser.handler.conditional;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class JeiHandler {

    @ExpectPlatform
    public static void register() {
        throw new AssertionError();
    }

}
