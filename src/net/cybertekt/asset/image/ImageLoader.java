package net.cybertekt.asset.image;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import net.cybertekt.asset.AssetKey;
import net.cybertekt.asset.AssetLoader;
import net.cybertekt.asset.AssetManager;
import net.cybertekt.asset.AssetManager.AssetInitializationException;
import net.cybertekt.asset.AssetTask;
import net.cybertekt.asset.AssetType;
import org.lwjgl.BufferUtils;

/**
 * Image Loader - (C) Cybertekt Software.
 *
 * Loader for constructing {@link Image image assets}. Currently, this loader
 * only supports the PNG image file format.
 *
 * @version 1.1.0
 * @since 1.1.0
 * @author Andrew Vektor
 */
public class ImageLoader extends AssetLoader {

    /**
     * {@link AssetType Asset type} for Portable Network Graphics (PNG) files.
     */
    public static final AssetType PNG = AssetType.getType("PNG");

    /**
     * Initializes the list of {@link AssetType asset types} supported by this
     * {@link AssetLoader asset loader}.
     */
    public ImageLoader() {
        SUPPORTED.add(PNG);
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
        if (key.getType().equals(PNG)) {
            return new PNGLoader(key, stream);
        }
        throw new UnsupportedOperationException("Unsupported Image File Type: " + key.getType().toString());
    }

    /**
     * PNG Image Loader - (C) Cybertekt Software
     *
     * Callable {@link AssetTask task} for constructing an {@link Image image}
     * using data from the {@link InputStream input stream} of an external PNG
     * file. This class does not support loading images with a bit depth less
     * than 8, nor does it support interlaced, indexed, or grayscale images
     * (these types of images are less than optimal and therefore should not be
     * used).
     *
     * @see https://www.w3.org/TR/2003/REC-PNG-20031110/ for specification.
     * @see http://www.schaik.com/pngsuite/ for test suite.
     * @version 1.1.0
     * @since 1.1.0
     * @author Andrew Vektor
     */
    private class PNGLoader extends AssetTask {

        /**
         * PNG File Signature.
         */
        private final byte[] PNG = {(byte) 137, 80, 78, 71, 13, 10, 26, 10};

        /**
         * Cyclic redundancy check to verify the file is not corrupted.
         */
        private final CRC32 CRC32 = new CRC32();

        /**
         * PNG Image Header Chunk Signature.
         */
        private static final int IHDR = 0x49484452;

        /**
         * PNG Color Pallette Chunk Signature.
         */
        private static final int PLTE = 0x504C5445;

        /**
         * PNG Image Data Chunk Signature.
         */
        private static final int IDAT = 0x49444154;

        /**
         * PNG Image End Chunk Signature.
         */
        private static final int IEND = 0x49454E44;

        /**
         * Constructs a task for loading a PNG [@link Image image} asset using
         * the file located at the path specified by the
         * {@link AssetKey asset key}.
         *
         * @param KEY the asset key for the PNG file to load.
         * @param INPUT the input stream for the file located at the path
         * specified by the asset key.
         */
        public PNGLoader(final AssetKey KEY, final InputStream INPUT) {
            super(KEY, INPUT);
        }

