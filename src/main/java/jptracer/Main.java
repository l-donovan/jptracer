package jptracer;

import jptracer.tracelib.*;
import jptracer.tracelib.basetypes.*;
import jptracer.tracelib.helper.Vec3;
import jptracer.tracelib.primitives.Plane;
import jptracer.tracelib.primitives.Sphere;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static jptracer.tracelib.Core.STD_COLORS;
import static jptracer.tracelib.Core.STD_MATERIALS;

public class Main {
    private static Scene scene = new Scene()
        .withBackgroundColor(STD_COLORS.get("celestial-blue"))
        .withMaterials(STD_MATERIALS)
        .withObjects(new SceneObject[] {
            new Sphere(new Vec3(-1.0, 1.0, 7.0), 2.0, "glass"),
            new Sphere(new Vec3(-0.75, -1.0, 12.0), 2.0, "mirror"),
            new Sphere(new Vec3(3.0, 0.5, 6.0), 1.5, "mirror"),
            new Sphere(new Vec3(2.5, 0.0, 4.0), 1.0, "glass"),
            new Plane(new Vec3(0, -5, 0), new Vec3(1, -5, 0), new Vec3(1, -5, 1), "floor"),
            new Sphere(new Vec3(-20,  70, -20), 1.5, "red-light"),
            new Sphere(new Vec3(30,  50,  12), 10.0, "white-light"),
        });

    private static Camera camera = new Camera()
        .withFOV(90.0)
        .withRes(1920, 1080)
        .withPos(new Vec3(0, 0, 0))
        .withRot(new Vec3(0, 0, 0.25));

    private static Options options = new Options()
        .withMaxDepth(5)
        .withProcCount(6)
        .withSampleCount(256);

    public static void main(String[] args) {
        Vec3[][] screen = Core.renderScene(scene, camera, options);
        BufferedImage img = Core.pixelsToImage(screen);
        try {
            File file = new File("render.bmp");
            ImageIO.write(img, "bmp", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
