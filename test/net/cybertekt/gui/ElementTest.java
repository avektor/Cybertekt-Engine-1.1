package net.cybertekt.gui;

import net.cybertekt.gui.element.Panel;
import net.cybertekt.gui.element.Element_Old;
import net.cybertekt.app.AppSettings;
import net.cybertekt.app.Application;
import net.cybertekt.asset.AssetManager;
import net.cybertekt.asset.AssetType;
import net.cybertekt.asset.shader.GLSLShader;
import net.cybertekt.asset.shader.ShaderLoader;
import net.cybertekt.app.listener.DisplayListener;
import net.cybertekt.app.display.DisplaySettings;
import net.cybertekt.app.listener.UpdateListener;
import net.cybertekt.ogl.GLMesh;
import net.cybertekt.ogl.buffer.GLBuffer;
import net.cybertekt.ogl.shader.GLShader;
import net.cybertekt.render.Renderer;
import org.joml.Matrix4f;
import org.joml.Vector2i;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glPointSize;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Vektor
 */
public class ElementTest extends Application implements UpdateListener, DisplayListener, Renderer {
    
    private static final Logger LOG = LoggerFactory.getLogger(ElementTest.class);
    
    private GLShader shader;
    
    private Matrix4f projectionMatrix;
    
    private Element_Old rootElement;
    
    public static final void main(final String[] args) {
        ElementTest app = new ElementTest();

        AppSettings appSettings = new AppSettings("Render Test", "1.0");
        DisplaySettings displaySettings = new DisplaySettings("Render Test", new Vector2i(800, 600));

        app.initialize(appSettings, displaySettings);
    }
    
    @Override
    public final void init() {
        // Register Shader Loader  //
        AssetManager.registerLoader(ShaderLoader.class, AssetType.getType("VERT"), AssetType.getType("FRAG"));

        // Load Element Shader //
        shader = new GLShader(AssetManager.get(GLSLShader.class, "Shaders/element.vert", "Shaders/element.frag"));
        
        // Initialize Projection Matrix //
        projectionMatrix = new Matrix4f().ortho2D(0, getWidth(), getHeight(), 0);
        
        // Initialize Root Element //
        rootElement = new Panel("Root Element");
        rootElement.setWidth(getWidth());
        rootElement.setHeight(getHeight());
        rootElement.setColor(1f, 0f, 0f, 1f);
        rootElement.setLocation(getWidth() / 2, getHeight() / 2, 0f);
        
        // Initialize Child Element //
        Panel panel1 = new Panel("Panel 1", true);
        panel1.setWidth(0.5f);
        panel1.setHeight(0.5f);
        panel1.setColor(0f, 1f, 0f, 1f);
        
        Panel panel2 = new Panel("Panel 2", true);
        panel2.setWidth(0.5f);
        panel2.setHeight(0.5f);
        panel2.setColor(0f, 0f, 1f, 1f);
        
        Panel panel3 = new Panel("Panel 3", true);
        panel3.setWidth(0.333f);
        panel3.setHeight(0.333f);
        panel3.setColor(0.5f, 0.5f, 0.5f, 1f);
        
        
        
        rootElement.addChild(panel1);
        rootElement.addChild(panel2);
        //rootElement.addChild(panel3);
        
        rootElement.update();
        
        addUpdateListener(this);
        addDisplayListener(this);
        addRenderer(this);
    }
    
    @Override
    public final void update(final double tpf) {
        rootElement.update();
    }
    
    @Override
    public final void update() {
    }

    @Override
    public final void render() {
        // Bind Shader //
        shader.bind();
        
        shader.setUniform(0, projectionMatrix);

        glPointSize(10f);
        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        
        // Render Mesh //
        render(rootElement);
        for (Element_Old e : rootElement.getChildren()) {
            render(e);
        }
    }

    public final void render(final Element_Old element) {
        shader.setUniform(1, element.getTransform());
        shader.setUniform(2, element.getColor());
        
        for (final GLMesh mesh : element.getMeshList()) {
            mesh.bind();
            if (mesh.hasBuffer(GLBuffer.Type.INDICES)) {
                glDrawElements(mesh.getMode().ID, mesh.getIndexCount(), mesh.getBuffer(GLBuffer.Type.INDICES).getFormat().ID, 0);
            } else if (mesh.hasBuffer(GLBuffer.Type.POSITIONS)) {
                glDrawArrays(mesh.getMode().ID, 0, mesh.getVertexCount());
            }
        }
    }
    
    @Override
    public final void exit() {
        
    }

    @Override
    public void onFocus(final boolean focused) {

    }

    @Override
    public void onIconify(final boolean iconified) {

    }

    @Override
    public void onMove(final int xPos, final int yPos) {

    }

    @Override
    public void onResize(final int width, final int height) {
        
    }

    @Override
    public void onClose() {
        stop();
    }
}
