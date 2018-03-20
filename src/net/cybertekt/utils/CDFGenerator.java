package net.cybertekt.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.GlyphVector;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Cybertekt Distance Font (CDF) Generator - (C) Cybertekt Software
 *
 * Static utility class that generates a Cybertekt Distance Font (CDF) from an
 * existing True Type Font (TTF).
 *
 * @version 1.1.0
 * @since 1.1.0
 * @author Andrew Vektor
 */
public class CDFGenerator {

    /**
     * CDF File Signature.
     */
    private static final int CDF = 0x676870; //67-68-70 (SDF)

    /**
     * CDF File Header.
     */
    private static final int HDR = 0x726882; //72-68-82 (HDR)

    /**
     * CDF Character Information.
     */
    private static final int CHR = 0x677282; //67-72-82 (CHR)

    /**
     * CDF Kerning Information.
     */
    private static final int KRN = 0x758278; //75-82-78 (KRN)

    /**
     * CDF Image Data.
     */
    private static final int IMG = 0x737771; //73-77-71 (IMG)

    /**
     * CDF File Footer.
     */
    private static final int FTR = 0x708482; //70-84-82 (FTR)

    /**
     * Basic Unicode Character Set - Characters 33 to 126.
     */
    public static final char[] BASIC_CHARSET = new char[94];

    /**
     * Initializes the standard character set arrays.
     */
    static {
        // Initialize Basic Character Set //
        for (int i = 33; i < 127; i++) {
            BASIC_CHARSET[i - 33] = (char) i;
        }
    }

    /**
     * Creates a Cybertekt Distance Font (CDF) from the existing True Type Font
     * (TTF) located at the path specified.
     *
     * @param TTF the location of the true type font (TTF) file.
     * @param SIZE the point size at which to render the distance font image.
     * @param SPREAD the amount of padding between glyphs in the CDF image.
     * @param CHARSET the characters to include in the distance font.
     * @param PATH the desired file location of the generated distance font.
     * @return the signed distance font image generated from the TTF. May be
     * used for previewing the results of the CDF generation.
     * @throws IOException if, for any reason, a CDF could not be generated from
     * the specified TTF.
     */
    public static final BufferedImage generate(final String TTF, final int SIZE, final int SPREAD, final char[] CHARSET, final String PATH) throws IOException {
        // Create Temporary AWT Graphics2D Object //
        final Graphics2D GFX = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();

        // Load True Type Font //
        final Font FONT = loadFont(TTF, SIZE);

        // Initialize Glyph List //
        final List<Glyph> GLYPHS = new ArrayList<>(CHARSET.length);
        for (final char C : CHARSET) {
            GLYPHS.add(new Glyph(C, FONT.createGlyphVector(GFX.getFontRenderContext(), new char[]{C})));
        }

        // Get Font Line Height //
        final int LINE = GFX.getFontMetrics(FONT).getAscent();
        
        // Get Font Space Width //
        final int SPACE = (int) FONT.createGlyphVector(GFX.getFontRenderContext(), new char[]{' '}).getGlyphMetrics(0).getAdvanceX();

        // Generate CDF Font Image //
        final BufferedImage IMAGE = loadImage(GLYPHS, SPREAD);

        // Dispose Of Temporary AWT Graphics2D Object //
        GFX.dispose();

        // Load Font Kernings //
        final List<Kerning> KERNINGS = loadKernings(TTF, SIZE, CHARSET);

        // Create Output Font File //
        try (final FileOutputStream OUTPUT = new FileOutputStream(PATH)) {

            // Write CDF File Signature //
            writeInt(OUTPUT, CDF);

            // Write CDF Header Data //
            writeInt(OUTPUT, HDR);
            writeInt(OUTPUT, SIZE); // Write Font Render Size
            writeInt(OUTPUT, IMAGE.getWidth()); // Write Font Image Width
            writeInt(OUTPUT, IMAGE.getHeight()); // Write Font Image Height
            writeInt(OUTPUT, LINE); // Write Font Line Height
            writeInt(OUTPUT, SPACE); // Write Font Space Width
            writeInt(OUTPUT, GLYPHS.size()); // Write Font Glyph Count
            writeInt(OUTPUT, KERNINGS.size()); // Write Font Kerning Count

            // Write CDF Characters Signature //
            writeInt(OUTPUT, CHR);

            // Write CDF Character Data //
            for (final Glyph G : GLYPHS) {
                writeInt(OUTPUT, G.getCode()); // Glyph Character Code
                writeInt(OUTPUT, G.getX()); // Glyph X-Axis Position.
                writeInt(OUTPUT, G.getY()); // Glyph Y-Axis Position.
                writeInt(OUTPUT, G.getWidth()); // Glyph Width.
                writeInt(OUTPUT, G.getHeight()); // Glyph Height.
                writeInt(OUTPUT, G.getOffsetX()); // Glyph X-Axis Offset.
                writeInt(OUTPUT, G.getOffsetY()); // Glyph Y-Axis Offset.
                writeInt(OUTPUT, G.getAdvance()); // Glyph X-Axis Advance.
            }

            // Write CDF Kernings Signature //
            writeInt(OUTPUT, KRN);

            // Write CDF Kerning Data //
            for (final Kerning K : KERNINGS) {
                writeInt(OUTPUT, K.getLeft()); // First Kerning Character Code
                writeInt(OUTPUT, K.getRight()); // Second Kerning Character Code
                writeInt(OUTPUT, K.getOffset()); // Kerning Position Offset
            }

            // Write CDF Image Data Signature //
            writeInt(OUTPUT, IMG);

            // Write CDF Image Data //
            for (int y = 0; y < IMAGE.getHeight(); y++) {
                for (int x = 0; x < IMAGE.getWidth(); x++) {
                    writeInt(OUTPUT, IMAGE.getRGB(x, y));
                }
            }

            // Write CDF File Footer //
            writeInt(OUTPUT, FTR);
        }
        return IMAGE;
    }

