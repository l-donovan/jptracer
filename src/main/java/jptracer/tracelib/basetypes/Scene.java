package jptracer.tracelib.basetypes;

import jptracer.tracelib.helper.Vec3;

import java.util.Map;

public class Scene {
    public Vec3 backgroundColor;

    public SceneObject[] objects;
    public Map<String, Material> materials;

    public Scene() {}
}
