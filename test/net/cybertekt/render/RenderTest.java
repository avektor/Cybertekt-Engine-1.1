package net.cybertekt.render;

import net.cybertekt.app.display.DisplaySettings;
import net.cybertekt.ogl.shader.GLShader;
import net.cybertekt.app.listener.DisplayListener;
import net.cybertekt.app.*;
import net.cybertekt.app.listener.UpdateListener;
import net.cybertekt.asset.AssetManager;
import net.cybertekt.asset.font.Font;
import net.cybertekt.asset.font.FontLoader;
import net.cybertekt.asset.image.Image;
import net.cybertekt.asset.image.ImageLoader;
import net.cybertekt.asset.shader.GLSLShader;
import net.cybertekt.asset.shader.ShaderLoader;
import net.cybertekt.gui.element.Text;
import net.cybertekt.math.Transform;
import net.cybertekt.ogl.GLMesh;
import net.cybertekt.ogl.GLObject;
import net.cybertekt.ogl.buffer.GLBuffer;
import net.cybertekt.ogl.texture.GLTexture.MagFilter;
import net.cybertekt.ogl.texture.GLTexture.MinFilter;
import net.cybertekt.ogl.texture.GLTexture2D;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.joml.Vector4f;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPointSize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Vektor
 */
public class RenderTest extends Application implements UpdateListener, DisplayListener, Renderer {

    private static final Logger LOG = LoggerFactory.getLogger(RenderTest.class);

    private final Vector3f[] vertices = new Vector3f[]{
        new Vector3f(0, 512f, 0f), // Top Left Vertex.
        new Vector3f(0, 0, 0f), // Bottom Left Vertex.
        new Vector3f(512f, 0, 0f), // Bottom Right Vertex.
        new Vector3f(512f, 512f, 0f) // Top Right Vertex.
    };
    
    private final Vector2f[] coordinates = new Vector2f[] { 
        new Vector2f(0f, 1f),
        new Vector2f(0f, 0f),
        new Vector2f(1f, 0f),
        new Vector2f(1f, 1f)
    };

    private final Vector3i[] indices = new Vector3i[]{
        new Vector3i(0, 1, 3),
        new Vector3i(3, 1, 2)
    };

    private GLMesh mesh;
    
    private GLShader shader;
    
    private GLTexture2D texture;
    
    private Font font;
    
    private Matrix4f projectionMatrix;
    
    private Transform transform;

    public static void main(final String[] args) {
        RenderTest app = new RenderTest();

        AppSettings appSettings = new AppSettings("Render Test", "1.0");
        DisplaySettings displaySettings = new DisplaySettings("Render Test", new Vector2i(800, 600));

        app.initialize(appSettings, displaySettings);
    }

