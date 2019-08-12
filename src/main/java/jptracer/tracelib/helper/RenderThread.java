package jptracer.tracelib.helper;

import jptracer.tracelib.common.Core;
import jptracer.tracelib.basetypes.Camera;
import jptracer.tracelib.basetypes.Options;
import jptracer.tracelib.basetypes.Scene;

import java.util.EmptyStackException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import static jptracer.tracelib.helper.MathUtils.clamp;

public class RenderThread implements Runnable {
    private Scene scene;
    private Camera camera;
    private Options options;
    private final PreloadedQueue<Integer> inQueue;
    private final SlottedList<Vec3[]> outList;

    private final AtomicBoolean running = new AtomicBoolean(true);

    public RenderThread(Scene scene, Camera camera, Options options, PreloadedQueue<Integer> inQueue, SlottedList<Vec3[]> outList) {
        this.scene = scene;
        this.camera = camera;
        this.options = options;
        this.inQueue = inQueue;
        this.outList = outList;
    }

    public void start() {
        Thread worker = new Thread(this);
        worker.start();
    }

    public void stop() {
        this.running.set(false);
    }

    @Override
    public void run() {
        running.set(true);
        while (running.get()) {
            try {
                int y = inQueue.pop();
                Vec3[] row = new Vec3[camera.res_x];
                for (int x = 0; x < camera.res_x; x++) {
                    Vec3 totalColor = new Vec3(0.0, 0.0, 0.0);
                    Vec3 vec = MathUtils.rot(new Vec3(
                        (2 * (x + 0.5) / camera.res_x - 1) * camera.xScale,
                        (1 - 2 * (y + 0.5) / camera.res_y) * camera.yScale,
                        1.0
                    ), camera.cos_rx, camera.cos_ry, camera.cos_rz,
                       camera.sin_rx, camera.sin_ry, camera.sin_rz).norm();

                    Vec3 colorSample;
                    for (int i = 0; i < options.sampleCount; i++) {
                        colorSample = Core.castRay(camera.pos, vec, scene, options);
                        totalColor = totalColor.add(colorSample);
                    }

                    totalColor = new Vec3(
                        clamp(totalColor.x / options.sampleCount, 0.0, 1.0),
                        clamp(totalColor.y / options.sampleCount, 0.0, 1.0),
                        clamp(totalColor.z / options.sampleCount, 0.0, 1.0)
                    );

                    row[x] = totalColor;
                }

                System.out.print(String.format("%1$4s ", y));

                outList.set(y, row);
            } catch (EmptyStackException | TimeoutException e) {
                running.set(false);
            }
        }
    }
}
