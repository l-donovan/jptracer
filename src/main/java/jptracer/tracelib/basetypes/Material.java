package jptracer.tracelib.basetypes;

import jptracer.tracelib.helper.Vec3;

public class Material {
    public Vec3 reflectance, refractance, emittance;
    public BSDF bsdf;

    public Material withReflectance(Vec3 reflectance) {
        this.reflectance = reflectance;
        return this;
    }

    public Material withRefractance(Vec3 refractance) {
        this.refractance = refractance;
        return this;
    }

    public Material withEmittance(Vec3 em) {
        this.emittance = em;
        return this;
    }

    public Material withBSDF(BSDF bsdf) {
        this.bsdf = bsdf;
        return this;
    }
}
