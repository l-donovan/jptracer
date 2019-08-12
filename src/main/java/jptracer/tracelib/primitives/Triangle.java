package jptracer.tracelib.primitives;

import jptracer.tracelib.basetypes.Intersection;
import jptracer.tracelib.basetypes.SceneObject;
import jptracer.tracelib.helper.Vec3;

import static jptracer.tracelib.basetypes.Intersection.getPlanarNormal;

public class Triangle extends SceneObject {
    public Vec3 v0, v1, v2;

    public Triangle() {}

    public Triangle(Vec3 v0, Vec3 v1, Vec3 v2, String materialName) {
        this.v0 = v0;
        this.v1 = v1;
        this.v2 = v2;
        this.materialName = materialName;
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

        if (n.dot(this.v1.sub(this.v0).cross(q.sub(this.v0))) < 0 ||
            n.dot(this.v2.sub(this.v1).cross(q.sub(this.v1))) < 0 ||
            n.dot(this.v0.sub(this.v2).cross(q.sub(this.v2))) < 0) {
            return Intersection.NO_INTERSECTION;
        }

        return new Intersection(true, t, q, this);
    }

    @Override
    public Vec3 normal(Vec3 p, Vec3 q) {
        return getPlanarNormal(p, this.v0, this.v0, this.v2);
    }
}
