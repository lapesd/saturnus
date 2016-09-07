package main;

public class Random {

    public static double exponential(double value) {
        return Math.abs(value * Math.log(Math.random()));
    }

    public static int randomInt(int min, int max) {
        java.util.Random rand = new java.util.Random();
        return rand.nextInt((max + 1) - min) + min;
    }
}
