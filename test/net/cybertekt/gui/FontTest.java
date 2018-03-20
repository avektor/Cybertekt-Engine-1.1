package net.cybertekt.gui;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import net.cybertekt.asset.AssetManager;
import net.cybertekt.asset.font.FontLoader;
import net.cybertekt.utils.CDFGenerator;
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
            BufferedImage img = CDFGenerator.generate("assets/Interface/Fonts/ARLRDBD.ttf", 72, 6, CDFGenerator.BASIC_CHARSET, "assets/Interface/Fonts/arial.cdf");
            frame.getGraphics().drawImage(img, 50, 50, null);
            try {
                File output = new File("image.png");
                ImageIO.write(img, "png", output);
            } catch (IOException e) {
                LOG.info("Exception: {}", e.getMessage());
            }
        } catch (IOException e) {
            LOG.info("SDF Font Generation Failed: {}", e.getMessage());
        }

        AssetManager.load("Interface/Fonts/arial.cdf");
        
        //String tag = "true";
        //try {
            //LOG.info("Tag = {}", tag.getBytes("ISO-8859-1"));\
        //} catch (UnsupportedEncodingException e) {

        //}

        /*

        FontConverter gen = new FontConverter("assets/Interface/Fonts/arial.ttf", 68, 10);
        BufferedImage img = gen.getImage();
        frame.getGraphics().drawImage(img, 50, 50, null);
        gen.export("assets/Interface/Fonts/arial.sdf"); */
    }
}
