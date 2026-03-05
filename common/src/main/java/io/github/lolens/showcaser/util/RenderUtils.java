package io.github.lolens.showcaser.util;

public class RenderUtils {

    public static byte[] intToRGB(int color) {
        byte r = (byte) ((color >> 16) & 0xFF );
        byte g = (byte) ((color >> 8) & 0xFF);
        byte b = (byte) (color  & 0xFF);

        return new byte[]{r, g, b};
    }

    public static float[] intToRGBNormalized(int color) {
        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color  & 0xFF) / 255f;

        return new float[]{r, g, b};
    }

}