    @Override
    public void init() {
        // Register Asset Loaders  //
        AssetManager.registerLoader(ShaderLoader.class);
        AssetManager.registerLoader(ImageLoader.class);
        AssetManager.registerLoader(FontLoader.class);
        
        // Create Shader //
        shader = new GLShader(AssetManager.get(GLSLShader.class, "Shaders/font.vert", "Shaders/font.frag"));
        
        // Basic PNG Images //
        
        //texture = new GLTexture2D(AssetManager.get(Image.class, "Textures/PNG/Grayscale.png"));
        //texture = new GLTexture2D(AssetManager.get(Image.class, "Textures/PNG/IDX8.png"));
        //texture = new GLTexture2D(AssetManager.get(Image.class, "Textures/PNG/LUM8.png"));
        //texture = new GLTexture2D(AssetManager.get(Image.class, "Textures/PNG/LUMA8.png"));
        //texture = new GLTexture2D(AssetManager.get(Image.class, "Textures/PNG/RGB08.png")); // Passed
        //texture = new GLTexture2D(AssetManager.get(Image.class, "Textures/PNG/RGB16.png")); // Passed
        //texture = new GLTexture2D(AssetManager.get(Image.class, "Textures/PNG/RGBA08.png")); // Passed
        //texture = new GLTexture2D(AssetManager.get(Image.class, "Textures/PNG/RGBA082.png")); // Passed
        //texture = new GLTexture2D(AssetManager.get(Image.class, "Textures/PNG/RGBA16.png")); // Passed
        //texture = new GLTexture2D(AssetManager.get(Image.class, "Textures/PNG/Test.png")); // Passed
        
        // Filtered PNG Images //
        //texture = new GLTexture2D(AssetManager.get(Image.class, "Textures/PNG/tests/filter/F0C8.png")); // Passed
        //texture = new GLTexture2D(AssetManager.get(Image.class, "Textures/PNG/tests/filter/F1C8.png")); // Passed
        //texture = new GLTexture2D(AssetManager.get(Image.class, "Textures/PNG/tests/filter/F2C8.png")); // Passed
        //texture = new GLTexture2D(AssetManager.get(Image.class, "Textures/PNG/tests/filter/F3C8.png")); // Passed
        //texture = new GLTexture2D(AssetManager.get(Image.class, "Textures/PNG/tests/filter/F4C8.png"));
        //texture = new GLTexture2D(AssetManager.get(Image.class, "Interface/Fonts/Arial/font.png"));
        //texture = new GLTexture2D(AssetManager.get(Image.class, "Interface/Fonts/DistFontTest.png"));
        
        //font = AssetManager.get(Font.class, "Interface/Fonts/CTF/arial.ctf");
        //font = AssetManager.get(Font.class, "Interface/Fonts/CTF/berlin.ctf");
        //font = AssetManager.get(Font.class, "Interface/Fonts/CTF/calibri.ctf");
        //font = AssetManager.get(Font.class, "Interface/Fonts/CTF/ebrima.ctf");
        //font = AssetManager.get(Font.class, "Interface/Fonts/CTF/eras.ctf");
        //font = AssetManager.get(Font.class, "Interface/Fonts/CTF/exo.ctf");
        //font = AssetManager.get(Font.class, "Interface/Fonts/CTF/gothic.ctf");
        //font = AssetManager.get(Font.class, "Interface/Fonts/CTF/open.ctf");
        //font = AssetManager.get(Font.class, "Interface/Fonts/CTF/play.ctf");
        //font = AssetManager.get(Font.class, "Interface/Fonts/CTF/titillium.ctf");
        //font = AssetManager.get(Font.class, "Interface/Fonts/CTF/trebuchet.ctf");
        //font = AssetManager.get(Font.class, "Interface/Fonts/CTF/verdana.ctf");
        Image img = new Image(font.getKey(), Image.Format.RGBA8, font.getWidth(), font.getHeight(), font.getData());
        
        // So Far The Best Looking = AA / No SDF / Trilinear Filtering / SDF Shader (Looks good from 8pt to 80pt)
        texture = new GLTexture2D(img, MinFilter.Linear, MagFilter.Linear);
        Text text = new Text(font, 72, "The quick brown fox jumps over the lazy dog. 1234567890");
        
        // Create Mesh //
        mesh = text.getMesh();
        //mesh = new GLMesh(GLMesh.Mode.Triangles);
        //mesh.setBuffer(new GLVertexBuffer(GLBuffer.Type.POSITIONS, glyph.getVertices(0.5f)));
        //mesh.setBuffer(new GLVertexBuffer(GLBuffer.Type.COORDINATES, glyph.getCoordinates()));
        //mesh.setBuffer(new GLVertexBuffer(GLBuffer.Type.COLORS, colors));
        //mesh.setBuffer(new GLIndexBuffer(glyph.getIndices()));
        
        // Create Projection Matrix //
        //projection = new Matrix4f().perspective((float) Math.toRadians(60f), getWidth() / getHeight(), 0.01f, 1000f);
        projectionMatrix = new Matrix4f().ortho2D(0, getWidth(), getHeight(), 0); // Left, Right, Bottom, Top
        transform = new Transform();
        //transform.setTranslation(200f, 200f, 0f);
        
        addUpdateListener(this);
        addDisplayListener(this);
        addRenderer(this);
    }

    @Override
    public void update() {

    }

    @Override
    public void update(final double delta) {

    }

    @Override
    public void exit() {
        LOG.info("GLObjects: {} [{}/{}/{}]", GLObject.count(), GLObject.count(GLObject.Type.Buffer), GLObject.count(GLObject.Type.Mesh), GLObject.count(GLObject.Type.Shader));
    }

    @Override
    public final void render() {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
        // Bind Shader //
        shader.bind();
        
        // Set Shader Uniforms //
        shader.setUniform(0, projectionMatrix);
        shader.setUniform(1, transform.getMatrix());
        shader.setUniform(2, new Vector2f(font.getWidth(), font.getHeight()));
        //shader.setUniform(3, new Vector4f(0f, 1f, 0f, 1f));
        
        glPointSize(10f);
        //glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        
        // Bind Texture //
        texture.bind();

        // Render Mesh //
        render(mesh);
    }

    public final void render(final GLMesh mesh) {
        mesh.bind();

        if (mesh.hasBuffer(GLBuffer.Type.INDICES)) {

            // Draw Indexed Mesh //
            glDrawElements(mesh.getMode().ID, mesh.getIndexCount(), mesh.getBuffer(GLBuffer.Type.INDICES).getFormat().ID, 0);

        } else if (mesh.hasBuffer(GLBuffer.Type.POSITIONS)) {

            // Draw Mesh Without Indices //
            glDrawArrays(mesh.getMode().ID, 0, mesh.getVertexCount());
        }
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
        projectionMatrix.setOrtho2D(0, getWidth(), getHeight(), 0);
    }

    @Override
    public void onClose() {
        stop();
    }
}
