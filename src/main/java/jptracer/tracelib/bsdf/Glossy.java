package jptracer.tracelib.bsdf;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jptracer.tracelib.basetypes.BSDF;
import jptracer.tracelib.helper.Vec3;

import static jptracer.tracelib.common.Core.reflect;
import static jptracer.tracelib.helper.MathUtils.randomUnitVecInHemisphere;

public class Glossy extends BSDF {
    private Vec3 diffuseColor;

    @JsonCreator
    public Glossy(@JsonProperty("diffuseColor") Vec3 diffuseColor) {
        this.diffuseColor = diffuseColor;
    }

    @Override
    public Vec3 resultantRay(Vec3 direction, Vec3 normal) {
        return randomUnitVecInHemisphere(reflect(direction, normal), 0.2);
    }

    @Override
    public Vec3 resultantColor(Vec3 direction, Vec3 normal) {
        return this.diffuseColor;
    }

    @Override
    public double pdf(Vec3 direction, Vec3 normal) {
        return direction.neg().dot(normal) * Math.PI;
    }
}
