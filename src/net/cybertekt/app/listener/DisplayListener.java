package net.cybertekt.app.listener;

/**
 * Application Display Listener - (C) Cybertekt Software
 *
 * Interface for responding events triggered by the application display.
 *
 * @version 1.1.0
 * @since 1.1.0
 * @author Andrew Vektor
 */
public interface DisplayListener {

    /**
     * Called when the application display has gained or lost input focus.
     *
     * @param focused true if the display has gained input focus. False if the
     * display has lost input focus.
     */
    public abstract void onFocus(final boolean focused);

    /**
     * Called when the application display has been iconified or restored from a
     * previously iconified state.
     *
     * @param iconified true if the display has been iconified. False if the
     * display has been restored from a previously iconified state.
     */
    public abstract void onIconify(final boolean iconified);

    /**
     * Called when the application display has been moved to a new location on
     * the virtual desktop.
     *
     * @param xPos the new x-axis position, in screen coordinates, of the
     * display on the virtual desktop.
     * @param yPos the new y-axis position, in screen coordinates, of the
     * display on the virtual desktop.
     */
    public abstract void onMove(final int xPos, final int yPos);

    /**
     * Called when the context (frame buffer) of the application display has
     * been resized.
     *
     * @param width the new width, in pixels, of the display context.
     * @param height the new height, in pixels, of the display context.
     */
    public abstract void onResize(final int width, final int height);

    /**
     * Called when the application display has been issued a request to close,
     * such as when the close widget of a windowed display is clicked by the
     * user or when the user presses alt-F4.
     */
    public abstract void onClose();
}
