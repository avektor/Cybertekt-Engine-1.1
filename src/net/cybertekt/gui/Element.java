package net.cybertekt.gui;

import java.util.ArrayList;
import java.util.List;
import org.joml.Vector2f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Element - (C) Cybertekt Software
 *
 * @version 1.1.0
 * @since 1.1.0
 * @author Andrew Vektor
 */
public class Element {

    private static final Logger LOG = LoggerFactory.getLogger(Element.class);

    /**
     * Indicates how elements will be positioned within their parent element.
     */
    public enum ChildLayout {

        /**
         * Elements will be positioned from left to right in the same order that
         * they were attached to the parent element.
         */
        HORIZONTAL,
        /**
         * Elements will be positioned from top to bottom in the same order that
         * they were attached to the parent element.
         */
        VERTICAL,
        /**
         * Elements will define their own positions and will not be positioned
         * by the parent element.
         */
        ABSOLUTE;
    }

    /**
     * Indicates how elements will be horizontally aligned within their parent
     * element.
     */
    public enum ChildHAlign {
        /**
         * Elements will be aligned on the left side of the parent element.
         */
        LEFT,
        /**
         * Elements will be aligned in the center of the parent element.
         */
        CENTER,
        /**
         * Elements will be aligned on the right side of the parent element.
         */
        RIGHT;
    }

    /**
     * Indicates how elements will be vertically aligned within their parent
     * element.
     */
    public enum ChildVAlign {
        /**
         * Elements will be aligned at the top of the parent element.
         */
        TOP,
        /**
         * Elements will be aligned in the middle of the parent element.
         */
        MIDDLE,
        /**
         * Elements will be aligned at the bottom of the parent element.
         */
        BOTTOM;
    }

    /**
     * Wraps an element size attribute.
     */
    private class Size {

        /**
         * The size value.
         */
        private float size;

        /**
         * Indicates if the value should be interpreted as a percentage.
         */
        private boolean relative;

        /**
         * Constructs a new size attribute with a default size value specified
         * in pixels.
         *
         * @param size the default size in pixels.
         */
        public Size(final float size) {
            set(size);
        }

        /**
         * Constructs a new size attribute with a default size value. This
         * attribute may be specified as a percentage using the '%' suffix, or
         * as an exact pixel size using the 'px' suffix.
         *
         * @param size the default size attribute.
         */
        public Size(final String size) {
            set(size);
        }

        /**
         * Sets the size in pixels.
         *
         * @param size the size attribute in pixels..
         */
        public final void set(final float size) {
            this.size = size;
            this.relative = false;
        }

        /**
         * Sets the element size attribute. This attribute may be specified as a
         * percentage using the '%' suffix, or as an exact pixel size using the
         * 'px' suffix.
         *
         * @param size the element size attribute.
         * @throws NumberFormatException if the attribute value is invalid or
         * not properly formatted.
         */
        public final void set(final String size) {
            if (size == null) {
                throw new NumberFormatException(size);
            }
            if (size.endsWith("%")) {
                try {
                    this.size = Float.parseFloat(size.substring(0, size.length() - 1)) / 100f;
                    relative = true;
                } catch (NullPointerException | NumberFormatException e) {
                    throw new NumberFormatException(size);
                }
            } else if (size.endsWith("px")) {
                try {
                    this.size = Float.parseFloat(size.substring(0, size.length() - 2));
                    relative = false;
                } catch (NullPointerException | NumberFormatException e) {
                    throw new NumberFormatException(size);
                }
            } else {
                throw new NumberFormatException(size);
            }
        }

        /**
         * Returns the value of the size attribute.
         *
         * @return the attribute value.
         */
        public final float get() {
            return size;
        }

        /**
         * Indicates if the size attribute was specified as a percentage.
         *
         * @return true if the attribute was specified as a percentage.
         */
        public final boolean isRelative() {
            return relative;
        }
    }

    /**
     * User-defined name of the element.
     */
    private final String name;

    /**
     * Element to which this element is attached.
     */
    private Element parent;

