package lapesd.saturnus.math;

import java.util.Random;

public class Functions {

    private static Random rand = new Random();

    public static int randomInt(int maxValue) {
        return rand.nextInt(maxValue);
    }
}
