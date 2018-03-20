package net.cybertekt.app.display;

import org.joml.Vector2i;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_HIDDEN;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;

/**
 * Display Settings - (C) Cybertekt Software
 *
 * Defines settings used for the initialization of full screen and windowed
 * displays. Any changes made to the internal fields of this class will not have
 * an immediate effect unless the settings are reapplied to the application by
 * calling the {@link Application#setDisplaySettings(Settings)} method.
 *
 * @version 1.1.0
 * @since 1.1.0
 * @author Andrew Vektor
 */
public class DisplaySettings {

    /**
     * Defines the two types of displays that can be creating using this class.
     */
    public enum Type {
        /**
         * Windowed displays may have a border or decorations and cover a
         * specified area on a display device.
         */
        Windowed,
        /**
         * Full screen displays have no border or decorations and cover the
         * entire display area of a display device.
         */
        Fullscreen;
    }

    /**
     * Defines the different types of cursor modes. Each provides a special form
     * of mouse motion input.
     */
    public static enum CursorMode {
        /**
         * Cursor is visible and its movement is uninhibited.
         */
        Normal(GLFW_CURSOR_NORMAL),
        /**
         * Cursor is invisible while within the content area of the display and
         * its movement is uninhibited.
         */
        Hidden(GLFW_CURSOR_HIDDEN),
        /**
         * Cursor is invisible and its position is locked to the display,
         * providing virtual and unlimited cursor movement.
         */
        Locked(GLFW_CURSOR_DISABLED);

        /**
         * Stores the GLFW identifier for each cursor mode.
         */
        int id;

        /**
         * Defines a display cursor mode.
         *
         * @param id the GLFW identifier of the cursor mode.
         */
        CursorMode(final int id) {
            this.id = id;
        }

        /**
         * Returns the GLFW identifier for the cursor mode.
         *
         * @return the GLFW constant identifier for the cursor mode.
         */
        public final int getId() {
            return id;
        }
    }

    /**
     * Determines if the display will be full screen or windowed.
     */
    private Type type;

    /**
     * Determines how the mouse cursor interacts with the display.
     */
    private CursorMode cursorMode;

    /**
     * Determines the {@link DisplayDevice device} onto which the display will
     * be positioned. For full screen displays, this field determines which
     * display device will enter full screen exclusive mode. For windowed
     * displays, this field determines the positioning of the window on the
     * virtual desktop.
     */
    private DisplayDevice device;

    /**
     * Determines the title of the display. The title is displayed in the title
     * bar of decorated display windows. The title may only be displayed in the
     * task bar or other similar interfaces for full screen and undecorated
     * display windows.
     */
    private String title;

    /**
     * Determines the width and height, in screen coordinates, of the display.
     * For windowed displays, this is defined as the width and height of the
     * windows context area. For full screen displays, this value determines the
     * resolution the display device on which the display is created. This value
     * should not be set directly for full screen displays, it should instead be
     * set using a {@link DisplayMode display mode} supported by the target
     * display device.
     */
    private final Vector2i size = new Vector2i();

    /**
     * Determines the position, in screen coordinates, of the display. The
     * display will be positioned so that the upper-left corner of its content
     * area will have the screen coordinates defined by this value. This value
     * only applies to windowed displays and will be ignored for full screen
     * displays.
     */
    private final Vector2i position = new Vector2i();

    /**
     * Determines the refresh rate of the display. This value only applied to
     * full screen displays and will be ignored for windowed displays.
     */
    private int refreshRate;

    /**
     * Hint that indicates if the display is resizable by the user. This hint
     * only applies to windowed displays and will be ignored for full screen
     * displays. The default value is true.
     */
    private boolean resizable = true;

    /**
     * Hint that indicates if the display will be visible. This hint only
     * applies to windowed displays and will be ignored for full screen
     * displays. The default value is true.
     */
    private boolean visible = true;

    /**
     * Hint that indicates if the display will be given input focus when
     * created. This hint will be ignored for full screen displays and for
     * windowed displays that are hidden. The default value is true.
     */
    private boolean focused = true;

    /**
     * Hint that indicates if the display will automatically iconify and restore
     * the previous display mode when input focus is lost. This hint only
     * applies to full screen displays and will be ignored for windowed
     * displays. The default value is true.
     */
    private boolean autoIconify = true;

    /**
     * Hint that indicates if the display will float above other displays, also
     * known as always-on-top mode. This hint only applies to windowed displays
     * and will be ignored for full screen displays and hidden windowed
     * displays. The default value is false.
     */
    private boolean floating = false;

