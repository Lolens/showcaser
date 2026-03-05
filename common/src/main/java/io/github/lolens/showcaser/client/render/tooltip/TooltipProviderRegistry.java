package io.github.lolens.showcaser.client.render.tooltip;

import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class TooltipProviderRegistry {

    // basically fancy switch (type) {}
    private static final Map<String, TooltipProvider> TOOLTIP_RENDERERS = new HashMap<>();

    private static void registerProvider(String namespace, String path, String type, TooltipProvider factory) {
        String key = namespace + ":" + path + ":" + type;

        if (TOOLTIP_RENDERERS.containsKey(key))
            throw new IllegalStateException("Tried registering tooltip renderer factory with id that is already registered");

        TOOLTIP_RENDERERS.put(key, factory);
    }

    public static void registerProvider(Identifier id, String type, TooltipProvider factory) {
        registerProvider(id.getNamespace(), id.getPath(), type, factory);
    }

    public static TooltipProvider getRenderer(Identifier id, String type) {
        String key = id.toString() + ":" + type;
        TooltipProvider factory = TOOLTIP_RENDERERS.get(key);

        if (factory == null)
            throw new NullPointerException("Tried getting tooltip renderer factory which was not registered. Key: " + key);

        return factory;
    }

}
