package jptracer.tracelib;

import jptracer.tracelib.basetypes.*;
import jptracer.tracelib.helper.PreloadedQueue;
import jptracer.tracelib.helper.RenderThread;
import jptracer.tracelib.helper.SlottedList;
import jptracer.tracelib.helper.Vec3;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.TimeoutException;

import static jptracer.tracelib.basetypes.Intersection.NO_INTERSECTION;
import static jptracer.tracelib.basetypes.Material.brdfModelType.*;

public class Core {
    public static double minPositive(double a, double b) {
        if ((a < 0) && (b < 0)) {
            return 0;
        } else if ((a < 0) ^ (b < 0)) {
            return Math.max(a, b);
        } else {
            return Math.min(a, b);
        }
    }

    private static Vec3 reflect(Vec3 d, Vec3 n) {
        return d.sub(n.mul(d.dot(n)).mul(2.0));
    }

    public static Vec3 rot(Vec3 v, double cos_rx, double cos_ry, double cos_rz, double sin_rx, double sin_ry, double sin_rz) {
        double c1 = cos_rx * v.z - sin_rx * v.y;
        double c2 = cos_rx * v.y + sin_rx * v.z;
        double c3 = cos_ry * v.x - sin_ry * c1;

        return new Vec3(
            cos_rz * c3 + sin_rz * c2,
            cos_rz * c2 - sin_rz * c3,
            cos_ry * c1 + sin_ry * v.x
        );
    }

    private static Intersection checkIntersection(Vec3 origin, Vec3 dir, SceneObject[] objects) {
        Intersection closest = NO_INTERSECTION;

        for (SceneObject object : objects) {
            Intersection i = object.intersection(origin, dir);
            if (i.didIntersect() && i.getDistance() < closest.getDistance()) {
                closest = i;
            }
        }

        return closest;
    }

    private static double randInRange(double l, double u) {
        return l + (u - l) * Math.random();
    }

    private static Vec3 randomUnitVecInHemisphere(Vec3 n, double k) {
        double th = Math.acos(n.y)       + k * Math.PI * randInRange(-0.5, 0.5),
               ph = Math.atan2(n.z, n.x) + k * Math.PI * randInRange(-1.0, 1.0);

        return new Vec3(
            Math.sin(th) * Math.cos(ph),
            Math.cos(th),
            Math.sin(th) * Math.sin(ph)
        );
    }

    public static Vec3 castRay(Vec3 pos, Vec3 dir, Scene scene, Options options, int depth) {
        Vec3 hitColor = scene.backgroundColor;

        if (depth > options.maxDepth) {
            return hitColor;
        }

        Intersection i = checkIntersection(pos, dir, scene.objects);
        Vec3 hitPos = i.intersectionPosition();
        SceneObject hitObj = i.intersectionObject();

        if (!i.didIntersect()) {
            return hitColor;
        }

        Material material = scene.materials.get(hitObj.getMaterialName());

        Vec3 n = hitObj.normal(pos, hitPos);
        Vec3 newRay;

        double diffusionConstant;
        switch(material.brdfModel) {
            case DIFFUSE:
                diffusionConstant = 1.00; break;
            case GLOSSY:
                diffusionConstant = 0.50; break;
            case MIRROR:
                diffusionConstant = 0.02; break;
            default:
                diffusionConstant = 0.25; break;
        }

        newRay = material.brdfModel == DIFFUSE ?
            randomUnitVecInHemisphere(n, diffusionConstant) :
            randomUnitVecInHemisphere(reflect(dir, n), diffusionConstant);

        Vec3 BRDF = material.reflectance.div(Math.PI);
        Vec3 incoming = castRay(hitPos.add(newRay.mul(options.bias)), newRay, scene, options, depth + 1);
        double cosTheta = newRay.dot(n);
        double p = 2.0 * Math.PI;

        return material.emittance.add(BRDF.mul(incoming).mul(cosTheta).mul(p));
    }

