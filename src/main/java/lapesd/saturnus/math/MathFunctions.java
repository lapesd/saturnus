package lapesd.saturnus.math;

import java.util.Random;

public class MathFunctions {

    private static Random rand = new Random();

    public static int randomInt(int maxValue) {
        return rand.nextInt(maxValue);
    }

    public static int ceilInt(double value) {
        return (int)Math.ceil(value);
    }
}
