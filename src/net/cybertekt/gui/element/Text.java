package net.cybertekt.gui.element;

import java.util.ArrayList;
import java.util.List;
import net.cybertekt.asset.font.Font;
import net.cybertekt.asset.font.Font.Glyph;
import net.cybertekt.ogl.GLMesh;
import net.cybertekt.ogl.buffer.GLBuffer;
import net.cybertekt.ogl.buffer.GLIndexBuffer;
import net.cybertekt.ogl.buffer.GLVertexBuffer;

/**
 * Text - (C) Cybertekt Software
 *
 * @verison 1.1.0
 * @since 1.1.0
 * @author Andrew Vektor
 */
public class Text {

    /**
     * {@link Font} for text rendering.
     */
    private Font font;

    /**
     * Text point render size.
     */
    private int size;

    /**
     * Text {@link StringBuilder string builder}.
     */
    private StringBuilder text;

    /**
     * Mesh vertex buffer.
     */
    private GLVertexBuffer vertexBuffer;

    /**
     * Mesh coordinate buffer.
     */
    private GLVertexBuffer coordinateBuffer;

    /**
     * Mesh index buffer.
     */
    private GLIndexBuffer indexBuffer;

    /**
     * Text render {@link GLMesh mesh}.
     */
    private GLMesh mesh;

    /**
     * Indicates if the render mesh is updated.
     */
    private boolean updated;

    public Text(final Font font) {
        this(font, 12);
    }

    public Text(final Font font, final int size) {
        this.font = font;
        this.size = size;
        this.text = new StringBuilder();
    }

    public Text(final Font font, final int size, final String text) {
        this.font = font;
        this.size = size;
        this.text = new StringBuilder(text);
    }

    public final void set(final String toSet) {
        text = new StringBuilder(toSet);
        updated = false;
    }

    public final void append(final char toAdd) {
        text.append(toAdd);
        updated = false;
    }

    public final void update() {
        if (!updated) {
            // Create Text Mesh //
            if (mesh == null) {
                mesh = new GLMesh(GLMesh.Mode.Triangles);
            }

            // Update Text Mesh //
            if (font != null && text != null && text.length() > 0) {
                float scale = (float) size / font.getSize();

                int xPosition = 0;
                int yPosition = Math.round(font.getLine() * scale);

                int indexOffset = 0;

                int last = -1;

                List<Float> vertices = new ArrayList<>(text.length() * 4);
                List<Float> coordinates = new ArrayList<>(text.length() * 4);
                List<Integer> indices = new ArrayList<>(text.length() * 6);

                for (int i = 0; i < text.length(); i++) {

                    // Get Character //
                    char current = text.charAt(i);

                    // Process Text Character //
                    if (font.hasGlyph(current)) {

                        // Get Font Glyph //
                        Glyph glyph = font.getGlyph(current);

                        // Get Kerning Offset //
                        int offset = Math.round(glyph.getKerningOffset(last) * scale);

                        // Update Vertices //
                        for (final float vertex : glyph.getVertices(xPosition + offset, yPosition, scale)) {
                            vertices.add(vertex);
                        }

                        // Update Coordinates //
                        for (final float coordinate : glyph.getCoordinates()) {
                            coordinates.add(coordinate);
                        }

                        // Update Indices //
                        for (final int index : glyph.getIndices(indexOffset)) {
                            indices.add(index);
                        }

                        // Update Position Offset //
                        xPosition += glyph.getAdvance() * scale + offset;

                        // Update Character Index Offset //
                        indexOffset += 4;
                    } else if (current == 32) {

                        // Add Space //
                        xPosition += Math.round(font.getSpace() * scale);
                    } else if (current == 10) {

                        // Add Line Break //
                        yPosition += Math.round(font.getLine() * scale);
                        xPosition = 0;
                    } else if (current == 9) {

                        // Add Tab //
                        xPosition += Math.round(font.getSpace() * scale) * 5;
                    }

                    // Save Previous Character (For Kerning) //
                    last = current;
                }

                // Update Vertex Buffer //
                if (vertexBuffer == null) {
                    mesh.setBuffer(vertexBuffer = new GLVertexBuffer(GLBuffer.Type.POSITIONS, GLBuffer.Usage.DYNAMIC, vertices));
                } else {
                    vertexBuffer.setData(vertices);
                }

                // Update Coordinate Buffer //
                if (coordinateBuffer == null) {
                    mesh.setBuffer(coordinateBuffer = new GLVertexBuffer(GLBuffer.Type.COORDINATES, GLBuffer.Usage.DYNAMIC, coordinates));
                } else {
                    coordinateBuffer.setData(coordinates);
                }

                // Update Index Buffer //
                if (indexBuffer == null) {
                    mesh.setBuffer(indexBuffer = new GLIndexBuffer(GLBuffer.Usage.DYNAMIC, indices));
                } else {
                    indexBuffer.setData(indices);
                }
            }
        }
    }

    @Override
    public final String toString() {
        return text.toString();
    }

    public final GLMesh getMesh() {
        update();
        return mesh;
    }
}
