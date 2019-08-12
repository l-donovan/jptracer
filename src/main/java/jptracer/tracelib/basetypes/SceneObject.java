package jptracer.tracelib.basetypes;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jptracer.tracelib.helper.Vec3;
import jptracer.tracelib.primitives.*;

import java.io.Serializable;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = Sphere.class, name = "sphere"),
    @JsonSubTypes.Type(value = Plane.class, name = "plane"),
    @JsonSubTypes.Type(value = Triangle.class, name = "triangle")
})
public abstract class SceneObject implements Serializable {
    public abstract Intersection intersection(Vec3 pos, Vec3 dir);
    public abstract Vec3 normal(Vec3 p, Vec3 q);
    public String materialName;
    public Vec3 pos;
}
