package lapesd.saturnus.math;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MathFunctions {

    private static Random rand = new Random();

    public static int randomInt(int maxValue) {
        return rand.nextInt(maxValue);
    }

    public static int[] randomInt(int maxValue, int numberOfIntegers) {
        ArrayList<Integer> range = new ArrayList<Integer>();
        int[] randomNumbers = new int[numberOfIntegers];
        for (int i = 0; i < maxValue; i++) {
            range.add(i);
        }
        Collections.shuffle(range);
        for (int i = 0; i < numberOfIntegers; i++) {
            randomNumbers[i] = range.get(i);
        }
        return randomNumbers;
    }

    public static int ceilInt(double value) {
        return (int)Math.ceil(value);
    }
}
