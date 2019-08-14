package jptracer.tracelib.basetypes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jptracer.tracelib.helper.Vec3;

public class Camera {
    public double fov, xScale, yScale, cosRotX, cosRotY, cosRotZ, sinRotX, sinRotY, sinRotZ;
    public int res_x, res_y;
    public Vec3 pos, rot;

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

    public void updateCachedValues() {
        this.cosRotX = Math.cos(this.rot.x);
        this.cosRotY = Math.cos(this.rot.y);
        this.cosRotZ = Math.cos(this.rot.z);
        this.sinRotX = Math.sin(this.rot.x);
        this.sinRotY = Math.sin(this.rot.y);
        this.sinRotZ = Math.sin(this.rot.z);

        this.yScale = Math.tan(Math.toRadians(this.fov / 2.0));
        this.xScale = this.yScale * this.res_x / this.res_y;
    }
}
