package jptracer.tracelib.helper;

public class MathUtils {
    public static double clamp(double num, double lower, double upper) {
        return Math.max(Math.min(num, upper), lower);
    }

    private static double randInRange(double l, double u) {
        return l + (u - l) * Math.random();
    }

    public static Vec3 randomUnitVecInHemisphere(Vec3 n, double k) {
        double th = Math.acos(n.y) + k * Math.PI * randInRange(-0.5, 0.5);
        double ph = Math.atan2(n.z, n.x) + k * Math.PI * randInRange(-1.0, 1.0);

        return new Vec3(
                Math.sin(th) * Math.cos(ph),
                Math.cos(th),
                Math.sin(th) * Math.sin(ph)
        );
    }

    public static Vec3 rot(Vec3 v, double cosRotX, double cosRotY, double cosRotZ, double sinRotX, double sinRotY, double sinRotZ) {
        double c1 = cosRotX * v.z - sinRotX * v.y;
        double c2 = cosRotX * v.y + sinRotX * v.z;
        double c3 = cosRotY * v.x - sinRotY * c1;

        return new Vec3(
                cosRotZ * c3 + sinRotZ * c2,
                cosRotZ * c2 - sinRotZ * c3,
                cosRotY * c1 + sinRotY * v.x
        );
    }

    public static double minPositive(double a, double b) {
        if ((a < 0) && (b < 0)) {
            return 0;
        } else if ((a < 0) ^ (b < 0)) {
            return Math.max(a, b);
        } else {
            return Math.min(a, b);
        }
    }

}
