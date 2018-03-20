package net.cybertekt.gui.element;

import java.util.ArrayList;
import java.util.List;
import net.cybertekt.math.Transform;
import net.cybertekt.ogl.GLMesh;
import net.cybertekt.ogl.buffer.GLBuffer;
import net.cybertekt.ogl.buffer.GLVertexBuffer;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.joml.Vector4i;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Vektor
 */
public class Element_Old {
    
    private static final Logger LOG = LoggerFactory.getLogger(Element_Old.class);

    public enum Layout {
        /**
         * Child elements will be positioned from left to right.
         */
        HORIZONTAL,
        /**
         * Child elements are positioned from top to bottom.
         */
        VERTICAL,
        ABSOLUTE;
    }

    public enum HAlign {
        LEFT,
        CENTER,
        RIGHT,
        JUSTIFY;
    }

    public enum VAlign {
        TOP,
        MIDDLE,
        BOTTOM,
        JUSTIFY;
    }

    public class Size {

        private float size;

        private boolean relative;

        public Size(final int size) {
            this.size = size;
            this.relative = false;
        }

        public Size(final float size) {
            this.size = size;
            this.relative = true;
        }

        public void set(final int size) {
            this.size = size;
            this.relative = false;
        }

        public void set(final float size) {
            this.size = size;
            this.relative = true;
        }

        public final float get() {
            return size;
        }

        public final boolean isRelative() {
            return relative;
        }
    }

    private final String NAME;

    private final List<Element_Old> CHILDREN = new ArrayList<>();

    private final Size MIN_WIDTH = new Size(0);
    private final Size WIDTH = new Size(0);
    private final Size MAX_WIDTH = new Size(9999);

    private final Size MIN_HEIGHT = new Size(0);
    private final Size HEIGHT = new Size(0);
    private final Size MAX_HEIGHT = new Size(9999);

    private final Vector4i PADDING = new Vector4i();
    private final Vector4i MARGIN = new Vector4i();

    private final Vector4f COLOR = new Vector4f();

    private final Vector2f SIZE = new Vector2f();

    private final Transform TRANSFORM = new Transform();

    private Element_Old parent;
    
    private final Layout layout;
    
    private final HAlign hAlign;
    
    private final VAlign vAlign;
    
    private boolean absolute;
    
    private boolean debugMode;
    
    private GLMesh debugMesh;
    
    protected List<GLMesh> meshList = new ArrayList();

    public Element_Old(final String name) {
        this(name, Layout.HORIZONTAL);
    }
    
    public Element_Old(final String name, final boolean debugMode) {
        this(name, Layout.HORIZONTAL, debugMode);
    }
    
    public Element_Old(final String name, final Layout layout) {
        this(name, layout, HAlign.LEFT, VAlign.TOP);
    }
    
    public Element_Old(final String name, final Layout layout, final boolean debugMode) {
        this(name, layout, HAlign.LEFT, VAlign.TOP, debugMode);
    }
    
    public Element_Old(final String name, final Layout layout, final HAlign hAlign, final VAlign vAlign) {
        this(name, layout, hAlign, vAlign, false);
    }
    
    public Element_Old(final String name, final Layout layout, final HAlign hAlign, final VAlign vAlign, final boolean debugMode) {
        this.NAME = name;
        this.layout = layout;
        this.hAlign = hAlign;
        this.vAlign = vAlign;
        this.debugMode = debugMode;
    }

    public void update() {
        // Update Element Size //
        updateSize();
        
        // Update Element Mesh //
        updateMesh();
        
        // Update Children //
        for (final Element_Old child : CHILDREN) {
            child.update();
        }

        // Update Element Layout //
        updateLayout();
    }

    private void updateSize() {
        // Calculate Width Values //
        float minWidth, width, maxWidth;
        minWidth = (parent != null && MIN_WIDTH.isRelative()) ? (MIN_WIDTH.get() * parent.getSize().x) : MIN_WIDTH.get();
        width = (parent != null && WIDTH.isRelative()) ? (WIDTH.get() * parent.getSize().x) : WIDTH.get();
        maxWidth = (parent != null && MAX_WIDTH.isRelative()) ? (MAX_WIDTH.get() * parent.getSize().x) : MAX_WIDTH.get();

        // Calculate Height Values //
        float minHeight, height, maxHeight;
        minHeight = (parent != null && MIN_HEIGHT.isRelative()) ? (MIN_HEIGHT.get() * parent.getSize().y) : MIN_HEIGHT.get();
        height = (parent != null && HEIGHT.isRelative()) ? (HEIGHT.get() * parent.getSize().y) : HEIGHT.get();
        maxHeight = (parent != null && MAX_HEIGHT.isRelative()) ? (MAX_HEIGHT.get() * parent.getSize().y) : MAX_HEIGHT.get();

        // Check Minimum/Maximum Width //
        width = (width < minWidth) ? minWidth : width;
        width = (width > maxWidth) ? maxWidth : width;

        // Check Minimum/Maximum Height //
        height = (height < minHeight) ? minHeight : height;
        height = (height > maxHeight) ? maxHeight : height;

        // Set Size Values //
        SIZE.set(width, height);
    }
    