    /**
     * Generates a signed distance field image for the glyphs in the list
     * provided.
     *
     * @param GLYPHS the list of glyphs to draw onto the image.
     * @param SPREAD the amount of space between each glyph.
     * @return the signed distance field image.
     */
    private static BufferedImage loadImage(final List<Glyph> GLYPHS, final int SPREAD) {
        // Sort Glyphs //
        Collections.sort(GLYPHS);

        // Default Image Size //
        int size = 128;

        // Create A GlyphNode For Glyph Packing //
        GlyphNode glyphNode = new GlyphNode(0, 0, size, size, SPREAD);

        // Pack Glyphs Into Glyph Tree //
        while (!glyphNode.insert(GLYPHS)) {
            // Increase Image Size And Try Again //
            size *= 2;
            glyphNode = new GlyphNode(SPREAD, SPREAD, size, size, SPREAD);
        }

        // Create Image //
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gfx = image.createGraphics();
        gfx.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Draw Font Image //
        for (final GlyphNode node : glyphNode.collect()) {
            if (node.hasGlyph()) {
                Glyph glyph = node.getGlyph();
                gfx.drawGlyphVector(glyph.getDrawable(), (glyph.getOffsetX() * -1) + node.getX(), (glyph.getOffsetY() * -1) + node.getY());
                glyph.setX(node.getX() - (SPREAD / 2));
                glyph.setY(node.getY() - (SPREAD / 2));
                glyph.setWidth((glyph.getWidth() + SPREAD) - 1);
                glyph.setHeight((glyph.getHeight() + SPREAD) - 1);
            }
        }
        return convertToSDF(Math.round(SPREAD / 2), image);
    }

