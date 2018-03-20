package net.cybertekt.shader;

import net.cybertekt.app.AppSettings;
import net.cybertekt.app.Application;
import net.cybertekt.asset.*;
import net.cybertekt.asset.shader.GLSLShader;
import net.cybertekt.asset.shader.ShaderLoader;
import net.cybertekt.ogl.shader.GLShader;
import net.cybertekt.app.display.DisplaySettings;
import org.joml.Vector2i;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Shader Test - (C) Cybertekt Software
 *
 * @version 1.1.0
 * @since 1.1.0
 * @author Andrew Vektor
 */
public class ShaderTest extends Application {

    public static final Logger LOG = LoggerFactory.getLogger(ShaderTest.class);

    public static void main(final String[] args) {
        ShaderTest app = new ShaderTest();

        AppSettings appSettings = new AppSettings("Shader Test", "1.0");
        DisplaySettings displaySettings = new DisplaySettings("Shader Test", new Vector2i(800, 600));

        app.initialize(appSettings, displaySettings);
    }

    @Override
    public void init() {
        // Register Shader Loader  //
        AssetManager.registerLoader(ShaderLoader.class, AssetType.getType("VERT"), AssetType.getType("FRAG"));

        GLShader solid = new GLShader(AssetManager.get(GLSLShader.class, "Shaders/solid.vert", "Shaders/solid.frag"));
        solid.validate();
    }
    
    @Override
    public final void exit() {
    }
}
