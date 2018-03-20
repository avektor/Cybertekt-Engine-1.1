package net.cybertekt.app.display;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3i;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import static org.lwjgl.glfw.GLFW.GLFW_CONNECTED;
import static org.lwjgl.glfw.GLFW.GLFW_DISCONNECTED;
import static org.lwjgl.glfw.GLFW.glfwGetGammaRamp;
import static org.lwjgl.glfw.GLFW.glfwGetMonitorName;
import static org.lwjgl.glfw.GLFW.glfwGetMonitorPhysicalSize;
import static org.lwjgl.glfw.GLFW.glfwGetMonitors;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoModes;
import static org.lwjgl.glfw.GLFW.glfwSetMonitorCallback;
import org.lwjgl.glfw.GLFWGammaRamp;
import org.lwjgl.glfw.GLFWMonitorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Display Device - (C) Cybertekt Software
 *
 * This class internally maintains a map of all active display devices currently
 * connected to the system. Each instance of this class represents a physical
 * display device (such as a monitor) connected to the current system. Methods
 * are provided for retrieving information about a specific display devices.
 * Instances of this class cannot be constructed directly by the user, instead,
 * instances of this class are automatically created each time a new device is
 * connected to the system. Instances of this class can be retrieved using the
 * static methods provided by this class. All static and instance methods
 * provided by this class make calls to GLFW and therefore <b>must only be
 * called from the main thread</b>.
 *
 * @version 1.1.0
 * @since 1.1.0
 * @author Andrew Vektor
 */
public final class DisplayDevice {

    /**
     * SLF4J Class logger for debugging.
     */
    protected static final Logger LOG = LoggerFactory.getLogger(DisplayDevice.class);

    /**
     * Internal {@link HashMap hash map} that stores all active display devices
     * on the current system.
     */
    private static final Map<Long, DisplayDevice> DEVICES = new HashMap<>();

    /**
     * GLFWMonitorCallback for updating the internal {@link #DEVICES device} map
     * when display devices are added or removed from the system.
     */
    public static final GLFWMonitorCallback DEVICE_CALLBACK = new GLFWMonitorCallback() {
        @Override
        public final void invoke(final long id, final int event) {
            if (event == GLFW_CONNECTED) {
                DEVICES.put(id, new DisplayDevice(id));
            } else if (event == GLFW_DISCONNECTED) {
                DEVICES.remove(id);
            }
        }
    };

    /**
     * Static initialization block that ensures GLFW has been initialized and
     * sets the GLFW monitor callback and populates the display device map.
     */
    static {
        // Set GLFW Monitor Callback //
        glfwSetMonitorCallback(DEVICE_CALLBACK);

        // Populate The Internal Display Device Map //
        PointerBuffer devices = glfwGetMonitors();
        while (devices.hasRemaining()) {
            long id = devices.get();
            DEVICES.put(id, new DisplayDevice(id));
        }
    }

    /**
     * Returns a reference to the primary display device on the current system.
     *
     * @return a reference to the primary display device.
     */
    public static DisplayDevice getPrimaryDisplayDevice() {
        return DEVICES.get(glfwGetPrimaryMonitor());
    }

    /**
     * Returns a Collection view of the display devices contained in the
     * internal {@link #DEVICES devices} map maintained by this class. The
     * collection is backed by the map, so changes to the map are reflected in
     * the collection, and vice-versa. If the map is modified while an iteration
     * over the collection is in progress (except through the iterator's own
     * remove operation), the results of the iteration are undefined. The
     * collection supports element removal, which removes the corresponding
     * mapping from the map, via the Iterator.remove, Collection.remove,
     * removeAll, retainAll and clear operations. It does not support the add or
     * addAll operations.
     *
     * @return a Collection view of the display devices in the
     * {@link #DEVICES devices} map.
     */
    public static final Collection<DisplayDevice> getAllDisplayDevices() {
        return DEVICES.values();
    }

    /**
     * GLFW pointer that identifies the display device.
     */
    private final long id;

    /**
     * Private constructor for creating new display devices. Display devices are
     * created automatically and are added to the {@link #DEVICES devices} map
     * when they are connected to the system. Display devices cannot be created
     * directly.
     *
     * @param id the GLFW identifier for the display device.
     */
    private DisplayDevice(final long id) {
        this.id = id;
    }

