package io.github.lolens.showcaser.client;

import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import io.github.lolens.showcaser.Showcaser;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class KeyMappings {

    public static final KeyBinding SHARE_ITEM_IN_CHAT = new KeyBinding(
            String.format("key.%s.share_item_in_chat", Showcaser.MOD_ID),
            InputUtil.Type.KEYSYM,
            InputUtil.GLFW_KEY_X,
            String.format("key.categories.%s", Showcaser.MOD_ID)
    );

    public static void register() {
        KeyMappingRegistry.register(SHARE_ITEM_IN_CHAT);
    }

}