    /**
     * Hint that indicates if the display will be maximized when created. This
     * hint only applies to windowed displays and will be ignored for full
     * screen displays and windowed displays that are hidden. The default value
     * is false.
     */
    private boolean maximized = false;

    /**
     * Hint that indicates if the display will have a frame and widgets for
     * minimization, maximization, and close. This hint only applies to windowed
     * displays and will be ignored for full screen displays. The default value
     * is true.
     */
    private boolean decorated = true;

    /**
     * Hint that indicates if vertical synchronization should be enabled for the
     * display. When enabled, the number of frame rendered per second will not
     * exceed the refresh rate of the display device. The default value is true.
     */
    private boolean vsync = true;

    /**
     * Constructor that creates new settings by copying the values from another
     * instance of display settings.
     *
     * @param settings the display settings to use for creating the new
     * settings.
     */
    public DisplaySettings(final DisplaySettings settings) {
        setSettings(settings);
    }

    /**
     * Constructor that defines settings for a windowed display which will be
     * positioned in the center of the primary
     * {@link DisplayDevice display device}.
     *
     * @param title the title of the display window.
     * @param size the size of the display window specified in screen
     * coordinates.
     */
    public DisplaySettings(final String title, final Vector2i size) {
        this(DisplayDevice.getPrimaryDisplayDevice(), title, size);
    }

    /**
     * Constructor that defines settings for a windowed display which will be
     * positioned in the center of the specified
     * {@link DisplayDevice display device}.
     *
     * @param device the device on which to position the display window.
     * @param title the title of the display window.
     * @param size the size of the display window specified in screen
     * coordinates.
     */
    public DisplaySettings(final DisplayDevice device, final String title, final Vector2i size) {
        this(device, title, size, new Vector2i(device.getDisplayMode().getWidth() / 2 - size.x() / 2, device.getDisplayMode().getHeight() / 2 - size.y() / 2));
    }

    /**
     * Full constructor that defines settings for a windowed display and its
     * position on the specified {@link DisplayDevice display device}.
     *
     * @param device the device on which to position the display window.
     * @param title the title of the display window.
     * @param size the size of the display window specified in screen
     * coordinates.
     * @param position the position of the display window specified in screen
     * coordinates.
     */
    public DisplaySettings(final DisplayDevice device, final String title, final Vector2i size, final Vector2i position) {
        this.type = Type.Windowed;
        this.cursorMode = CursorMode.Normal;
        this.device = device;
        this.title = title;
        this.size.set(size);
        this.position.set(device.getPosition().x() + position.x(), device.getPosition().y() + position.y());
        this.refreshRate = device.getDisplayMode().getRefreshRate();
    }

    /**
     * Constructor that defines settings for a full screen display on the
     * primary {@link DisplayDevice display device} using the current
     * {@link DisplayMode display mode}.
     *
     * @param title the title of the full screen display.
     */
    public DisplaySettings(final String title) {
        this(title, DisplayDevice.getPrimaryDisplayDevice().getDisplayMode());
    }

    /**
     * Constructor that defines settings for a full screen display on the
     * primary {@link DisplayDevice display device} using the specified
     * {@link DisplayMode display mode}.
     *
     * @param title the title of the full screen display.
     * @param mode the display mode of the full screen display.
     */
    public DisplaySettings(final String title, final DisplayMode mode) {
        this(title, DisplayDevice.getPrimaryDisplayDevice(), mode);
    }

    /**
     * Full constructor that defines settings for a full screen display on a
     * specific {@link DisplayDevice display device} using the
     * {@link DisplayMode display mode} provided.
     *
     * @param title the title of the full screen display.
     * @param device the device on which to create the display.
     * @param mode the display mode of the full screen display.
     */
    public DisplaySettings(final String title, final DisplayDevice device, final DisplayMode mode) {
        this.type = Type.Fullscreen;
        this.cursorMode = CursorMode.Normal;
        this.title = title;
        this.device = device;
        this.size.set(mode.getWidth(), mode.getHeight());
        this.refreshRate = mode.getRefreshRate();
    }

    /**
     * Applies the specified display settings to these settings. Any changes
     * made to the provided display settings after this call <b>will not</b>
     * have any effect on these settings unless the provided settings are
     * reapplied to these settings via another call to this method.
     *
     * @param settings the display settings to apply.
     */
    public final void setSettings(final DisplaySettings settings) {
        this.type = settings.getType();
        this.cursorMode = settings.getCursorMode();
        this.device = settings.getDisplayDevice();
        this.title = settings.getTitle();
        this.size.set(settings.getWidth(), settings.getHeight());
        this.position.set(settings.getXPosition(), settings.getYPosition());
        this.refreshRate = settings.getRefreshRate();
        this.resizable = settings.isResizable();
        this.visible = settings.isVisible();
        this.focused = settings.isFocused();
        this.autoIconify = settings.isAutoIconify();
        this.floating = settings.isFloating();
        this.maximized = settings.isMaximized();
        this.decorated = settings.isDecorated();
        this.vsync = settings.isVerticalSync();

    }

