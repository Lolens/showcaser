package io.github.lolens.showcaser.registry;

import io.github.lolens.showcaser.Showcaser;
import io.github.lolens.showcaser.api.ClientShareHandler;
import io.github.lolens.showcaser.core.PriorityCalculator;
import net.minecraft.client.gui.screen.Screen;

import java.util.*;
import java.util.stream.Collectors;

public final class CachedPriorityRegistry {

    // Screen class / sorted client handlers by execution priority
    private static final Map<Class<?>, List<ClientShareHandler<? extends Screen>>> CLIENT_CACHE = new HashMap<>();

    // these are for classes added by handlers using blacklist(Class<?> clazz)
    private static final Set<Class<?>> EXACT_BLACKLIST = new HashSet<>();
    private static final Set<Class<?>> INHERIT_BLACKLIST = new HashSet<>();

    // these are for classes added by config using blacklist(String className)
    private static final Set<Class<?>> EXACT_BLACKLIST_BY_CONFIG = new HashSet<>();
    private static final Set<Class<?>> INHERIT_BLACKLIST_BY_CONFIG = new HashSet<>();

    private static boolean dirty = true;

    /**
     * excludes specified class from handling
     *
     * @param clazz class excluded from handling
     */
    public static void blacklistExact(Class<?> clazz) {
        EXACT_BLACKLIST.add(clazz);
        invalidate();
    }

    /**
     * excludes specified class and it's inheritors from handling
     *
     * @param clazz class excluded from handling
     */
    public static void blacklistWithInheritors(Class<?> clazz) {
        INHERIT_BLACKLIST.add(clazz);
        invalidate();
    }

    public static void blacklistExact(String className) {
        try {
            EXACT_BLACKLIST_BY_CONFIG.add(Class.forName(className));
            invalidate();
        } catch (ClassNotFoundException e) {
            Showcaser.LOGGER.error("Tried adding {} to exact class blacklist but class was not found.", className);
        }
    }

    public static void blacklistWithInheritors(String className) {
        try {
            INHERIT_BLACKLIST_BY_CONFIG.add(Class.forName(className));
            invalidate();
        } catch (ClassNotFoundException e) {
            Showcaser.LOGGER.error("Tried adding {} and its inheritors to blacklist but class was not found.", className);
        }
    }


    public static void whitelistExact(Class<?> clazz) {
        EXACT_BLACKLIST.remove(clazz);
        invalidate();
    }

    public static void whitelistInherit(Class<?> clazz) {
        INHERIT_BLACKLIST.remove(clazz);
        invalidate();
    }

    private static boolean isBlacklisted(Class<?> screenClass) {
        if (EXACT_BLACKLIST.contains(screenClass) || EXACT_BLACKLIST_BY_CONFIG.contains(screenClass)) {
            return true;
        }

        for (Class<?> blacklisted : INHERIT_BLACKLIST) {
            if (blacklisted.isAssignableFrom(screenClass)) {
                return true;
            }
        }

        for (Class<?> blacklisted : INHERIT_BLACKLIST_BY_CONFIG) {
            if (blacklisted.isAssignableFrom(screenClass)) {
                return true;
            }
        }

        return false;
    }

    public static void clearConfigBlacklistedClasses() {
        INHERIT_BLACKLIST_BY_CONFIG.clear();
        EXACT_BLACKLIST_BY_CONFIG.clear();
        invalidate();
    }

    public static List<ClientShareHandler<? extends Screen>> getClientHandlersFor(Screen screen) {
        return getClientHandlersFor(screen.getClass());
    }

