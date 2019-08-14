package jptracer.tracelib.primitives;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jptracer.tracelib.basetypes.Intersection;
import jptracer.tracelib.basetypes.SceneObject;
import jptracer.tracelib.helper.Vec3;

public class Quad extends SceneObject {
    private Triangle t0;
    private Triangle t1;

    @JsonCreator
    public Quad(
            @JsonProperty("vertices") Vec3[] vertices,
            @JsonProperty("materialName") String materialName) {
        this.t0 = new Triangle(vertices[0], vertices[1], vertices[2], materialName);
        this.t1 = new Triangle(vertices[2], vertices[3], vertices[0], materialName);
        this.materialName = materialName;
    }

    public Quad(Vec3 v0, Vec3 v1, Vec3 v2, Vec3 v3, String materialName) {
        this.t0 = new Triangle(v0, v1, v2, materialName);
        this.t1 = new Triangle(v2, v3, v0, materialName);
        this.materialName = materialName;
    }

    @Override
    public Intersection intersection(Vec3 pos, Vec3 dir) {
        double minDist = Double.MAX_VALUE;
        Intersection collision;

        collision = t0.intersection(pos, dir);
        if (collision.didIntersect()) {
            minDist = collision.getDistance();
        }

        collision = t1.intersection(pos, dir);
        if (collision.didIntersect()) {
            double dist = collision.getDistance();
            if (dist < minDist) {
                minDist = dist;
            }
        }

        if (minDist < Double.MAX_VALUE) {
            return new Intersection(true, minDist, pos.add(dir.mul(minDist)), this);
        } else {
            return Intersection.NO_INTERSECTION;
        }
    }

    @Override
    public Vec3 normal(Vec3 pos, Vec3 dir, Vec3 hitPos) {
        double minDist = Double.MAX_VALUE;
        Intersection collision;
        Triangle tri = t0;

        collision = t0.intersection(pos, dir);
        if (collision.didIntersect()) {
            minDist = collision.getDistance();
        }

        collision = t1.intersection(pos, dir);
        if (collision.didIntersect() && collision.getDistance() < minDist) {
            tri = t1;
        }

        return tri.normal(pos, dir, hitPos);
    }
}
