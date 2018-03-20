package net.cybertekt.app;

import net.cybertekt.app.display.DisplaySettings;
import net.cybertekt.app.listener.DisplayListener;
import java.text.DecimalFormat;
import net.cybertekt.app.listener.UpdateListener;
import org.joml.Vector2i;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Vektor
 */
public class TimerTest extends Application implements UpdateListener, DisplayListener {

    private static final Logger LOG = LoggerFactory.getLogger(TimerTest.class);
    
    private final DecimalFormat df = new DecimalFormat(".#");
    
    double time = 0;
    double glfwTime = 0;
    double deltaTime = 0;
    
    public static void main(final String[] args) {
        TimerTest app = new TimerTest();
        
        AppSettings appSettings = new AppSettings("Timer Test", "1.0", 40);
        DisplaySettings displaySettings = new DisplaySettings("Timer Test", new Vector2i(800, 600));
        
        app.initialize(appSettings, displaySettings);
    }

    @Override
    public void init() {
        addUpdateListener(this);
        addDisplayListener(this);
        glfwTime = glfwGetTime();
    }

    @Override
    public void update() {        
        time += 0.025;
        setTitle("Fixed Time: " + df.format(time) + "      GLFW Time: " + df.format(glfwGetTime() - glfwTime) + "      Variable Time: " + df.format(deltaTime / 1000));
    }
    
    @Override
    public void update(final double delta) {
        deltaTime += delta;
    }

    @Override
    public void exit() {

    }

    @Override
    public void onFocus(final boolean focused) {

    }

    @Override
    public void onIconify(final boolean iconified) {

    }

    @Override
    public void onMove(final int xPos, final int yPos) {

    }

    @Override
    public void onResize(final int width, final int height) {

    }

    @Override
    public void onClose() {
        stop();
    }
}
