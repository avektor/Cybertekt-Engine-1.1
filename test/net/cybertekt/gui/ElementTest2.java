package net.cybertekt.gui;

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
import net.cybertekt.ogl.shader.GLShader;
import net.cybertekt.render.Renderer;
import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Vektor
 */
public class ElementTest2 extends Application implements UpdateListener, DisplayListener, Renderer {
    
    private static final Logger LOG = LoggerFactory.getLogger(ElementTest2.class);
    
    private GLShader shader;
    
    private Matrix4f projectionMatrix;
    
    
    public static final void main(final String[] args) {
        ElementTest2 app = new ElementTest2();

        AppSettings appSettings = new AppSettings("Element Test", "1.0");
        DisplaySettings displaySettings = new DisplaySettings("Element Test", new Vector2i(800, 600));

        app.initialize(appSettings, displaySettings);
    }
    
    @Override
    public final void init() {
        // Attach Listeners //
        addUpdateListener(this);
        addDisplayListener(this);
        addRenderer(this);
        
        // Register Shader Loader  //
        AssetManager.registerLoader(ShaderLoader.class, AssetType.getType("VERT"), AssetType.getType("FRAG"));

        // Load Element Shader //
        shader = new GLShader(AssetManager.get(GLSLShader.class, "Shaders/element.vert", "Shaders/element.frag"));
        
        // Initialize Projection Matrix //
        projectionMatrix = new Matrix4f().ortho2D(0, getWidth(), getHeight(), 0);
        
        Element elementA = new Element("A", Element.ChildLayout.HORIZONTAL, Element.ChildHAlign.LEFT, Element.ChildVAlign.TOP);
        elementA.setWidth("800px");
        elementA.setHeight("600px");
        
        Element elementB = new Element("B", Element.ChildLayout.HORIZONTAL, Element.ChildHAlign.LEFT, Element.ChildVAlign.TOP);
        elementB.setWidth("50%");
        elementB.setHeight("50%");
        
        elementA.attachChild(elementB);
        
        elementA.calculateSize();
        elementB.calculateSize();
        
        LOG.info("Size of A: {}x{}", (int) elementA.getSize().x, (int) elementA.getSize().y);
        LOG.info("Size of B: {}x{}", (int) elementB.getSize().x, (int) elementB.getSize().y);
        
    }
    
    @Override
    public final void update(final double tpf) {
    }
    
    @Override
    public final void update() {
    }

    @Override
    public final void render() {
        // Bind Shader //
        shader.bind();
        
        shader.setUniform(0, projectionMatrix);

        //glPointSize(10f);
        //glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        
        // Render Mesh //
        //render(rootElement);
        //for (Element_Old e : rootElement.getChildren()) {
        //    render(e);
        //}
    }

    public final void render(final Element_Old element) {
        //shader.setUniform(1, element.getTransform());
        //shader.setUniform(2, element.getColor());
        
        //for (final GLMesh mesh : element.getMeshList()) {
        //    mesh.bind();
        //    if (mesh.hasBuffer(GLBuffer.Type.Indices)) {
        //        glDrawElements(mesh.getMode().ID, mesh.getIndexCount(), mesh.getBuffer(GLBuffer.Type.Indices).getFormat().ID, 0);
        //    } else if (mesh.hasBuffer(GLBuffer.Type.Positions)) {
        //        glDrawArrays(mesh.getMode().ID, 0, mesh.getVertexCount());
        //    }
        //}
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