    private void updateMesh() {
        if (debugMesh == null && debugMode) {
            debugMesh = new GLMesh(GLMesh.Mode.Points);
            debugMesh.setBuffer(new GLVertexBuffer(GLBuffer.Type.POSITIONS, GLBuffer.Usage.DYNAMIC, new Vector3f[] { 
                new Vector3f(0f, 0f, 0f), // Origin
                new Vector3f(-getSize().x / 2, -getSize().y / 2, 0f), // Bottom Left
                new Vector3f(-getSize().x / 2, getSize().y / 2, 0f), // Top Left
                new Vector3f(getSize().x / 2, getSize().y / 2, 0f), // Top Right
                new Vector3f(getSize().x / 2, -getSize().y / 2, 0f) // Bottom Right
            }));
            meshList.add(debugMesh);
        } else if (debugMesh != null && !debugMode) {
            meshList.remove(debugMesh);
            debugMesh = null;
        }
    }

    private void updateLayout() {
        switch(layout) {
            case HORIZONTAL: {
                float xPos = 0f;
                for (final Element_Old child : CHILDREN) {
                    switch (hAlign) {
                        case LEFT: {
                            child.setLocation(xPos - child.getSize().x, -child.getSize().y, 0f);
                            xPos += child.getSize().x;
                            break;
                        }
                    }
                }
                break;
            }
        }
    }

    public final void addChild(final Element_Old child) {
        if (child != null && child.parent != this) {
            if (child.parent != null) {
                child.parent.removeChild(child);
            }
            CHILDREN.add(child);
            child.parent = this;
        }
    }

    public final void removeChild(final Element_Old child) {
        CHILDREN.remove(child);
        child.parent = null;
    }

    public final List<Element_Old> getChildren() {
        return CHILDREN;
    }

    public final void setMinWidth(final int minWidth) {
        MIN_WIDTH.set(minWidth);
    }

    public final void setMinWidth(final float minWidth) {
        MIN_WIDTH.set(minWidth);
    }

    public final void setWidth(final int width) {
        WIDTH.set(width);
    }

    public final void setWidth(final float width) {
        WIDTH.set(width);
    }

    public final void setMaxWidth(final int maxWidth) {
        MAX_WIDTH.set(maxWidth);
    }

    public final void setMaxWidth(final float maxWidth) {
        MAX_WIDTH.set(maxWidth);
    }

    public final void setMinHeight(final int minHeight) {
        MIN_HEIGHT.set(minHeight);
    }

    public final void setMinHeight(final float minHeight) {
        MIN_HEIGHT.set(minHeight);
    }

    public final void setHeight(final int height) {
        HEIGHT.set(height);
    }

    public final void setHeight(final float height) {
        HEIGHT.set(height);
    }

    public final void setMaxHeight(final int maxHeight) {
        MAX_HEIGHT.set(maxHeight);
    }

    public final void setMaxHeight(final float maxHeight) {
        MAX_HEIGHT.set(maxHeight);
    }

    public final void setColor(final Vector4f color) {
        COLOR.set(color);
    }

    public final void setColor(final float red, final float green, final float blue, final float alpha) {
        COLOR.set(red, green, blue, alpha);
    }

    public final Vector4f getColor() {
        return COLOR;
    }

    public final Vector2f getSize() {
        return SIZE;
    }

    public final void setLocation(final float xLoc, final float yLoc, final float zLoc) {
        TRANSFORM.setTranslation(xLoc, yLoc, zLoc);
    }

    public final void setLocation(final Vector3f location) {
        TRANSFORM.setTranslation(location);
    }

    public final Matrix4f getTransform() {
        if (parent != null) {
            return parent.getTransform().mulLocal(TRANSFORM.getMatrix());
        }
        return TRANSFORM.getMatrix();
    }
    
    public final List<GLMesh> getMeshList() {
        return meshList;
    }
}
