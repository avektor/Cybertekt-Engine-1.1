package net.cybertekt.ogl;

import java.util.EnumMap;
import java.util.Map;
import net.cybertekt.render.Renderer;
import net.cybertekt.ogl.shader.GLShader;
import net.cybertekt.ogl.buffer.GLBuffer;
import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;
import static org.lwjgl.opengl.GL11.GL_LINE_STRIP;
import static org.lwjgl.opengl.GL11.GL_POINTS;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_FAN;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL40.GL_PATCHES;

/**
 * OpenGL Mesh - (C) Cybertekt Software
 *
 * {@link GLObject} that contains geometric render data and encapsulates an
 * OpenGL vertex array object. Each mesh maintains an internal map of buffers
 * which are stored based on their {@link GLBuffer.Type buffer type} and used
 * for rendering. The mesh class provides a {@link #bind()} method which binds
 * the vertex array and its associated buffers to the current OpenGL context.
 * The mesh class extends {@link GLObject} and contains multiple OpenGL calls.
 * Therefore, the methods provided by this class should never be called from a
 * thread not associated with an active OpenGL rendering context.
 *
 * @version 1.1.0
 * @since 1.1.0
 * @author Andrew Vektor
 */
public class GLMesh extends GLObject {

    /**
     * Indicates how the mesh vertices will be interpreted and connected.
     */
    public static enum Mode {
        /**
         * Mesh composed of individual points, each defined by a single vertex.
         * The size of each point is determined by the {@link Renderer renderer}
         * or {@link GLShader shader}.
         */
        Points(GL_POINTS),
        /**
         * Mesh composed of unconnected line segments, every two vertices
         * defines a line. If the mesh contains a non-even number of vertices
         * then the extra vertex will be ignored. The mesh will not be rendered
         * if it does not contain at least two vertices.
         */
        Lines(GL_LINES),
        /**
         * Mesh composed of a sequence of connected line segments. A line
         * segment is rendered between the first and second vertices, between
         * the second and third, between the third and fourth, and so on. If a
         * mesh specifies n vertices, n-1 line segments are drawn. The mesh will
         * not be rendered if it does not contain at least two vertices.
         */
        LineStrip(GL_LINE_STRIP),
        /**
         * Mesh composed of a sequence of connected line segments similar to
         * {@link #LineStrip line strip mode} but with a closing line segment
         * between the final and first vertices. The mesh will not be rendered
         * if it does not contain at least two vertices.
         */
        LineLoop(GL_LINE_LOOP),
        /**
         * Mesh composed of individual triangles. A triangle is rendered for
         * each group of three vertices. If the number of vertices is not a
         * multiple of three then any excess vertices are ignored. The mesh will
         * not be rendered if it does not contain at least 3 vertices.
         */
        Triangles(GL_TRIANGLES),
        /**
         * Mesh composed of a sequence of triangles that share edges. A
         * triangles is drawn using the first, second and third vertices, and
         * then another using the second, third and fourth vertices, and so on.
         * The mesh will not be rendered if it does not contain at least 3
         * vertices.
         */
        TriangleStrip(GL_TRIANGLE_STRIP),
        /**
         * Mesh composed of a fan of triangles that share edges and also share a
         * vertex. Each triangle shares the first vertex specified. The mesh
         * will not be rendered if it does not contain at least 3 vertices.
         */
        TriangleFan(GL_TRIANGLE_FAN),
        /**
         * Mesh composed of a user-defined number of vertices which is then
         * tessellated based on the control and evaluation shader into regular
         * points, lines, or triangles. Can only be used when tessellation is
         * active. As with other primitive types that take multiple vertex
         * values, incomplete patches are ignored.
         */
        Patches(GL_PATCHES);

        /**
         * Stores the identifier for the OpenGL constant that defines this mode.
         */
        public final int ID;

        /**
         * Mesh Mode Enumeration Constructor.
         *
         * @param ID the OpenGL constant for this mode.
         */
        Mode(final int ID) {
            this.ID = ID;
        }
    }

    /**
     * Internal private class that contains information about the state of each
     * {@link GLBuffer buffer} bound to a mesh.
     */
    private class BufferInfo {

        /**
         * The mesh buffer.
         */
        private final GLBuffer BUFFER;

        /**
         * Indicates if the buffer has been bound to the mesh vertex array.
         */
        private boolean bound;

        /**
         * Constructs an object for tracking the state of a mesh buffer.
         *
         * @param buffer the mesh buffer to track, must not be null.
         */
        public BufferInfo(final GLBuffer buffer) {
            this.BUFFER = buffer;
        }

        /**
         * Initializes the buffer and binds it to a vertex array object. This
         * method does nothing if the buffer is already bound.
         *
         * @param VAO the vertex array object.
         */
        public final void bind(final int VAO) {
            if (!bound) {
                BUFFER.bind(VAO);
                bound = true;
            }
        }

        /**
         * Unbinds the buffer from a vertex array object. This method does
         * nothing if the buffer is not already bound.
         *
         * @param VAO the vertex array object.
         */
        public final void unbind(final int VAO) {
            if (bound) {
                BUFFER.unbind(VAO);
                bound = false;
            }
        }

