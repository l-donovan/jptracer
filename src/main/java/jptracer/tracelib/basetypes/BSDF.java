package jptracer.tracelib.basetypes;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jptracer.tracelib.bsdf.*;
import jptracer.tracelib.helper.Vec3;

import java.io.Serializable;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Glass.class, name = "glass"),
        @JsonSubTypes.Type(value = Glossy.class, name = "glossy"),
        @JsonSubTypes.Type(value = Lambert.class, name = "lambert"),
        @JsonSubTypes.Type(value = Mirror.class, name = "mirror")
})
public abstract class BSDF implements Serializable {
    public abstract Vec3 resultantRay(Vec3 direction, Vec3 normal);

    public abstract Vec3 resultantColor(Vec3 direction, Vec3 normal);

    public abstract double pdf(Vec3 direction, Vec3 normal);
}
