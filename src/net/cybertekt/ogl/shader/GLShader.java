package net.cybertekt.ogl.shader;

import java.nio.FloatBuffer;
import net.cybertekt.asset.shader.GLSLShader;
import net.cybertekt.ogl.GLObject;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VALIDATE_STATUS;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform2fv;
import static org.lwjgl.opengl.GL20.glUniform4fv;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;
import org.lwjgl.system.MemoryStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OpenGL Shader Program - (C) Cybertekt Software
 *
 * {@link GLObject} that encapsulates an OpenGL shader program. A shader program
 * is created from one or more {@link GLSLShader GLSL shader sources}.
 *
 * @version 1.1.0
 * @since 1.1.0
 * @author Andrew Vektor
 */
public class GLShader extends GLObject {

    /**
     * Static class logger for debugging.
     */
    private static final Logger LOG = LoggerFactory.getLogger(GLShader.class);

    public GLShader(final GLSLShader... sources) {
        super(GLObject.Type.Shader);

        // Ensure Shader Was Created Successfully //
        if (ID == 0) {
            throw new RuntimeException("Unable to create additional shader programs");
        }

        // Array For Storing GLSL Shader Source Ids //
        int[] ids = new int[sources.length];

        // Compile And Attach Shader Sources //
        for (int i = 0; i < sources.length; i++) {
            ids[i] = compile(sources[i]);
            glAttachShader(ID, ids[i]);
        }

        // Link Shader Program //
        glLinkProgram(ID);

        // Verify Program Link //
        if (glGetProgrami(ID, GL_LINK_STATUS) == 0) {
            throw new RuntimeException("Shader Program Link Failed: " + glGetProgramInfoLog(ID));
        }

        // Detach And Delete GLSL Shader Sources //
        for (int i = 0; i < ids.length; i++) {
            glDetachShader(ID, ids[i]);
            glDeleteShader(ids[i]);
        }
    }

    /**
     * Binds the shader program to the current OpenGL context.
     */
    public final void bind() {
        glUseProgram(ID);
    }

    /**
     * Validates the shader program. Shader program validation requires
     * significant processing time and should only be used for the purpose of
     * debugging. Validation is not required for a shader program to function
     * properly.
     *
     * @return true if the shader program has been validated successfully. False
     * if the shader program has failed validation.
     */
    public final boolean validate() {
        glValidateProgram(ID);
        if (glGetProgrami(ID, GL_VALIDATE_STATUS) == 0) {
            LOG.error("Shader Validation Failed: {}", glGetProgramInfoLog(ID));
            return false;
        }
        return true;
    }

    /**
     * Compiles the {@link GLSLShader GLSL shader source code} and returns its
     * OpenGL pointer.
     *
     * @param source the GLSL shader source to compile.
     * @return the pointer identifier of the compiled shader source.
     * @throws OGLException if source code compilation fails.
     */
    private int compile(final GLSLShader source) {

        // Create Shader //
        int id = glCreateShader(source.getType().getId());

        // Set Shader Source //
        glShaderSource(id, source.getSource());

        // Compile Shader //
        glCompileShader(id);

        // Verify Compilation Status //
        if (glGetShaderi(id, GL_COMPILE_STATUS) == 0) {
            throw new RuntimeException("Shader Source Compilation Failed: " + source.getKey().getName() + "\n\t" + glGetShaderInfoLog(id));
        }

        // Return Compiled Shader ID //
        return id;
    }
    
    public void setUniform(final int location, final Matrix4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(16);
            value.get(buffer);
            glUniformMatrix4fv(location, false, buffer);
        }
    }
    
    public void setUniform(final int location, final Vector2f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(2);
            value.get(buffer);
            glUniform2fv(location, buffer);
        }
    }
    
    public void setUniform(final int location, final Vector4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(4);
            value.get(buffer);
            glUniform4fv(location, buffer);
        }
    }

    public class InvalidUniformException extends RuntimeException {

        public InvalidUniformException(final GLShader shader, final String uniform) {
            super("Invalid Shader Uniform: " + uniform);
        }

    }

}
