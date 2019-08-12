package jptracer.tracelib.bsdf;

import jptracer.tracelib.basetypes.BSDF;
import jptracer.tracelib.helper.Vec3;

import static jptracer.tracelib.common.Core.reflect;

public class Mirror extends BSDF {
    public Vec3 specularColor;

    public Mirror() {}

    @Override
    public Vec3 resultantRay(Vec3 direction, Vec3 normal) {
        return reflect(direction, normal);
    }

    @Override
    public Vec3 resultantColor(Vec3 direction, Vec3 normal) {
        return this.specularColor;
    }

    @Override
    public double pdf(Vec3 direction, Vec3 normal) {
        return 1.0;
    }

    public Mirror withSpecularColor(Vec3 specularColor) {
        this.specularColor = specularColor;
        return this;
    }
}
