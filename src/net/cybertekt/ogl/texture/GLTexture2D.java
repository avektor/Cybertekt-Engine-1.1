package net.cybertekt.ogl.texture;

import net.cybertekt.asset.image.Image;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

/**
 * OpenGL Texture Object - (C) Cybertekt Software
 *
 * @version 1.1.0
 * @since 1.1.0
 * @author Andrew Vektor
 */
public class GLTexture2D extends GLTexture {

    /**
     * The texture {@link Image image}.
     */
    private Image image;

    /**
     * Indicates if the image data has been uploaded to the GPU.
     */
    private boolean initialized;

    /**
     * Indicates if mipmaps have been generated for the texture image.
     */
    private boolean mipmaps;

    public GLTexture2D(final Image image) {
        super(GL_TEXTURE_2D);
        this.image = image;
    }
    
    public GLTexture2D(final Image image, final MinFilter minFilter, final MagFilter magFilter) {
        super(GL_TEXTURE_2D, minFilter, magFilter);
        this.image = image;
    }

    public final void bind() {

        // Bind Texture //
        glBindTexture(TYPE, ID);

        // Upload Texture Data //
        if (!initialized) {
            glTexImage2D(TYPE, 0, image.getFormat().ID, image.getWidth(), image.getHeight(), 0, image.getFormat().ID, image.getFormat().TYPE, image.getBuffer());
            initialized = true;
        }

        // Set Texture Parameters //
        glTexParameteri(TYPE, GL_TEXTURE_MIN_FILTER, minFilter.ID); // Minification Filter.
        glTexParameteri(TYPE, GL_TEXTURE_MAG_FILTER, magFilter.ID); // Magnification Filter.
        glTexParameteri(TYPE, GL_TEXTURE_WRAP_S, hWrap.ID); // Horizontal Wrap Mode.
        glTexParameteri(TYPE, GL_TEXTURE_WRAP_T, vWrap.ID); // Vertical Wrap Mode.

        // Generate Mip Maps If Needed //
        if (!mipmaps && minFilter.MIPMAPS) {
            glGenerateMipmap(TYPE);
            mipmaps = true;
        }
    }

    public final void setImage(final Image image) {
        this.image = image;
        initialized = false;
        mipmaps = false;
    }
}
