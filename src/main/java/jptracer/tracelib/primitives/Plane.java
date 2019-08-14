package jptracer.tracelib.primitives;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jptracer.tracelib.basetypes.Intersection;
import jptracer.tracelib.basetypes.SceneObject;
import jptracer.tracelib.helper.Vec3;

import java.io.Serializable;

import static jptracer.tracelib.basetypes.Intersection.getPlanarNormal;

public class Plane extends SceneObject implements Serializable {
    public Vec3 v0, v1, v2;

    @JsonCreator
    public Plane(
            @JsonProperty("vertices") Vec3[] vertices,
            @JsonProperty("materialName") String materialName) {
        this.materialName = materialName;
        this.v0 = vertices[0];
        this.v1 = vertices[1];
        this.v2 = vertices[2];
    }

    @Override
    public Intersection intersection(Vec3 pos, Vec3 dir) {
        Vec3 n = this.v1.sub(this.v0).cross(this.v2.sub(this.v0));

        double nDotDir = n.dot(dir);

        if (Math.abs(nDotDir) < 0.0001) {
            return Intersection.NO_INTERSECTION;
        }

        double s = n.dot(this.v0);
        double t = (n.dot(pos) + s) / nDotDir;

        if (t < 0) {
            return Intersection.NO_INTERSECTION;
        }

        Vec3 q = pos.add(dir.mul(t));

        return new Intersection(true, t, q, this);
    }

    @Override
    public Vec3 normal(Vec3 pos, Vec3 dir, Vec3 hitPos) {
        return getPlanarNormal(pos, this.v0, this.v1, this.v2);
    }
}
