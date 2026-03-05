package io.github.lolens.showcaser.client.render.icon;

import io.github.lolens.showcaser.model.ShareContext;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class IconRendererRegistry {

    private static final Map<String, IconRendererFactory> ICON_RENDERER_FACTORIES = new HashMap<>();

    // icon renderer implementations are almost always stateful
    @FunctionalInterface
    public interface IconRendererFactory {
        IconRenderer create(ShareContext context);
    }

    private static void registerRendererFactory(String namespace, String path, String type, IconRendererFactory factory) {
        String key = namespace + ":" + path + ":" + type;

        if (ICON_RENDERER_FACTORIES.containsKey(key))
            throw new IllegalStateException("Tried registering icon renderer factory with id that is already registered");

        ICON_RENDERER_FACTORIES.put(key, factory);
    }

    public static void registerRendererFactory(Identifier id, String type, IconRendererFactory factory) {
        registerRendererFactory(id.getNamespace(), id.getPath(), type, factory);
    }

    public static IconRendererFactory getRendererFactory(Identifier id, String type) {
        String key = id.toString() + ":" + type;
        IconRendererFactory factory = ICON_RENDERER_FACTORIES.get(key);

        if (factory == null)
            throw new NullPointerException("Tried getting icon renderer factory which was not registered. Key: " + key);

        return factory;
    }

}
