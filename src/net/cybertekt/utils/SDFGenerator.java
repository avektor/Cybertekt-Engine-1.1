package net.cybertekt.utils;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Signed Distance Field Generator - (C) Cybertekt Software
 *
 * Generates a signed distance field image from an existing image.
 *
 * @version 1.1.0
 * @since 1.1.0
 * @author Andrew Vektor
 */
public class SDFGenerator {

    /**
     * Generates a signed distance field for the image provided.
     *
     * @param size the signed distance size.
     * @param img the image to convert.
     * @return the converted distance field image.
     */
    public static BufferedImage genSDF(final int size, final BufferedImage img) {
        final boolean[][] pixels = new boolean[img.getWidth()][img.getHeight()];
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                pixels[y][x] = isInside(img.getRGB(x, y));
            }
        }
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                float signedDistance = getSignedDist(size, x, y, pixels);
                img.setRGB(x, y, distanceToRGB(size, signedDistance));
            }
        }
        return img;
    }

    /**
     * Returns {@code true} if the color is considered as the "inside" of the
     * image, {@code false} if considered "outside".
     *
     * <p>
     * Any color with one of its color channels at least 128
     * <em>and</em> its alpha channel at least 128 is considered "inside".
     */
    private static boolean isInside(int rgb) {
        return (rgb & 0x808080) != 0 && (rgb & 0x80000000) != 0;
    }

    /**
     * Returns the signed distance for a given point. For inside points, this is
     * the distance to the closest outside pixel. For outside points, this is
     * the negative distance to the closest inside pixel.
     *
     * @param xCenter the x coordinate of the center point
     * @param yCenter the y coordinate of the center point
     * @param pixels the array representation of an image, {@code true}
     * representing an inside pixel.
     * @return the signed distance
     */
    private static float getSignedDist(final int spread, final int xCenter, final int yCenter, boolean[][] pixels) {
        final int width = pixels[0].length;
        final int height = pixels.length;
        final boolean base = pixels[yCenter][xCenter];
        final int delta = (int) Math.ceil(spread);
        final int xStart = Math.max(0, xCenter - delta);
        final int xEnd = Math.min(width - 1, xCenter + delta);
        final int yStart = Math.max(0, yCenter - delta);
        final int yEnd = Math.min(height - 1, yCenter + delta);
        int closestSquareDist = delta * delta;
        for (int y = yStart; y <= yEnd; ++y) {
            for (int x = xStart; x <= xEnd; ++x) {
                if (base != pixels[y][x]) {
                    final int squareDist = getDistSquared(xCenter, yCenter, x, y);
                    if (squareDist < closestSquareDist) {
                        closestSquareDist = squareDist;
                    }
                }
            }
        }
        float closestDist = (float) Math.sqrt(closestSquareDist);
        return (base ? 1 : -1) * Math.min(closestDist, spread);
    }

    /**
     * Calculate the distance squared between two points
     *
     * @param x1 The x coordinate of the first point
     * @param y1 The y coordinate of the first point
     * @param x2 The x coordinate of the second point
     * @param y2 The y coordinate of the second point
     * @return The squared distance between the two points
     */
    private static int getDistSquared(final int x1, final int y1, final int x2, final int y2) {
        final int dx = x1 - x2;
        final int dy = y1 - y2;
        return dx * dx + dy * dy;
    }

    /**
     * Convert a signed distance value into an RGBA color value.
     *
     * @param size the signed distance size.
     * @param signedDistance the signed distance value.
     * @return the RGBA color value.
     */
    private static int distanceToRGB(final int size, float signedDistance) {
        float alpha = 0.5f + 0.5f * (signedDistance / size);
        alpha = Math.min(1, Math.max(0, alpha));
        int alphaByte = (int) (alpha * 0xFF);
        return (alphaByte << 24) | (Color.WHITE.getRGB() & 0xFFFFFF);
    }
}