        /**
         * Attempts to create an {@link Image image} using the data from the PNG
         * {@link InputStream input stream} provided during construction.
         *
         * @return the {@link Image image} creating using the data from the
         * input stream provided to this object during construction.
         * @throws AssetInitializationException if an image could not be created
         * from the input stream for any reason.
         */
        @Override
        public final Image load() throws AssetInitializationException {
            try {
                // Validate PNG File Signature //
                validateSignature(readBytes(8));

                // Read PNG Data Chunks //
                List<Chunk> chunkList = readChunks();

                // Ensure Chunk List Was Populated //
                if (chunkList.isEmpty()) {
                    throw new IOException("Missing Image Data");
                }

                // Process Header Chunk //
                Chunk header = chunkList.get(0);
                if (header.TYPE != IHDR) {
                    throw new IOException("Missing Header");
                }

                // Retrieve Image Size //
                final int WIDTH = header.getInt(0);
                final int HEIGHT = header.getInt(4);

                // Validate Image Size //
                if (WIDTH <= 0 || HEIGHT <= 0) {
                    throw new IOException("Zero Image Size");
                }

                // Retrieve Image Bit Depth //
                final int BIT_DEPTH = header.getByte(8) & 255;

                // Retrieve Image Color Type //
                final int COLOR_TYPE = header.getByte(9) & 255;

                // Validate Compression Method (Must Be Zero) //
                if (header.getByte(10) != 0) {
                    throw new IOException("Invalid Compression Method");
                }

                // Validate Filtering Method (Must Be Zero) //
                if (header.getByte(11) != 0) {
                    throw new IOException("Invalid Filtering Method");
                }

                // Validate Interlacing Mode (Interlacing Not Supported) //
                if (header.getByte(12) != 0) {
                    throw new IOException("Unsupported Image Type: Interlaced");
                }

                // Create Appropriate Image Format //
                final Image.Format FORMAT = getFormat(COLOR_TYPE, BIT_DEPTH);

                // Create Image Surface Data Buffer //
                final ByteBuffer DATA = BufferUtils.createByteBuffer(WIDTH * HEIGHT * FORMAT.BPP);

                // Create Inflater Used For Decompression //
                final Inflater INFLATER = new Inflater();

                for (final Chunk CHUNK : chunkList) {
                    if (CHUNK.TYPE == IDAT) {
                        // Process Image Data Chunk Based On PNG Color Type //
                        switch (COLOR_TYPE) {
                            case 2: {
                                // Calculate Scanline Size //
                                int length = WIDTH * FORMAT.BPP;

                                // Stores The Current Scanline //
                                byte[] current = new byte[length + 1];

                                // Stores The Last Scanline Processed (Required For Filtering) //
                                byte[] last = new byte[length + 1];

                                // Set Decompression Data //
                                INFLATER.setInput(CHUNK.DATA);

                                // Decompress And Process Image Data //
                                while (!INFLATER.finished()) {
                                    try {
                                        INFLATER.inflate(current);
                                    } catch (DataFormatException e) {
                                        throw new IOException("Decompression Failed: " + e.getMessage());
                                    }
                                    DATA.put(unfilter(current, last, FORMAT.BPP), 1, current.length - 1);
                                    last = Arrays.copyOf(current, current.length);
                                }
                                break;
                            }
                            case 6: {
                                // Calculate Scanline Size //
                                int length = WIDTH * FORMAT.BPP;

                                // Stores The Current Scanline //
                                byte[] current = new byte[length + 1];

                                // Stores The Last Scanline Processed (Required For Filtering) //
                                byte[] last = new byte[length + 1];

                                // Inflater For Image Decompression //
                                INFLATER.setInput(CHUNK.DATA);

                                // Decompress And Process Image Data //
                                while (!INFLATER.finished()) {
                                    try {
                                        INFLATER.inflate(current);
                                    } catch (DataFormatException e) {
                                        throw new IOException("Decompression Failed: " + e.getMessage());
                                    }
                                    DATA.put(unfilter(current, last, FORMAT.BPP), 1, current.length - 1);
                                    last = Arrays.copyOf(current, current.length);
                                }
                                break;
                            }
                        }
                    }
                }
                DATA.flip();
                return new Image(KEY, FORMAT, WIDTH, HEIGHT, DATA);
            } catch (IOException e) {
                throw new AssetManager.AssetInitializationException(KEY, "Image file is invalid or corrupt (" + e.getMessage() + ")");
            }
        }

        /**
         * Indicates if the provided signature is a valid PNG file signature.
         *
         * @param signature the byte signature to validate.
         * @throws IOException if the provided signature is not a valid PNG file
         * signature.
         */
        private void validateSignature(byte[] signature) throws IOException {
            if (!Arrays.equals(PNG, signature)) {
                throw new IOException("Invalid Signature");
            }
        }