        /**
         * Returns the {@link GLBuffer buffer} tracked by this object.
         *
         * @return the mesh buffer.
         */
        public final GLBuffer getBuffer() {
            return BUFFER;
        }
    }

    /**
     * Mesh rendering {@link Mode mode}.
     */
    private Mode mode;

    /**
     * Map of {@link GLBuffer buffers} bound to the mesh.
     */
    private final Map<GLBuffer.Type, BufferInfo> BUFFERS = new EnumMap<>(GLBuffer.Type.class);

    /**
     * Constructs a mesh without any initial {@link GLBuffer buffers}. A mesh
     * created with this constructor will not be renderable until its buffers
     * have been set.
     *
     * @param mode the mesh rendering {@link Mode mode}.
     */
    public GLMesh(final GLMesh.Mode mode) {
        super(GLObject.Type.Mesh);
        this.mode = mode;
    }

    /**
     * Constructs a mesh and sets its initial {@link GLBuffer buffers}.
     *
     * @param mode the mesh rendering {@link Mode mode}.
     * @param buffers the initial mesh {@link GLBuffer buffers}.
     */
    public GLMesh(final GLMesh.Mode mode, final GLBuffer... buffers) {
        super(GLObject.Type.Mesh);
        this.mode = mode;
        for (GLBuffer buffer : buffers) {
            setBuffer(buffer);
        }
    }

    /**
     * Prepares the mesh for rendering by binding it to the current OpenGL
     * context and updating its internal state if necessary.
     */
    public final void bind() {
        // Bind Vertex Array //
        glBindVertexArray(ID);

        // Bind Vertex Buffers //
        for (BufferInfo buffer : BUFFERS.values()) {
            buffer.bind(ID);
        }
    }

    /**
     * Returns the mesh rendering {@link Mode mode}.
     *
     * @return the mesh rendering mode.
     */
    public final Mode getMode() {
        return mode;
    }
    
    /**
     * Sets the mesh rendering {@link Mode mode}.
     * 
     * @param mode the mesh rendering mode.
     */
    public final void setMode(final Mode mode) {
        this.mode = mode;
    }

    /**
     * Binds a buffer to the mesh. A mesh may only have one buffer of each
     * {@link GLBuffer.Type buffer type} bound to it at any given time. If a
     * buffer of the same type is already bound to the mesh, the previous mesh
     * buffer will be unbound and replaced. Buffers may be bound to more than
     * one mesh at a time.
     *
     * @param buffer the buffer to bind to the mesh.
     */
    public final void setBuffer(final GLBuffer buffer) {
        BUFFERS.put(buffer.getType(), new BufferInfo(buffer));
    }

    /**
     * Returns the mesh buffer of the specified {@link GLBuffer.Type type}. If a
     * buffer of the specified type is not currently bound to the mesh, null is
     * returned. Note that a buffer may be bound to more than one mesh at a time
     * and any changes made to the buffer data will have an affect on every mesh
     * to which the buffer is bound.
     *
     * @param type the type of mesh buffer to retrieve.
     * @return a buffer of the specified type, if one is bound to the mesh.
     * Otherwise, null is returned.
     */
    public final GLBuffer getBuffer(final GLBuffer.Type type) {
        return hasBuffer(type) ? BUFFERS.get(type).getBuffer() : null;
    }

    /**
     * Indicates if a buffer of the specified {@link GLBuffer.Type buffer type}
     * is currently bound to the mesh.
     *
     * @param type the mesh buffer type.
     * @return true if a buffer for the specified type is bound to the mesh.
     */
    public final boolean hasBuffer(final GLBuffer.Type type) {
        return BUFFERS.containsKey(type);
    }

    /**
     * Unbinds the buffer of the specified {@link GLBuffer.Type type} from the
     * mesh. No changes are made to the underlying buffer object when calling
     * this method.
     *
     * @param type the type of mesh buffer to clear.
     */
    public final void clearBuffer(final GLBuffer.Type type) {
        if (hasBuffer(type)) {
            getBuffer(type).unbind(ID);
            BUFFERS.remove(type);
        }
    }

    /**
     * Unbinds all buffers currently bound to the mesh.
     */
    public final void clearBuffers() {
        for (BufferInfo buffer : BUFFERS.values()) {
            buffer.unbind(ID);
        }
        BUFFERS.clear();
    }

    /**
     * Returns the number of vertices stored in the mesh
     * {@link GLBuffer.Type#POSITIONS vertex position buffer}.
     *
     * @return the number of mesh vertices.
     */
    public final int getVertexCount() {
        return hasBuffer(GLBuffer.Type.POSITIONS) ? getBuffer(GLBuffer.Type.POSITIONS).getCount() : 0;
    }

    /**
     * Returns the number of indices stored in the mesh
     * {@link GLBuffer.Type#INDICES index buffer}.
     *
     * @return the number of mesh indices.
     */
    public final int getIndexCount() {
        return hasBuffer(GLBuffer.Type.INDICES) ? getBuffer(GLBuffer.Type.INDICES).getCount() : 0;
    }
}
