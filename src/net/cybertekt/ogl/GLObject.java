package net.cybertekt.ogl;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;
import static org.lwjgl.opengl.ARBVertexArrayObject.glDeleteVertexArrays;
import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OpenGL Object - (C) Cybertekt Software
 *
 * Provides the foundation for classes that encapsulate an OpenGL object. Every
 * GLObject is defined by its {@link Type OpenGL object type} and the identifier
 * assigned to it by OpenGL during construction. This class creates and stores a
 * weak reference to every GLObject so that it may be properly destroyed when
 * the object is no longer in use. GLObjects must only be constructed and used
 * in a thread that has an active OpenGL context. A GLObject is only valid from
 * within the OpenGL context in which it was constructed.
 *
 * @version 1.1.0
 * @since 1.1.0
 * @author Andrew Vektor
 */
public abstract class GLObject {

    /**
     * Static class logger for debugging.
     */
    private static final Logger LOG = LoggerFactory.getLogger(GLObject.class);

    /**
     * Defines each unique type of OpenGL object.
     */
    public static enum Type {
        /**
         * OpenGL Buffer Object.
         */
        Buffer,
        /**
         * OpenGL Vertex Array Object.
         */
        Mesh,
        /**
         * OpenGL Shader Program.
         */
        Shader,
        /**
         * OpenGL Texture Object.
         */
        Texture;
    }

    /**
     * List of remaining GLObject weak references.
     */
    private static final List<GLObjectReference> glObjects = new LinkedList<>();

    /**
     * Queue into which GLObject references are enqueued when their associated
     * referent has gone out of scope. Used to delete OpenGL objects when they
     * are no longer in use.
     */
    private static final ReferenceQueue<GLObject> glObjectQueue = new ReferenceQueue<>();

    /**
     * Polls the GLObject reference queue and deletes any OpenGL object
     * associated with a GLObject that has gone out of scope.
     */
    public static final void clean() {
        for (Reference<? extends GLObject> ref = glObjectQueue.poll(); ref != null; ref = glObjectQueue.poll()) {
            delete((GLObjectReference) ref);
        }
    }

    /**
     * Returns the total number of remaining OpenGL objects.
     *
     * @return the number of remaining OpenGL objects.
     */
    public static final int count() {
        return glObjects.size();
    }

    /**
     * Returns the total number of remaining OpenGL objects of the specified
     * {@link Type OpenGL object type}.
     *
     * @param type the OpenGL object type.
     * @return the number of remaining OpenGL objects of the specified type.
     */
    public static final int count(final Type type) {
        return (int) glObjects.stream().filter(ref -> ref.TYPE == type).count();
    }

    /**
     * Deletes the OpenGL object associated with the GLObjectReference provided.
     *
     * @param reference the GLObjectReference of the OpenGL object to delete.
     */
    private static void delete(final GLObjectReference reference) {
        delete(reference.ID, reference.TYPE);
        glObjects.remove(reference);
    }

    /**
     * Deletes the OpenGL object defined by the specified identifier and OpenGL
     * object type.
     *
     * @param ID the identifier of the OpenGL object to delete.
     * @param TYPE the type of OpenGL object to delete.
     */
    private static void delete(final int ID, final Type TYPE) {
        switch (TYPE) {
            case Mesh: {
                // Delete GL Vertex Array Object //
                glDeleteVertexArrays(ID);
                LOG.info("Mesh [{}] Destroyed!", ID);
                break;
            }
            case Buffer: {
                // Delete GL Buffer Object //
                glDeleteBuffers(ID);
                LOG.info("Buffer [{}] Destroyed!", ID);
                break;
            }
            case Shader: {
                // Delete GL Shader Program //
                glDeleteShader(ID);
                LOG.info("Shader [{}] Destroyed!", ID);
                break;
            }
            case Texture: {
                // Delete GL Texture Object //
                glDeleteTextures(ID);
                LOG.info("Texture [{}] Destroyed!", ID);
                break;
            }
            default: {
                throw new IllegalArgumentException("Unable to destroy GLObject [" + ID + "] - Invalid OpenGL Object Type: " + TYPE);
            }
        }
    }

    /**
     * The OpenGL identifier assigned to the object during construction.
     */
    protected final int ID;

    /**
     * The type of OpenGL object encapsulated by the GLObject.
     */
    private final Type TYPE;

    /**
     * Constructs a GLObject and assigns its OpenGL identifier based on the
     * {@link Type OpenGL object type} that it encapsulates. GLObjects must only
     * be constructed in a thread that has an active OpenGL context. The OpenGL
     * identifier assigned to the GLObject is only valid within the OpenGL
     * context in which it was created.
     *
     * @param type the type of OpenGL object encapsulated by the GLObject.
     */
    protected GLObject(final Type type) {
        switch (type) {
            case Buffer: {
                ID = glGenBuffers();
                break;
            }
            case Mesh: {
                ID = glGenVertexArrays();
                break;
            }
            case Shader: {
                ID = glCreateProgram();
                break;
            }
            case Texture: {
                ID = glGenTextures();
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid GLObject Type: " + type);
            }
        }
        this.TYPE = type;
        glObjects.add(new GLObjectReference(this, glObjectQueue));
    }

    /**
     * GLObject Weak Reference - (C) Cybertekt Software
     *
     * Stores a weak reference to a GLObject along with the {@link #ID id} and
     * {@link #TYPE type} of OpenGL object that it encapsulates. Weak references
     * are used here because they are enqueued earlier than phantom references.
     */
    private static class GLObjectReference extends WeakReference<GLObject> {

        /**
         * The identifier assigned to the referent by OpenGL.
         */
        private final int ID;

        /**
         * The type of OpenGL object encapsulated by the referent.
         */
        private final Type TYPE;

        /**
         * Constructs a weak reference to the provided GLObject and adds it to
         * the reference queue provided.
         *
         * @param referent the GLObject referent.
         * @param queue the GLObject reference queue.
         */
        public GLObjectReference(final GLObject referent, final ReferenceQueue queue) {
            super(referent, queue);
            this.ID = referent.ID;
            this.TYPE = referent.TYPE;
        }
    }
}