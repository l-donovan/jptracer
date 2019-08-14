package jptracer.tracelib.basetypes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class Scene {
    public SceneObject[] objects;
    public Map<String, Material> materials;

    @JsonCreator
    public Scene(@JsonProperty("objects") SceneObject[] objects,
                 @JsonProperty("materials") Map<String, Material> materials) {
        this.objects = objects;
        this.materials = materials;
    }
}
