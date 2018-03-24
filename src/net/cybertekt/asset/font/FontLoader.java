package net.cybertekt.asset.font;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;
import net.cybertekt.asset.AssetKey;
import net.cybertekt.asset.AssetLoader;
import net.cybertekt.asset.AssetManager;
import net.cybertekt.asset.AssetManager.AssetInitializationException;
import net.cybertekt.asset.AssetTask;
import net.cybertekt.asset.AssetType;
import net.cybertekt.asset.image.Image;
import org.lwjgl.BufferUtils;

/**
 * Font Loader - (C) Cybertekt Software
 *
 * Loader for constructing {@link Font font} assets. Currently, this loader only
 * supports the CTF font file format.
 *
 * @version 1.1.0
 * @since 1.1.0
 * @author Andrew Vektor
 */
public class FontLoader extends AssetLoader {

    /**
     * {@link AssetType Asset type} for Cybertekt Font (CTF) files.
     */
    public static final AssetType CTF = AssetType.getType("CTF");

    /**
     * Initializes the list of {@link AssetType asset types} supported by this
     * {@link AssetLoader asset loader}.
     */
    public FontLoader() {
        SUPPORTED.add(CTF);
    }

    /**
     * Returns a callable {@link AssetTask loading task} for constructing an
     * {@link Image image asset} from the {@link InputStream input stream} of
     * the image file at the path specified by the {@link AssetKey asset key}.
     *
     * @param key the {@link AssetKey key} associated with the image file.
     * @param stream the {@link InputStream input stream} of the image file.
     * @return the callable task for constructing the {@link Image image asset}.
     */
    @Override
    public AssetTask newTask(final AssetKey key, final InputStream stream) {
        if (key.getType().equals(CTF)) {
            return new CTFLoader(key, stream);
        }
        throw new UnsupportedOperationException("Unsupported Font File Type: " + key.getType().toString());
    }

    /**
     * Cybertekt Font Loader - (C) Cybertekt Software.
     */
    private class CTFLoader extends AssetTask {

        /**
         * CTF File Signature.
         */
        private static final int CTF = 0x846870; //84-68-70 (CTF)

        /**
         * CTF File Header.
         */
        private static final int HDR = 0x726882; //72-68-82 (HDR)

        /**
         * CTF Character Information.
         */
        private static final int CHR = 0x677282; //67-72-82 (CHR)

        /**
         * CTF Kerning Information.
         */
        private static final int KRN = 0x758278; //75-82-78 (KRN)

        /**
         * CTF Image Data.
         */
        private static final int IMG = 0x737771; //73-77-71 (IMG)

        /**
         * CTF File Footer.
         */
        private static final int FTR = 0x708482; //70-84-82 (FTR)

        public CTFLoader(final AssetKey KEY, final InputStream INPUT) {
            super(KEY, INPUT);
        }

        @Override
        public final Font load() throws AssetInitializationException {
            try {
                // Validate CTF File Signature //
                if (readInt() != CTF) {
                    throw new IOException("Invalid File Signature");
                }

                // Read CTF Header Data //
                final int SIZE, WIDTH, HEIGHT, LINE, SPACE, COUNT, KERNS;
                if (readInt() == HDR) {
                    int[] info = readInts(7);
                    SIZE = info[0]; // Render Size
                    WIDTH = info[1]; // Image Width
                    HEIGHT = info[2]; // Image Height
                    LINE = info[3]; // Line Height
                    SPACE = info[4]; // Space Width
                    COUNT = info[5]; // Glyph Count
                    KERNS = info[6]; // Kernings Count
                } else {
                    throw new IOException("Invalid File Header");
                }

                // Read CTF Character Data //
                final Map<Integer, Font.Glyph> GLYPHS = new HashMap<>(COUNT);
                if (readInt() == CHR) {
                    for (int i = 0; i < COUNT; i++) {
                        int[] info = readInts(8);
                        GLYPHS.put(info[0], new Font.Glyph(info[0], info[1], info[2], info[3], info[4], info[5], info[6], info[7]));
                    }
                } else {
                    throw new IOException("Missing Glyph Data");
                }

                // Read CTF Kerning Data //
                if (readInt() == KRN) {
                    for (int i = 0; i < KERNS; i++) {
                        int[] info = readInts(3);
                        GLYPHS.get(info[1]).addKerning(info[0], info[2]);
                    }
                } else {
                    throw new IOException("Missing Kerning Data");
                }

                // Read CTF Image Data //
                final ByteBuffer DATA = BufferUtils.createByteBuffer(WIDTH * HEIGHT * 8).order(ByteOrder.nativeOrder());
                if (readInt() == IMG) {
                    DATA.asIntBuffer().put(readInts(WIDTH * HEIGHT));
                    DATA.flip();
                } else {
                    throw new IOException("Missing Image Data");
                }

                // Read CTF File Footer //
                if (readInt() != FTR) {
                    throw new IOException("Invalid File Footer");
                }

                // Create CTF Font //
                return new Font(KEY, SIZE, WIDTH, HEIGHT, LINE, SPACE, DATA, GLYPHS);
            } catch (IOException e) {
                throw new AssetManager.AssetInitializationException(KEY, "Font file is invalid or corrupt (" + e.getMessage() + ")");
            }
        }

        /**
         * Reads the next integer from the input stream.
         *
         * @param INPUT the input stream from which to read.
         * @return the integer read from the input stream.
         * @throws IOException if the end of the input stream is reached
         * unexpectedly while reading.
         */
        private int readInt() throws IOException {
            byte[] bytes = readBytes(4);
            return ((bytes[0]) << 24) | ((bytes[1] & 255) << 16) | ((bytes[2] & 255) << 8) | ((bytes[3] & 255));
        }

        /**
         * Reads a number of consecutive integers from the input stream.
         *
         * @param count the number of integers to read.
         * @return the integers read from the input stream.
         * @throws IOException if the end of the input stream is reached
         * unexpectedly while reading.
         */
        private int[] readInts(final int count) throws IOException {
            int[] ints = new int[count];
            byte[] bytes = readBytes(4 * count);
            for (int i = 0; i < ints.length; i++) {
                ints[i] = ((bytes[i * 4]) << 24) | ((bytes[i * 4 + 1] & 255) << 16) | ((bytes[i * 4 + 2] & 255) << 8) | ((bytes[i * 4 + 3] & 255));
            }
            return ints;
        }

        /**
         * Reads a number of bytes from the {@link #INPUT input stream}.
         *
         * @param count the number of bytes to read.
         * @return the array containing the bytes read.
         * @throws IOException if the end of the input stream is reached
         * unexpectedly while reading.
         */
        private byte[] readBytes(final int count) throws IOException {
            byte[] bytes = new byte[count];
            if (INPUT.read(bytes) > -1) {
                return bytes;
            } else {
                throw new EOFException("Reached End Of File Unexpectedly");
            }
        }
    }

}
