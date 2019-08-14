package jptracer.tracelib.helper;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
public class Vec3 {
    public double x, y, z;

    public Vec3() {}

    public Vec3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double dot(Vec3 v) {
        return this.x * v.x + this.y * v.y + this.z * v.z;
    }

    public Vec3 cross(Vec3 v) {
        return new Vec3(
            this.y * v.z - this.z * v.y,
            this.z * v.x - this.x * v.z,
            this.x * v.y - this.y * v.x
        );
    }

    public double mag2() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    private double mag() {
        return Math.sqrt(this.mag2());
    }

    public Vec3 norm() {
        double m = this.mag();

        if (m == 0.0) {
            return new Vec3(0, 0, 0);
        } else {
            return new Vec3(this.x / m, this.y / m, this.z / m);
        }
    }

    public void eqnorm() {
        double m = this.mag();

        if (m == 0.0) {
            this.x = 0;
            this.y = 0;
            this.z = 0;
        } else {
            this.x /= m;
            this.y /= m;
            this.z /= m;
        }
    }

    public Vec3 add(double n) {
        return new Vec3(this.x + n, this.y + n, this.z + n);
    }

    public Vec3 add(Vec3 v) {
        return new Vec3(this.x + v.x, this.y + v.y, this.z + v.z);
    }

    public void eqadd(double n) {
        this.x += n;
        this.y += n;
        this.z += n;
    }

    public void eqadd(Vec3 v) {
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;
    }

    public Vec3 sub(double n) {
        return new Vec3(this.x - n, this.y - n, this.z - n);
    }

    public Vec3 sub(Vec3 v) {
        return new Vec3(this.x - v.x, this.y - v.y, this.z - v.z);
    }

    public void eqsub(double n) {
        this.x -= n;
        this.y -= n;
        this.z -= n;
    }

    public void eqsub(Vec3 v) {
        this.x -= v.x;
        this.y -= v.y;
        this.z -= v.z;
    }

    public Vec3 mul(double n) {
        return new Vec3(this.x * n, this.y * n, this.z * n);
    }

    public Vec3 mul(Vec3 v) {
        return new Vec3(this.x * v.x, this.y * v.y, this.z * v.z);
    }

    public void eqmul(double n) {
        this.x *= n;
        this.y *= n;
        this.z *= n;
    }

    public void eqmul(Vec3 v) {
        this.x *= v.x;
        this.y *= v.y;
        this.z *= v.z;
    }

    public Vec3 div(double n) {
        return new Vec3(this.x / n, this.y / n, this.z / n);
    }

    public Vec3 div(Vec3 v) {
        return new Vec3(this.x / v.x, this.y / v.y, this.z / v.z);
    }

    public void eqdiv(double n) {
        this.x /= n;
        this.y /= n;
        this.z /= n;
    }

    public void eqdiv(Vec3 v) {
        this.x /= v.x;
        this.y /= v.y;
        this.z /= v.z;
    }

    public Vec3 neg() {
        return new Vec3(-this.x, -this.y, -this.z);
    }
}