    /**
     * Returns the display {@link Type type}, either {@link Type#Windowed} or
     * {@link Type#Fullscreen}.
     *
     * @param toSet the display type.
     */
    public final void setType(final Type toSet) {
        this.type = toSet;
    }

    /**
     * Returns the {@link Type type} of display, either {@link Type#Windowed} or
     * {@link Type#Fullscreen}.
     *
     * @return the display type.
     */
    public final Type getType() {
        return type;
    }

    /**
     * Sets the {@link CursorMode cursor mode} of the display. The cursor mode
     * determines how the cursor is treated by the display.
     *
     * @param mode the cursor mode to set for the display.
     */
    public final void setCursorMode(final CursorMode mode) {
        this.cursorMode = mode;
    }

    /**
     * Returns the {@link CursorMode cursor mode} of the display. The cursor
     * mode determines how the cursor is treated by the display.
     *
     * @return the cursor mode of the display.
     */
    public final CursorMode getCursorMode() {
        return cursorMode;
    }

    /**
     * Sets the {@link DisplayDevice display device} onto which the display will
     * be positioned. For full screen displays, this field determines which
     * display device will enter full screen mode. For windowed displays, this
     * field determines which device the window will be placed upon.
     *
     * @param device the display device on which to position the display.
     */
    public final void setDisplayDevice(final DisplayDevice device) {
        this.device = device;
    }

    /**
     * Returns the {@link DisplayDevice display device} onto which the display
     * will be positioned. For full screen displays, this determines which
     * display device will enter full screen mode. For windowed displays, this
     * determines which device the window will be placed upon.
     *
     * @return the display device on which the display will be positioned.
     */
    public final DisplayDevice getDisplayDevice() {
        return device;
    }

    /**
     * Sets the width, height, and refresh rate settings using the provided
     * {@link DisplayMode display mode}.
     *
     * @param mode the display mode of the display.
     */
    public final void setDisplayMode(final DisplayMode mode) {
        this.size.set(mode.getWidth(), mode.getHeight());
        this.refreshRate = mode.getRefreshRate();
    }

    /**
     * Sets the title of the display. The title is displayed in the title bar of
     * decorated display windows. Titles may only be displayed in the task bar
     * or other similar interfaces for full screen and undecorated display
     * windows.
     *
     * @param toSet the title of the display.
     */
    public final void setTitle(final String toSet) {
        this.title = toSet;
    }

    /**
     * Returns the title of the display. The title is displayed in the title bar
     * of decorated display windows. Titles may only be displayed in the task
     * bar or other similar interfaces for full screen and undecorated display
     * windows.
     *
     * @return the title of the display.
     */
    public final String getTitle() {
        return title;
    }

    /**
     * Sets the size, in screen coordinates, of the display. For windowed
     * displays, this defines the width and height of the content area of the
     * window. For full screen displays, this value defines the resolution of
     * the display mode on the display device.
     *
     * @param size the width and height, in screen coordinates, of the display.
     */
    public final void setSize(final Vector2i size) {
        this.size.set(size);
    }

    /**
     * Sets the width and height, in screen coordinates, of the display. For
     * windowed displays, this defines the width and height of the content area
     * of the window. For full screen displays, this value defines the
     * resolution of the the display mode on the display device.
     *
     * @param width the width, in screen coordinates, of the display.
     * @param height the height, in screen coordinates, of the display.
     */
    public final void setSize(final int width, final int height) {
        this.size.set(width, height);
    }

    /**
     * Returns the width and height, in screen coordinates, of the display. For
     * windowed displays, this defines the width and height of the content area
     * of the window. For full screen displays, this value defines the
     * resolution of the the display mode on the display device.
     *
     * @return the size of the display in screen coordinates.
     */
    public final Vector2i getSize() {
        return new Vector2i(size);
    }

    /**
     * Returns the width, in screen coordinates, of the display. For windowed
     * displays, this returns the width of the content area of the window. For
     * full screen displays, this value defines the width of the display mode.
     *
     * @return the width, in screen coordinates, of the display.
     */
    public final int getWidth() {
        return size.x();
    }

