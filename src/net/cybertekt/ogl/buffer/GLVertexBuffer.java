package net.cybertekt.ogl.buffer;

import java.nio.FloatBuffer;
import java.util.List;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL45.glDisableVertexArrayAttrib;
import static org.lwjgl.opengl.GL45.glEnableVertexArrayAttrib;
import static org.lwjgl.opengl.GL45.glVertexArrayAttribBinding;
import static org.lwjgl.opengl.GL45.glVertexArrayAttribFormat;
import static org.lwjgl.opengl.GL45.glVertexArrayVertexBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

/**
 * OpenGL Vertex Buffer - (C) Cybertekt Software
 *
 * {@link GLBuffer} that contains floating point vertex data to be used for
 * rendering. The actual vertex data is stored in a temporary float array until
 * the buffer is {@link #bind(int) bound}, at which point the data will be sent
 * to the GPU. The vertex data may be modified by the user at any time after
 * initialization. Any changes made to the vertex data will not go into effect
 * until the next time the buffer is {@link #bind(int) bound}.
 *
 * @version 1.1.0
 * @since 1.1.0
 * @author Andrew Vektor
 */
public class GLVertexBuffer extends GLBuffer {

    /**
     * Local Vertex Data Storage Array.
     */
    private float[] data;

    /**
     * Constructs an OpenGL vertex buffer of the specified {@link Type type}.
     * The vertex data will be temporarily stored in an internal float array and
     * will be uploaded to the GPU on the next call to {@link #bind(int)}. This
     * constructor applies the default {@link Usage#STATIC} usage hint to the
     * vertex buffer.
     *
     * @param TYPE the {@link Type type} of vertex buffer to create.
     * @param vertexData the vertex data to be stored in the buffer.
     */
    public GLVertexBuffer(final GLBuffer.Type TYPE, final float[] vertexData) {
        this(TYPE, GLBuffer.Usage.STATIC, vertexData);
    }

    /**
     * Constructs an OpenGL vertex buffer of the specified {@link Type type} and
     * applies the specified {@link Usage usage hint}. The vertex data will be
     * temporarily stored in an internal float array and will be uploaded to the
     * GPU on the next call to {@link #bind(int)}.
     *
     * @param TYPE the {@link Type type} of vertex buffer to create.
     * @param USAGE the buffer {@link Usage usage hint}.
     * @param vertexData the vertex data to store in the buffer.
     */
    public GLVertexBuffer(final GLBuffer.Type TYPE, final GLBuffer.Usage USAGE, final float[] vertexData) {
        super(TYPE, GLBuffer.Format.FLOAT, USAGE);
        setData(vertexData);
    }

    /**
     * Constructs an OpenGL vertex buffer of the specified {@link Type type} and
     * applies the specified {@link Usage usage hint}. The vertex data will be
     * temporarily stored in an internal float array to be uploaded to the GPU
     * on the next call to {@link #bind(int)}.
     *
     * @param TYPE the {@link Type type} of vertex buffer to create.
     * @param vertexData the vertex buffer data.
     */
    public GLVertexBuffer(final GLBuffer.Type TYPE, final List<Float> vertexData) {
        this(TYPE, GLBuffer.Usage.STATIC, vertexData);
    }

    /**
     * Constructs an OpenGL vertex buffer of the specified {@link Type type} and
     * applies the specified {@link Usage usage hint}. The vertex data will be
     * temporarily stored in an internal float array to be uploaded to the GPU
     * on the next call to {@link #bind(int)}.
     *
     * @param TYPE the {@link Type type} of vertex buffer to create.
     * @param USAGE the buffer {@link Usage usage hint}.
     * @param vertexData the vertex buffer data.
     */
    public GLVertexBuffer(final GLBuffer.Type TYPE, final GLBuffer.Usage USAGE, final List<Float> vertexData) {
        super(TYPE, GLBuffer.Format.FLOAT, USAGE);
        setData(vertexData);
    }

    /**
     * Constructs an OpenGL vertex buffer of the specified {@link Type type}.
     * The vertex data will be temporarily stored in an internal float array and
     * will be uploaded to the GPU on the next call to {@link #bind(int)}. This
     * constructor applies the default {@link Usage#STATIC} usage hint to the
     * vertex buffer.
     *
     * @param TYPE the {@link Type type} of vertex buffer to create.
     * @param vertexData the vertex data to store in the buffer.
     */
    public GLVertexBuffer(final GLBuffer.Type TYPE, final Vector2f[] vertexData) {
        this(TYPE, GLBuffer.Usage.STATIC, vertexData);
    }