    /**
     * Retrieves kerning information from the TTF file located at the specified
     * path. The kerning offsets will be scaled by the appropriate amount based
     * on the specified font size. Kerning information will only be retrieved
     * for characters that are part of the specified character set. An empty
     * list will be returned if no kerning information is available for the
     * specified font.
     *
     * @param PATH the path to the TTF file.
     * @param SIZE the font point size.
     * @param CHARSET the font character set.
     * @return a list of character kernings.
     * @throws IOException if an I/O error occurs.
     */
    private static List<Kerning> loadKernings(final String PATH, final int SIZE, final char[] CHARSET) throws IOException {
        try {
            final List<Kerning> KERNINGS = new ArrayList<>();

            // Get TTF Input Stream //
            final InputStream INPUT = new FileInputStream(PATH);

            // Get Table Count //
            INPUT.skip(4);
            int tables = readShort(INPUT);
            INPUT.skip(6);

            // Tracks Input Stream Position //
            int position = 12;

            // Find Kerning Table //
            byte[] tag = new byte[4];
            int headTableOffset = -1, kernTableOffset = -1;
            for (int i = 0; i < tables && (headTableOffset == -1 || kernTableOffset == -1); i++) {
                INPUT.read(tag);
                INPUT.skip(4);
                switch (new String(tag, "ISO-8859-1")) {
                    case "head": {
                        headTableOffset = (int) readLong(INPUT);
                        INPUT.skip(4);
                        break;
                    }
                    case "kern": {
                        kernTableOffset = (int) readLong(INPUT);
                        INPUT.skip(4);
                        break;
                    }
                    default: {
                        INPUT.skip(8);
                        break;
                    }
                }
                position += 16;
            }

            // Process Header Table //
            if (headTableOffset != -1) {
                INPUT.skip((headTableOffset - position) + 18);
                final float SCALE = (float) SIZE / readShort(INPUT);
                position += (headTableOffset - position) + 20;

                // Process Kerning Table //
                if (kernTableOffset != -1) {

                    // Sort Charset Array (For Later Binary Searching) //
                    Arrays.sort(CHARSET);

                    // Skip To Kerning Table //
                    INPUT.skip((kernTableOffset - position) + 2);
                    for (int subtables = readShort(INPUT); subtables > 0; subtables--) {
                        INPUT.skip(4);

                        // Check Coverage Byte (Vertical Bit) (Cross Stream Bit) (Variation Bit) (Format Bit) //
                        int coverage = readShort(INPUT);
                        if ((coverage & 0x8000) != 0 || (coverage & 0x4000) != 0 || (coverage & 0x2000) != 0 || (coverage & 0x00FF) > 1) {
                            break;
                        }

                        // Read Kerning Count //
                        int count = readShort(INPUT);
                        INPUT.skip(6);

                        // Process Kernings //
                        while (count-- > 0) {
                            final int FIRST = readShort(INPUT);
                            final int LAST = readShort(INPUT);
                            final int OFFSET = (short) readShort(INPUT);
                            if (Arrays.binarySearch(CHARSET, (char) FIRST) >= 0 && Arrays.binarySearch(CHARSET, (char) LAST) >= 0) {
                                KERNINGS.add(new Kerning(FIRST, LAST, Math.round(OFFSET * SCALE)));
                            }
                        }
                    }
                }
            }
            return KERNINGS;
        } catch (IOException e) {
            throw new IOException("Unable To Read Kerning Data: " + e.getMessage());
        }
    }

