/*
 * Showcaser - <https://github.com/Lolens/showcaser>
 * Copyright (C) 2026-present Lolens <https://github.com/Lolens>
 *
 * This file is part of Showcaser.
 *
 * Showcaser is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 3 of the License,
 * or (at your option) any later version.
 *
 * Showcaser is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along Showcaser.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.lolens.showcaser.core;

import io.github.lolens.showcaser.Showcaser;
import io.github.lolens.showcaser.api.handler.ClientShareHandler;
import io.github.lolens.showcaser.api.handler.ShareHandler;

import java.util.Comparator;

public class PriorityCalculator {

    private static final int MAX_SEARCH_DEPTH = 10;
    private static final int PRIORITY_STEP = 100;
    private static final int EXACT_MATCH_PRIORITY = 0;

    private static final int OVERLAY_PRIORITY = Integer.MIN_VALUE;
    private static final int INCOMPATIBLE_PRIORITY = Integer.MAX_VALUE;

    public static int calculatePriority(ShareHandler handler, Class<?> targetClass) {
        Class<?> handlerClass = handler.getTargetClass();

        // overlays always go first
        if (handlerClass == null) return OVERLAY_PRIORITY;

        // should not be returned as caller checks it, but anyway...
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

    // <T extends ClientShareHandler<?>> because inheritance-wise comparison should be used only on client handlers
    public static <T extends ClientShareHandler<?>> Comparator<T> createComparator(Class<?> targetClass) {
        return (h1, h2) -> {
            int p1 = calculatePriority(h1, targetClass);
            int p2 = calculatePriority(h2, targetClass);

            // in case 2 handlers target the same class they are compared by getPriority()
            if (p1 == p2) {
//                Showcaser.LOGGER.info("priority comparison: {} against {}. -> {}:{}",
//                        h1.getIdentifier(), h2.getIdentifier(), h1.getPriority(), h2.getPriority());
                return Integer.compare(h1.getPriority(), h2.getPriority());
            }

            return Integer.compare(p1, p2);
        };
    }
}