    /**
     * Constructs an OpenGL vertex buffer of the specified {@link Type type} and
     * applies the specified {@link Usage usage hint}. The vertex data will be
     * temporarily stored in an internal float array and will be uploaded to the
     * GPU on the next call to {@link #bind(int)}.
     *
     * @param TYPE the {@link Type type} of vertex buffer to create.
     * @param USAGE the buffer {@link Usage usage hint}.
     * @param vertexData the vertex data to store in the buffer.
     */
    public GLVertexBuffer(final GLBuffer.Type TYPE, final GLBuffer.Usage USAGE, final Vector2f[] vertexData) {
        super(TYPE, GLBuffer.Format.FLOAT, USAGE);
        setData(vertexData);
    }

    /**
     * Constructs an OpenGL vertex buffer of the specified {@link Type type}.
     * The vertex data will be temporarily stored in an internal float array and
     * will be uploaded to the GPU on the next call to {@link #bind(int)}. This
     * constructor applies the default {@link Usage#STATIC} usage hint to the
     * vertex buffer.
     *
     * @param TYPE the {@link Type type} of vertex buffer to create.
     * @param vertexData the vertex data to store in the buffer.
     */
    public GLVertexBuffer(final GLBuffer.Type TYPE, final Vector3f[] vertexData) {
        this(TYPE, GLBuffer.Usage.STATIC, vertexData);
    }

    /**
     * Constructs an OpenGL vertex buffer of the specified {@link Type type} and
     * applies the specified {@link Usage usage hint}. The vertex data will be
     * temporarily stored in an internal float array and will be uploaded to the
     * GPU on the next call to {@link #bind(int)}.
     *
     * @param TYPE the {@link Type type} of vertex buffer to create.
     * @param USAGE the buffer {@link Usage usage hint}.
     * @param vertexData the vertex data to store in the buffer.
     */
    public GLVertexBuffer(final GLBuffer.Type TYPE, final GLBuffer.Usage USAGE, final Vector3f[] vertexData) {
        super(TYPE, GLBuffer.Format.FLOAT, USAGE);
        setData(vertexData);
    }

    /**
     * Constructs an OpenGL vertex buffer of the specified {@link Type type}.
     * The vertex data will be temporarily stored in an internal float array and
     * will be uploaded to the GPU on the next call to {@link #bind(int)}. This
     * constructor applies the default {@link Usage#STATIC} usage hint to the
     * vertex buffer.
     *
     * @param TYPE the {@link Type type} of vertex buffer to create.
     * @param vertexData the vertex data to store in the buffer.
     */
    public GLVertexBuffer(final GLBuffer.Type TYPE, final Vector4f[] vertexData) {
        this(TYPE, GLBuffer.Usage.STATIC, vertexData);
    }

    /**
     * Constructs an OpenGL vertex buffer of the specified {@link Type type} and
     * applies the specified {@link Usage usage hint}. The vertex data will be
     * temporarily stored in an internal float array and will be uploaded to the
     * GPU on the next call to {@link #bind(int)}.
     *
     * @param TYPE the {@link Type type} of vertex buffer to create.
     * @param USAGE the buffer {@link Usage usage hint}.
     * @param vertexData the vertex data to store in the buffer.
     */
    public GLVertexBuffer(final GLBuffer.Type TYPE, final GLBuffer.Usage USAGE, final Vector4f[] vertexData) {
        super(TYPE, GLBuffer.Format.FLOAT, USAGE);
        setData(vertexData);
    }

