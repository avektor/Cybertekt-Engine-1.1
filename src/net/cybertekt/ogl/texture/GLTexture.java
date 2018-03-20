package net.cybertekt.ogl.texture;

import net.cybertekt.ogl.GLObject;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_NEAREST;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_NEAREST_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST_MIPMAP_NEAREST;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL14.GL_MIRRORED_REPEAT;

/**
 * OpenGL Texture Object - (C) Cybertekt Software
 *
 * {@link GLObject} that provides a foundation for classes that encapsulate an
 * OpenGL texture object.
 *
 * @version 1.1.0
 * @since 1.1.0
 * @author Andrew Vektor
 */
public abstract class GLTexture extends GLObject {

    /**
     * Determines how a texture is wrapped on a surface that exceeds the
     * boundaries of the texture.
     */
    public enum WrapMode {
        /**
         * The texture will be repeated.
         */
        Repeat(GL_REPEAT),
        /**
         * The texture will be repeated and mirrored.
         */
        Mirror(GL_MIRRORED_REPEAT),
        /**
         * The texture will be stretched to reach the edge of the surface.
         */
        Clamp(GL_CLAMP_TO_EDGE);

        /**
         * The OpenGL wrap mode identifier constant.
         */
        public final int ID;

        /**
         * Wrap Mode Constructor.
         *
         * @param ID the OpenGL wrap mode identifier constant.
         */
        WrapMode(final int ID) {
            this.ID = ID;
        }
    }

    /**
     * Texture magnification filters determine how a texture will be drawn when
     * the dimensions of the textured surface are larger than the texture image.
     */
    public enum MagFilter {

        /**
         * Nearest neighbor interpolation samples the color closest to the pixel
         * center. Fastest magnification method but may result in aliasing or
         * shimmering.
         */
        Nearest(GL_NEAREST),
        /**
         * Linear interpolation samples the four colors nearest to the pixel
         * center and returns the color using weighted averages.
         */
        Linear(GL_LINEAR);

        /**
         * The OpenGL magnification filter constant.
         */
        public final int ID;

        /**
         * Magnification Filter Constructor.
         *
         * @param ID the OpenGL magnification filter constant.
         */
        MagFilter(final int ID) {
            this.ID = ID;
        }
    }

    public enum MinFilter {

        /**
         * Nearest neighbor interpolation samples the color closest to the pixel
         * center. Fastest minification method but may result in aliasing or
         * shimmering.
         */
        Nearest(GL_NEAREST, false),
        /**
         * Nearest neighbor interpolation with mipmaps samples the color closest
         * to the pixel center at the closest mipmap level.
         */
        NearestMipMap(GL_NEAREST_MIPMAP_NEAREST, true),
        /**
         * Linear interpolation samples the four colors nearest to the pixel
         * center and returns the color using weighted averages.
         */
        Linear(GL_LINEAR, false),
        /**
         * Linear interpolation with mipmaps samples the four colors nearest to
         * the pixel center at the closest mipmap level and returns the color
         * using weighted averages.
         */
        LinearNearest(GL_LINEAR_MIPMAP_NEAREST, true),
        /**
         * Nearest linear interpolation with mipmaps samples the four colors
         * nearest to the pixel center at the two closest mipmap levels and
         * returns the color using weighted averages.
         */
        NearestLinear(GL_NEAREST_MIPMAP_LINEAR, true),
        /**
         * Trilinear interpolation with mipmaps samples the nearest pixels on
         * the two closest mipmap levels and linear interpolates the results.
         * Results in a smoother degradation of texture quality as the mipmap
         * level changes.
         */
        Trilinear(GL_LINEAR_MIPMAP_LINEAR, true);

        /**
         * The OpenGL minification filter constant.
         */
        public final int ID;

        /**
         * Indicates if the minification filter mode uses mipmaps.
         */
        public final boolean MIPMAPS;

        /**
         * Minification Filter Constructor.
         *
         * @param ID the OpenGL minification filter constant.
         */
        MinFilter(final int ID, final boolean MIPMAPS) {
            this.ID = ID;
            this.MIPMAPS = MIPMAPS;
        }
    }

    /**
     * Determines the type of texture and its binding target.
     */
    protected final int TYPE;
    
    /**
     * Texture horizontal wrap mode.
     */
    protected WrapMode hWrap = WrapMode.Repeat;
    
    /**
     * Texture vertical wrap mode.
     */
    protected WrapMode vWrap = WrapMode.Repeat;

    /**
     * Texture minification filtering mode.
     */
    protected MinFilter minFilter = MinFilter.Linear;

    /**
     * Texture magnification filtering mode.
     */
    protected MagFilter magFilter = MagFilter.Linear;

    protected GLTexture(final int TYPE) {
        super(GLObject.Type.Texture);
        this.TYPE = TYPE;
    }
    
    protected GLTexture(final int TYPE, final MinFilter minFilter, final MagFilter magFilter) {
        super(GLObject.Type.Texture);
        this.TYPE = TYPE;
        this.minFilter = minFilter;
        this.magFilter = magFilter;
    }
}