    /**
     * Returns the GLFW assigned identifier of this display device.
     *
     * @return the identifier assigned to this display device.
     */
    public final long getId() {
        return id;
    }

    /**
     * Returns a human-readable name, encoded as UTF-8, of this display device.
     * The name typically reflects the make and model of the device and is not
     * guaranteed to be unique among the connected monitors. This method must
     * only be called from the main thread.
     *
     * @return the human-readable name of the display device.
     */
    public final String getName() {
        return glfwGetMonitorName(id);
    }

    /**
     * Returns the size, in millimeters, of the display area of this device.
     * Some systems do not provide accurate display size information, either
     * because the device EDID data is incorrect or because the driver does not
     * report it accurately. On Windows, the OS calculates the returned physical
     * size from the current resolution and system DPI instead of querying the
     * device EDID data. This method must only be called from the main thread.
     *
     * @return the physical size of the device in millimeters.
     */
    public final Vector2i getSize() {
        IntBuffer width = MemoryUtil.memAllocInt(1);
        IntBuffer height = MemoryUtil.memAllocInt(1);

        glfwGetMonitorPhysicalSize(id, width, height);
        Vector2i size = new Vector2i(width.get(), height.get());

        MemoryUtil.memFree(width);
        MemoryUtil.memFree(height);

        return size;
    }

    /**
     * Returns the position, in screen coordinates, of the upper-left corner of
     * this device within the virtual desktop. This method must only be called
     * from the main thread.
     *
     * @return the position of the device in screen coordinates.
     */
    public final Vector2i getPosition() {
        IntBuffer xPos = MemoryUtil.memAllocInt(1);
        IntBuffer yPos = MemoryUtil.memAllocInt(1);

        GLFW.glfwGetMonitorPos(id, xPos, yPos);
        Vector2i position = new Vector2i(xPos.get(), yPos.get());

        MemoryUtil.memFree(xPos);
        MemoryUtil.memFree(yPos);

        return position;
    }

    /**
     * Returns the ratio between the current DPI and the platform's default DPI.
     * If you scale all pixel dimensions by this scale then your content should
     * appear at an appropriate size. This is especially important for text and
     * any UI elements. The content scale may depend on the device resolution,
     * pixel density, and on user settings. It may be very different from the
     * raw DPI calculated from the physical size and current resolution. This
     * method must only be called from the main thread.
     *
     * @return the content scale of this device.
     */
    public final Vector2f getContentScale() {
        FloatBuffer xScale = MemoryUtil.memAllocFloat(1);
        FloatBuffer yScale = MemoryUtil.memAllocFloat(1);

        GLFW.glfwGetMonitorContentScale(id, xScale, yScale);
        Vector2f contentScale = new Vector2f(xScale.get(), yScale.get());

        MemoryUtil.memFree(xScale);
        MemoryUtil.memFree(yScale);

        return contentScale;
    }

    /**
     * Returns the current gamma ramp of this device as an RGB vector. This
     * method must only be called from the main thread.
     *
     * @return the current gamma ramp of this device in an RGB vector.
     */
    public final Vector3i getGammaRamp() {
        GLFWGammaRamp gammaRamp = glfwGetGammaRamp(id);
        return new Vector3i(gammaRamp.red().get(), gammaRamp.green().get(), gammaRamp.blue().get());
    }

    /**
     * Returns the current {@link DisplayMode display mode} for this device.
     * This method must only be called from the main thread.
     *
     * @return the current {@link DisplayMode display mode} for this device.
     */
    public final DisplayMode getDisplayMode() {
        return new DisplayMode(GLFW.glfwGetVideoMode(id));
    }

    /**
     * Returns a list of all {@link DisplayMode display modes} supported by this
     * device, including the current display mode set for this device. The
     * current display mode will always be the first in the returned list. This
     * method must only be called from the main thread.
     *
     * @return a list of {@link DisplayMode display modes} supported by this
     * device.
     */
    public final List<DisplayMode> getSupportedDisplayModes() {
        GLFWVidMode.Buffer modes = glfwGetVideoModes(id);
        List<DisplayMode> displayModes = new ArrayList<>();
        while (modes.hasRemaining()) {
            displayModes.add(new DisplayMode(modes.get()));
        }
        return displayModes;
    }
}