    /**
     * List of elements attached to this element.
     */
    private final List<Element> children;

    /**
     * Determines how child elements are to be positioned.
     */
    private ChildLayout layout;

    /**
     * Determines how child elements are to be horizontally aligned.
     */
    private ChildHAlign hAlign;

    /**
     * Determines how child elements are to be vertically aligned.
     */
    private ChildVAlign vAlign;

    /**
     * The element width attribute.
     */
    private final Size WIDTH, MIN_WIDTH, MAX_WIDTH;

    /**
     * The element height attribute.
     */
    private final Size HEIGHT, MIN_HEIGHT, MAX_HEIGHT;

    /**
     * The calculated element size.
     */
    private final Vector2f size;

    public Element(final String name, final ChildLayout layout, final ChildHAlign hAlign, final ChildVAlign vAlign) {
        this.name = name;
        this.layout = layout;
        this.hAlign = hAlign;
        this.vAlign = vAlign;
        this.children = new ArrayList<>();
        this.size = new Vector2f();

        // Initialize Width Attributes //
        this.MIN_WIDTH = new Size("0px");
        this.WIDTH = new Size("0px");
        this.MAX_WIDTH = new Size("9999px");

        // Initialize Height Attributes //
        this.MIN_HEIGHT = new Size("0px");
        this.HEIGHT = new Size("0px");
        this.MAX_HEIGHT = new Size("9999px");
    }

    /**
     * Calculates the actual size of the element using the element size
     * attributes.
     */
    public final void calculateSize() {
        // Calculate Element Width //
        float minWidth, width, maxWidth;
        minWidth = (parent != null && MIN_WIDTH.isRelative()) ? (MIN_WIDTH.get() * parent.getSize().x) : MIN_WIDTH.get();
        width = (parent != null && WIDTH.isRelative()) ? (WIDTH.get() * parent.getSize().x) : WIDTH.get();
        maxWidth = (parent != null && MAX_WIDTH.isRelative()) ? (MAX_WIDTH.get() * parent.getSize().x) : MAX_WIDTH.get();
        width = (width < minWidth) ? minWidth : width;
        width = (width > maxWidth) ? maxWidth : width;

        // Calculate Element Height //
        float minHeight, height, maxHeight;
        minHeight = (parent != null && MIN_HEIGHT.isRelative()) ? (MIN_HEIGHT.get() * parent.getSize().y) : MIN_HEIGHT.get();
        height = (parent != null && HEIGHT.isRelative()) ? (HEIGHT.get() * parent.getSize().y) : HEIGHT.get();
        maxHeight = (parent != null && MAX_HEIGHT.isRelative()) ? (MAX_HEIGHT.get() * parent.getSize().y) : MAX_HEIGHT.get();
        height = (height < minHeight) ? minHeight : height;
        height = (height > maxHeight) ? maxHeight : height;

        // Update Size Vector //
        size.set(width, height);
    }

    /**
     * Attaches the specified element to this element. The element will first be
     * detached from its current parent if one exists. This method does nothing
     * if the specified element is already a child of this element.
     *
     * @param toAttach the child element to attach.
     */
    public final void attachChild(final Element toAttach) {
        if (toAttach.parent != this) {
            if (toAttach.parent != null) {
                toAttach.parent.detachChild(toAttach);
            }
            children.add(toAttach);
            toAttach.parent = this;
        }
    }

    /**
     * Detaches the specified element from this element. This method does
     * nothing if the element is not a child of this element.
     *
     * @param toDetach the child element to detach.
     */
    public final void detachChild(final Element toDetach) {
        if (toDetach.parent == this) {
            children.remove(toDetach);
            toDetach.parent = null;
        }
    }

    /**
     * Returns the user-defined name of the element.
     *
     * @return the name of the element.
     */
    public final String getName() {
        return name;
    }

