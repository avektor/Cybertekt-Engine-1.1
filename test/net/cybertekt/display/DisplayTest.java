package net.cybertekt.display;

import net.cybertekt.app.display.DisplaySettings;
import net.cybertekt.app.listener.DisplayListener;
import java.util.Collection;
import net.cybertekt.app.AppSettings;
import net.cybertekt.app.Application;
import net.cybertekt.input.Input;
import net.cybertekt.input.listener.KeyInputListener;
import net.cybertekt.input.listener.MouseInputListener;
import net.cybertekt.input.listener.MouseMotionListener;
import org.joml.Vector2i;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Vektor
 */
public class DisplayTest extends Application implements DisplayListener, MouseMotionListener, MouseInputListener, KeyInputListener {

    private static final Logger LOG = LoggerFactory.getLogger(DisplayTest.class);

    private DisplaySettings fullscreenSettings;
    private DisplaySettings windowSettings;
    
    public static void main(final String[] args) {
        DisplayTest app = new DisplayTest();

        AppSettings appSettings = new AppSettings("Display Test", "1.0");
        DisplaySettings displaySettings = new DisplaySettings("Display Test", new Vector2i(800, 600));

        app.initialize(appSettings, displaySettings);
    }

    @Override
    public void init() {
        windowSettings = new DisplaySettings("Display Test", new Vector2i(1200, 800));
        fullscreenSettings = new DisplaySettings("Display Test");
        
        // Attach Listeners //
        addDisplayListener(this);
        addMouseMotionListener(this);
        addMouseInputListener(this);
        addKeyInputListener(this);
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

    @Override
    public void onMouseMove(final double xPos, final double yPos) {
    }

    @Override
    public void onMouseEnter(final boolean entered) {

    }

    @Override
    public void onMouseInput(final Input.Mouse input, final Input.State state, final Collection<Input.Mod> mods) {
        switch (state) {
            case Pressed: {
                switch (input) {
                    case Left: {
                        Vector2i size = getDisplaySettings().getSize();
                        Vector2i pos = getDisplaySettings().getPosition();
                        LOG.info("Window Size: {} x {}", size.x(), size.y());
                        LOG.info("Window Position: {} x {}", pos.x(), pos.y());
                        break;
                    }
                    case Right: {
                        break;
                    }
                    case Middle: {
                        
                        break;
                    }
                }
                break;
            }
            case Released: {
                
                break;
            }
        }
    }

    @Override
    public void onKeyInput(final Input.Key input, final Input.State state, final Collection<Input.Mod> mods) {
        LOG.info("{} {} {}", mods, input, state);
    }
}
