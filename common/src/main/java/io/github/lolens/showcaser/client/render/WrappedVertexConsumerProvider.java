package io.github.lolens.showcaser.client.render;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;

public class WrappedVertexConsumerProvider implements VertexConsumerProvider {

    private final VertexConsumerProvider delegate;

    public WrappedVertexConsumerProvider(VertexConsumerProvider provider) {
        this.delegate = provider;
    }

    @Override
    public VertexConsumer getBuffer(RenderLayer layer) {
        return new WrappedVertexConsumer(delegate.getBuffer(layer));
    }
}