    public static List<ClientShareHandler<? extends Screen>> getClientHandlersFor(Class<?> screenClass) {
        if (isBlacklisted(screenClass)) {
            // overlay handlers should work even on blacklisted screens
            return List.copyOf(ClientHandlerRegistry.getClientOverlayShareHandlers());
        }

        clearIfDirty();

        return CLIENT_CACHE.computeIfAbsent(screenClass, clazz -> {

            // eg. InventoryScreen -> showcaser:player_inventory(1000), showcaser:fallback(800) , ...overlays
            List<ClientShareHandler<? extends Screen>> handlers = new ArrayList<>(ClientHandlerRegistry.getClientOverlayShareHandlers());

            // add all handlers that can handle that screen class
            for (ClientShareHandler<? extends Screen> handler : ClientHandlerRegistry.getClientShareHandlers()) {
                Class<?> target = handler.getTargetClass();

                if (target == null || target.isAssignableFrom(clazz)) {
                    handlers.add(handler);
                }
            }

            // sort them by inheritance distance
            handlers.sort(PriorityCalculator.createComparator(clazz));

            return Collections.unmodifiableList(handlers);
        });
    }

    private static void clearIfDirty() {
        if (dirty) {
            CLIENT_CACHE.clear();
            dirty = false;
        }
    }

    public static void invalidate() {
        dirty = true;
    }

    public static void prewarmCache(Class<?>... classes) {
        for (Class<?> clazz : classes) {
            getClientHandlersFor(clazz);
        }

        printInfo(classes);
    }

    public static void prewarmCache() {
        Set<Class<?>> targets = new HashSet<>();

        ClientHandlerRegistry.getClientShareHandlers().forEach(handler -> {
            if (handler.getTargetClass() != null) {
                targets.add(handler.getTargetClass());
            }
        });

        prewarmCache(targets.toArray(new Class[0]));
    }

    public static void printInfo(Class<?>... classes) {
        StringBuilder sb = new StringBuilder();
        sb.append("Prewarmed cache for ").append(classes.length).append(" classes:\n");

        if (CLIENT_CACHE.isEmpty()) {
            sb.append("  Client cache is empty\n");
        } else {
            sb.append("  Client cache entries:\n");
            CLIENT_CACHE.forEach((clazz, handlers) -> {
                sb.append("    ").append(clazz.getSimpleName()).append(" -> ");

                String handlerInfo = handlers.stream()
                        .map(h -> {
                            int priority = PriorityCalculator.calculatePriority(h, clazz);
                            return h.getIdentifier() + "(" + priority + ")";
                        })
                        .collect(Collectors.joining(", "));

                sb.append(handlerInfo).append("\n");
            });
        }

        if (EXACT_BLACKLIST.isEmpty()) {
            sb.append("\nNo classes banned (by handlers exact)");
        } else {
            sb.append("\nClasses banned: (by handlers exact)\n");
            EXACT_BLACKLIST.forEach(clazz -> {
                sb.append("   ").append(clazz.getName()).append("\n");
            });
        }

        if (INHERIT_BLACKLIST.isEmpty()) {
            sb.append("\nNo classes banned (by handlers with inheritors)");
        } else {
            sb.append("\nClasses banned: (by handlers with inheritors)\n");
            INHERIT_BLACKLIST.forEach(clazz -> {
                sb.append("   ").append(clazz.getName()).append("\n");
            });
        }

        if (EXACT_BLACKLIST_BY_CONFIG.isEmpty()) {
            sb.append("\nNo classes banned (by config exact)");
        } else {
            sb.append("\nClasses banned: (by config exact)\n");
            EXACT_BLACKLIST_BY_CONFIG.forEach(clazz -> {
                sb.append("   ").append(clazz.getName()).append("\n");
            });
        }

        if (INHERIT_BLACKLIST_BY_CONFIG.isEmpty()) {
            sb.append("\nNo classes banned (by config with inheritors)");
        } else {
            sb.append("\nClasses banned: (by config with inheritors)\n");
            INHERIT_BLACKLIST_BY_CONFIG.forEach(clazz -> {
                sb.append("   ").append(clazz.getName()).append("\n");
            });
        }

        Showcaser.LOGGER.info(sb.toString());
    }

}