package jptracer.tracelib.basetypes;

import jptracer.tracelib.helper.Vec3;

public class Material {
    public Vec3 reflectance, emittance;
    public brdfModelType brdfModel;

    public enum brdfModelType {
        DIFFUSE, GLOSSY, MIRROR
    }

    public Material withReflectance(Vec3 reflectance) {
        this.reflectance = reflectance;
        return this;
    }

    public Material withEmittance(Vec3 em) {
        this.emittance = em;
        return this;
    }

    public Material withBRDFModel(brdfModelType model) {
        this.brdfModel = model;
        return this;
    }
}