    /**
     * Updates the vertex buffer contents if needed and binds the buffer to the
     * target vertex array object.
     *
     * @param VAO the vertex array object to which to bind the vertex buffer.
     */
    @Override
    public final void bind(final int VAO) {

        // Upload Buffer Data To GPU //
        if (data != null) {
            // Bind Buffer To Target //
            glBindBuffer(TYPE.TARGET, ID);

            // Upload Buffer Data //
            if (size * FORMAT.BYTES <= MemoryStack.stackGet().getSize()) {
                try (MemoryStack stack = MemoryStack.stackPush()) {
                    glBufferData(TYPE.TARGET, (FloatBuffer) stack.callocFloat(size).put(data).flip(), USAGE.ID);
                }
            } else {
                FloatBuffer buffer = (FloatBuffer) MemoryUtil.memAllocFloat(size).put(data).flip();
                glBufferData(TYPE.TARGET, buffer, USAGE.ID);
                MemoryUtil.memFree(buffer);
            }

            // Unbind Buffer From Target //
            glBindBuffer(TYPE.TARGET, 0);

            // Destroy Local Data Array //
            data = null;
        }

        // Enable Vertex Attribute Array //
        glEnableVertexArrayAttrib(VAO, TYPE.LOCATION);

        // Set Vertex Attribute Format And Buffer Binding Point //
        glVertexArrayAttribFormat(VAO, TYPE.LOCATION, TYPE.COMPONENTS, FORMAT.ID, TYPE.NORMALIZE, 0);
        glVertexArrayAttribBinding(VAO, TYPE.LOCATION, TYPE.LOCATION);

        // Bind Vertex Buffer To VAO //
        glVertexArrayVertexBuffer(VAO, TYPE.LOCATION, ID, 0, FORMAT.BYTES * TYPE.COMPONENTS);
    }

    /**
     * Unbinds the vertex buffer from the target vertex array object.
     *
     * @param VAO the target vertex array object from which to unbind the
     * buffer.
     */
    @Override
    public final void unbind(final int VAO) {
        // Disable Vertex Attribute Array //
        glDisableVertexArrayAttrib(VAO, TYPE.LOCATION);

        // Unbind Vertex Buffer //
        glVertexArrayVertexBuffer(VAO, TYPE.LOCATION, 0, 0, 0);
    }

    /**
     * Sets the vertex data to be stored in the buffer. The data will be
     * uploaded to the GPU on the next call to {@link #bind(int)}.
     *
     * @param vertexData the vertex buffer data.
     */
    public final void setData(final float[] vertexData) {
        data = vertexData;
        size = vertexData.length;
    }

    /**
     * Sets the vertex data to be stored in the buffer. The data will be copied
     * to an internal float array and, on the next call to {@link #bind(int)},
     * will be uploaded to the GPU.
     *
     * @param vertexData the vertex buffer data.
     */
    public final void setData(final List<Float> vertexData) {
        data = new float[size = vertexData.size()];
        for (int i = 0; i < vertexData.size(); i++) {
            data[i] = vertexData.get(i);
        }
    }

    /**
     * Sets the vertex data to be stored in the buffer. The data will be copied
     * to an internal float array and, on the next call to {@link #bind(int)},
     * will be uploaded to the GPU.
     *
     * @param vertexData the vertex buffer data.
     */
    public final void setData(final Vector2f[] vertexData) {
        data = new float[size = vertexData.length * 2];
        for (int i = 0; i < vertexData.length; i++) {
            data[i * 2] = vertexData[i].x;
            data[i * 2 + 1] = vertexData[i].y;
        }
    }

    /**
     * Sets the vertex data to be stored in the buffer. The data will be copied
     * to an internal float array and, on the next call to {@link #bind(int)},
     * will be uploaded to the GPU.
     *
     * @param vertexData the vertex buffer data.
     */
    public final void setData(final Vector3f[] vertexData) {
        data = new float[size = vertexData.length * 3];
        for (int i = 0; i < vertexData.length; i++) {
            data[i * 3] = vertexData[i].x;
            data[i * 3 + 1] = vertexData[i].y;
            data[i * 3 + 2] = vertexData[i].z;
        }
    }

    /**
     * Sets the vertex data to be stored in the buffer. The data will be copied
     * to an internal float array and, on the next call to {@link #bind(int)},
     * will be uploaded to the GPU.
     *
     * @param vertexData the vertex buffer data.
     */
    public final void setData(final Vector4f[] vertexData) {
        data = new float[size = vertexData.length * 4];
        for (int i = 0; i < vertexData.length; i++) {
            data[i * 4] = vertexData[i].x;
            data[i * 4 + 1] = vertexData[i].y;
            data[i * 4 + 2] = vertexData[i].z;
            data[i * 4 + 3] = vertexData[i].w;
        }
    }
}
