package net.cybertekt.input;

import net.cybertekt.app.display.DisplaySettings;
import net.cybertekt.app.listener.DisplayListener;
import java.util.Collection;
import net.cybertekt.app.AppSettings;
import net.cybertekt.app.Application;
import net.cybertekt.app.listener.UpdateListener;
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
public class InputTest extends Application implements UpdateListener, DisplayListener, MouseMotionListener, MouseInputListener, KeyInputListener {

    private static final Logger LOG = LoggerFactory.getLogger(InputTest.class);

    public static void main(final String[] args) {
        InputTest app = new InputTest();

        AppSettings appSettings = new AppSettings("Display Test", "1.0");
        DisplaySettings displaySettings = new DisplaySettings("Display Test", new Vector2i(800, 600));

        app.initialize(appSettings, displaySettings);
    }

    @Override
    public void init() {
        // Attach Listeners //
        addUpdateListener(this);
        addDisplayListener(this);
        addMouseMotionListener(this);
        addMouseInputListener(this);
        addKeyInputListener(this);
        addInputMapping(WALK, SPRINT, COPY, PASTE);
    }

    @Override
    public void update() {
        if (WALK.isActivated()) {
            if (SPRINT.isActivated()) {
                LOG.info("[SPRINTING]");
            } else {
                LOG.info("[WALKING]");
            }
        }
    }

    @Override
    public void update(final double tpf) {

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
        //LOG.info("{}", inputSet);
    }

    @Override
    public void onKeyInput(final Input.Key input, final Input.State state, final Collection<Input.Mod> mods) {

    }
    
    private final InputMapping WALK = new InputMapping(Input.Key.W);
    
    private final InputMapping SPRINT = new InputMapping(Input.Key.ShiftLeft);
    
    private final InputMapping COPY = new InputMapping(true, true, Input.Mod.Ctrl, Input.Key.C) {

        @Override
        public final void onAction(final boolean activated) {
            if (activated) {
                LOG.info("[COPY]");
            }
        }
    };

    private final InputMapping PASTE = new InputMapping(Input.Mod.Ctrl, Input.Key.V) {

        @Override
        public final void onAction(final boolean activated) {
            if (activated) {
                LOG.info("[PASTE]");
            }
        }
    };
}
