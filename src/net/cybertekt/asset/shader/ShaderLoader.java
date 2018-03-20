package net.cybertekt.asset.shader;

import java.io.InputStream;
import net.cybertekt.asset.AssetKey;
import net.cybertekt.asset.AssetLoader;
import net.cybertekt.asset.AssetTask;
import net.cybertekt.asset.AssetType;

/**
 * Shader Loader - (C) Cybertekt Software.
 *
 * Asset loader used for constructing four different types of
 * {@link GLSLShader GLSL shaders}.
 *
 * @author Andrew Vektor
 * @version 1.1.0
 * @since 1.1.0
 */
public class ShaderLoader extends AssetLoader {

    /**
     * GLSL Vertex Shader {@link AssetType asset type}.
     */
    private final AssetType VERT = AssetType.getType("VERT");

    /**
     * GLSL Fragment Shader {@link AssetType asset type}.
     */
    private final AssetType FRAG = AssetType.getType("FRAG");

    /**
     * GLSL Geometry Shader {@link AssetType asset type}.
     */
    private final AssetType GEOM = AssetType.getType("GEOM");

    /**
     * GLSL Compute Shader (@link AssetType asset type}.
     */
    private final AssetType COMP = AssetType.getType("COMP");

    /**
     * Initializes the list of {@link AssetType asset types} supported by this
     * class.
     */
    public ShaderLoader() {
        SUPPORTED.add(VERT);
        SUPPORTED.add(FRAG);
        SUPPORTED.add(GEOM);
        SUPPORTED.add(COMP);
    }

    /**
     * Returns a task for constructing a {@link GLSLShader shader} using the
     * input stream for the file located at the path specified by the asset key.
     *
     * @param key the asset key for the GLSL shader.
     * @param stream the input stream for the file located at the path specified
     * by the asset key.
     * @return the asset task for constructing the shader.
     */
    @Override
    public AssetTask newTask(final AssetKey key, final InputStream stream) {
        if (key.getType().equals(VERT)) {
            return new VSLoader(key, stream);
        } else if (key.getType().equals(FRAG)) {
            return new FSLoader(key, stream);
        } else if (key.getType().equals(GEOM)) {
            return new GSLoader(key, stream);
        } else if (key.getType().equals(COMP)) {
            return new CSLoader(key, stream);
        }
        throw new UnsupportedOperationException("Invalid shader file type extension: " + key.getAbsolutePath());
    }

    /**
     * Vertex Shader Loader - (C) Cybertekt Software.
     *
     * {@link AssetTask Task} that constructs a {@link GLSLShader vertex shader}
     * by extracting valid GLSL source code using the input stream from the file
     * located at a path specified by an {@link AssetKey key}.
     */
    private class VSLoader extends AssetTask {

        /**
         * Constructs a new shader loader loader for the
         * {@link AssetKey asset key} and its corresponding
         * {@link InputStream input stream}.
         *
         * @param key an {@link AssetKey asset key} that points to the location
         * of a valid GLSL vertex shader.
         * @param stream the {@link InputStream input stream} for the file
         * located at the path specified by the {@link AssetKey key}.
         */
        VSLoader(final AssetKey key, final InputStream stream) {
            super(key, stream);
        }

        /**
         * Creates a {@link GLSLShader vertex shader asset} by extracting a
         * String from the {@link InputStream stream} provided during the
         * construction of this {@link AssetTask task} and using it as the
         * source code to construct the {@link GLSLShader vertex shader asset}.
         *
         * @return the {@link GLSLShader vertex shader} constructed using the
         * String extracted from the {@link InputStream input stream} associated
         * with this loader as the shader source code.
         */
        @Override
        public GLSLShader load() {
            return new GLSLShader(KEY, GLSLShader.Type.Vertex, new java.util.Scanner(INPUT).useDelimiter("\\A").next());
        }

    }

    /**
     * Fragment Shader Loader - (C) Cybertekt Software.
     *
     * {@link AssetTask Task} that constructs a {@link GLSLShader frag shader}
     * by extracting valid GLSL source code from an input stream of the file
     * located at the path specified by an {@link AssetKey key}.
     */
    private class FSLoader extends AssetTask {

