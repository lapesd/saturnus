package lapesd.saturnus.model.math;

import java.util.ArrayList;
import java.util.Collections;

public class MathFunctions {
    public static int[] randomInt(int maxValue, int numberOfIntegers) {
        ArrayList<Integer> range = new ArrayList<>();
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
}
