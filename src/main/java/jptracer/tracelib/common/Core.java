package jptracer.tracelib.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import jptracer.tracelib.basetypes.*;
import jptracer.tracelib.helper.PreloadedQueue;
import jptracer.tracelib.helper.RenderThread;
import jptracer.tracelib.helper.SlottedList;
import jptracer.tracelib.helper.Vec3;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static jptracer.tracelib.basetypes.Intersection.NO_INTERSECTION;

public class Core {
    public static Vec3 reflect(Vec3 d, Vec3 n) {
        return d.sub(n.mul(d.dot(n)).mul(2.0));
    }

    public static Vec3 refract(Vec3 d, Vec3 n, double ior) {
        double cosi = d.dot(n);
        double etai = 1.0;
        double etat = ior;

        if (cosi > 0.0) {
            double t = etai;
            etai = etat;
            etat = t;
            n = n.neg();
        } else {
            cosi = -cosi;
        }

        double eta = etai / etat;
        double k = 1.0 - Math.pow(eta, 2.0) * (1.0 - Math.pow(cosi, 2.0));

        if (k < 0.0) {
            return new Vec3(0.0, 0.0, 0.0);
        } else {
            return d.mul(eta).add(n.mul(eta * cosi - Math.sqrt(k)));
        }
    }

    public static double fresnel(Vec3 d, Vec3 n, double ior) {
        double cosi = d.dot(n);
        double etai = 1.0;
        double etat = ior;

        if (cosi > 0.0) {
            double t = etai;
            etai = etat;
            etat = t;
        }

        double sint = etai / etat * Math.sqrt(Math.max(1.0 - Math.pow(cosi, 2.0), 0.0));

        if (sint >= 1.0) {
            return 1.0;
        } else {
            double cost = Math.sqrt(Math.max(1.0 - Math.pow(sint, 2.0), 0.0));
            cosi = Math.abs(cosi);
            double rs = (etat * cosi - etai * cost) / (etat * cosi + etai * cost);
            double rp = (etai * cosi - etat * cost) / (etai * cosi + etat * cost);
            return (Math.pow(rs, 2.0) + Math.pow(rp, 2.0)) / 2.0;
        }
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

    public static Vec3 castRay(Vec3 pos, Vec3 dir, Scene scene, Options options) {
        Vec3 color = new Vec3(0.0, 0.0, 0.0);
        Vec3 throughput = new Vec3(1.0, 1.0, 1.0);

        for (int i = 0; i < options.maxDepth; i++) {
            Intersection intersection = checkIntersection(pos, dir, scene.objects);

            if (!intersection.didIntersect()) {
                color.eqadd(throughput.mul(scene.backgroundColor));
                break;
            }

            SceneObject intersectionObject = intersection.intersectionObject();
            Vec3 intersectionPos = intersection.intersectionPosition();
            Material material = scene.materials.get(intersectionObject.materialName);

            color.eqadd(throughput.mul(material.emittance));

            Vec3 normal = intersectionObject.normal(pos, intersectionPos);
            Vec3 wi = material.bsdf.resultantRay(dir, normal);
            double pdf = material.bsdf.pdf(dir, normal);

            throughput.eqmul(material.bsdf.resultantColor(dir, normal).div(pdf));

            double p = Math.max(throughput.x, Math.max(throughput.y, throughput.z));
            if (Math.random() > p) {
                break;
            }

            throughput.eqdiv(p);

            pos = intersectionPos.add(wi.mul(options.bias));
            dir = wi;
        }

        return color;
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

    public static SceneContainer loadScene(String filename) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(filename), SceneContainer.class);
    }
}
