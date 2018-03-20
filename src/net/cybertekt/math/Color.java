package net.cybertekt.math;

import org.joml.Vector4f;

/**
 * Color - (C) Cybertekt Software
 * 
 * @version 1.1.0
 * @since 1.1.0
 * @author Andrew Vektor
 */
public class Color {
    
    public static final Color WHITE = new Color(1f, 1f, 1f, 1f);
    
    public static final Color BLACK = new Color(0, 0f, 0f, 1f);
    
    public static final Color RED = new Color(1f, 0f, 0f, 1f);
    
    public static final Color GREEN = new Color(0f, 1f, 0f, 1f);
    
    public static final Color BLUE = new Color(0f, 0f, 1f, 1f);
    
    private final Vector4f COLOR = new Vector4f(0f, 0f, 0f, 1f);
    
    private Color(final float red, final float green, final float blue, final float alpha) {
        COLOR.set(red, green, blue, alpha);
    }
    
    
}
