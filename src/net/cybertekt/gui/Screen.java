package net.cybertekt.gui;

import net.cybertekt.app.listener.UpdateListener;
import net.cybertekt.render.Renderer;

/**
 * Screen - (C) Cybertekt Software
 * 
 * @version 1.1.0
 * @since 1.1.0
 * @author Andrew Vektor
 */
public class Screen implements UpdateListener, Renderer {
    
    private final String NAME;
    
    private final Element ROOT;
    
    public Screen(final String NAME) {
        this.NAME = NAME;
        ROOT = new Element(NAME + "-ROOT", Element.ChildLayout.HORIZONTAL, Element.ChildHAlign.LEFT, Element.ChildVAlign.TOP);
    }
    
    @Override
    public final void update() {
        
    }
    
    @Override
    public final void update(final double tpf) {
        
    }
    
    @Override
    public final void render() {
        
    }
}
