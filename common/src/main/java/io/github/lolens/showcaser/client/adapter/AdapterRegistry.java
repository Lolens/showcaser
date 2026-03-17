package io.github.lolens.showcaser.client.adapter;

import io.github.lolens.showcaser.Showcaser;
import io.github.lolens.showcaser.api.adapter.ShareContextAdapter;
import io.github.lolens.showcaser.api.resource.EmptyResource;
import io.github.lolens.showcaser.api.resource.ShareableResource;
import io.github.lolens.showcaser.api.shareContext.ShareContext;
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
        Showcaser.LOGGER.info("Registered context adapter for handler with id {}", adapter.getHandlerId());
    }

    public static ShareableResource adapt(ShareContext context) {
        if (context == null) return new EmptyResource();

        Identifier handlerId = context.getId();
        List<ShareContextAdapter> adapters = ADAPTERS.get(handlerId);

        for (ShareContextAdapter adapter : adapters) {
            try {
                ShareableResource resource = adapter.adapt(context);
                if (resource != null && !(resource instanceof EmptyResource)) {
                    return resource;
                }

            } catch (Exception e) {
                Showcaser.LOGGER.error("Adapter with handler id {} failed to create ShareableResource",
                        adapter.getHandlerId(), e);
            }
        }

        return new EmptyResource();
    }


}
