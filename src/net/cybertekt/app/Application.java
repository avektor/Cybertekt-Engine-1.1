package net.cybertekt.app;

import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import static net.cybertekt.app.display.DisplayDevice.DEVICE_CALLBACK;
import net.cybertekt.app.listener.DisplayListener;
import net.cybertekt.app.display.DisplayMode;
import net.cybertekt.app.display.DisplaySettings;
import net.cybertekt.app.display.DisplaySettings.CursorMode;
import net.cybertekt.app.listener.UpdateListener;
import net.cybertekt.exception.InitializationException;
import net.cybertekt.input.Input;
import net.cybertekt.input.InputMapping;
import net.cybertekt.input.listener.CharInputListener;
import net.cybertekt.input.listener.KeyInputListener;
import net.cybertekt.input.listener.MouseInputListener;
import net.cybertekt.input.listener.MouseMotionListener;
import net.cybertekt.input.listener.MouseScrollListener;
import net.cybertekt.ogl.GLObject;
import net.cybertekt.render.Renderer;
import org.joml.Vector2i;
import static org.lwjgl.glfw.GLFW.GLFW_AUTO_ICONIFY;
import static org.lwjgl.glfw.GLFW.GLFW_CLIENT_API;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_DECORATED;
import static org.lwjgl.glfw.GLFW.GLFW_FLOATING;
import static org.lwjgl.glfw.GLFW.GLFW_FOCUSED;
import static org.lwjgl.glfw.GLFW.GLFW_ICONIFIED;
import static org.lwjgl.glfw.GLFW.GLFW_MAXIMIZED;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_API;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_FORWARD_COMPAT;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwGetCurrentContext;
import static org.lwjgl.glfw.GLFW.glfwGetFramebufferSize;
import static org.lwjgl.glfw.GLFW.glfwGetVersionString;
import static org.lwjgl.glfw.GLFW.glfwGetWindowAttrib;
import static org.lwjgl.glfw.GLFW.glfwHideWindow;
import static org.lwjgl.glfw.GLFW.glfwIconifyWindow;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwMaximizeWindow;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwRestoreWindow;
import static org.lwjgl.glfw.GLFW.glfwSetCharCallback;
import static org.lwjgl.glfw.GLFW.glfwSetCursorEnterCallback;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowAttrib;
import static org.lwjgl.glfw.GLFW.glfwSetWindowCloseCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowFocusCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowIconifyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowMonitor;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowRefreshCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowTitle;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWWindowCloseCallback;
import org.lwjgl.glfw.GLFWWindowFocusCallback;
import org.lwjgl.glfw.GLFWWindowIconifyCallback;
import org.lwjgl.glfw.GLFWWindowPosCallback;
import org.lwjgl.glfw.GLFWWindowRefreshCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_STENCIL_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glGetString;
import static org.lwjgl.opengl.GL11.glViewport;
import org.lwjgl.system.Configuration;
import org.lwjgl.system.MemoryStack;
import static org.lwjgl.system.MemoryUtil.NULL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Application - (C) Cybertekt Software
 *
 * Provides the foundation for Cybertekt Engine applications. This class is
 * responsible for managing the application display, input polling, and the
 * timing of the {@link #loop() main loop}. Each application has a single GLFW
 * display and OpenGL rendering context.
 *
 * @version 1.1.0
 * @since 1.1.0
 * @author Andrew Vektor
 */
public abstract class Application {

    /**
     * SLF4J Application Class Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    /**
     * List of {@link Renderer renderers} attached to the application.
     */
    private final Set<Renderer> RENDERERS = new LinkedHashSet<>();

    /**
     * List of {@link UpdateListener update listeners} attached to the
     * application.
     */
    private final Set<UpdateListener> UPDATE_LISTENERS = new LinkedHashSet<>();

    /**
     * List of {@link DisplayListener display listeners} attached to the
     * application.
     */
    private final Set<DisplayListener> DISPLAY_LISTENERS = new HashSet<>();

    /**
     * List of {@link MouseMotionListener mouse motion listeners} attached to
     * the application.
     */
    private final Set<MouseMotionListener> MOUSE_MOTION_LISTENERS = new HashSet<>();

    /**
     * List of {@link MouseScrollListener mouse scroll listeners} attached to
     * the application.
     */
    private final Set<MouseScrollListener> MOUSE_SCROLL_LISTENERS = new HashSet<>();

    /**
     * List of {@link MouseInputListener mouse input listeners} attached to the
     * application.
     */
    private final Set<MouseInputListener> MOUSE_INPUT_LISTENERS = new HashSet();

    /**
     * List of {@link KeyInputListener key input listeners} attached to the
     * application.
     */
    private final Set<KeyInputListener> KEY_INPUT_LISTENERS = new HashSet();

    /**
     * List of {@link CharInputListener character input listeners} attached to
     * the application.
     */
    private final Set<CharInputListener> CHAR_INPUT_LISTENERS = new HashSet();

    /**
     * List of {@link InputMapping input mappings} attached to the application.
     */
    private final Set<InputMapping> INPUT_MAPPINGS = new HashSet();

    /**
     * Set that contains a list of activated {@link Input.Mod input modifiers}
     * stored in the same order that they were activated.
     */
    private final Set<Input.Mod> MOD_INPUT_SET = new LinkedHashSet<>();

    /**
     * Application {@link AppSettings settings}.
     */
    private AppSettings appSettings;

    /**
     * Application {@link DisplaySettings display settings}.
     */
    private DisplaySettings displaySettings;

    /**
     * Indicates if the application has been initialized.
     */
    private boolean initialized;

    /**
     * GLFW identifier assigned to the application display.
     */
    private long display;

    /**
     * Tracks the number of {@link #update()} calls each second.
     */
    private int ups;

    /**
     * Tracks the number of {@link #render()} calls made each second.
     */
    private int fps;

    /**
     * Static initializer that initializes LWJGL and GLFW.
     */
    static {
        // LWJGL Configuration //
        Configuration.DEBUG_MEMORY_ALLOCATOR.set(Boolean.TRUE);
        Configuration.DEBUG_STACK.set(Boolean.TRUE);
        //Configuration.DEBUG.set(Boolean.TRUE);
        Configuration.STACK_SIZE.set(64);

        // Initialize LWJGL //
        LOG.info("LWJGL {} Initialized", org.lwjgl.Version.getVersion());

        // Initialize GLFW //
        if (!glfwInit()) {
            throw new InitializationException("GLFW could not be initialized.");
        }
        LOG.info("GLFW {} Initialized", glfwGetVersionString());

    }

    /**
     * Initializes the application using the settings provided. This method can
     * only be called once unless the application has been terminated by calling
     * the {@link #stop()} method. This method initializes the application
     * display and then beings executing the {@link #loop() main loop}. The
     * application settings are used to define the name and version of the
     * application and also determines the frequency as which the application is
     * updated. The display settings provided for initialization are copied over
     * into a new display settings instance maintained by the application. The
     * application will update its own internal copy of display settings if any
     * changes are made to the display while the application is running. The
     * original display settings provided to this method will not be modified in
     * any way by the application. This allows the user to preserve the original
     * display settings used for initialization. To retrieve a current snapshot
     * of the application display settings, use {@link #getDisplaySettings()}.
     *
     *
     * @param appSettings the application initialization settings.
     * @param displaySettings the application display initialization settings.
     */
    public final void initialize(final AppSettings appSettings, final DisplaySettings displaySettings) {
        if (!initialized) {

            // Set Application Settings //
            this.appSettings = appSettings;
            this.displaySettings = new DisplaySettings(displaySettings);

            // Set Current Time Before Initialization //
            long time = System.nanoTime();

            // Set GLFW Error Callback //
            glfwSetErrorCallback(ERROR_CALLBACK);

            // Apply Application Display Creation Hints //
            glfwDefaultWindowHints(); // Apply Default Hints
            glfwWindowHint(GLFW_RESIZABLE, displaySettings.isResizable() ? GL_TRUE : GL_FALSE);
            glfwWindowHint(GLFW_VISIBLE, displaySettings.isVisible() ? GL_TRUE : GL_FALSE);
            glfwWindowHint(GLFW_FOCUSED, displaySettings.isFocused() ? GL_TRUE : GL_FALSE);
            glfwWindowHint(GLFW_AUTO_ICONIFY, displaySettings.isAutoIconify() ? GL_TRUE : GL_FALSE);
            glfwWindowHint(GLFW_FLOATING, displaySettings.isFloating() ? GL_TRUE : GL_FALSE);
            glfwWindowHint(GLFW_MAXIMIZED, displaySettings.isMaximized() ? GL_TRUE : GL_FALSE);
            glfwWindowHint(GLFW_DECORATED, displaySettings.isDecorated() ? GL_TRUE : GL_FALSE);

            // OpenGL Context Creation Hints //
            glfwWindowHint(GLFW_CLIENT_API, GLFW_OPENGL_API);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 5);
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

            // Application Display Creation //
            switch (displaySettings.getType()) {

                // Create Windowed Application Display //
                case Windowed: {
                    display = glfwCreateWindow(displaySettings.getWidth(), displaySettings.getHeight(), displaySettings.getTitle(), NULL, NULL);
                    glfwSetWindowPos(display, displaySettings.getXPosition(), displaySettings.getYPosition()); // Set Window Position //
                    break;
                }

                // Create Full Screen Application Display //
                case Fullscreen: {
                    display = glfwCreateWindow(displaySettings.getWidth(), displaySettings.getHeight(), displaySettings.getTitle(), displaySettings.getDisplayDevice().getId(), NULL);
                    break;
                }
            }

            // Initialize Application Display //
            if (display > 0) {

                // Attach Display Event Callbacks //
                glfwSetWindowRefreshCallback(display, REFRESH_CALLBACK);
                glfwSetWindowFocusCallback(display, FOCUS_CALLBACK);
                glfwSetWindowIconifyCallback(display, ICONIFY_CALLBACK);
                glfwSetWindowCloseCallback(display, CLOSE_CALLBACK);
                glfwSetWindowPosCallback(display, MOVE_CALLBACK);
                glfwSetWindowSizeCallback(display, SIZE_CALLBACK);
                glfwSetFramebufferSizeCallback(display, RESIZE_CALLBACK);

                // Attach Mouse Input Callbacks //
                glfwSetCursorEnterCallback(display, MOUSE_ENTER_CALLBACK);
                glfwSetCursorPosCallback(display, MOUSE_MOTION_CALLBACK);
                glfwSetScrollCallback(display, MOUSE_SCROLL_CALLBACK);
                glfwSetMouseButtonCallback(display, MOUSE_BUTTON_CALLBACK);

                // Attach Keyboard Input Callbacks //
                glfwSetKeyCallback(display, KEY_CALLBACK);
                glfwSetCharCallback(display, CHAR_CALLBACK);

                // Set Cursor Mode //
                glfwSetInputMode(display, GLFW_CURSOR, displaySettings.getCursorMode().getId());

                // Create OpenGL Context Capabilities //
                glfwMakeContextCurrent(display);
                GL.createCapabilities();

                // Set Vertical Synchronization //
                glfwSwapInterval(displaySettings.isVerticalSync() ? GL_TRUE : GL_FALSE);
            } else {

                // Display Initialization Failure //
                LOG.error("Unable to create application display");
                throw new InitializationException("Unable to create application display.");
            }

            LOG.info("OpenGL Version {}", glGetString(GL_VERSION));

            // Application Subclass Initialization //
            init();
            initialized = true;

            // Log Time Required For Initialization //
            LOG.info("{} Version {} Initialized in {}ms", appSettings.getName(), appSettings.getVersion(), (System.nanoTime() - time) / 1_000_000);

            // Application Main Loop Start //
            loop();
        }
    }

    /**
     * Main Application Loop. This methods polls input, updates the application,
     * and renders the current frame.
     */
    private void loop() {

        // For calculating number of render and update calls each second //
        long time = System.nanoTime();
        int upsCount = 0;
        int frameCount = 0;

        // Calculates the timing of the update calls. //
        long last = System.nanoTime() / 1_000_000;
        long current, delta;
        long accumulated = 0;

        // Application Main Loop //
        while (initialized) {

            // Clean OpenGL Objects //
            GLObject.clean();

            // Fixed Update Time Step Control Variables //
            current = System.nanoTime() / 1_000_000;
            delta = current - last;
            last = current;
            accumulated += delta;

            // Poll Input //
            glfwPollEvents();

            // Fixed Time Step Update //
            for (int count = 0; accumulated >= appSettings.getMsPerUpdate() && count <= appSettings.getMaxUpdates(); count++) {
                for (final UpdateListener listener : UPDATE_LISTENERS) {
                    listener.update();
                }
                accumulated -= appSettings.getMsPerUpdate();
                upsCount++;
            }

            // Variable Time Step Update //
            for (final UpdateListener listener : UPDATE_LISTENERS) {
                listener.update(delta);
            }

            // Set OpenGL Context //
            if (glfwGetCurrentContext() != display) {
                glfwMakeContextCurrent(display);
            }

            // Clear Display //
            glClearColor(0f, 0f, 0f, 0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);

            // Render Next Frame //
            RENDERERS.stream().forEach(renderer -> {
                renderer.render();
            });

            // Swap Frame Buffers //
            glfwSwapBuffers(display);

            // Increment Frame Counter //
            frameCount++;

            // Calculate UPS and FPS Once Each Second //
            delta = (System.nanoTime() - time) / 1_000_000_000;
            if (delta > 1) {
                ups = (int) (upsCount / delta);
                fps = (int) (frameCount / delta);
                time = System.nanoTime();
                upsCount = 0;
                frameCount = 0;
            }
        }
        // Application Exit //
        exit();

        // Free All GLFW Callbacks //
        ERROR_CALLBACK.free();
        REFRESH_CALLBACK.free();
        FOCUS_CALLBACK.free();
        ICONIFY_CALLBACK.free();
        SIZE_CALLBACK.free();
        CLOSE_CALLBACK.free();
        MOVE_CALLBACK.free();
        RESIZE_CALLBACK.free();
        MOUSE_ENTER_CALLBACK.free();
        MOUSE_MOTION_CALLBACK.free();
        MOUSE_SCROLL_CALLBACK.free();
        MOUSE_BUTTON_CALLBACK.free();
        KEY_CALLBACK.free();
        CHAR_CALLBACK.free();
        DEVICE_CALLBACK.free();

        // Terminate GLFW //
        glfwTerminate();
        LOG.info("{} - Version {} Terminated Successfully", appSettings.getName(), appSettings.getVersion());
    }

    /**
     * Forces the application to close, preventing any further calls to the
     * {@link #update()} or {@link #render()} methods. The application may be
     * restarted via another call to {@link #initialize()}.
     */
    public final void stop() {
        initialized = false;
    }

    /**
     * This method is called once each time the application is
     * {@link #initialize() initialized}. This method should be used to
     * initialize any resources needed before the main loop beings executing.
     */
    protected abstract void init();

    /**
     * This method is called once after the {@link #loop{} main loop} has exited
     * but before GLFW has been terminated.
     */
    protected abstract void exit();

    /**
     * Attaches the specified {@link Renderer renderer} to this application. The
     * {@link Renderer#render()} method of each renderer attached to this
     * application will be called once per frame. This method does nothing if
     * the specified renderer is already attached to the application.
     *
     * @param toAdd the renderer to attach to the application.
     * @return true if the renderer was successfully attached to the
     * application.
     */
    public final boolean addRenderer(final Renderer toAdd) {
        return RENDERERS.add(toAdd);
    }

    /**
     * Detaches the specified {@link Renderer renderer} from the application.
     * The {@link Renderer#render()} method of the specified render will no
     * longer be called once per frame by the application. This method does
     * nothing if the specified renderer is not attached to the application.
     *
     * @param toRemove the renderer to detach from the application.
     * @return true if the renderer was successfully detached from the
     * application.
     */
    public final boolean removeRenderer(final Renderer toRemove) {
        return RENDERERS.remove(toRemove);
    }

    /**
     * Removes all {@link Renderer renderers} attached to the application.
     */
    public final void clearRenderers() {
        RENDERERS.clear();
    }

    /**
     * Attaches the specified {@link UpdateListener update listener} to this
     * application. The attached listener will receive applicate update events.
     * This method does nothing if the specified update listener is already
     * attached to the application.
     *
     * @param toAdd the update listener to attach to the application.
     * @return ture if the listener was successfully attached to the
     * application.
     */
    public final boolean addUpdateListener(final UpdateListener toAdd) {
        return UPDATE_LISTENERS.add(toAdd);
    }

    /**
     * Detaches the specified {@link UpdateListener update listener} from the
     * application. The detached listener will no longer receive application
     * update events. This method does nothing if the specified update listener
     * is not attached to the application.
     *
     * @param toRemove the update listener to detach from the application.
     * @return true if the listener was successfully detached from the
     * application.
     */
    public final boolean removeUpdateListener(final UpdateListener toRemove) {
        return UPDATE_LISTENERS.remove(toRemove);
    }

    /**
     * Removes all {@link UpdateListener update listeners} attached to the
     * application.
     */
    public final void clearUpdateListeners() {
        UPDATE_LISTENERS.clear();
    }

    /**
     * Attaches the specified {@link DisplayListener display listener} to this
     * application. The attached listener will receive events triggered by the
     * application display. This method does nothing if the specified display
     * listener is already attached to the application.
     *
     * @param toAdd the display listener to attach to the application.
     * @return true if the listener was successfully attached to the
     * application.
     */
    public final boolean addDisplayListener(final DisplayListener toAdd) {
        return DISPLAY_LISTENERS.add(toAdd);
    }

    /**
     * Detaches the specified {@link DisplayListener display listener} from the
     * application. The listener will no longer receive events triggered by the
     * application display. This method does nothing if the specified display
     * listener is not attached to the application.
     *
     * @param toRemove the display listener to detach from the application.
     * @return true if the listener was successfully detached from the
     * application.
     */
    public final boolean removeDisplayListener(final DisplayListener toRemove) {
        return DISPLAY_LISTENERS.remove(toRemove);
    }

    /**
     * Removes all {@link DisplayListener display listeners} attached to the
     * application.
     */
    public final void clearDisplayListeners() {
        DISPLAY_LISTENERS.clear();
    }

    /**
     * Attaches a {@link MouseMotionListener mouse motion listener} to the
     * application. The attached listener will receive cursor movement events
     * trigger by the application display when the cursor is within the content
     * area of the application display. This method does nothing if the
     * specified mouse motion listener is already attached to the application.
     *
     * @param toAdd the mouse motion listener to attach to the application.
     * @return true if the listener was successfully attached to the
     * application.
     */
    public final boolean addMouseMotionListener(final MouseMotionListener toAdd) {
        return MOUSE_MOTION_LISTENERS.add(toAdd);
    }

    /**
     * Detached the specified {@link MouseMotionListener mouse motion listener}
     * from the application. The listener will no longer receive cursor movement
     * events triggered by the application display. This method does nothing if
     * the specified mouse motion listener is not attached to the application.
     *
     * @param toRemove the mouse motion listener to detach from the application.
     * @return true if the listener was successfully detached from the
     * application.
     */
    public final boolean removeMouseMotionListener(final MouseMotionListener toRemove) {
        return MOUSE_MOTION_LISTENERS.remove(toRemove);
    }

    /**
     * Removes all {@link MouseMotionListener mouse motion listeners} attached
     * to the application.
     */
    public final void clearMouseMotionListeners() {
        MOUSE_MOTION_LISTENERS.clear();
    }

    /**
     * Attaches a {@link MouseScrollListener mouse scroll listener} to the
     * application. The attached listener will receive mouse scroll wheel events
     * trigger by the application display. This method does nothing if the
     * specified mouse scroll listener is already attached to the application.
     *
     * @param toAdd the mouse scroll listener to attach to the application.
     * @return true if the listener was successfully attached to the
     * application.
     */
    public final boolean addMouseScrollListener(final MouseScrollListener toAdd) {
        return MOUSE_SCROLL_LISTENERS.add(toAdd);
    }

    /**
     * Detached the specified {@link MouseScrollListener mouse scroll listener}
     * from the application. The listener will no longer receive mouse scroll
     * wheel events triggered by the application display. This method does
     * nothing if the specified mouse motion listener is not attached to the
     * application.
     *
     * @param toRemove the mouse scroll listener to detach from the application.
     * @return true if the listener was successfully detached from the
     * application.
     */
    public final boolean removeMouseScrollListener(final MouseScrollListener toRemove) {
        return MOUSE_SCROLL_LISTENERS.remove(toRemove);
    }

    /**
     * Removes all {@link MouseScrollListener mouse scroll listeners} attached
     * to the application.
     */
    public final void clearMouseScrollListeners() {
        MOUSE_SCROLL_LISTENERS.clear();
    }

    /**
     * Attaches a {@link MouseInputListener mouse input listener} to the
     * application. The attached listener will receive mouse input events
     * triggered by the application display. This method does nothing if the
     * specified mouse input listener is already attached to the application.
     *
     * @param toAdd the mouse input listener to attach to the application.
     * @return true if the listener was successfully attached to the
     * application.
     */
    public final boolean addMouseInputListener(final MouseInputListener toAdd) {
        return MOUSE_INPUT_LISTENERS.add(toAdd);
    }

    /**
     * Detaches the specified {@link MouseInputListener mouse input listener}
     * from the application. The listener will no longer receive mouse input
     * events triggered by the application display. This method does nothing if
     * the specified mouse input listener is not attached to the application.
     *
     * @param toRemove the mouse input listener to detach from the application.
     * @return true if the listener was successfully detached from the
     * application.
     */
    public final boolean removeMouseInputListener(final MouseInputListener toRemove) {
        return MOUSE_INPUT_LISTENERS.remove(toRemove);
    }

    /**
     * Removes all {@link MouseInputListener mouse input listeners} attached to
     * the application.
     */
    public final void clearMouseInputListeners() {
        MOUSE_INPUT_LISTENERS.clear();
    }

    /**
     * Attaches a {@link KeyInputListener key input listener} to the
     * application. The attached listener will receive key input events
     * triggered by the application. This method does nothing if the specified
     * key input listener is already attached to the application.
     *
     * @param toAdd the key input listener to attach to the application.
     * @return true if the listener was successfully attached to the
     * application.
     */
    public final boolean addKeyInputListener(final KeyInputListener toAdd) {
        return KEY_INPUT_LISTENERS.add(toAdd);
    }

    /**
     * Detaches a {@link KeyInputListener key input listener} from the
     * application. The listener will no longer receive key input events
     * triggered by the application. This method does nothing if the specified
     * key input listener is not attached to the application.
     *
     * @param toRemove the key input listener to detach from the application.
     * @return true if the listener was successfully removed from the
     * application.
     */
    public final boolean removeKeyInputListener(final KeyInputListener toRemove) {
        return KEY_INPUT_LISTENERS.remove(toRemove);
    }

    /**
     * Removes all {@link KeyInputListener key input listeners} attached to the
     * application.
     */
    public final void clearKeyInputListeners() {
        KEY_INPUT_LISTENERS.clear();
    }

    /**
     * Attaches a {@link CharInputListener character input listener} to the
     * application. The attached listener will receive character input events
     * triggered by the application. This method does nothing if the specified
     * character input listener is already attached to the application.
     *
     * @param toAdd the character input listener to attach to the application.
     * @return true if the listener was successfully attached to the
     * application.
     */
    public final boolean addCharInputListener(final CharInputListener toAdd) {
        return CHAR_INPUT_LISTENERS.add(toAdd);
    }

    /**
     * Detaches a {@link CharInputListener character input listener} from the
     * application. The listener will no longer receive character input events
     * triggered by the application. This method does nothing if the specified
     * character input listener is not attached to the application.
     *
     * @param toRemove the character input listener to detach from the
     * application.
     * @return true if the listener was successfully removed from the
     * application.
     */
    public final boolean removeKeyInputListener(final CharInputListener toRemove) {
        return CHAR_INPUT_LISTENERS.remove(toRemove);
    }

    /**
     * Removes all {@link CharInputListener character input listeners} attached
     * to the application.
     */
    public final void clearCharInputListeners() {
        CHAR_INPUT_LISTENERS.clear();
    }

    /**
     * Attaches an {@link InputMapping input mapping} to the application. The
     * attached input mapping will receive input events triggered by the
     * application. This method does nothing if the specified input mapping is
     * already attached to the application.
     *
     * @param toAdd the input mapping to attach to the application.
     */
    public final void addInputMapping(final InputMapping toAdd) {
        INPUT_MAPPINGS.add(toAdd);
    }

    /**
     * Attaches {@link InputMapping input mappings} to the application. The
     * attached input mappings will receive input events triggered by the
     * application. This method will only attach an input mapping if it is not
     * already attached to the application.
     *
     * @param toAdd the input mappings to attach to the application.
     */
    public final void addInputMapping(final InputMapping... toAdd) {
        INPUT_MAPPINGS.addAll(Arrays.asList(toAdd));
    }

    /**
     * Attaches a list of {@link InputMapping input mappings} to the
     * application. The attached input mappings will receive input events
     * triggered by the application. This method will only attach the input
     * mappings from the list that are not already attached to the application.
     *
     * @param toAdd the list of input mappings to attach to the application.
     */
    public final void addInputMapping(final List<InputMapping> toAdd) {
        INPUT_MAPPINGS.addAll(toAdd);
    }

    /**
     * Detaches an {@link InputMapping input mapping} from the application. The
     * detached input mapping will no longer receive input events triggered by
     * the application. This method does nothing if the input mapping is not
     * already attached to the application.
     *
     * @param toRemove the input mapping to detach from the application.
     */
    public final void removeInputMapping(final InputMapping toRemove) {
        INPUT_MAPPINGS.remove(toRemove);
    }

    /**
     * Detaches {@link InputMapping input mappings} from the application. The
     * detached input mappings will no longer receive input events triggered by
     * the application. This method will only detach the mappings that are
     * already attached to the application.
     *
     * @param toRemove the input mappings to detach from the application.
     */
    public final void removeInputMapping(final InputMapping... toRemove) {
        INPUT_MAPPINGS.removeAll(Arrays.asList(toRemove));
    }

    /**
     * Detaches the list of {@link InputMapping input mappings} from the
     * application. The detached mappings will no longer receive input events
     * triggered by the application. This method will only detach the mappings
     * in the list that are attached to the application.
     *
     * @param toRemove the list of input mappings to detach from the
     * application.
     */
    public final void removeInputMapping(final List<InputMapping> toRemove) {
        INPUT_MAPPINGS.removeAll(toRemove);
    }

    /**
     * Removes all {@link InputMapping input mappings} currently attached to the
     * application.
     */
    public final void clearInputMappings() {
        INPUT_MAPPINGS.clear();
    }

    /**
     * Returns an {@link Collections#unmodifiableSet(Set) unmodifiable} view of
     * the {@link #MOD_INPUT_SET modifier input set}. The modifier input set
     * stores the list of activated input modifiers in the order that they were
     * activated. Any attempts to modify the set returned by this method will
     * cause an exception to be thrown.
     *
     * @return an unmodifiable view of the modifier input set.
     */
    public final Set<Input.Mod> getModInputSet() {
        return Collections.unmodifiableSet(MOD_INPUT_SET);
    }

    /**
     * Returns the current width and height, in pixels, of the display frame
     * buffer (rendering context).
     *
     * @return a vector containing the width and height, in pixels, of the
     * display frame buffer.
     */
    public final Vector2i getResolution() {
        Vector2i resolution;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.callocInt(1);
            IntBuffer height = stack.callocInt(1);
            glfwGetFramebufferSize(display, width, height);
            resolution = new Vector2i(width.get(), height.get());
        }
        return resolution;
    }

    /**
     * Returns the current width, in pixels, of the application display frame
     * buffer (rendering context). Both the width and the height of the frame
     * buffer can be returned with a single call to {@link #getResolution()}.
     *
     * @return the width, in pixels, of the display frame buffer.
     */
    public final int getWidth() {
        int width;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer buffer = stack.callocInt(1);
            glfwGetFramebufferSize(display, buffer, null);
            width = buffer.get();
        }
        return width;
    }

    /**
     * Returns the current height, in pixels, of the application display frame
     * buffer (rendering context). Both the width and the height of the frame
     * buffer can be returned with a single call to {@link #getResolution()}.
     *
     * @return the height, in pixels, of the display frame buffer.
     */
    public final int getHeight() {
        int height;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer buffer = stack.callocInt(1);
            glfwGetFramebufferSize(display, null, buffer);
            height = buffer.get();
        }
        return height;
    }

    /**
     * Applies the values of the specified settings to the settings of this
     * application. The original {@link AppSettings settings} used for the
     * construction of the application are retained. Any changes made to the
     * specified settings after this method has been called will not be
     * automatically applied to the settings of this application. Use the
     * {@link #getAppSettings()} method to modify the settings of this
     * application directly.
     *
     * @param settings
     */
    public final void setAppSettings(final AppSettings settings) {
        appSettings.setSettings(settings);
    }

    /**
     * Returns a direct reference to the {@link AppSettings settings} of the
     * application. Any changes made to the settings returned by this method
     * will automatically be applied to this application.
     *
     * @return the application settings.
     */
    public final AppSettings getAppSettings() {
        return appSettings;
    }

    /**
     * Applies new {@link DisplaySettings display settings} to the application.
     * The display settings provided to this method are copied over into the
     * display settings instance maintained by the application. The application
     * will update its own internal copy of display settings if/when any changes
     * are made to the display while the application is running. The display
     * settings provided to this method will not be modified by the application
     * in any way. {@link #getDisplaySettings()} may be used in order to
     * retrieve a copy of the current application display settings.
     *
     * @param toSet the display settings to apply to the application.
     */
    public final void setDisplaySettings(final DisplaySettings toSet) {
        // Set Cursor Mode //
        glfwSetInputMode(display, GLFW_CURSOR, toSet.getCursorMode().getId());

        // Set Display Title //
        setTitle(toSet.getTitle());

        // Set Vertical Synchronization //
        setVerticalSync(toSet.isVerticalSync());

        switch (toSet.getType()) {
            case Windowed: {
                // Apply Windowed Display Attributes //
                glfwSetWindowAttrib(display, GLFW_RESIZABLE, toSet.isResizable() ? GL_TRUE : GL_FALSE);
                glfwSetWindowAttrib(display, GLFW_FLOATING, toSet.isFloating() ? GL_TRUE : GL_FALSE);
                glfwSetWindowAttrib(display, GLFW_DECORATED, toSet.isDecorated() ? GL_TRUE : GL_FALSE);

                setVisible(toSet.isVisible());
                setMaximized(toSet.isMaximized());

                // Set Windowed Display Mode //
                glfwSetWindowMonitor(display, NULL, toSet.getXPosition(), toSet.getYPosition(), toSet.getWidth(), toSet.getHeight(),
                        toSet.getDisplayDevice().getDisplayMode().getRefreshRate());
                break;
            }
            case Fullscreen: {
                // Apply Full Screen Display Attributes //
                glfwSetWindowAttrib(display, GLFW_AUTO_ICONIFY, toSet.isFocused() ? GL_TRUE : GL_FALSE);

                // Set Full Screen Display Mode //
                glfwSetWindowMonitor(display, toSet.getDisplayDevice().getId(), toSet.getXPosition(), toSet.getYPosition(), toSet.getWidth(), toSet.getHeight(),
                        toSet.getRefreshRate());
                break;
            }
        }
        displaySettings.setSettings(toSet);
    }

    /**
     * Returns a copy of the current display {@link DisplaySettings settings}.
     * Any changes made directly to the display settings instance returned by
     * this method will not be automatically applied to the application. The
     * settings returned by this method are only a snapshot of the current
     * display settings at the time this method was called. The returned
     * instance will not be automatically updated if the application display
     * settings are changed. {@link #setDisplaySettings(DisplaySettings)} may be
     * called in order to apply new display settings to the application.
     *
     * @return a copy of the current application display settings.
     */
    public final DisplaySettings getDisplaySettings() {
        return new DisplaySettings(displaySettings);
    }

    /**
     * Sets the {@link DisplayMode display mode} of the application display.
     * This method changes the width, height, and refresh rate of the display to
     * match the width, height, and refresh rate of the display mode provided.
     *
     * @param toSet the display mode to apply to the application display.
     */
    public final void setDisplayMode(final DisplayMode toSet) {
        displaySettings.setDisplayMode(toSet);

        switch (displaySettings.getType()) {
            case Windowed: {
                glfwSetWindowMonitor(display, NULL, displaySettings.getXPosition(), displaySettings.getYPosition(), displaySettings.getWidth(),
                        displaySettings.getHeight(), displaySettings.getRefreshRate());
                break;
            }
            case Fullscreen: {
                glfwSetWindowMonitor(display, displaySettings.getDisplayDevice().getId(), displaySettings.getXPosition(), displaySettings.getYPosition(),
                        displaySettings.getWidth(), displaySettings.getHeight(), displaySettings.getRefreshRate());
                break;
            }
        }
    }

    /**
     * Sets the title of the application display. The title will be displayed in
     * the title bar of windowed displays that are decorated. The title may be
     * displayed on the task bar and other similar interfaces for full screen
     * and undecorated displays.
     *
     * @param title the title of the application display.
     */
    public final void setTitle(final String title) {
        displaySettings.setTitle(title);
        glfwSetWindowTitle(display, title);
    }

    /**
     * Returns the title of the application display.
     *
     * @return
     */
    public final String getTitle() {
        return displaySettings.getTitle();
    }

    /**
     * Enables or disables vertical synchronization on the application display.
     * When enabled, the number of frame rendered per second will not exceed the
     * refresh rate of the display device.
     *
     * @param enable true to enable vertical synchronization, false to disable
     * vertical synchronization.
     */
    public final void setVerticalSync(final boolean enable) {
        displaySettings.setVerticalSync(enable);

        // Make Display Context Current //
        if (glfwGetCurrentContext() != display) {
            glfwMakeContextCurrent(display);
        }

        // Set Vertical Synchronization //
        glfwSwapInterval(enable ? GL_TRUE : GL_FALSE);
    }

    /**
     * Indicates if vertical synchronization is enabled on the application
     * display. When enabled, the number of frame rendered per second will not
     * exceed the refresh rate of the display device.
     *
     * @return true if vertical synchronization is enabled, false is vertical
     * synchronization is disabled.
     */
    public final boolean isVerticalSync() {
        return displaySettings.isVerticalSync();
    }

    /**
     * Sets the visibility of the application display window. Hiding a display
     * window makes it completely invisible to the user, including removing it
     * from the task bar, dock or window list. When a display window is hidden
     * it will automatically lose input focus. Full screen displays cannot be
     * hidden. Calling this method will have no effect if the application
     * display is in full screen mode.
     *
     * @param visible true to show the application display. False to hide the
     * application display.
     */
    public final void setVisible(final boolean visible) {
        displaySettings.setVisible(visible);
        if (visible) {
            glfwShowWindow(display);
        } else {
            glfwHideWindow(display);
        }
    }

    /**
     * Returns the current visibility state of the application display. Full
     * screen displays cannot be hidden and this method will always return true
     * if the application is in full screen mode.
     *
     * @return true if the application display is visible. False if the
     * application display is hidden.
     */
    public final boolean isVisible() {
        return glfwGetWindowAttrib(display, GLFW_VISIBLE) == GL_TRUE;
    }

    /**
     * Enables or disables auto iconify for full screen application displays.
     * When enabled, the application display will automatically be iconified
     * (minimized) whenever it loses input focus. The auto iconify feature is
     * ignored when the application display is in windowed mode.
     *
     * @param autoIconify true to enable auto iconify, false to disable auto
     * iconify.
     */
    public final void setAutoIconify(final boolean autoIconify) {
        displaySettings.setAutoIconify(autoIconify);
    }

    /**
     * Indicates if auto iconify is enabled for the application display. When
     * enabled, the application display will automatically be iconified
     * (minimized) whenever it loses input focus. Auto iconify only applies to
     * full screen application displays and will always return false when the
     * application is in windowed mode.
     *
     * @return true if auto iconify is enabled, false if auto iconify is
     * disabled.
     */
    public final boolean isAutoIconify() {
        return displaySettings.isAutoIconify();
    }

    /**
     * Sets the iconification state of the application display. Calling this
     * method affects both windowed and full screen displays. When a full screen
     * display is iconified (minimized), the original display mode of the
     * display device will be restored. When a full screen display is restored
     * from the iconified state the full screen display mode will be reapplied
     * to the display device.
     *
     * @param iconify true to iconify the display. False to restore an iconified
     * display.
     */
    public final void setIconified(final boolean iconify) {
        if (iconify) {
            glfwIconifyWindow(display);
        } else if (isIconified()) {
            glfwRestoreWindow(display);
        }
    }

    /**
     * Returns true if the application display is iconified (minimized).
     *
     * @return true if the application display is iconified. False if the
     * application display is not iconified.
     */
    public final boolean isIconified() {
        return glfwGetWindowAttrib(display, GLFW_ICONIFIED) == GL_TRUE;
    }

    /**
     * Sets the maximization state of the application display window. This
     * method only affects applications with windowed displays and will be
     * ignored if the application display is in full screen mode.
     *
     * @param maximize true to maximize the application display window, false to
     * restore the application display window to its original state.
     */
    public final void setMaximized(final boolean maximize) {
        displaySettings.setMaximized(maximize);
        if (maximize) {
            glfwMaximizeWindow(display);
        } else if (isMaximized()) {
            glfwRestoreWindow(display);
        }
    }

    /**
     * Returns true if the application display window is maximized. This method
     * only applies to windowed application displays. This method will always
     * return false when the application display is in full screen mode.
     *
     * @return true if the application display window is maximized, false if the
     * application window is not maximized.
     */
    public final boolean isMaximized() {
        return glfwGetWindowAttrib(display, GLFW_MAXIMIZED) == GL_TRUE;
    }

    /**
     * Indicates if the application currently has input focus.
     *
     * @return true if the application has input focus, false otherwise.
     */
    public final boolean isFocused() {
        return glfwGetWindowAttrib(display, GLFW_FOCUSED) == GL_TRUE;
    }

    /**
     * Returns the frequency, in seconds, at which the fixed {@link #update()}
     * method is called.
     *
     * @return the number of updates per second.
     */
    public final int getUps() {
        return ups;
    }

    /**
     * Returns the frequency, in seconds, at which the {@link #render()} render
     * method is called.
     *
     * @return the number of frames rendered per second.
     */
    public final int getFps() {
        return fps;
    }

    /**
     * GLFW Callback for receiving and logging GLFW errors.
     */
    private final GLFWErrorCallback ERROR_CALLBACK = new GLFWErrorCallback() {
        @Override
        public final void invoke(final int error, final long description) {
            LOG.error("GLFW Error: {}", GLFWErrorCallback.getDescription(description));
            throw new RuntimeException("GLFW Exception - " + GLFWErrorCallback.getDescription(description));
        }
    };

    /**
     * GLFW Callback that indicates when the content area of a display has been
     * damaged and needs to be refreshed. On compositing window systems such as
     * Aero, Compiz or Aqua, where the window contents are saved off-screen,
     * this callback might only be called when the window or frame buffer is
     * resized.
     */
    private final GLFWWindowRefreshCallback REFRESH_CALLBACK = new GLFWWindowRefreshCallback() {

        /**
         * Called by GLFW when a display has been damaged and needs to be
         * refreshed.
         *
         * @param id the GLFW id of the display that has been damaged.
         */
        @Override
        public final void invoke(final long id) {
            // Set OpenGL Context //
            if (glfwGetCurrentContext() != display) {
                glfwMakeContextCurrent(display);
            }

            // Render Next Frame //
            RENDERERS.stream().forEach(renderer -> {
                renderer.render();
            });

            // Swap Frame Buffers //
            glfwSwapBuffers(display);
        }
    };

    /**
     * GLFW Callback that indicates when the application display has gained or
     * lost input focus.
     */
    private final GLFWWindowFocusCallback FOCUS_CALLBACK = new GLFWWindowFocusCallback() {

        /**
         * Called by GLFW when the application display has gained or lost input
         * focus.
         *
         * @param id the id of the display that gained or lost input focus.
         * @param focused true if the display has gained input focus. False if
         * the display has lost input focus.
         */
        @Override
        public final void invoke(final long id, final boolean focused) {
            for (DisplayListener listener : DISPLAY_LISTENERS) {
                listener.onFocus(focused);
            }
        }
    };

    /**
     * GLFW Callback that indicates when the application display has been
     * iconified or restored from a previously iconified state.
     */
    private final GLFWWindowIconifyCallback ICONIFY_CALLBACK = new GLFWWindowIconifyCallback() {

        /**
         * Called by GLFW when the application display has been iconified or
         * restored.
         *
         * @param id the id of the display that has been iconified or restored.
         * @param iconified true if the display has been iconified. False if the
         * display has been restored from an iconified state.
         */
        @Override
        public final void invoke(final long id, final boolean iconified) {
            for (DisplayListener listener : DISPLAY_LISTENERS) {
                listener.onIconify(iconified);
            }
        }
    };

    /**
     * GLFW Callback that indicates when the application display has been moved
     * to a new position on the virtual desktop.
     */
    private final GLFWWindowPosCallback MOVE_CALLBACK = new GLFWWindowPosCallback() {

        /**
         * Called by GLFW when the application display has been moved.
         *
         * @param id the id of the display that has been moved.
         * @param xPos the new x-axis position of the display.
         * @param yPos the new y-axis position of the display.
         */
        @Override
        public final void invoke(final long id, final int xPos, final int yPos) {
            displaySettings.setPosition(xPos, yPos);
            for (DisplayListener listener : DISPLAY_LISTENERS) {
                listener.onMove(xPos, yPos);
            }
        }
    };

    /**
     * GLFW Callback and indicates when the application display window size has
     * been changed.
     */
    private final GLFWWindowSizeCallback SIZE_CALLBACK = new GLFWWindowSizeCallback() {

        /**
         * Called by GLFW when the application display window has been resized.
         *
         * @param id the GLFW id of the display window.
         * @param width the new width of the display, in screen coordinates.
         * @param height the new height of the display, in screen coordinates.
         */
        @Override
        public final void invoke(final long id, final int width, final int height) {
            displaySettings.setSize(width, height);
        }
    };

    /**
     * GLFW Callback that indicates when the application frame buffer has been
     * resized.
     */
    private final GLFWFramebufferSizeCallback RESIZE_CALLBACK = new GLFWFramebufferSizeCallback() {

        /**
         * Called by GLFW when application frame buffer has been resized.
         *
         * @param id the id of the display that has been resized.
         * @param width the new width of the display frame buffer in pixels.
         * @param height the new height of the display frame buffer in pixels.
         */
        @Override
        public final void invoke(final long id, final int width, final int height) {
            glViewport(0, 0, width, height);
            for (DisplayListener listener : DISPLAY_LISTENERS) {
                listener.onResize(width, height);
            }
        }
    };

    /**
     * GLFW Callback that indicates when the application display has been issued
     * a request to close.
     */
    private final GLFWWindowCloseCallback CLOSE_CALLBACK = new GLFWWindowCloseCallback() {

        /**
         * Called by GLFW when the application display has been issued a close
         * request.
         *
         * @param id the id of the display that has been issued the close
         * request.
         */
        @Override
        public final void invoke(final long id) {
            for (DisplayListener listener : DISPLAY_LISTENERS) {
                listener.onClose();
            }
        }
    };

    /**
     * Callback that indicates when the mouse cursor has entered or exited the
     * content area of a display.
     */
    private final GLFWCursorEnterCallback MOUSE_ENTER_CALLBACK = new GLFWCursorEnterCallback() {

        /**
         * Called by GLFW when the mouse cursor enters or exits the content area
         * of a display.
         *
         * @param id the id of the display that the mouse cursor has entered or
         * exited.
         * @param entered true if the mouse cursor has entered the content area
         * of the display. False if the mouse cursor has exited the content area
         * of the display.
         */
        @Override
        public final void invoke(final long id, final boolean entered) {
            for (MouseMotionListener listener : MOUSE_MOTION_LISTENERS) {
                listener.onMouseEnter(entered);
            }
        }
    };

    /**
     * Callback that receives the cursor position, in screen coordinates,
     * relative to the top-left corner of the application display context. On
     * platforms that provide it, the full sub-pixel cursor position will be
     * used. If the {@link CursorMode cursor mode} of the display is set to
     * {@link CursorMode#Locked locked} then the unbounded virtual cursor
     * position will be used. The virtual cursor position does not correspond to
     * a specific location within the display.
     */
    private final GLFWCursorPosCallback MOUSE_MOTION_CALLBACK = new GLFWCursorPosCallback() {

        /**
         * Called by GLFW when the mouse cursor moves within the display context
         * of the application.
         *
         * @param id the id of the display that contains the mouse cursor.
         * @param xPos the x-axis position of the mouse cursor, in screen
         * coordinates, relative to the top-left corner of the display context.
         * If the cursor mode of the display is set to locked, then a virtual
         * cursor position will be provided that does not correspond to a
         * particular location within the content area of the display.
         * @param yPos the y-axis position of the mouse cursor, in screen
         * coordinates, relative to the top-left corner of the display context.
         * If the cursor mode of the display is set to locked, then a virtual
         * cursor position will be provided that does not correspond to a
         * particular location within the content area of the display.
         */
        @Override
        public final void invoke(final long id, final double xPos, final double yPos) {
            for (MouseMotionListener listener : MOUSE_MOTION_LISTENERS) {
                listener.onMouseMove(xPos, yPos);
            }
        }
    };

    /**
     * GLFW Callback that indicates when the mouse wheel is scrolled.
     */
    private final GLFWScrollCallback MOUSE_SCROLL_CALLBACK = new GLFWScrollCallback() {

        /**
         * Called by GLFW when the mouse wheel is scrolled.
         *
         * @param id the id of the display that has input focus.
         * @param xOffset the horizontal (x-axis) offset of the scroll wheel.
         * @param yOffset the vertical (y-axis) offset of the scroll wheel.
         */
        @Override
        public final void invoke(final long id, final double xOffset, final double yOffset) {
            for (MouseScrollListener listener : MOUSE_SCROLL_LISTENERS) {
                listener.onMouseScroll(yOffset);
            }
        }
    };

    /**
     * GLFW Callback that indicates when a mouse button has been pressed or
     * released.
     */
    private final GLFWMouseButtonCallback MOUSE_BUTTON_CALLBACK = new GLFWMouseButtonCallback() {

        /**
         * Called by GLFW when a mouse button is pressed or released.
         *
         * @param id the id of the display with input focus.
         * @param button the GLFW id of the mouse button.
         * @param action the GLFW id of the action.
         * @param mods the GLFW id of the modifiers.
         */
        @Override
        public final void invoke(final long id, final int button, final int action, final int mods) {

            if (action == GLFW_PRESS || action == GLFW_RELEASE) {

                Input.Mouse input = Input.MOUSE_MAP.get(button);
                Input.State state = Input.STATE_MAP.get(action);
                Collection<Input.Mod> modifiers = Collections.unmodifiableCollection(MOD_INPUT_SET);

                // Send Input Event To Mouse Input Listeners //
                for (MouseInputListener listener : MOUSE_INPUT_LISTENERS) {
                    listener.onMouseInput(input, state, modifiers);
                }

                // Send Input Event To Input Mappings //
                for (InputMapping mapping : INPUT_MAPPINGS) {
                    mapping.onInput(input, state);
                }
            }
        }

    };

    /**
     * GLFW Callback that indicates when a key has been pressed or released.
     */
    private final GLFWKeyCallback KEY_CALLBACK = new GLFWKeyCallback() {
        /**
         * Called by GLFW when a key has been pressed or released.
         *
         * @param id the id of the display that triggered the event.
         * @param key the GLFW id of the key that was pressed or released.
         * @param scancode the scancode of the key which was pressed or
         * released. Scancodes are platform-specific but consistent over time.
         * @param action the GLFW id of the action that was triggered.
         * @param mods the modifier flags.
         */
        @Override
        public void invoke(long id, int key, int scancode, int action, int mods) {
            // Update Global Input Modifier Set //
            if (Input.MOD_MAP.containsKey(key)) {
                if (action == GLFW_PRESS) {
                    MOD_INPUT_SET.add(Input.MOD_MAP.get(key));
                } else if (action == GLFW_RELEASE) {
                    MOD_INPUT_SET.remove(Input.MOD_MAP.get(key));
                }
            }
            if (action == GLFW_PRESS || action == GLFW_RELEASE) {

                Input.Key input = Input.KEY_MAP.get(key);
                Input.State state = Input.STATE_MAP.get(action);
                Collection<Input.Mod> modifiers = Collections.unmodifiableCollection(MOD_INPUT_SET);

                // Send Input Event To Key Input Listeners //
                for (KeyInputListener listener : KEY_INPUT_LISTENERS) {
                    listener.onKeyInput(input, state, modifiers);
                }

                // Send Input Event To Input Mappings //
                for (InputMapping mapping : INPUT_MAPPINGS) {
                    mapping.onInput(input, state);
                }
            }
        }
    };

    /**
     * GLFW Callback that indicates when a character key has been pressed.
     */
    private final GLFWCharCallback CHAR_CALLBACK = new GLFWCharCallback() {
        /**
         * Called by GLFW when a character is entered.
         *
         * @param id the id of the display that triggered the event.
         * @param character the character that was entered.
         */
        @Override
        public void invoke(long id, int character) {
            for (CharInputListener listener : CHAR_INPUT_LISTENERS) {
                listener.onChar((char) character);
            }
        }
    };
}
