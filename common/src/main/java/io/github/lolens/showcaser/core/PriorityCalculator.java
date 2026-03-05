package io.github.lolens.showcaser.core;

import io.github.lolens.showcaser.api.ShareHandler;

import java.util.Comparator;

public class PriorityCalculator {

    private static final int MAX_SEARCH_DEPTH = 10;
    private static final int PRIORITY_STEP = 100;
    private static final int EXACT_MATCH_PRIORITY = 0;

    private static final int OVERLAY_PRIORITY = Integer.MIN_VALUE;
    private static final int INCOMPATIBLE_PRIORITY = Integer.MAX_VALUE;

    public static int calculatePriority(ShareHandler handler, Class<?> targetClass) {
        Class<?> handlerClass = handler.getTargetClass();

        if (handlerClass == null) return OVERLAY_PRIORITY;

        if (!handlerClass.isAssignableFrom(targetClass)) return INCOMPATIBLE_PRIORITY;

        if (handlerClass.equals(targetClass)) return EXACT_MATCH_PRIORITY;

        int distance = getInheritanceDistance(handlerClass, targetClass);
        if (distance == Integer.MAX_VALUE) return INCOMPATIBLE_PRIORITY;

        return distance * PRIORITY_STEP;
    }

    private static int getInheritanceDistance(Class<?> ancestor, Class<?> descendant) {
        int distance = 0;
        Class<?> current = descendant;

        while (current != null && !current.equals(ancestor)) {
            current = current.getSuperclass();
            distance++;
        }

        return current != null ? distance : Integer.MAX_VALUE;
    }

    public static <T extends ShareHandler> Comparator<T> createComparator(Class<?> targetClass) {
        return (h1, h2) -> {
            int p1 = calculatePriority(h1, targetClass);
            int p2 = calculatePriority(h2, targetClass);

            if (p1 == p2) {
                return Integer.compare(h1.getPriority(), h2.getPriority());
            }

            return Integer.compare(p1, p2);
        };
    }
}