        /**
         * Constructs a new shader loader for an {@link AssetKey asset key} and
         * its corresponding {@link InputStream input stream}.
         *
         * @param key an {@link AssetKey asset key} that points to the location
         * of a valid fragment shader (.frag) written in GLSL.
         * @param stream the {@link InputStream input stream} for the file
         * located at the path specified by the {@link AssetKey key}.
         */
        FSLoader(final AssetKey key, final InputStream stream) {
            super(key, stream);
        }

        /**
         * Creates a {@link GLSLShader fragment shader asset} by extracting a
         * String from the {@link InputStream stream} provided during the
         * construction of this {@link AssetTask task} and using it as the
         * source code to construct the
         * {@link GLSLShader fragment shader asset}.
         *
         * @return the {@link GLSLShader fragment shader} constructed using the
         * String extracted from the {@link InputStream input stream} associated
         * with this loader as the shader source code.
         */
        @Override
        public GLSLShader load() {
            return new GLSLShader(KEY, GLSLShader.Type.Fragment, new java.util.Scanner(INPUT).useDelimiter("\\A").next());
        }
    }

    /**
     * Geometry Shader Loader - (C) Cybertekt Software.
     *
     * {@link AssetTask Task} that constructs a {@link GLSLShader geom shader}
     * by extracting valid GLSL source code from an input stream of the file
     * located at the path specified by an {@link AssetKey key}.
     */
    private class GSLoader extends AssetTask {

        /**
         * Constructs a new shader loader for an {@link AssetKey asset key} and
         * its corresponding {@link InputStream input stream}.
         *
         * @param key an {@link AssetKey asset key} that points to the location
         * of a valid geometry shader (.geom) written in GLSL.
         * @param stream the {@link InputStream input stream} for the file
         * located at the path specified by the {@link AssetKey key}.
         */
        GSLoader(final AssetKey key, final InputStream stream) {
            super(key, stream);
        }

        /**
         * Creates a {@link GLSLShader geometry shader asset} by extracting a
         * String from the {@link InputStream stream} provided during the
         * construction of this {@link AssetTask task} and using it as the
         * source code to construct the
         * {@link GLSLShader geometry shader asset}.
         *
         * @return the {@link GLSLShader geometry shader} constructed using the
         * String extracted from the {@link InputStream input stream} associated
         * with this loader as the shader source code.
         */
        @Override
        public GLSLShader load() {
            return new GLSLShader(KEY, GLSLShader.Type.Geometry, new java.util.Scanner(INPUT).useDelimiter("\\A").next());
        }
    }

    /**
     * Compute Shader Loader - (C) Cybertekt Software.
     *
     * {@link AssetTask Task} that constructs a {@link GLSLShader comp shader}
     * by extracting valid GLSL source code from an input stream of the file
     * located at the path specified by an {@link AssetKey key}.
     */
    private class CSLoader extends AssetTask {

        /**
         * Constructs a new shader loader for an {@link AssetKey asset key} and
         * its corresponding {@link InputStream input stream}.
         *
         * @param key an {@link AssetKey asset key} that points to the location
         * of a valid compute shader (.comp) written in GLSL.
         * @param stream the {@link InputStream input stream} for the file
         * located at the path specified by the {@link AssetKey key}.
         */
        CSLoader(final AssetKey key, final InputStream stream) {
            super(key, stream);
        }

        /**
         * Creates a {@link GLSLShader compute shader asset} by extracting a
         * String from the {@link InputStream stream} provided during the
         * construction of this {@link AssetTask task} and using it as the
         * source code for constructing an
         * {@link GLSLShader compute shader asset}.
         *
         * @return the {@link GLSLShader compute shader} constructed using the
         * String extracted from the {@link InputStream input stream} associated
         * with this loader as the shader source code.
         */
        @Override
        public GLSLShader load() {
            return new GLSLShader(KEY, GLSLShader.Type.Compute, new java.util.Scanner(INPUT).useDelimiter("\\A").next());
        }
    }
}
