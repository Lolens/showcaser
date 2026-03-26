package io.github.lolens.showcaser.util.forge;

import icyllis.modernui.mc.ModernUIMod;
import icyllis.modernui.mc.text.ModernStringSplitter;
import icyllis.modernui.mc.text.TextLayoutEngine;
import net.minecraft.client.MinecraftClient;

import static io.github.lolens.showcaser.util.ResourceUtils.INV_CHAR_STR;

public class PlatformUtilsImpl {

    public static boolean modernui$isMUIModernTextEngineActive() {
        return ModernUIMod.isTextEngineEnabled();
    }

    public static float modernui$measureText(String text) {
        if (!ModernUIMod.isTextEngineEnabled()) {
            return MinecraftClient.getInstance().textRenderer.getWidth(INV_CHAR_STR);
        }
        ModernStringSplitter splitter = TextLayoutEngine.getInstance().getStringSplitter();
        return splitter.measureText(text);
    }

}