    /**
     * Returns the height, in screen coordinates, of the display. For windowed
     * displays, this returns the height of the content area of the window. For
     * full screen displays, this value returns the height of the display mode.
     *
     * @return the width, in screen coordinates, of the display.
     */
    public final int getHeight() {
        return size.y();
    }

    /**
     * Sets the position, in screen coordinates, of the display. The display
     * will be positioned so that the upper-left corner of its content area will
     * have the screen coordinates defined by this value. This value only
     * applies to windowed displays and will be ignored for full screen
     * displays.
     *
     * @param position the position, in screen coordinates, of the upper-left
     * corner of the display content area.
     */
    public final void setPosition(final Vector2i position) {
        this.position.set(position);
    }

    /**
     * Sets the position, in screen coordinates, of the display relative to the
     * position of its parent {@link DisplayDevice device}. The display will be
     * positioned so that the upper-left corner of its content area will have
     * the screen coordinates defined by this value. This value only applies to
     * windowed displays and will be ignored for full screen displays.
     *
     * @param xPosition the x-axis position, in screen coordinates, of the
     * upper-left corner of the display content area.
     * @param yPosition the y-axis position, in screen coordinates, of the
     * upper-left corner of the display content area.
     */
    public final void setPosition(final int xPosition, final int yPosition) {
        this.position.set(xPosition, yPosition);
    }

    /**
     * Returns the position, in screen coordinates, of the display relative to
     * the position of its parent {@link DisplayDevice device}. The returned
     * position is the location of the upper-left corner of the display content
     * area.
     *
     * @return the position of the display in screen coordinates.
     */
    public final Vector2i getPosition() {
        return new Vector2i(position);
    }

    /**
     * Returns the x-axis position, in screen coordinates, of the upper-left
     * corner of the display content area relative to the position of its parent
     * {@link DisplayDevice device}. This value only applies to windowed
     * displays. This method will always return the virtual position of the
     * parent {@link DisplayDevice display device} for full screen displays.
     *
     * @return the x-axis position, in screen coordinates, of the upper-left
     * corner of the display content area.
     */
    public final int getXPosition() {
        if (type == Type.Windowed) {
            return device.getPosition().x() + position.x();
        }
        return device.getPosition().x();
    }

    /**
     * Returns the y-axis position, in screen coordinates, of the upper-left
     * corner of the display content area relative to the position of its parent
     * {@link DisplayDevice device}. This value only applies to windowed
     * displays. This method will return the virtual position of the parent
     * {@link DisplayDevice display device} for full screen displays.
     *
     * @return the y-axis position, in screen coordinates, of the upper-left
     * corner of the display content area.
     */
    public final int getYPosition() {
        if (type == Type.Windowed) {
            return device.getPosition().y() + position.y();
        }
        return device.getPosition().y();
    }

    /**
     * Returns the refresh rate, in Hz, of the display.
     *
     * @return the display refresh rate in Hz.
     */
    public final int getRefreshRate() {
        return refreshRate;
    }

    /**
     * Sets the hint that indicates if the display should be resizable by the
     * user. This hint only applies to windowed displays and will be ignored for
     * full screen displays. Default value is true.
     *
     * @param resizable true to allow the user to resize the display window.
     * False to prevent the user from resizing the display window.
     */
    public final void setResizable(final boolean resizable) {
        this.resizable = resizable;
    }

    /**
     * Returns the value of the hint that indicates if the display is resizable
     * by the user. This hint only applies to windowed displays. This method
     * will always return false for {@link Type#Fullscreen full screen}
     * displays.
     *
     * @return true if the display window is resizable by the user. False if the
     * display window is not resizable by the user.
     */
    public final boolean isResizable() {
        if (type == Type.Windowed) {
            return resizable;
        }
        return false;
    }

    /**
     * Sets the hint that indicates if the display should be visible. This hint
     * only applies to windowed displays and will be ignored for full screen
     * displays. Default value is true.
     *
     * @param visible true if the display should be visible. False if the
     * display should be hidden.
     */
    public final void setVisible(final boolean visible) {
        this.visible = visible;
    }

    /**
     * Returns the value of the hint that indicates if the display is visible.
     * This hint only applies to windowed displays. This method will always
     * return true for {@link Type#Fullscreen full screen} displays.
     *
     * @return true if the display is visible. False if the display is hidden.
     */
    public final boolean isVisible() {
        if (type == Type.Windowed) {
            return visible;
        }
        return true;
    }

