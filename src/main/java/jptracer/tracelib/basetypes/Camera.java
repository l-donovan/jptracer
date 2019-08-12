package jptracer.tracelib.basetypes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jptracer.tracelib.helper.Vec3;

public class Camera {
    public double fov, xScale, yScale, cos_rx, cos_ry, cos_rz, sin_rx, sin_ry, sin_rz;
    public int res_x, res_y;
    public Vec3 pos, rot;

    public Camera() {}

    @JsonCreator
    public Camera(
            @JsonProperty("fov") double fov,
            @JsonProperty("pos") Vec3 pos,
            @JsonProperty("rot") Vec3 rot,
            @JsonProperty("res") int[] res) {
        this.fov = fov;
        this.pos = pos;
        this.rot = rot;
        this.res_x = res[0];
        this.res_y = res[1];
        this.updateCachedValues();
    }

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

        this.yScale = Math.tan(Math.toRadians(this.fov / 2.0));
        this.xScale = this.yScale * this.res_x / this.res_y;
    }
}
