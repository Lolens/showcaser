package io.github.lolens.showcaser.api.handler;

import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// base handler for side-specific handlers
public interface ShareHandler {

    @NotNull
    Identifier getIdentifier();


    // null if works everywhere (for overlay mods)
    @Nullable
    Class<?> getTargetClass();

    //
    default int getPriority() {
        return 100;
    }

}
