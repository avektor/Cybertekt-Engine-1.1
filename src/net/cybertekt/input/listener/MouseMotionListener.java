package net.cybertekt.input.listener;

/**
 * Mouse Motion Listener - (C) Cybertekt Software
 *
 * Interface that can be attached to a {@link Display display} in order to be
 * notified of events related to the movement of the cursor within the content
 * area of a display.
 *
 * @version 1.1.0
 * @since 1.1.0
 * @author Andrew Vektor
 */
public interface MouseMotionListener {

    /**
     * Called when the mouse cursor has entered or exited the content area of a
     * {@link Display display}. This method may never be called when the
     * listener is attached to a display that has its cursor mode set to locked.
     *
     * @param entered true if the mouse cursor has entered the content area of a
     * display. False if the mouse cursor has exited the content area of a
     * display.
     */
    public abstract void onMouseEnter(final boolean entered);

    /**
     * Called when the mouse cursor is moved within the content area of a
     * {@link Display display}. Receives the cursor position, in screen
     * coordinates, relative to the top-left corner of the display content area.
     * On platforms that provide it, the full sub-pixel cursor position will be
     * used. If the {@link CursorMode cursor mode} of the display is set to
     * {@link CursorMode#Locked locked} then the unbounded virtual cursor
     * position will be used. The virtual cursor position does not correspond to
     * a specific location within the display.
     *
     * @param xPos the x-axis position of the cursor, in screen coordinates,
     * relative to the top-left corner of the display content area. If the
     * cursor mode of the display is set to locked, then a virtual cursor
     * position will be provided that does not correspond to a particular
     * location within the content area of the display.
     * @param yPos the y-axis position of the cursor, in screen coordinates,
     * relative to the top-left corner of the display content area. If the
     * cursor mode of the display is set to locked, then a virtual cursor
     * position will be provided that does not correspond to a particular
     * location within the content area of the display.
     */
    public abstract void onMouseMove(final double xPos, final double yPos);

}
