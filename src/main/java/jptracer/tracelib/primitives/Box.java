package jptracer.tracelib.primitives;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jptracer.tracelib.basetypes.Intersection;
import jptracer.tracelib.basetypes.SceneObject;
import jptracer.tracelib.helper.Vec3;

public class Box extends SceneObject {
    private Quad[] quads = new Quad[6];
    private Vec3 pos;

    private double cosRotX, cosRotY, cosRotZ, sinRotX, sinRotY, sinRotZ;

    private Vec3 rotPos(double x, double y, double z) {
        double c1 = cosRotX * z - sinRotX * y;
        double c2 = cosRotX * y + sinRotX * z;
        double c3 = cosRotY * x - sinRotY * c1;

        return new Vec3(
                cosRotZ * c3 + sinRotZ * c2 + pos.x,
                cosRotZ * c2 - sinRotZ * c3 + pos.y,
                cosRotY * c1 + sinRotY * x + pos.z
        );
    }

    @JsonCreator
    public Box(
            @JsonProperty("pos") Vec3 pos,
            @JsonProperty("rot") Vec3 rot,
            @JsonProperty("dim") double[] dim,
            @JsonProperty("materialName") String materialName) {
        this.pos = pos;
        this.materialName = materialName;

        this.cosRotX = Math.cos(rot.x);
        this.cosRotY = Math.cos(rot.y);
        this.cosRotZ = Math.cos(rot.z);
        this.sinRotX = Math.sin(rot.x);
        this.sinRotY = Math.sin(rot.y);
        this.sinRotZ = Math.sin(rot.z);

        double l = dim[0];
        double w = dim[1];
        double h = dim[2];

        Vec3 v0 = rotPos(-l / 2.0,  h / 2.0, -w / 2.0);
        Vec3 v1 = rotPos( l / 2.0,  h / 2.0, -w / 2.0);
        Vec3 v2 = rotPos( l / 2.0,  h / 2.0,  w / 2.0);
        Vec3 v3 = rotPos(-l / 2.0,  h / 2.0,  w / 2.0);
        Vec3 v4 = rotPos(-l / 2.0, -h / 2.0, -w / 2.0);
        Vec3 v5 = rotPos( l / 2.0, -h / 2.0, -w / 2.0);
        Vec3 v6 = rotPos( l / 2.0, -h / 2.0,  w / 2.0);
        Vec3 v7 = rotPos(-l / 2.0, -h / 2.0,  w / 2.0);

        quads[0] = new Quad(v0, v1, v5, v4, materialName);
        quads[1] = new Quad(v1, v2, v6, v5, materialName);
        quads[2] = new Quad(v2, v3, v7, v6, materialName);
        quads[3] = new Quad(v3, v0, v4, v7, materialName);
        quads[4] = new Quad(v3, v2, v1, v0, materialName);
        quads[5] = new Quad(v4, v5, v6, v7, materialName);
    }

    @Override
    public Intersection intersection(Vec3 pos, Vec3 dir) {
        double minDist = Double.MAX_VALUE;
        Intersection collision;

        for (Quad quad : quads) {
            collision = quad.intersection(pos, dir);

            if (collision.didIntersect() && collision.getDistance() < minDist) {
                minDist = collision.getDistance();
            }
        }

        if (minDist < Double.MAX_VALUE) {
            return new Intersection(true, minDist, pos.add(dir.mul(minDist)), this);
        } else {
            return Intersection.NO_INTERSECTION;
        }
    }

    @Override
    public Vec3 normal(Vec3 pos, Vec3 dir, Vec3 hitPos) {
        double minDist = Double.MAX_VALUE;
        Intersection collision;
        Quad q = quads[0];

        for (Quad quad : quads) {
            collision = quad.intersection(pos, dir);

            if (collision.didIntersect() && collision.getDistance() < minDist) {
                minDist = collision.getDistance();
                q = quad;
            }
        }

        return q.normal(pos, dir, hitPos);
    }
}
