package net.cybertekt.asset.image;

import java.nio.ByteBuffer;
import net.cybertekt.asset.Asset;
import net.cybertekt.asset.AssetKey;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_SHORT;

/**
 * Image - (C) Cybertekt Software
 *
 * Immutable {@link Asset asset} containing the surface data of an image.
 *
 * @version 1.1.0
 * @since 1.1.0
 * @author Andrew Vektor
 */
public class Image extends Asset {

    /**
     * Specifies the format of the image buffer data.
     */
    public enum Format {
        /**
         * Three 8-Bit Color Channels: Red, Green, and Blue.
         */
        RGB8(GL_RGB, GL_UNSIGNED_BYTE, 3),
        /**
         * Three 16-Bit Color Channels: Red, Green, and Blue.
         */
        RGB16(GL_RGB, GL_UNSIGNED_SHORT, 6),
        /**
         * Four 8-Bit Color Channels: Red, Green, Blue, and Alpha.
         */
        RGBA8(GL_RGBA, GL_UNSIGNED_BYTE, 4),
        /**
         * Four 16-Bit Color Channels: Red, Green, Blue, and Alpha.
         */
        RGBA16(GL_RGBA, GL_UNSIGNED_SHORT, 8);

        /**
         * OpenGL Texture Constant.
         */
        public final int ID;

        /**
         * OpenGL Data Type.
         */
        public final int TYPE;

        /**
         * Bytes Per Pixel.
         */
        public final int BPP;

        /**
         * Image Format Constructor.
         *
         * @param ID the OpenGL texture constant.
         * @param TYPE the OpenGL data type.
         * @param BPP number of bytes per pixel.
         */
        Format(final int ID, final int TYPE, final int BPP) {
            this.ID = ID;
            this.TYPE = TYPE;
            this.BPP = BPP;
        }
    }

    /**
     * Format of the surface data in the image buffer.
     */
    private final Format FORMAT;

    /**
     * Width of the image in pixels.
     */
    private final int WIDTH;

    /**
     * Height of the image in pixels.
     */
    private final int HEIGHT;

    /**
     * Buffer that stores the image surface data.
     */
    private final ByteBuffer DATA;

    /**
     * Constructs a new image {@link Asset asset} defined by the file located at
     * the path specified by the {@link AssetKey asset key}.
     *
     * @param KEY the asset key for the image file.
     * @param FORMAT the image format.
     * @param WIDTH the image width, in pixels.
     * @param HEIGHT the image height, in pixels.
     * @param DATA the image surface data.
     */
    public Image(final AssetKey KEY, final Format FORMAT, final int WIDTH, final int HEIGHT, final ByteBuffer DATA) {
        super(KEY);
        this.FORMAT = FORMAT;
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.DATA = DATA;
    }

    /**
     * Returns the image {@link Format format}.
     *
     * @return the image format.
     */
    public final Format getFormat() {
        return FORMAT;
    }

    /**
     * Returns the width of the image in pixels.
     *
     * @return the image width.
     */
    public final int getWidth() {
        return WIDTH;
    }

    /**
     * Returns the height of the image in pixels.
     *
     * @return the image height.
     */
    public final int getHeight() {
        return HEIGHT;
    }

    /**
     * Returns a read-only view of the buffer containing the image surface data.
     *
     * @return the image buffer.
     */
    public final ByteBuffer getBuffer() {
        return DATA.asReadOnlyBuffer();
    }
}
