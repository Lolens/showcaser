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

package io.github.lolens.showcaser.client.adapter;

import io.github.lolens.showcaser.Showcaser;
import io.github.lolens.showcaser.api.adapter.ShareContextAdapter;
import io.github.lolens.showcaser.api.resource.ShareableResource;
import io.github.lolens.showcaser.api.sharecontext.ShareContext;
import io.github.lolens.showcaser.exception.ResourceAdapterException;
import net.minecraft.util.Identifier;

import java.util.*;

public class AdapterRegistry {

    private static final Map<Identifier, List<ShareContextAdapter>> ADAPTERS = new HashMap<>();

    private static final Comparator<ShareContextAdapter> ADAPTER_COMPARATOR =
            Comparator.comparingInt(ShareContextAdapter::getPriority);

    public static void register(ShareContextAdapter adapter) {
        ADAPTERS.compute(adapter.getHandlerId(), (id, adapterList) -> {
            if (adapterList == null) {
                adapterList = new ArrayList<>();
            }

            adapterList.add(adapter);
            adapterList.sort(ADAPTER_COMPARATOR);

            return adapterList;
        });
        Showcaser.LOGGER.debug("Registered context adapter for handler with id {}", adapter.getHandlerId());
    }

    public static ShareableResource adapt(ShareContext context) {
        if (context == null) throw new ResourceAdapterException("Context is null");

        Identifier handlerId = context.getId();
        List<ShareContextAdapter> adapters = ADAPTERS.get(handlerId);

        for (ShareContextAdapter adapter : adapters) {
            try {
                Optional<ShareableResource> resource = adapter.adapt(context);
                if (resource.isPresent()) {
                    return resource.get();
                }

            } catch (Exception e) {
                Showcaser.LOGGER.error("Adapter with handler id {} failed to create ShareableResource",
                        adapter.getHandlerId(), e);
            }
        }

        throw new ResourceAdapterException("No handlers can handle context. Context: " + context);
    }


}
