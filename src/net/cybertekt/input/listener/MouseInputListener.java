package net.cybertekt.input.listener;

import java.util.Collection;
import net.cybertekt.input.Input;

/**
 * Mouse Input Listener - (C) Cybertekt Software
 *
 * Interface for receiving and responding to mouse input events.
 *
 * @version 1.1.0
 * @since 1.1.0
 * @author Andrew Vektor
 */
public interface MouseInputListener {
    
    /**
     * Called when the {@link Input.State state} of a
     * {@link Input.Mouse mouse input} is changed.
     *
     * @param input
     * @param state
     * @param mods unmodifiable collection containing the list of activated
     * input modifiers in the order they were activated.
     */
    public abstract void onMouseInput(final Input.Mouse input, final Input.State state, final Collection<Input.Mod> mods);
}