        /**
         * Returns the appropriate {@link Image.Format image format} for the
         * color type and bit depth provided.
         *
         * @param COLOR_TYPE the image color type.
         * @param BIT_DEPTH the image bit depth.
         * @return the {@link Image.Format image format} for the color type and
         * bit depth provided.
         * @throws IOException if the provided color type and/or bit depth is
         * unsupported.
         */
        private Image.Format getFormat(final int COLOR_TYPE, final int BIT_DEPTH) throws IOException {
            switch (COLOR_TYPE) {

                // Grayscale (No Alpha) Color Type //
                case 0: {
                    throw new IOException("Unsupported Image Type: Grayscale");
                }

                // Colored (No Alpha) Color Type //
                case 2: {
                    switch (BIT_DEPTH) {
                        case 8:
                            return Image.Format.RGB8;
                        case 16:
                            return Image.Format.RGB16;
                        default:
                            throw new IOException("Unsupported Bit Depth: " + BIT_DEPTH);
                    }
                }

                // Indexed Color Type //
                case 3: {
                    throw new IOException("Unsupported Image Type: Indexed");
                }

                // Grayscale (With Alpha) Color Type //
                case 4: {
                    throw new IOException("Unsupported Image Type: Grayscale");
                }

                // Colored (With Alpha) Color Type //
                case 6: {
                    switch (BIT_DEPTH) {
                        case 8:
                            return Image.Format.RGBA8;
                        case 16:
                            return Image.Format.RGBA16;
                        default:
                            throw new IOException("Unsupported Bit Depth: " + BIT_DEPTH);
                    }
                }
                // Unknown/Invalid Color Type //
                default: {
                    throw new IOException("Invalid Image Color Type: " + COLOR_TYPE);
                }
            }
        }