    /**
     * Sets the minimum element width attribute. This attribute may be specified
     * as a percentage relative to the width of the parent element using the '%'
     * suffix, or as an exact minimum width in pixels using the 'px' suffix.
     *
     * @param minWidth the minimum element width.
     * @throw InvalidElementAttribException if the attribute value is invalid or
     * improperly formatted.
     */
    public final void setMinWidth(final String minWidth) {
        try {
            this.MIN_WIDTH.set(minWidth);
        } catch (NumberFormatException e) {
            throw new InvalidElementAttribException("Invalid 'minWidth' attribute - " + name + ": " + e.getMessage());
        }
    }

    /**
     * Sets the element width attribute. This attribute may be specified as a
     * percentage relative to the width of the parent element using the '%'
     * suffix, or as an exact width in pixels using the 'px' suffix.
     *
     * @param width the element width.
     * @throws InvalidElementAttribException if the attribute value is invalid
     * or improperly formatted.
     */
    public final void setWidth(final String width) {
        try {
            this.WIDTH.set(width);
        } catch (NumberFormatException e) {
            throw new InvalidElementAttribException("Invalid 'width' attribute - " + name + ": " + e.getMessage());
        }
    }

    /**
     * Sets the maximum element width attribute. This attribute may be specified
     * as a percentage relative to the width of the parent element using the '%'
     * suffix, or as an exact maximum width in pixels using the 'px' suffix.
     *
     * @param maxWidth the maximum element width.
     * @throw InvalidElementAttribException if the attribute value is invalid or
     * improperly formatted.
     */
    public final void setMaxWidth(final String maxWidth) {
        try {
            this.MAX_WIDTH.set(maxWidth);
        } catch (NumberFormatException e) {
            throw new InvalidElementAttribException("Invalid 'maxWidth' attribute - " + name + ": " + e.getMessage());
        }
    }

    /**
     * Sets the minimum element height attribute. This attribute may be
     * specified as a percentage relative to the height of the parent element
     * using the '%' suffix, or as an exact minimum height in pixels using the
     * 'px' suffix.
     *
     * @param minHeight the minimum element height.
     * @throw InvalidElementAttribException if the attribute value is invalid or
     * improperly formatted.
     */
    public final void setMinHeight(final String minHeight) {
        try {
            this.MIN_HEIGHT.set(minHeight);
        } catch (NumberFormatException e) {
            throw new InvalidElementAttribException("Invalid 'minHeight' attribute - " + name + ": " + e.getMessage());
        }
    }

    /**
     * Sets the element height attribute. This attribute may be specified as a
     * percentage relative to the height of the parent element using the '%'
     * suffix, or as an exact height in pixels using the 'px' suffix.
     *
     * @param height the element height attribute.
     * @throws InvalidElementAttribException if the attribute value is invalid
     * or improperly formatted.
     */
    public final void setHeight(final String height) {
        try {
            this.HEIGHT.set(height);
        } catch (NumberFormatException e) {
            throw new InvalidElementAttribException("Invalid 'height' attribute - " + name + ": " + e.getMessage());
        }
    }

    /**
     * Sets the maximum element height attribute. This attribute may be
     * specified as a percentage relative to the height of the parent element
     * using the '%' suffix, or as an exact maximum height in pixels using the
     * 'px' suffix.
     *
     * @param maxHeight the minimum element height.
     * @throw InvalidElementAttribException if the attribute value is invalid or
     * improperly formatted.
     */
    public final void setMaxHeight(final String maxHeight) {
        try {
            this.MAX_HEIGHT.set(maxHeight);
        } catch (NumberFormatException e) {
            throw new InvalidElementAttribException("Invalid 'maxHeight' attribute - " + name + ": " + e.getMessage());
        }
    }

    /**
     * Returns the actual width and height of the element, in pixels, calculated
     * using the various element size attributes.
     *
     * @return the size of the element, in pixels.
     */
    public final Vector2f getSize() {
        return size;
    }

    /**
     * Exception thrown when attempting to set an element attribute using an
     * invalid or improperly formatted value.
     */
    public final class InvalidElementAttribException extends RuntimeException {

        public InvalidElementAttribException(final String msg) {
            super(msg);
        }
    }

}
