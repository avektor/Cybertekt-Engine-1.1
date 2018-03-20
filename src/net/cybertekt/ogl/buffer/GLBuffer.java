package net.cybertekt.ogl.buffer;

import net.cybertekt.ogl.GLObject;
import static org.lwjgl.opengl.GL11.GL_BYTE;
import static org.lwjgl.opengl.GL11.GL_DOUBLE;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_INT;
import static org.lwjgl.opengl.GL11.GL_SHORT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_SHORT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_STREAM_DRAW;

/**
 * OpenGL Buffer Object - (C) Cybertekt Software
 *
 * {@link GLObject} that provides the foundation for objects that encapsulate
 * OpenGL buffer objects.
 *
 * @version 1.1.0
 * @since 1.1.0
 * @author Andrew Vektor
 */
public abstract class GLBuffer extends GLObject {

    /**
     * Defines the location, binding point, and component count of a buffer.
     */
    public enum Type {
        /**
         * Vertex Positions - 3 Floats Per Vertex.
         */
        POSITIONS(0, GL_ARRAY_BUFFER, 3, false),
        /**
         * Vertex Texture Coordinates - 2 Floats Per Vertex.
         */
        COORDINATES(1, GL_ARRAY_BUFFER, 2, false),
        /**
         * Vertex Normals - 3 Floats Per Vertex.
         */
        NORMALS(2, GL_ARRAY_BUFFER, 3, false),
        /**
         * Vertex Colors - 3 Floats Per Vertex.
         */
        COLORS(3, GL_ARRAY_BUFFER, 4, false),
        /**
         * Vertex Indices - Unsigned Integer.
         */
        INDICES(0, GL_ELEMENT_ARRAY_BUFFER, 1, false);

        /**
         * The location of the shader attribute.
         */
        protected final int LOCATION;

        /**
         * The buffer binding target.
         */
        protected final int TARGET;

        /**
         * The number of values per vertex.
         */
        protected final int COMPONENTS;

        /**
         * Indicates if data values should be normalized.
         */
        protected final boolean NORMALIZE;

        /**
         * Constructor for buffer type enumerators.
         *
         * @param LOCATION the shader location of the attribute.
         * @param TARGET the buffer binding target.
         * @param COMPONENTS the number of values per vertex.
         * @param NORMALIZE indicates if the attribute values should be
         * normalized.
         */
        Type(final int LOCATION, final int TARGET, final int COMPONENTS, final boolean NORMALIZE) {
            this.LOCATION = LOCATION;
            this.TARGET = TARGET;
            this.COMPONENTS = COMPONENTS;
            this.NORMALIZE = NORMALIZE;
        }
    }

    /**
     * Defines the possible formats of buffer data.
     */
    public enum Format {
        /**
         * 8-Bit Signed Byte.
         */
        BYTE(GL_BYTE, 1),
        /**
         * 8-Bit Unsigned Byte.
         */
        UBYTE(GL_UNSIGNED_BYTE, 1),
        /**
         * 16-Bit Signed Short.
         */
        SHORT(GL_SHORT, 2),
        /**
         * 16-Bit Unsigned Short.
         */
        USHORT(GL_UNSIGNED_SHORT, 2),
        /**
         * 32-Bit Signed Integer.
         */
        INTEGER(GL_INT, 4),
        /**
         * 32-Bit Unsigned Integer.
         */
        UINTEGER(GL_UNSIGNED_INT, 4),
        /**
         * 32-Bit Floating Point Format.
         */
        FLOAT(GL_FLOAT, 4),
        /**
         * 64-Bit Double Floating Point Format.
         */
        DOUBLE(GL_DOUBLE, 8);

        /**
         * The OpenGL Format Identifier.
         */
        public final int ID;

        /**
         * The number of bytes in each value.
         */
        public final int BYTES;

        /**
         * Format Enumeration Constructor.
         *
         * @param ID the OpenGL format identifier constant.
         * @param BYTES the number of bytes in each value.
         */
        Format(final int ID, final int BYTES) {
            this.ID = ID;
            this.BYTES = BYTES;
        }
    }

    /**
     * Defines how a buffer data will be accessed and how often the data will be
     * modified. A usage hint helps OpenGL optimize buffer storage and access
     * but does not place any hard restrictions on how the buffer may be used.
     */
    public enum Usage {
        /**
         * The buffer data will be modified rarely or never.
         */
        STATIC(GL_STATIC_DRAW),
        /**
         * The buffer data will be modified occasionally.
         */
        DYNAMIC(GL_DYNAMIC_DRAW),
        /**
         * The buffer data will be modified often.
         */
        STREAM(GL_STREAM_DRAW);

        /**
         * The OpenGL Usage Hint Identifier.
         */
        public final int ID;

        /**
         * Usage Enumeration Constructor.
         *
         * @param ID the OpenGL usage hint identifier constant.
         */
        Usage(final int ID) {
            this.ID = ID;
        }
    }

    /**
     * Buffer data [@link Type type}.
     */
    protected final Type TYPE;

    /**
     * Buffer data {@link Format format}.
     */
    protected final Format FORMAT;

    /**
     * Buffer {@link Usage usage hint}.
     */
    protected final Usage USAGE;

    /**
     * Number of values stored in the buffer.
     */
    protected int size;

    /**
     * Constructs an OpenGL Buffer Object and indicates the type of data to be
     * stored in the buffer and how the buffer data will be accessed and used by
     * OpenGL.
     *
     * @param TYPE the buffer data {@link Type type}.
     * @param FORMAT the buffer data {@link Format format}.
     * @param USAGE the buffer {@link Usage usage hint}.
     */
    public GLBuffer(final Type TYPE, final Format FORMAT, final Usage USAGE) {
        super(GLObject.Type.Buffer);
        this.TYPE = TYPE;
        this.FORMAT = FORMAT;
        this.USAGE = USAGE;
    }

    /**
     * Prepares the buffer for rendering by initializing its state and binding
     * it to the specified vertex array object. A buffer may be bound to more
     * that one vertex array object. Binding a buffer to a new vertex array will
     * have no affect on previous calls to bind.
     *
     * @param VAO the vertex array object to which the buffer will be bound.
     */
    public abstract void bind(final int VAO);

    /**
     * Unbinds the buffer from the specified vertex array object.
     *
     * @param VAO the vertex array object from which to unbind the buffer.
     */
    public abstract void unbind(final int VAO);

    /**
     * Returns the buffer {@link Type type}.
     *
     * @return the buffer type.
     */
    public final Type getType() {
        return TYPE;
    }

    /**
     * Returns the buffer data {@link Format format}.
     *
     * @return the buffer data format.
     */
    public final Format getFormat() {
        return FORMAT;
    }

    /**
     * Returns the buffer {@link Usage usage hint}.
     *
     * @return the buffer usage hint.
     */
    public final Usage getUsage() {
        return USAGE;
    }

    /**
     * Returns the number of values stored in the buffer.
     *
     * @return the number of values stored in the buffer.
     */
    public final int getSize() {
        return size;
    }

    /**
     * Returns the number of components stored in the buffer.
     *
     * @return the number of components stored in the buffer.
     */
    public final int getCount() {
        return size / TYPE.COMPONENTS;
    }
}