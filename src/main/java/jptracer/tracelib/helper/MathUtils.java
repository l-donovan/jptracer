package jptracer.tracelib.helper;

public class MathUtils {
    public static double clamp(double num, double lower, double upper) {
        return Math.max(Math.min(num, upper), lower);
    }

    private static double randInRange(double l, double u) {
        return l + (u - l) * Math.random();
    }

    public static Vec3 randomUnitVecInHemisphere(Vec3 n, double k) {
        double th = Math.acos(n.y)       + k * Math.PI * randInRange(-0.5, 0.5),
               ph = Math.atan2(n.z, n.x) + k * Math.PI * randInRange(-1.0, 1.0);

        return new Vec3(
            Math.sin(th) * Math.cos(ph),
            Math.cos(th),
            Math.sin(th) * Math.sin(ph)
        );
    }

    public static Vec3 rot(Vec3 v, double cos_rx, double cos_ry, double cos_rz, double sin_rx, double sin_ry, double sin_rz) {
        double c1 = cos_rx * v.z - sin_rx * v.y;
        double c2 = cos_rx * v.y + sin_rx * v.z;
        double c3 = cos_ry * v.x - sin_ry * c1;

        return new Vec3(
                cos_rz * c3 + sin_rz * c2,
                cos_rz * c2 - sin_rz * c3,
                cos_ry * c1 + sin_ry * v.x
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
