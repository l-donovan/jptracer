package jptracer;

import jptracer.tracelib.basetypes.*;
import jptracer.tracelib.common.Core;
import jptracer.tracelib.helper.Vec3;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import static jptracer.tracelib.common.Core.loadScene;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        // The following line is for testing purposes only
        args = new String[] { "scene.json", "render.bmp" };

        if (args.length < 2) {
            System.out.println("Usage: jptracer <scene> <outfile>");
            System.exit(1);
        } else {
            try {
                SceneContainer c = loadScene(args[0]);
                Vec3[][] screen = Core.renderScene(c.scene, c.camera, c.options);
                BufferedImage img = Core.pixelsToImage(screen);
                File file = new File(args[1]);
                int dot = args[1].indexOf('.');
                if (dot == -1) {
                    logger.severe("No extension provided for output image!");
                    System.exit(1);
                } else {
                    ImageIO.write(img, args[1].substring(dot + 1), file);
                    System.exit(0);
                }
            } catch (IOException e) {
                logger.severe(e.toString());
            }
        }
    }
}
