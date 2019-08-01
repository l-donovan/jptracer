package jptracer.tracelib.basetypes;

import jptracer.tracelib.helper.Vec3;

public class Camera {
    public double fov, aspectRatio, scale, cos_rx, cos_ry, cos_rz, sin_rx, sin_ry, sin_rz;
    public int res_x, res_y;
    public Vec3 pos, rot;

    public Camera() {}

    public Camera withFOV(double fov) {
        this.fov = fov;
        return this;
    }

    public Camera withRes(int width, int height) {
        this.res_x = width;
        this.res_y = height;
        return this;
    }

    public Camera withPos(Vec3 pos) {
        this.pos = pos;
        return this;
    }

    public Camera withRot(Vec3 rot) {
        this.rot = rot;
        return this;
    }

    public void updateCachedValues() {
        this.cos_rx = Math.cos(this.rot.x);
        this.cos_ry = Math.cos(this.rot.y);
        this.cos_rz = Math.cos(this.rot.z);
        this.sin_rx = Math.sin(this.rot.x);
        this.sin_ry = Math.sin(this.rot.y);
        this.sin_rz = Math.sin(this.rot.z);

        this.aspectRatio = 1.0 * this.res_x / this.res_y;
        this.scale = Math.tan(Math.toRadians(this.fov / 2.0));
    }
}
