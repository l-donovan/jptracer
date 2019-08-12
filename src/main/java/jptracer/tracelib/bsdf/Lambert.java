package jptracer.tracelib.bsdf;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jptracer.tracelib.basetypes.BSDF;
import jptracer.tracelib.helper.Vec3;

import static jptracer.tracelib.helper.MathUtils.randomUnitVecInHemisphere;

public class Lambert extends BSDF {
    private Vec3 diffuseColor;

    @JsonCreator
    public Lambert(@JsonProperty("diffuseColor") Vec3 diffuseColor) {
        this.diffuseColor = diffuseColor;
    }

    @Override
    public Vec3 resultantRay(Vec3 direction, Vec3 normal) {
        return randomUnitVecInHemisphere(normal, 1.0);
    }

    @Override
    public Vec3 resultantColor(Vec3 direction, Vec3 normal) {
        return this.diffuseColor.mul(this.pdf(direction, normal));
    }

    @Override
    public double pdf(Vec3 direction, Vec3 normal) {
        return direction.neg().dot(normal) * Math.PI;
    }

    public Lambert withDiffuseColor(Vec3 diffuseColor) {
        this.diffuseColor = diffuseColor;
        return this;
    }
}
