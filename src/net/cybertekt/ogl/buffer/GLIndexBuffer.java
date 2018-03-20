package net.cybertekt.ogl.buffer;

import java.nio.IntBuffer;
import java.util.List;
import org.joml.Vector2i;
import org.joml.Vector3i;
import org.joml.Vector4i;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL45.glVertexArrayElementBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

/**
 * OpenGL Index Buffer - (C) Cybertekt Software
 *
 * {@link GLBuffer} that contains indices stored as unsigned integers to be used
 * for rendering. The actual index data is stored in a temporary integer array
 * until the buffer is {@link #bind(int) bound}, at which point the data will be
 * sent to the GPU. The buffer data may be modified by the user at any time
 * after initialization. Any changes made to the buffer data will not go into
 * effect until the next time the buffer is {@link #bind(int) bound}.
 *
 * @version 1.1.0
 * @since 1.1.0
 * @author Andrew Vektor
 */
public class GLIndexBuffer extends GLBuffer {

    /**
     * Local Index Data Storage Array.
     */
    private int[] data;

    /**
     * Creates an OpenGL index buffer object. The index data will be temporarily
     * stored in an internal integer array to be uploaded to the GPU on the next
     * call to {@link #bind(int)}. This constructor applies the default
     * {@link Usage#STATIC} usage hint to the buffer.
     *
     * @param indexData the index data to store in the buffer.
     */
    public GLIndexBuffer(final int[] indexData) {
        this(GLBuffer.Usage.STATIC, indexData);
    }

    /**
     * Creates an OpenGL index buffer object with the {@link Usage usage hint}
     * specified. The index data will be temporarily stored in an internal
     * integer array to be uploaded to the GPU on the next call to
     * {@link #bind(int)}.
     *
     * @param USAGE the buffer {@link Usage usage hint}.
     * @param indexData the index data to store in the buffer.
     */
    public GLIndexBuffer(final GLBuffer.Usage USAGE, final int[] indexData) {
        super(GLBuffer.Type.INDICES, GLBuffer.Format.UINTEGER, USAGE);
        setData(indexData);
    }

    /**
     * Creates an OpenGL index buffer object. The index data will be temporarily
     * stored in an internal integer array to be uploaded to the GPU on the next
     * call to {@link #bind(int)}. This constructor applies the default
     * {@link Usage#STATIC} usage hint to the buffer.
     *
     * @param indexData the index data to store in the buffer.
     */
    public GLIndexBuffer(final List<Integer> indexData) {
        this(GLBuffer.Usage.STATIC, indexData);
    }

    /**
     * Creates an OpenGL index buffer object with the {@link Usage usage hint}
     * specified. The index data will be temporarily stored in an internal
     * integer array to be uploaded to the GPU on the next call to
     * {@link #bind(int)}.
     *
     * @param USAGE the buffer {@link Usage usage hint}.
     * @param indexData the index data to store in the buffer.
     */
    public GLIndexBuffer(final GLBuffer.Usage USAGE, final List<Integer> indexData) {
        super(GLBuffer.Type.INDICES, GLBuffer.Format.UINTEGER, USAGE);
        setData(indexData);
    }

    /**
     * Creates an OpenGL index buffer object. The index data will be temporarily
     * stored in an internal integer array to be uploaded to the GPU on the next
     * call to {@link #bind(int)}. This constructor applies the default
     * {@link Usage#STATIC} usage hint to the buffer.
     *
     * @param indexData the index data to store in the buffer.
     */
    public GLIndexBuffer(final Vector2i[] indexData) {
        this(GLBuffer.Usage.STATIC, indexData);
    }

    /**
     * Creates an OpenGL index buffer object with the {@link Usage usage hint}
     * specified. The index data will be temporarily stored in an internal
     * integer array to be uploaded to the GPU on the next call to
     * {@link #bind(int)}.
     *
     * @param USAGE the buffer {@link Usage usage hint}.
     * @param indexData the index data to store in the buffer.
     */
    public GLIndexBuffer(final GLBuffer.Usage USAGE, final Vector2i[] indexData) {
        super(GLBuffer.Type.INDICES, GLBuffer.Format.UINTEGER, USAGE);
        setData(indexData);
    }

    /**
     * Creates an OpenGL index buffer object. The index data will be temporarily
     * stored in an internal integer array to be uploaded to the GPU on the next
     * call to {@link #bind(int)}. This constructor applies the default
     * {@link Usage#STATIC} usage hint to the buffer.
     *
     * @param indexData the index data to store in the buffer.
     */
    public GLIndexBuffer(final Vector3i[] indexData) {
        this(GLBuffer.Usage.STATIC, indexData);
    }

    /**
     * Creates an OpenGL index buffer object with the {@link Usage usage hint}
     * specified. The index data will be temporarily stored in an internal
     * integer array to be uploaded to the GPU on the next call to
     * {@link #bind(int)}.
     *
     * @param USAGE the buffer {@link Usage usage hint}.
     * @param indexData the index data to store in the buffer.
     */
    public GLIndexBuffer(final GLBuffer.Usage USAGE, final Vector3i[] indexData) {
        super(GLBuffer.Type.INDICES, GLBuffer.Format.UINTEGER, USAGE);
        setData(indexData);
    }

