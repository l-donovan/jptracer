package jptracer.tracelib.primitives;

import jptracer.tracelib.basetypes.Intersection;
import jptracer.tracelib.basetypes.SceneObject;
import jptracer.tracelib.helper.Vec3;

public class Mesh extends SceneObject {
    @Override
    public Intersection intersection(Vec3 pos, Vec3 dir) {
        return null;
    }

    @Override
    public Vec3 normal(Vec3 pos, Vec3 dir, Vec3 hitPos) {
        return null;
    }
}
