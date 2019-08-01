package jptracer.tracelib.basetypes;

import jptracer.tracelib.helper.Vec3;

public interface SceneObject {
    Intersection intersection(Vec3 pos, Vec3 dir);
    Vec3 normal(Vec3 p, Vec3 q);
    String getMaterialName();
}
