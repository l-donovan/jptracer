package jptracer.tracelib.basetypes;

import jptracer.tracelib.helper.Vec3;

public class Intersection {
    private boolean intersected;
    private double distance;
    private Vec3 position;
    private SceneObject object;

    public static final Intersection NO_INTERSECTION = new Intersection(false, Double.POSITIVE_INFINITY, null, null);

    public Intersection(boolean intersected, double dist, Vec3 pos, SceneObject object) {
        this.intersected = intersected;
        this.distance = dist;
        this.position = pos;
        this.object = object;
    }

    public boolean didIntersect() {
        return this.intersected;
    }

    public double getDistance() {
        return this.distance;
    }

    public Vec3 intersectionPosition() {
        return this.position;
    }

    public SceneObject intersectionObject() {
        return this.object;
    }

    public static Vec3 getPlanarNormal(Vec3 p, Vec3 v0, Vec3 v1, Vec3 v2) {
        Vec3 n = v1.sub(v0).cross(v2.sub(v0));
        double r = n.dot(p) + n.dot(v0);

        if (r >= 0) {
            n = n.neg();
        }

        return n.norm();
    }
}