    public static Vec3[][] renderScene(Scene scene, Camera camera, Options options) {
        System.out.println("Rendering scene...");
        long u = System.currentTimeMillis();
        camera.updateCachedValues();

        int lineCount = camera.res_y;
        final PreloadedQueue<Integer> inQueue = new PreloadedQueue<>();
        final SlottedList<Vec3[]> outList = new SlottedList<>(lineCount);

        for (int i = 0; i < lineCount; i++) {
            inQueue.push(i);
        }

        for (int i = 0; i < options.procCount; i++) {
            RenderThread process = new RenderThread(scene, camera, options, inQueue, outList);
            process.start();
        }

        try {
            outList.waitUntilFull();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        Vec3[][] screen = new Vec3[camera.res_y][camera.res_x];
        for (int i = 0; i < lineCount; i++) {
            screen[i] = outList.get(i);
        }

        long v = System.currentTimeMillis();
        System.out.println(String.format("\nScene took %f seconds to render", (v - u) / 1000.0));

        return screen;
    }

    public static BufferedImage pixelsToImage(Vec3[][] pixels) {
        int height = pixels.length;
        int width = pixels[0].length;
        BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                out.setRGB(x, y, new Color(
                    (int) (pixels[y][x].x * 0xff),
                    (int) (pixels[y][x].y * 0xff),
                    (int) (pixels[y][x].z * 0xff)).getRGB());
            }
        }
        return out;
    }

    public static Map<String, Material> STD_MATERIALS = Map.ofEntries(
        Map.entry("glass", new Material()
            .withEmittance(new Vec3(0, 0, 0))
            .withReflectance(new Vec3(0.9, 0.9, 0.9))
            .withBRDFModel(GLOSSY)),
        Map.entry("floor", new Material()
            .withEmittance(new Vec3(0, 0, 0))
            .withReflectance(new Vec3(0.2, 0.2, 0.2))
            .withBRDFModel(DIFFUSE)),
        Map.entry("mirror", new Material()
            .withEmittance(new Vec3(0, 0, 0))
            .withReflectance(new Vec3(0.9, 0.9, 0.9))
            .withBRDFModel(MIRROR)),
        Map.entry("white-light", new Material()
            .withEmittance(new Vec3(1.0, 1.0, 1.0))
            .withReflectance(new Vec3(1.0, 1.0, 1.0))
            .withBRDFModel(DIFFUSE)),
        Map.entry("red-light", new Material()
            .withEmittance(new Vec3(1.0, 0.0, 0.0))
            .withReflectance(new Vec3(1.0, 0.0, 0.0))
            .withBRDFModel(DIFFUSE))
    );

    public static Map<String, Vec3> STD_COLORS = Map.ofEntries(
        Map.entry("black",          new Vec3(0.0, 0.0, 0.0)),
        Map.entry("white",          new Vec3(1.0, 1.0, 1.0)),
        Map.entry("red",            new Vec3(1.0, 0.0, 0.0)),
        Map.entry("green",          new Vec3(0.0, 1.0, 0.0)),
        Map.entry("blue",           new Vec3(0.0, 0.0, 1.0)),
        Map.entry("yellow",         new Vec3(1.0, 1.0, 0.0)),
        Map.entry("cyan",           new Vec3(0.0, 1.0, 1.0)),
        Map.entry("magenta",        new Vec3(1.0, 0.0, 1.0)),
        Map.entry("grey",           new Vec3(0.5, 0.5, 0.5)),
        Map.entry("navy",           new Vec3(0.0, 0.0, 0.5)),
        Map.entry("olive",          new Vec3(0.5, 0.5, 0.0)),
        Map.entry("maroon",         new Vec3(0.5, 0.0, 0.0)),
        Map.entry("teal",           new Vec3(0.0, 0.5, 0.5)),
        Map.entry("purple",         new Vec3(0.5, 0.0, 0.5)),
        Map.entry("rose",           new Vec3(1.0, 0.0, 0.5)),
        Map.entry("azure",          new Vec3(0.0, 0.5, 1.0)),
        Map.entry("lime",           new Vec3(0.749019, 1.0, 0.0)),
        Map.entry("gold",           new Vec3(1.0, 0.843138, 0.0)),
        Map.entry("brown",          new Vec3(0.164706, 0.164706, 0.647059)),
        Map.entry("orange",         new Vec3(1.0, 0.647059, 0.0)),
        Map.entry("indigo",         new Vec3(0.294118, 0.0, 0.509804)),
        Map.entry("pink",           new Vec3(1.0, 0.752941, 0.796078)),
        Map.entry("cherry",         new Vec3(0.870588, 0.113725, 0.388235)),
        Map.entry("silver",         new Vec3(0.752941, 0.752941, 0.752941)),
        Map.entry("violet",         new Vec3(0.541176, 0.168627, 0.886275)),
        Map.entry("apricot",        new Vec3(0.984314, 0.807843, 0.694118)),
        Map.entry("chartreuse",     new Vec3(0.5, 1.0, 0.0)),
        Map.entry("orange-red",     new Vec3(1.0, 0.270588, 0.0)),
        Map.entry("blueberry",      new Vec3(0.309804, 0.52549, 0.968627)),
        Map.entry("raspberry",      new Vec3(0.890196, 0.043137, 0.360784)),
        Map.entry("turquoise",      new Vec3(0.25098, 0.878431, 0.815686)),
        Map.entry("amethyst",       new Vec3(0.6, 0.4, 0.8)),
        Map.entry("celestial-blue", new Vec3(0.286275, 0.592157, 0.815686))
    );
}
