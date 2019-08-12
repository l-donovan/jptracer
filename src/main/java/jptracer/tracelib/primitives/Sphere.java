package jptracer.tracelib.primitives;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jptracer.tracelib.basetypes.Intersection;
import jptracer.tracelib.basetypes.SceneObject;
import jptracer.tracelib.helper.MathUtils;
import jptracer.tracelib.helper.Vec3;

public class Sphere extends SceneObject {
    private Vec3 pos;
    private double radius2;

    @JsonCreator
    public Sphere(
            @JsonProperty("pos") Vec3 pos,
            @JsonProperty("radius") double radius,
            @JsonProperty("materialName") String materialName) {
        this.pos = pos;
        this.radius2 = Math.pow(radius, 2.0);
        this.materialName = materialName;
    }

    @Override
    public Intersection intersection(Vec3 pos, Vec3 dir) {
        Vec3 m = pos.sub(this.pos);
        double b = m.dot(dir);
        double c = m.mag2() - this.radius2;

        if (c > 0 && b > 0) {
            return Intersection.NO_INTERSECTION;
        }

        double discr = Math.pow(b, 2) - c;

        if (discr < 0) {
            return Intersection.NO_INTERSECTION;
        }

        double t = MathUtils.minPositive(-b - Math.sqrt(discr), -b + Math.sqrt(discr));
        Vec3 q = pos.add(dir.mul(t));

        return new Intersection(true, t, q, this);
    }

    @Override
    public Vec3 normal(Vec3 p, Vec3 q) {
        return q.sub(this.pos).norm();
    }
}
