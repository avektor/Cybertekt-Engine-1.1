package net.cybertekt.input.listener;

import java.util.Collection;
import net.cybertekt.input.Input;

/**
 * Key Input Listener - (C) Cybertekt Software
 *
 * Interface for receiving and responding to keyboard input events.
 *
 * @version 1.1.0
 * @since 1.1.0
 * @author Andrew Vektor
 */
public interface KeyInputListener {

    public abstract void onKeyInput(final Input.Key input, final Input.State state, final Collection<Input.Mod> mods);
}