    /**
     * Sets the hint that indicates if the display should be given initial input
     * focus. This hint only applies to windowed displays and will be ignored
     * for full screen displays. This hint is also ignored for windowed displays
     * that are hidden. Default value is true.
     *
     * @param focused true if the display should be given initial input focus.
     * False if the display should not be given initial input focus.
     */
    public final void setFocused(final boolean focused) {
        this.focused = focused;
    }

    /**
     * Returns the hint that indicates if the display will be given initial
     * input focus. This hint only applies to windowed displays. This method
     * will always return true for {@link Type#Fullscreen full screen} displays.
     *
     * @return true if the display will be given initial input focus. False if
     * the display will not be given initial input focus.
     */
    public final boolean isFocused() {
        if (type == Type.Windowed) {
            return focused;
        }
        return true;
    }

    /**
     * Sets the hint that indicates if the display will automatically iconify
     * and restore the previous {@link DisplayMode display mode} when input
     * focus is lost. This hint only applies to full screen displays and will be
     * ignored for windowed displays. Default value is true.
     *
     * @param autoIconify true if the displays should automatically iconify and
     * restore the previous display mode when input focus is lost.
     */
    public final void setAutoIconify(final boolean autoIconify) {
        this.autoIconify = autoIconify;
    }

    /**
     * Returns the hint that indicates if the display will automatically iconify
     * and restore the previous {@link DisplayMode display mode} when input
     * focus is lost. This hint only applies to full screen displays. This
     * method will always return false for {@link Type#Windowed windowed}
     * displays.
     *
     * @return true if the displays should automatically iconify and restore the
     * previous display mode when input focus is lost.
     */
    public final boolean isAutoIconify() {
        if (type == Type.Fullscreen) {
            return autoIconify;
        }
        return false;
    }

    /**
     * Sets the hint that indicates if the display will float above other
     * displays, also known as always-on-top mode. This hint only applies to
     * windowed displays and will be ignored for full screen displays. Default
     * value is false.
     *
     * @param floating true if the display will float above other displays.
     */
    public final void setFloating(final boolean floating) {
        this.floating = floating;
    }

    /**
     * Sets the hint that indicates if the display will float above other
     * displays, also known as always-on-top mode. This hint only applies to
     * windowed displays. This method will always return false for
     * {@link Type#Fullscreen full screen} displays.
     *
     * @return true if the display will float above other displays.
     */
    public final boolean isFloating() {
        if (type == Type.Windowed) {
            return floating;
        }
        return false;
    }

    /**
     * Sets the hint that indicates if the display will be maximized. This hint
     * only applies to windowed displays and will be ignored for full screen
     * displays. Default value is false.
     *
     * @param maximized true the display should be maximized.
     */
    public final void setMaximized(final boolean maximized) {
        this.maximized = maximized;
    }

    /**
     * Sets the hint that indicates if the display is maximized. This hint only
     * applies to windowed displays. This method will always return false for
     * {@link Type#Fullscreen full screen} displays.
     *
     * @return true if the display is maximized.
     */
    public final boolean isMaximized() {
        if (type == Type.Windowed) {
            return maximized;
        }
        return false;
    }

    /**
     * Sets the hint that indicates if the display window should have a frame
     * and widgets for minimization, maximization, and close. This hint only
     * applies to windowed displays and will be ignored for full screen
     * displays. The default value is true.
     *
     * @param decorated true if the windowed display should be decorated.
     */
    public final void setDecorated(final boolean decorated) {
        this.decorated = decorated;
    }

    /**
     * Returns the hint that indicates if the display window has a frame and
     * widgets for minimization, maximization, and close. This hint only applies
     * to windowed displays. This method will always return false for
     * {@link Type#Fullscreen full screen} displays.
     *
     * @return true if the windowed display is decorated.
     */
    public final boolean isDecorated() {
        if (type == Type.Windowed) {
            return decorated;
        }
        return false;
    }

    /**
     * Sets the hint that indicates if vertical synchronization should be
     * enabled on the display. When enabled, the number of frame rendered per
     * second will not exceed the refresh rate of the display device. The
     * default value is true.
     *
     * @param vsync true to enable vertical synchronization, false to disable
     * vertical synchronization.
     */
    public final void setVerticalSync(final boolean vsync) {
        this.vsync = vsync;
    }

    /**
     * Returns the hint that indicates if vertical synchronization is enabled on
     * the display. When enabled, the number of frame rendered per second will
     * not exceed the refresh rate of the display device.
     *
     * @return true if vertical synchronization is enabled, false if vertical
     * synchronization is disable.
     */
    public final boolean isVerticalSync() {
        return vsync;
    }

}
