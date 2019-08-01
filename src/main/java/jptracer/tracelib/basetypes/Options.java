package jptracer.tracelib.basetypes;

public class Options {
    public int maxDepth, procCount, sampleCount;
    public double bias = 0.00001;

    public Options withMaxDepth(int depth) {
        this.maxDepth = depth;
        return this;
    }

    public Options withProcCount(int procCount) {
        this.procCount = procCount;
        return this;
    }

    public Options withSampleCount(int sampleCount) {
        this.sampleCount = sampleCount;
        return this;
    }

    public Options withBias(double bias) {
        this.bias = bias;
        return this;
    }
}