        /**
         * Returns a list of PNG chunks from the {@link #INPUT input stream}.
         * The IEND chunk is not added to the list as it contains no data.
         *
         * @return the list of PNG chunks read from the input stream.
         * @throws IOException if the PNG chunks cannot be read for any reason.
         */
        private List<Chunk> readChunks() throws IOException {
            List<Chunk> chunkList = new ArrayList<>();

            int length = readInt();
            Chunk chunk = new Chunk(length, readBytes(4), readBytes(length), readInt());

            while (chunk.TYPE != IEND) {
                chunkList.add(chunk);
                chunk = new Chunk(length = readInt(), readBytes(4), readBytes(length), readInt());
            }

            return chunkList;
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

        /**
         * Unfilters a scanline based on the filter method indicated by the
         * first byte in the line.
         *
         * @param line the scanline to unfilter.
         * @param last the previous scanline, if any.
         * @param bpp the number of bytes per pixel.
         * @return the unfiltered data.
         */
        private byte[] unfilter(byte[] line, byte[] last, int bpp) {
            switch (line[0]) {
                case 1:
                    return unfilterSub(line, bpp);
                case 2:
                    return unfilterUp(line, last);
                case 3:
                    return unfilterAverage(line, last, bpp);
                case 4:
                    return unfilterPaeth(line, last, bpp);
            }
            return line;
        }

        /**
         * Each byte is replaced with the difference between it and the
         * corresponding byte to its left.
         *
         * @param line the line to unfilter.
         * @param bpp number of bytes per pixel.
         */
        private byte[] unfilterSub(byte[] line, int bpp) {
            for (int i = bpp + 1; i < line.length; i++) {
                line[i] += line[i - bpp];
            }
            return line;
        }

        /**
         * Each byte is replaced with the difference between it and the byte
         * above it (in the previous row, as it was before filtering).
         *
         * @param line the line to unfilter.
         * @param last the previous line.
         */
        private byte[] unfilterUp(byte[] line, byte[] last) {
            for (int i = 1; i < line.length; i++) {
                line[i] += last[i];
            }
            return line;
        }

        /**
         * Each byte is replaced with the difference between it and the average
         * of the corresponding bytes to its left and above it, truncating any
         * fractional part.
         *
         * @param curLine the line to unfilter.
         * @param prevLine the previous line.
         * @param bpp number of bytes per pixel.
         */
        private byte[] unfilterAverage(byte[] line, byte[] last, int bpp) {
            for (int i = 1; i <= bpp; i++) {
                line[i] += ((last[i] & 0xFF) >>> 1);
            }
            for (int i = bpp + 1; i < line.length; i++) {
                line[i] += (((line[i - bpp] & 0xFF) + (last[i] & 0xFF)) >>> 1);
            }
            return line;
        }

        /**
         * Each byte is replaced with the difference between it and the Paeth
         * predictor of the corresponding bytes to its left, above it, and to
         * its upper left.
         *
         * @param line the line to unfilter.
         * @param last the previous line.
         * @param bpp number of bytes per pixel.
         */
        private byte[] unfilterPaeth(byte[] line, byte[] last, int bpp) {
            for (int i = 1; i <= bpp; i++) {
                line[i] += last[i];
            }
            for (int i = bpp + 1; i < line.length; i++) {
                int a = line[i - bpp] & 255;
                int b = last[i] & 255;
                int c = last[i - bpp] & 255;
                int p = a + b - c;
                int pa = p - a;
                if (pa < 0) {
                    pa = -pa;
                }
                int pb = p - b;
                if (pb < 0) {
                    pb = -pb;
                }
                int pc = p - c;
                if (pc < 0) {
                    pc = -pc;
                }
                if (pa <= pb && pa <= pc) {
                    c = a;
                } else if (pb <= pc) {
                    c = b;
                }
                line[i] += (byte) c;
            }
            return line;
        }

        /**
         * Encapsulates the data for a PNG chunk.
         *
         * @see http://www.libpng.org/pub/png/spec/1.2/PNG-Chunks.html
         */
        private class Chunk {

            /**
             * The PNG Chunk {@link ChunkType type}.
             */
            private final int TYPE;

            /**
             * The PNG Chunk Data.
             */
            private final byte[] DATA;

            /**
             * Constructs a PNG Data Chunk.
             *
             * @param length the length of the chunk data.
             * @param type the chunk type signature.
             * @param data the chunk data.
             * @param crc the chunk CRC code.
             * @throws IOException if the chunk type signature is invalid or if
             * the chunk CRC check fails.
             */
            public Chunk(final int length, final byte[] type, final byte[] data, final int crc) throws IOException {

                // Validate Chunk Signature //
                if (type.length != 4) {
                    throw new IOException("Invalid Chunk Signature");
                }

                // Set Chunk Fields //
                TYPE = ((type[0]) << 24) | ((type[1] & 255) << 16) | ((type[2] & 255) << 8) | ((type[3] & 255));
                DATA = data;

                // Validate Chunk CRC //
                CRC32.reset();
                CRC32.update(type);
                CRC32.update(data);
                if (crc != (int) CRC32.getValue()) {
                    throw new IOException("CRC Check Failure");
                }
            }

            /**
             * Returns the integer at the specified location within the chunk
             * data array.
             *
             * @param location the location within the chunk data array from
             * which to retrieve an integer.
             * @return the integer at the specified location within the chunk
             * data array.
             */
            public int getInt(int location) {
                return ((DATA[location]) << 24) | ((DATA[location + 1] & 255) << 16) | ((DATA[location + 2] & 255) << 8) | ((DATA[location + 3] & 255));
            }

            /**
             * Returns the byte at the specified location within the chunk data
             * array.
             *
             * @param location the location within the chunk data array from
             * which to retrieve the byte.
             * @return the byte at the specified location within the chunk data
             * array.
             */
            public byte getByte(final int location) {
                return DATA[location];
            }

        }
    }
}
