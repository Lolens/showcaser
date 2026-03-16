package io.github.lolens.showcaser.client;

import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import io.github.lolens.showcaser.Showcaser;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class KeyMappings {

    // maybe should be rewritten to make use of forge key binding modifier and use Screen.isShiftDown() on fabric
    public static final KeyBinding SHARE_ITEM_IN_CHAT = new KeyBinding(
            "key.showcaser.share_item_in_chat",
            InputUtil.Type.KEYSYM,
            InputUtil.GLFW_KEY_X,
            "key.showcaser.category"
    );

    public static void register() {
        KeyMappingRegistry.register(SHARE_ITEM_IN_CHAT);
    }

}
