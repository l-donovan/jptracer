package jptracer.tracelib.basetypes;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jptracer.tracelib.helper.Vec3;

import java.io.Serializable;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type")
public abstract class SceneObject implements Serializable {
    public abstract Intersection intersection(Vec3 pos, Vec3 dir);

    public abstract Vec3 normal(Vec3 pos, Vec3 dir, Vec3 hitPos);

    public String materialName;
}
