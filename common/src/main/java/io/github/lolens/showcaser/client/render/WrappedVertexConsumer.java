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

package io.github.lolens.showcaser.client.render;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3i;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class WrappedVertexConsumer implements VertexConsumer {

    private final VertexConsumer delegate;

    public WrappedVertexConsumer(VertexConsumer delegate) {
        this.delegate = delegate;
    }

    @Override
    public VertexConsumer vertex(double x, double y, double z) {
        return delegate.vertex(x, y, z);
    }

    @Override
    public VertexConsumer color(int red, int green, int blue, int alpha) {
        return delegate.color(red, green, blue, alpha * RenderableHoverEvent.currentAlpha);
    }

    @Override
    public VertexConsumer texture(float u, float v) {
        return delegate.texture(u, v);
    }

    @Override
    public VertexConsumer overlay(int u, int v) {
        return delegate.overlay(u, v);
    }

    @Override
    public VertexConsumer light(int u, int v) {
        return delegate.light(u, v);
    }

    @Override
    public VertexConsumer normal(float x, float y, float z) {
        return delegate.normal(x, y, z);
    }

    @Override
    public void next() {
        delegate.next();
    }

    @Override
    public void fixedColor(int red, int green, int blue, int alpha) {
        delegate.fixedColor(red, green, blue, (int) (alpha * RenderableHoverEvent.currentAlpha));
    }

    @Override
    public void unfixColor() {
        delegate.unfixColor();
    }

    @Override
    public void vertex(float x, float y, float z, float red, float green, float blue, float alpha, float u, float v, int overlay, int light, float normalX, float normalY, float normalZ) {
        delegate.vertex(x, y, z, red, green, blue, alpha * RenderableHoverEvent.currentAlpha, u, v, overlay, light, normalX, normalY, normalZ);
    }

    @Override
    public VertexConsumer color(float red, float green, float blue, float alpha) {
        int newAlpha = (int) (alpha * RenderableHoverEvent.currentAlpha);
        System.out.println("Color: alpha=" + alpha + " newAlpha=" + newAlpha + " currentAlpha=" + RenderableHoverEvent.currentAlpha);
        return delegate.color(red, green, blue, alpha * RenderableHoverEvent.currentAlpha);
    }

    @Override
    public VertexConsumer color(int argb) {
        int a = (argb >> 24) & 255;
        int r = (argb >> 16) & 255;
        int g = (argb >> 8) & 255;
        int b = argb & 255;

        a = (int) (a * RenderableHoverEvent.currentAlpha);

        return delegate.color((a << 24) | (r << 16) | (g << 8) | b);
    }

    @Override
    public VertexConsumer light(int uv) {
        return delegate.light(uv);
    }

    @Override
    public VertexConsumer overlay(int uv) {
        return delegate.overlay(uv);
    }

    @Override
    public void quad(MatrixStack.Entry matrixEntry, BakedQuad quad, float red, float green, float blue, int light, int overlay) {
        delegate.quad(matrixEntry, quad, red, green, blue, light, overlay);
    }

    @Override
    public void quad(MatrixStack.Entry matrixEntry, BakedQuad quad, float[] brightnesses, float red, float green, float blue, int[] lights, int overlay, boolean useQuadColorData) {
        float[] fs = new float[]{brightnesses[0], brightnesses[1], brightnesses[2], brightnesses[3]};
        int[] is = new int[]{lights[0], lights[1], lights[2], lights[3]};
        int[] js = quad.getVertexData();
        Vec3i vec3i = quad.getFace().getVector();
        Matrix4f matrix4f = matrixEntry.getPositionMatrix();
        Vector3f vector3f = matrixEntry.getNormalMatrix().transform(new Vector3f(vec3i.getX(), vec3i.getY(), vec3i.getZ()));
        int i = 8;
        int j = js.length / 8;

        try (MemoryStack memoryStack = MemoryStack.stackPush()) {
            ByteBuffer byteBuffer = memoryStack.malloc(VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL.getVertexSizeByte());
            IntBuffer intBuffer = byteBuffer.asIntBuffer();

            for (int k = 0; k < j; k++) {
                intBuffer.clear();
                intBuffer.put(js, k * 8, 8);
                float f = byteBuffer.getFloat(0);
                float g = byteBuffer.getFloat(4);
                float h = byteBuffer.getFloat(8);
                float o;
                float p;
                float q;
                if (useQuadColorData) {
                    float l = (byteBuffer.get(12) & 255) / 255.0F;
                    float m = (byteBuffer.get(13) & 255) / 255.0F;
                    float n = (byteBuffer.get(14) & 255) / 255.0F;
                    o = l * fs[k] * red;
                    p = m * fs[k] * green;
                    q = n * fs[k] * blue;
                } else {
                    o = fs[k] * red;
                    p = fs[k] * green;
                    q = fs[k] * blue;
                }

                int r = is[k];
                float m = byteBuffer.getFloat(16);
                float n = byteBuffer.getFloat(20);
                Vector4f vector4f = matrix4f.transform(new Vector4f(f, g, h, 1.0F));
                // 1.0f constant to RenderableHoverEvent.currentAlpha
                System.out.println("drawed vertex with current alpha: " + RenderableHoverEvent.currentAlpha);
                delegate.vertex(vector4f.x(), vector4f.y(), vector4f.z(), o, p, q, RenderableHoverEvent.currentAlpha, m, n, overlay, r, vector3f.x(), vector3f.y(), vector3f.z());
            }
        }
    }

    @Override
    public VertexConsumer vertex(Matrix4f matrix, float x, float y, float z) {
        return delegate.vertex(matrix, x, y, z);
    }

    @Override
    public VertexConsumer normal(Matrix3f matrix, float x, float y, float z) {
        return delegate.normal(matrix, x, y, z);
    }
}
