package net.cybertekt.asset.font;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import net.cybertekt.asset.Asset;
import net.cybertekt.asset.AssetKey;

/**
 * Font - (C) Cybertekt Software
 *
 * Immutable {@link Asset asset} containing information for rendering text.
 *
 * @version 1.1.0
 * @since 1.1.0
 * @author Andrew Vektor
 */
public final class Font extends Asset {

    /**
     * Font render size.
     */
    private final int SIZE;

    /**
     * Font image width.
     */
    private final int WIDTH;

    /**
     * Font image height.
     */
    private final int HEIGHT;

    /**
     * Font line height.
     */
    private final int LINE;

    /**
     * Font space width.
     */
    private final int SPACE;

    /**
     * Font image surface data.
     */
    private final ByteBuffer DATA;

    /**
     * List of {@link Glyph glyphs} available for the font.
     */
    private final Map<Integer, Glyph> GLYPHS;

    /**
     * Constructs a font {@link Asset asset} for the file located at the path
     * specified by an {@link AssetKey asset key}.
     *
     * @param KEY the font asset key.
     * @param SIZE the default font render size.
     * @param WIDTH the width of the font image.
     * @param HEIGHT the height of the font image.
     * @param LINE the font line height.
     * @param SPACE the font space width.
     * @param DATA the font image surface data.
     * @param GLYPHS the list of {@link Glyph glyphs} available for the font.
     */
    public Font(final AssetKey KEY, final int SIZE, final int WIDTH, final int HEIGHT, final int LINE, final int SPACE, final ByteBuffer DATA, final Map<Integer, Glyph> GLYPHS) {
        super(KEY);
        this.SIZE = SIZE;
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.LINE = LINE;
        this.SPACE = SPACE;
        this.DATA = DATA;
        this.GLYPHS = GLYPHS;
    }

    /**
     * Returns the default font point size.
     *
     * @return the default point size.
     */
    public final int getSize() {
        return SIZE;
    }

    /**
     * Returns the width, in pixels, of the font image.
     *
     * @return the font image width.
     */
    public final int getWidth() {
        return WIDTH;
    }

    /**
     * Returns the height, in pixels, of the font image.
     *
     * @return the font image height.
     */
    public final int getHeight() {
        return HEIGHT;
    }

    /**
     * Returns the line height of the font.
     *
     * @return the font line height.
     */
    public final int getLine() {
        return LINE;
    }

    /**
     * Returns the space width of the font.
     *
     * @return the font space width.
     */
    public final int getSpace() {
        return SPACE;
    }

    /**
     * Returns the image surface data for the font.
     *
     * @return the font image surface data.
     */
    public final ByteBuffer getData() {
        return DATA;
    }

    /**
     * Returns true if the font has a {@link Glyph glyph} for the specified
     * character code.
     *
     * @param code the character code.
     * @return true if the font has a glyph for the specified character code.
     */
    public final boolean hasGlyph(final int code) {
        return GLYPHS.containsKey(code);
    }

    /**
     * Returns the {@link Glyph glyph} for the specified character code, if one
     * exists.
     *
     * @param code the character code.
     * @return the glyph for the specified character code, or null if no glyph
     * exists for the specified character code.
     */
    public final Glyph getGlyph(final int code) {
        return GLYPHS.get(code);
    }

    /**
     * Stores the information for an individual font character.
     */
    public static class Glyph {

        /**
         * Glyph character code.
         */
        private final int CODE;

        /**
         * Glyph position.
         */
        private final int xPOS, yPOS;

        /**
         * Glyph size.
         */
        private final int WIDTH, HEIGHT;

        /**
         * Glyph position offsets.
         */
        private final int xOFFSET, yOFFSET;

        /**
         * Glyph x-axis advance.
         */
        private final int ADVANCE;

        /**
         * Glyph Kerning Offsets.
         */
        private final Map<Integer, Integer> KERNINGS;

        /**
         * Constructs a glyph which stores information for an individual font
         * character.
         *
         * @param CODE the glyph character code.
         * @param xPOS the x-axis position of the glyph image.
         * @param yPOS the y-axis position of the glyph image.
         * @param WIDTH the width of the glyph image.
         * @param HEIGHT the height of the glyph image.
         * @param xOFFSET the x-axis position offset of the glyph.
         * @param yOFFSET the y-axis position offset of the glyph.
         * @param ADVANCE the amount of space to place between the glyph and any
         * subsequent glyphs.
         */
        public Glyph(final int CODE, final int xPOS, final int yPOS, final int WIDTH, final int HEIGHT, final int xOFFSET, final int yOFFSET, final int ADVANCE) {
            this.CODE = CODE;
            this.xPOS = xPOS;
            this.yPOS = yPOS;
            this.WIDTH = WIDTH;
            this.HEIGHT = HEIGHT;
            this.xOFFSET = xOFFSET;
            this.yOFFSET = yOFFSET;
            this.ADVANCE = ADVANCE;
            this.KERNINGS = new HashMap<>();
        }

        /**
         * Returns the character code of the glyph.
         *
         * @return the glyph character code.
         */
        public final int getCode() {
            return CODE;
        }

        /**
         * Returns the x-axis position of the glyph within the font texture.
         *
         * @return the x-axis position of the glyph.
         */
        public final int getX() {
            return xPOS;
        }

        /**
         * Returns the y-axis position of the glyph within the font texture.
         * 
         * @return the y-axis position of the glyph.
         */
        public final int getY() {
            return yPOS;
        }

        /**
         * Returns the width of the glyph within the font texture.
         * 
         * @return the width of the glyph.
         */
        public final int getWidth() {
            return WIDTH;
        }

        /**
         * Returns the height of the glyph within the font texture.
         * 
         * @return the height of the glyph.
         */
        public final int getHeight() {
            return HEIGHT;
        }

        public final int getOffsetX() {
            return xOFFSET;
        }

        public final int getOffsetY() {
            return yOFFSET;
        }

        public final int getAdvance() {
            return ADVANCE;
        }

        public final void addKerning(final int lastChar, final int offset) {
            KERNINGS.put(lastChar, offset);
        }

        public final void removeKerning(final int lastChar) {
            KERNINGS.remove(lastChar);
        }

        public final int getKerningOffset(final int lastChar) {
            if (KERNINGS.containsKey(lastChar)) {
                return KERNINGS.get(lastChar);
            }
            return 0;
        }

        public final float[] getVertices(final int xPos, final int yPos, final float scale) {
            return new float[]{
                xOFFSET * scale + xPos, (HEIGHT + yOFFSET) * scale + yPos, 0f, // Top Left
                (WIDTH + xOFFSET) * scale + xPos, (HEIGHT + yOFFSET) * scale + yPos, 0f, // Top Right
                (WIDTH + xOFFSET) * scale + xPos, yOFFSET * scale + yPos, 0f, // Bottom Right
                xOFFSET * scale + xPos, yOFFSET * scale + yPos, 0f, // Bottom Left
            };
        }

        public final float[] getCoordinates() {
            return new float[]{
                xPOS, yPOS + HEIGHT, // Top Left Coordinate
                xPOS + WIDTH, yPOS + HEIGHT, // Top Right Coordinate
                xPOS + WIDTH, yPOS, // Bottom Right Coordinate
                xPOS, yPOS, // Bottom Left Coordinate
            };
        }

        public final int[] getIndices(final int offset) {
            return new int[]{
                3 + offset, 0 + offset, 1 + offset,
                1 + offset, 2 + offset, 3 + offset
            };
        }
    }
}