    /**
     * Creates an OpenGL index buffer object. The index data will be temporarily
     * stored in an internal integer array to be uploaded to the GPU on the next
     * call to {@link #bind(int)}. This constructor applies the default
     * {@link Usage#STATIC} usage hint to the buffer.
     *
     * @param indexData the index data to store in the buffer.
     */
    public GLIndexBuffer(final Vector4i[] indexData) {
        this(GLBuffer.Usage.STATIC, indexData);
    }

    /**
     * Creates an OpenGL index buffer object with the {@link Usage usage hint}
     * specified. The index data will be temporarily stored in an internal
     * integer array to be uploaded to the GPU on the next call to
     * {@link #bind(int)}.
     *
     * @param USAGE the buffer {@link Usage usage hint}.
     * @param indexData the index data to store in the buffer.
     */
    public GLIndexBuffer(final GLBuffer.Usage USAGE, final Vector4i[] indexData) {
        super(GLBuffer.Type.INDICES, GLBuffer.Format.UINTEGER, USAGE);
        setData(indexData);
    }

    /**
     * Updates the index buffer contents if needed and binds the buffer to the
     * target vertex array object.
     *
     * @param VAO the vertex array object to which to bind the index buffer.
     */
    @Override
    public final void bind(final int VAO) {

        // Upload Buffer Data To GPU //
        if (data != null) {

            // Bind Buffer To Target //
            glBindBuffer(TYPE.TARGET, ID);

            // Upload Buffer Data //
            if (size * FORMAT.BYTES <= MemoryStack.stackGet().getSize()) {
                // Use Memory Stack If Buffer Size Is Less Than Memory Stack Size //
                try (MemoryStack stack = MemoryStack.stackPush()) {
                    glBufferData(TYPE.TARGET, (IntBuffer) stack.callocInt(size).put(data).flip(), USAGE.ID);
                }
            } else {
                // Use Memory Util If Buffer Size Is Greater Than Memory Stack Size //
                IntBuffer buffer = (IntBuffer) MemoryUtil.memAllocInt(data.length).put(data).flip();
                glBufferData(TYPE.TARGET, buffer, USAGE.ID);
                MemoryUtil.memFree(buffer);
            }

            // Unbind Buffer From Target //
            glBindBuffer(TYPE.TARGET, 0);

            // Destroy Local Data Array //
            data = null;
        }

        // Bind Index Buffer To VAO //
        glVertexArrayElementBuffer(VAO, ID);
    }

    /**
     * Unbinds the index buffer from the target vertex array object.
     *
     * @param VAO the target vertex array object from which to unbind the
     * buffer.
     */
    @Override
    public final void unbind(final int VAO) {
        glVertexArrayElementBuffer(VAO, 0);
    }

    /**
     * Sets the index data to be stored in the buffer. The data will be uploaded
     * to the GPU on the next call to {@link #bind(int)}.
     *
     * @param indexData the index buffer data.
     */
    public final void setData(final int[] indexData) {
        data = indexData;
        size = indexData.length;
    }

    /**
     * Sets the index data to be stored in the buffer. The data will be copied
     * to an internal integer array and, on the next call to {@link #bind(int)},
     * will be uploaded to the GPU.
     *
     * @param indexData the index buffer data.
     */
    public final void setData(final List<Integer> indexData) {
        data = new int[size = indexData.size()];
        for (int i = 0; i < indexData.size(); i++) {
            data[i] = indexData.get(i);
        }
    }

    /**
     * Sets the index data to be stored in the buffer. The data will be copied
     * to an internal integer array and, on the next call to {@link #bind(int)},
     * will be uploaded to the GPU.
     *
     * @param indexData the index buffer data.
     */
    public final void setData(final Vector2i[] indexData) {
        data = new int[size = indexData.length * 2];
        for (int i = 0; i < indexData.length; i++) {
            data[i * 2] = indexData[i].x;
            data[i * 2 + 1] = indexData[i].y;
        }
    }

    /**
     * Sets the index data to be stored in the buffer. The data will be copied
     * to an internal integer array and, on the next call to {@link #bind(int)},
     * will be uploaded to the GPU.
     *
     * @param indexData the index buffer data.
     */
    public final void setData(final Vector3i[] indexData) {
        data = new int[size = indexData.length * 3];
        for (int i = 0; i < indexData.length; i++) {
            data[i * 3] = indexData[i].x;
            data[i * 3 + 1] = indexData[i].y;
            data[i * 3 + 2] = indexData[i].z;
        }
    }

    /**
     * Sets the index data to be stored in the buffer. The data will be copied
     * to an internal integer array and, on the next call to {@link #bind(int)},
     * will be uploaded to the GPU.
     *
     * @param indexData the index buffer data.
     */
    public final void setData(final Vector4i[] indexData) {
        data = new int[size = indexData.length * 4];
        for (int i = 0; i < indexData.length; i++) {
            data[i * 4] = indexData[i].x;
            data[i * 4 + 1] = indexData[i].y;
            data[i * 4 + 2] = indexData[i].z;
            data[i * 4 + 3] = indexData[i].w;
        }
    }
}