package jptracer.tracelib.basetypes;

import jptracer.tracelib.helper.Vec3;

import java.util.Map;

public class Scene {
    public Vec3 backgroundColor;
    public SceneObject[] objects;
    public Map<String, Material> materials;

    public Scene() {}

    public Scene withBackgroundColor(Vec3 color) {
        this.backgroundColor = color;
        return this;
    }

    public Scene withMaterials(Map<String, Material> materials) {
        this.materials = materials;
        return this;
    }

    public Scene withObjects(SceneObject[] objects) {
        this.objects = objects;
        return this;
    }
}
