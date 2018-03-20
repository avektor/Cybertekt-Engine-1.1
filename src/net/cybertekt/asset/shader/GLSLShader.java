package net.cybertekt.asset.shader;

import net.cybertekt.asset.Asset;
import net.cybertekt.asset.AssetKey;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;
import static org.lwjgl.opengl.GL43.GL_COMPUTE_SHADER;

/**
 * GLSLShader - (C) Cybertekt Software
 *
 * Immutable {@link Asset asset} that contains the source code for a GLSL
 * shader.
 *
 * @version 1.1.0
 * @since 1.1.0
 * @author Andrew Vektor
 */
public class GLSLShader extends Asset {

    /**
     * Wraps the LWJGL constants that identify each shader type.
     */
    public enum Type {
        /**
         * Vertex Shader Type.
         */
        Vertex(GL_VERTEX_SHADER),
        /**
         * Fragment Shader Type.
         */
        Fragment(GL_FRAGMENT_SHADER),
        /**
         * Geometry Shader Type.
         */
        Geometry(GL_GEOMETRY_SHADER),
        /**
         * Compute Shader Type.
         */
        Compute(GL_COMPUTE_SHADER);

        /**
         * Index that maps to each types corresponding LWJGL constant.
         */
        private final int ID;

        /**
         * Constructs a new shader type with an integer constant that identifies
         * the shader type.
         *
         * @param id the value of the LWJGL constant that identifies the shader
         * type.
         */
        Type(final int id) {
            this.ID = id;
        }

        /**
         * Returns the value of the LWJGL constant that identifies the shader
         * type.
         *
         * @return the value of the LWJGL constant that corresponds to this
         * shader type.
         */
        public final int getId() {
            return ID;
        }
    }

    /**
     * The GLSL shader {@link Type type}.
     */
    private final Type TYPE;

    /**
     * The GLSL shader source code.
     */
    private final String SOURCE;

    /**
     * Constructs a GLSL shader asset.
     *
     * @param key the {@link AssetKey key} that points to the location of the
     * external GLSL shader file.
     * @param type the GLSL shader {@link Type type}.
     * @param source the GLSL shader source code.
     */
    public GLSLShader(final AssetKey key, final Type type, final String source) {
        super(key);
        this.TYPE = type;
        this.SOURCE = source;
    }

    /**
     * Returns the GLSL shader {@link Type type}.
     *
     * @return the GLSL shader {@link Type}.
     */
    public final Type getType() {
        return TYPE;
    }

    /**
     * Returns the GLSL shader source code.
     *
     * @return the GLSL shader source code.
     */
    public final String getSource() {
        return SOURCE;
    }

    /**
     * Overridden to return the name of the shader.
     *
     * @return the name of the shader.
     */
    @Override
    public final String toString() {
        return getKey().getName(false);
    }

}
