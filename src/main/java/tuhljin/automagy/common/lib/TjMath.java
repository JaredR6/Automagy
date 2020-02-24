package tuhljin.automagy.common.lib;

public class TjMath {
    public TjMath() {
    }

    public static double approachLinear(double a, double b, double max) {
        return a > b ? (a - b < max ? b : a - max) : (b - a < max ? b : a + max);
    }
}