    /**
     * Returns the {@link Font font} loaded from the TTF file at the specified
     * path.
     *
     * @param PATH the TTF font file path.
     * @param SIZE the desired font size.
     * @return the loaded font.
     * @throws IOException if the font could not be loaded for any reason.
     */
    private static Font loadFont(final String PATH, final int SIZE) throws IOException {
        try {
            // Load Font From File //
            Font fnt = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(PATH));

            // Set Font Attributes //
            Map attribs = fnt.getAttributes();
            attribs.put(TextAttribute.SIZE, new Float(SIZE));
            //attribs.put(TextAttribute.WEIGHT, BOLD ? TextAttribute.WEIGHT_BOLD : TextAttribute.WEIGHT_REGULAR);
            //attribs.put(TextAttribute.POSTURE, ITALIC ? TextAttribute.POSTURE_OBLIQUE : TextAttribute.POSTURE_REGULAR);

            // Return Font With Updated Attributes //
            return fnt.deriveFont(attribs);
        } catch (FontFormatException | IOException e) {
            throw new IOException("Unable To Generate SDF Font: " + e.getMessage());
        }
    }

    /**
     * Converts the image into a signed distance field image.
     *
     * @param size the signed distance size.
     * @param img the image to convert.
     * @return the converted distance field image.
     */
    private static BufferedImage convertToSDF(final int size, final BufferedImage img) {
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

    /**
     * Writes an integer to an {@link OutputStream output stream} as bytes in
     * {@link ByteOrder#BIG_ENDIAN big endian order}.
     *
     * @param STREAM the output stream to write into.
     * @param INT the integer to write to the output stream.
     * @throws IOException if the integer could not be written to the output
     * stream for any reason.
     */
    private static void writeInt(final OutputStream STREAM, final int INT) throws IOException {
        STREAM.write(ByteBuffer.allocate(4).putInt(INT).order(ByteOrder.BIG_ENDIAN).array());
    }

    /**
     * Reads and unsigned short from the current position of the provided input
     * stream.
     *
     * @param STREAM the input stream from which to read.
     * @return the unsigned short value.
     * @throws IOException if an I/O error occurs.
     */
    private static int readShort(final InputStream STREAM) throws IOException {
        return (STREAM.read() << 8) + STREAM.read();
    }

    /**
     * Reads and unsigned long from the current position of the provided input
     * stream.
     *
     * @param STREAM the input stream from which to read.
     * @return the unsigned long value.
     * @throws IOException if an I/O error occurs.
     */
    private static long readLong(final InputStream STREAM) throws IOException {
        long value = STREAM.read();
        value = (value << 8) + STREAM.read();
        value = (value << 8) + STREAM.read();
        return (value << 8) + STREAM.read();
    }

    /**
     * Stores the attributes of a font character glyph.
     */
    private static class Glyph implements Comparable<Glyph> {

        /**
         * Glyph character code.
         */
        private final int CODE;

        /**
         * Glyph AWT drawable.
         */
        private final GlyphVector DRAWABLE;

        /**
         * Glyph position.
         */
        private int xPos, yPos;

        /**
         * Glyph size.
         */
        private int width, height;

        /**
         * Glyph position offsets.
         */
        private int xOffset, yOffset;

        /**
         * Glyph x-axis advance.
         */
        private int advance;

        /**
         * Constructs a new Glyph for the specified character code initialized
         * with attributes taken from a drawable AWT glyph vector.
         *
         * @param code the glyph character code.
         * @param drawable the drawable AWT glyph vector.
         */
        public Glyph(final int code, final GlyphVector drawable) {
            this.CODE = code;
            this.DRAWABLE = drawable;
            setOffsetX(DRAWABLE.getPixelBounds(null, 0, 0).x);
            setOffsetY(DRAWABLE.getPixelBounds(null, 0, 0).y);
            setWidth(DRAWABLE.getPixelBounds(null, 0, 0).width);
            setHeight(DRAWABLE.getPixelBounds(null, 0, 0).height);
            setAdvance((int) DRAWABLE.getGlyphMetrics(0).getAdvanceX());
        }

        /**
         * Special Glyph Constructor used for creating constructing a glyph
         * which has no drawable (such as a space character).
         *
         * @param code the glyph character code.
         * @param advance the glyph x-axis advance.
         */
        public Glyph(final int code, final int advance) {
            this.CODE = code;
            this.DRAWABLE = null;
            setAdvance(advance);
        }

        /**
         * Returns the glyph character code.
         *
         * @return the character code.
         */
        public final int getCode() {
            return CODE;
        }

        /**
         * Returns the AWT drawable glyph vector.
         *
         * @return the drawable glyph vector.
         */
        public final GlyphVector getDrawable() {
            return DRAWABLE;
        }

        /**
         * Sets the glyph x-axis position.
         *
         * @param xPos the x-axis position.
         */
        public final void setX(final int xPos) {
            this.xPos = xPos;
        }

        /**
         * Returns the glyph x-axis position.
         *
         * @return the x-axis position.
         */
        public final int getX() {
            return xPos;
        }

        /**
         * Sets the glyph y-axis position.
         *
         * @param yPos the y-axis position.
         */
        public final void setY(final int yPos) {
            this.yPos = yPos;
        }

        /**
         * Returns the glyph y-axis position.
         *
         * @return the y-axis position.
         */
        public final int getY() {
            return yPos;
        }

        /**
         * Sets the glyph x-axis offset.
         *
         * @param xOffset the x-axis offset.
         */
        public final void setOffsetX(final int xOffset) {
            this.xOffset = xOffset;
        }

        /**
         * Returns the glyph x-axis offset.
         *
         * @return the x-axis offset.
         */
        public final int getOffsetX() {
            return xOffset;
        }

        /**
         * Sets the glyph y-axis offset.
         *
         * @param yOffset the y-axis offset.
         */
        public final void setOffsetY(final int yOffset) {
            this.yOffset = yOffset;
        }

        /**
         * Returns the glyph y-axis offset.
         *
         * @return the y-axis offset.
         */
        public final int getOffsetY() {
            return yOffset;
        }

        /**
         * Sets the width of the glyph.
         *
         * @param width the glyph width in pixels.
         */
        public final void setWidth(final int width) {
            this.width = width;
        }

        /**
         * Returns the width of the glyph.
         *
         * @return the glyph width in pixels.
         */
        public final int getWidth() {
            return width;
        }

        /**
         * Sets the height of the glyph.
         *
         * @param height the glyph height in pixels.
         */
        public final void setHeight(final int height) {
            this.height = height;
        }

        /**
         * Returns the height of the glyph.
         *
         * @return the glyph height in pixels.
         */
        public final int getHeight() {
            return height;
        }

        /**
         * Sets the glyph x-axis advance.
         *
         * @param advance the x-axis advance.
         */
        public final void setAdvance(final int advance) {
            this.advance = advance;
        }

        /**
         * Returns the glyph x-axis advance.
         *
         * @return the x-axis advance.
         */
        public final int getAdvance() {
            return advance;
        }

        /**
         * Compares glyphs based on height.
         *
         * @param glyph the glyph to compare.
         * @return the height difference between this glyph and the parameter
         * glyph.
         */
        @Override
        public final int compareTo(final Glyph glyph) {
            return glyph.getHeight() - getHeight();
        }
    }

    /**
     * Stores glyph kerning information.
     */
    private static class Kerning {

        /**
         * The glyph index for the left-hand glyph in the kerning pair.
         */
        private final int LEFT;

        /**
         * The glyph index for the right-hand glyph in the kerning pair.
         */
        private final int RIGHT;

        /**
         * The position offset, in pixels, for the kerning pair. If this value
         * is greater than zero the glyphs will be positioned further apart. If
         * this value is less than zero the glyphs will be positioned closer
         * together.
         */
        private final int OFFSET;

        /**
         * Constructs a kerning pair.
         *
         * @param LEFT the index of the left-hand glyph for the kerning pair.
         * @param RIGHT the index of the right-hand glyph for the kerning pair.
         * @param OFFSET the position offset, in pixels, for the kerning pair.
         */
        public Kerning(final int LEFT, final int RIGHT, final int OFFSET) {
            this.LEFT = LEFT;
            this.RIGHT = RIGHT;
            this.OFFSET = OFFSET;
        }

        /**
         * Returns the index of the left-hand glyph for the kerning pair.
         *
         * @return the index of the left-hand glyph for the kerning pair.
         */
        public final int getLeft() {
            return LEFT;
        }

        /**
         * Returns the index of the right-hand glyph for the kerning pair.
         *
         * @return the index of the right-hand glyph for the kerning pair.
         */
        public final int getRight() {
            return RIGHT;
        }

        /**
         * Returns the position offset, in pixels, for the kerning pair. When
         * this value is greater than zero the glyphs should be positioned
         * further apart. When this value is less than zero the glyphs should be
         * positioned closer together.
         *
         * @return the position offset, in pixels, for the kerning pair.
         */
        public final int getOffset() {
            return OFFSET;
        }
    }

    /**
     * Binary tree used for glyph packing.
     */
    private static class GlyphNode {

        /**
         * Node Children.
         */
        private final GlyphNode[] CHILDREN = new GlyphNode[2];

        /**
         * Node Position.
         */
        private final int xPOS, yPOS;

        /**
         * Node Size.
         */
        private final int WIDTH, HEIGHT;

        /**
         * Node Padding.
         */
        private final int PADDING;

        /**
         * Node Glyph.
         */
        private Glyph glyph;

        /**
         * Glyph Node Constructor.
         *
         * @param xPos the x-axis position of the node.
         * @param yPos the y-axis position of the node.
         * @param width the width of the node.
         * @param height the height of the node.
         * @param padding the amount of spacing between the node and its
         * children.
         */
        public GlyphNode(final int xPos, final int yPos, final int width, final int height, final int padding) {
            this.xPOS = xPos;
            this.yPOS = yPos;
            this.WIDTH = width;
            this.HEIGHT = height;
            this.PADDING = padding;
        }

        /**
         * Attempts to insert a list of glyphs into this node. If a glyph cannot
         * be inserted into this node, an attempt will be made to insert the
         * glyph into the children of this node. This method will immediately
         * return false if a glyph cannot be inserted into this node or any of
         * its children.
         *
         * @param glyphs the glyphs to insert into the node.
         * @return true if all the glyphs were successfully inserted into the
         * node. False if any glyph was unable to be inserted into the node.
         */
        public final boolean insert(final List<Glyph> glyphs) {
            for (final Glyph g : glyphs) {
                if (!insert(g)) {
                    return false;
                }
            }
            return true;
        }

        /**
         * Attempts to insert the specified glyph into this node. The glyph will
         * be inserted into this node if this node does not already have a glyph
         * and the glyph fits within the boundaries of this node. If the glyph
         * cannot be inserted into this node, an attempt will be made to insert
         * the glyph into the children of this node. This method returns false
         * if the glyph could not be inserted into this node or into any of its
         * child nodes.
         *
         * @param glyph the glyph to insert.
         * @return true if the glyph was successfully inserted into this node or
         * into one of its child nodes. False if the glyph could not be inserted
         * into this node or any of its child nodes.
         */
        public final boolean insert(final Glyph glyph) {
            if (!hasGlyph()) {
                if ((getWidth() - PADDING) >= (glyph.getWidth() + PADDING) && getHeight() >= (glyph.getHeight() + PADDING)) {
                    // Insert Glyph Into This Node //
                    this.glyph = glyph;

                    // Create Child Nodes //
                    CHILDREN[0] = new GlyphNode(getX() + (glyph.getWidth() + PADDING), getY(), getWidth() - (glyph.getWidth() + PADDING), (glyph.getHeight() + PADDING), PADDING); // Right Node
                    CHILDREN[1] = new GlyphNode(getX(), getY() + (glyph.getHeight() + PADDING), getWidth(), getHeight() - (glyph.getHeight() + PADDING), PADDING); // Bottom Node

                    // Glyph Inserted //
                    return true;
                } else {
                    // Glyph Will Not Fit //
                    return false;
                }
            } else {
                return CHILDREN[0].insert(glyph) ? true : CHILDREN[1].insert(glyph);
            }
        }

        /**
         * Returns a list containing this node and all nodes attached to it.
         *
         * @return the list of nodes.
         */
        public final List<GlyphNode> collect() {
            return collect(new ArrayList<>());
        }

        /**
         * Adds the node and all its child nodes to the specified list.
         *
         * @param nodeList the list to which nodes will be added.
         * @return the modified list.
         */
        public final List<GlyphNode> collect(final List<GlyphNode> nodeList) {
            nodeList.add(this);
            if (CHILDREN[0] != null) {
                CHILDREN[0].collect(nodeList);
            }
            if (CHILDREN[1] != null) {
                CHILDREN[1].collect(nodeList);
            }
            return nodeList;
        }

        /**
         * Returns the x-axis position of the node.
         *
         * @return the x-axis position.
         */
        public final int getX() {
            return xPOS;
        }

        /**
         * Returns the y-axis position of the node.
         *
         * @return the y-axis position.
         */
        public final int getY() {
            return yPOS;
        }

        /**
         * Returns the width of the node.
         *
         * @return the node width.
         */
        public final int getWidth() {
            return WIDTH;
        }

        /**
         * Returns the height of the node.
         *
         * @return the node height.
         */
        public final int getHeight() {
            return HEIGHT;
        }

        /**
         * Returns true if the node has a glyph.
         *
         * @return true if the node has a glyph.
         */
        public final boolean hasGlyph() {
            return glyph != null;
        }

        /**
         * Returns the node glyph.
         *
         * @return the node glyph.
         */
        public final Glyph getGlyph() {
            return glyph;
        }
    }
}
