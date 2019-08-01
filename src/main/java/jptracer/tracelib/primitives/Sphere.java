package jptracer.tracelib.primitives;

import jptracer.tracelib.Core;
import jptracer.tracelib.basetypes.Intersection;
import jptracer.tracelib.basetypes.SceneObject;
import jptracer.tracelib.helper.Vec3;

public class Sphere implements SceneObject {
    private Vec3 pos;
    private double radius2;
    private String materialName;

    public Sphere(Vec3 pos, double radius, String materialName) {
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

        double t = Core.minPositive(-b - Math.sqrt(discr), -b + Math.sqrt(discr));
        Vec3 q = pos.add(dir.mul(t));

        return new Intersection(true, t, q, this);
    }

    @Override
    public Vec3 normal(Vec3 p, Vec3 q) {
        return q.sub(this.pos).norm();
    }

    @Override
    public String getMaterialName() {
        return this.materialName;
    }
}
