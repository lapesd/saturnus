package lapesd.saturnus.model.utils;

import java.util.ArrayList;
import java.util.Collections;

public class MathFunctions {
    public static long[] randomLong(long maxValue, long numberOfIntegers) {
        ArrayList<Integer> range = new ArrayList<>();
        long[] randomNumbers = new long[(int)numberOfIntegers];
        for (int i = 0; i < maxValue; i++) {
            range.add(i);
        }
        Collections.shuffle(range);
        for (int i = 0; i < numberOfIntegers; i++) {
            randomNumbers[i] = range.get(i);
        }
        return randomNumbers;
    }
}
