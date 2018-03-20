package net.cybertekt.app.display;

import java.util.Objects;
import org.joml.Vector2i;
import org.joml.Vector2ic;
import org.joml.Vector3i;
import org.joml.Vector3ic;
import org.lwjgl.glfw.GLFWVidMode;

/**
 * Display Mode - (C) Cybertekt Software
 *
 * Immutable class that contains the size, refresh rate, and bit depth display
 * settings for a {@link DisplayDevice display device}.
 *
 * @version 1.1.0
 * @since 1.1.0
 * @author Andrew Vektor
 */
public final class DisplayMode {

    /**
     * Immutable vector containing the width and height of the display mode in
     * screen coordinates.
     */
    private final Vector2ic size;

    /**
     * The refresh rate, in Hz, of this display mode.
     */
    private final int refreshRate;

    /**
     * Immutable vector that stores the number of bits per pixel of the red,
     * green, and blue color channels.
     */
    private final Vector3ic bpp;

    /**
     * Constructs a new display mode object using setting retrieved from the
     * provided {@link GLFWVidMode GLFW video mode}.
     *
     * @param mode the GLFW {@link GLFWvidmode video mode} settings to use for
     * initializing this display mode.
     */
    protected DisplayMode(final GLFWVidMode mode) {
        this(new Vector2i(mode.width(), mode.height()), mode.refreshRate(), new Vector3i(mode.redBits(), mode.greenBits(), mode.blueBits()));
    }

    /**
     * Constructs an immutable display mode using the properties provided.
     *
     * @param resolution the width and height of the display mode specified in
     * screen coordinates.
     * @param refreshRate the display refresh rate in Hz.
     * @param bpp the number of bits per pixel for the red, green, and blue
     * color channels.
     */
    protected DisplayMode(final Vector2i resolution, final int refreshRate, final Vector3i bpp) {
        this.size = resolution.toImmutable();
        this.refreshRate = refreshRate;
        this.bpp = bpp.toImmutable();
    }

    /**
     * Constructs an immutable display mode using the properties provided.
     *
     * @param width the width of the display mode in screen coordinates.
     * @param height the height of the display mode in screen coordinates.
     * @param refreshRate the refresh rate of the display mode in Hz.
     * @param redBitDepth the bit depth of the red color channel.
     * @param greenBitDepth the bit depth of the green color channel.
     * @param blueBitDepth the bit depth of the blue color channel.
     */
    protected DisplayMode(final int width, final int height, final int refreshRate, final int redBitDepth, final int greenBitDepth, final int blueBitDepth) {
        this.size = new Vector2i(width, height).toImmutable();
        this.refreshRate = refreshRate;
        this.bpp = new Vector3i(redBitDepth, greenBitDepth, blueBitDepth).toImmutable();
    }

    /**
     * Returns the width of this display mode specified in screen coordinates.
     *
     * @return the width of the display mode in screen coordinates.
     */
    public final int getWidth() {
        return size.x();
    }

    /**
     * Returns the height of this display mode specified in screen coordinates.
     *
     * @return the height of the display mode in screen coordinates.
     */
    public final int getHeight() {
        return size.y();
    }

    /**
     * Returns a reference to the <b>immutable</b> size vector of the display
     * mode which contains the width and height of the display mode in screen
     * coordinates.
     *
     * @return the <b>immutable</b> size vector for the display mode.
     */
    public final Vector2ic getSize() {
        return size;
    }

    /**
     * Returns a vector containing the width and height of the display in screen
     * coordinates. If a storage vector is provided its components will be set
     * equal to the width and height of this display mode. If the parameter
     * vector is null, a new vector containing the width and height of this
     * display mode will be created and returned.
     *
     * @param store the vector in which to store the size of this display mode.
     * Null values are accepted. If a null value is provided a new vector equal
     * to the size of this display mode will be created and returned.
     * @return a vector equal to the size of this display mode.
     */
    public final Vector2i getSize(final Vector2i store) {
        if (store != null) {
            return store.set(size);
        } else {
            return new Vector2i(size);
        }
    }

    /**
     * Returns the refresh rate, in Hz, for this display mode.
     *
     * @return the refresh rate of the display mode in Hz.
     */
    public final int getRefreshRate() {
        return refreshRate;
    }

    /**
     * Returns the number of bits per pixel for the red color channel of this
     * display mode.
     *
     * @return the bit depth of the red color channel as defined by the 'x'
     * value of the immutable {@link #bpp bits per pixel vector}.
     */
    public final int getRedBits() {
        return bpp.x();
    }

    /**
     * Returns the number of bits per pixel for the green color channel of this
     * display mode.
     *
     * @return the bit depth of the green color channel as defined by the 'y'
     * value of the immutable {@link #bpp bits per pixel vector}.
     */
    public final int getGreenBits() {
        return bpp.y();
    }

    /**
     * Returns the number of bits per pixel for the blue color channel of this
     * display mode.
     *
     * @return the bit depth of the blue color channel as defined by the 'z'
     * value of the immutable {@link #bpp bits per pixel vector}.
     */
    public final int getBlueBits() {
        return bpp.z();
    }

    /**
     * Returns the total number of bits per pixel by adding together the bit
     * depth of the red, green, and blue color channels.
     *
     * @return the total number of bits per pixel.
     */
    public final int getBpp() {
        return bpp.x() + bpp.y() + bpp.z();
    }

    /**
     * Returns true if the provided object is equal to this object. Returns true
     * only if and only if the parameter object is an instance of
     * {@link DisplayMode} and its {@link #size}, {@link #refreshRate}, and
     * {@link #bpp} fields are equal to the size, refresh rate, and bits per
     * pixel fields of this display mode.
     *
     * @param obj the object to compare with this display mode.
     * @return true if the provided object is equal to this display mode.
     */
    @Override
    public final boolean equals(final Object obj) {
        if (obj instanceof DisplayMode) {
            DisplayMode m = (DisplayMode) obj;
            if (this == m) {
                return true;
            } else {
                return m.size.equals(size) && m.refreshRate == refreshRate && m.bpp.equals(bpp);
            }
        } else {
            return false;
        }
    }

    /**
     * Returns the hash code for this display mode which is calculated using the
     * internal {@link #size}, {@link #refreshRate}, and {@link #bpp} fields.
     *
     * @return the hash code calculated for this display mode.
     */
    @Override
    public final int hashCode() {
        int hash = 17;
        hash = 53 * hash + Objects.hashCode(size);
        hash = 53 * hash + refreshRate;
        hash = 53 * hash + Objects.hashCode(bpp);
        return hash;
    }

    /**
     * Returns a human-readable String that summarizes the internal fields of
     * this display mode. The returned String is defined as: "[Width]x[Height] @
     * [Refresh Rate]Hz - Bit Depth: [Bits Per Pixel]"
     *
     * @return a human-readable String that summarizes the internal properties
     * of this display mode.
     */
    @Override
    public final String toString() {
        return getWidth() + " x " + getHeight() + " @ " + getRefreshRate() + "Hz - " + getBpp() + " BPP";
    }
}
