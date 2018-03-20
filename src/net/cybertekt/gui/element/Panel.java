package net.cybertekt.gui.element;

import java.util.ArrayList;
import java.util.List;
import net.cybertekt.ogl.GLMesh;
import net.cybertekt.ogl.buffer.GLBuffer;
import net.cybertekt.ogl.buffer.GLIndexBuffer;
import net.cybertekt.ogl.buffer.GLVertexBuffer;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;

/**
 *
 * @author Vektor
 */
public class Panel extends Element_Old {

    private final Vector2f LAST_SIZE = new Vector2f();
    
    private GLMesh panel;
    
    private GLMesh debug;

    public Panel(final String name) {
        super(name);
    }
    
    public Panel(final String name, final boolean debugMode) {
        super(name, debugMode);
    }

    @Override
    public void update() {
        super.update();
        updateMesh();
    }

    private void updateMesh() {
        if (panel == null) {
            panel = new GLMesh(GLMesh.Mode.Triangles);
            panel.setBuffer(new GLVertexBuffer(GLBuffer.Type.POSITIONS, GLBuffer.Usage.DYNAMIC, new float[]{
                -getSize().x / 2, -getSize().y / 2, 0f, // Bottom Left
                -getSize().x / 2, getSize().y / 2, 0f, // Top Left
                getSize().x / 2, getSize().y / 2, 0f, // Top Right
                getSize().x / 2, -getSize().y / 2, 0f // Bottom Right
            }));
            panel.setBuffer(new GLIndexBuffer(new int[]{
                0, 1, 2,
                2, 3, 0
            }));
            meshList.add(panel);
        } else if (!LAST_SIZE.equals(getSize())) {
            ((GLVertexBuffer) panel.getBuffer(GLBuffer.Type.POSITIONS)).setData(new float[]{
                -getSize().x / 2, -getSize().y / 2, 0f, // Bottom Left
                -getSize().x / 2, getSize().y / 2, 0f, // Top Left
                getSize().x / 2, getSize().y / 2, 0f, // Top Right
                getSize().x / 2, -getSize().y / 2, 0f // Bottom Right
            });
        }
        LAST_SIZE.set(getSize());
    }
}
