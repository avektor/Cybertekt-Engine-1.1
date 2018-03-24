package net.cybertekt.gui;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import net.cybertekt.asset.AssetManager;
import net.cybertekt.asset.font.FontLoader;
import net.cybertekt.utils.FontGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Vektor
 */
public class FontTest {

    private static final Logger LOG = LoggerFactory.getLogger(FontTest.class);

    private JFrame frame;

    public static final void main(final String[] args) {
        FontTest test = new FontTest();
        test.start();
    }

    public void start() {
        AssetManager.registerLoader(FontLoader.class);

        frame = new JFrame("Font Preview");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(1980, 1060));
        frame.setVisible(true);
        try {
            BufferedImage img = FontGenerator.generate("assets/Interface/Fonts/TTF/VERDANA.ttf", 32, 3, FontGenerator.BASIC_CHARSET, "assets/Interface/Fonts/CTF/verdana.ctf");
            frame.getGraphics().drawImage(img, 50, 50, null);
            try {
                File output = new File("image.png");
                ImageIO.write(img, "png", output);
            } catch (IOException e) {
                LOG.info("Exception: {}", e.getMessage());
            }
        } catch (IOException e) {
            LOG.info("CTF Font Generation Failed: {}", e.getMessage());
        }
    }
}
