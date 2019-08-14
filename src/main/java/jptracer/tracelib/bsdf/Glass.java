package jptracer.tracelib.bsdf;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jptracer.tracelib.basetypes.BSDF;
import jptracer.tracelib.helper.Vec3;

import static jptracer.tracelib.common.Core.*;

public class Glass extends BSDF {
    private double ior;

    @JsonCreator
    public Glass(@JsonProperty("ior") double ior) {
        this.ior = ior;
    }

    @Override
    public Vec3 resultantRay(Vec3 direction, Vec3 normal) {
        double f = fresnel(direction, normal, ior);

        if (Math.random() <= f) {
            return reflect(direction, normal);
        } else {
            return refract(direction, normal, ior);
        }
    }

    @Override
    public Vec3 resultantColor(Vec3 direction, Vec3 normal) {
        return new Vec3(0.9, 0.9, 0.9);
    }

    @Override
    public double pdf(Vec3 direction, Vec3 normal) {
        return 1.0;
    }
}
