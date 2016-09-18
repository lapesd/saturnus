package main;

public class Random {

    public static double exponential(double value) {
        return Math.abs(value * Math.log(Math.random()));
    }
}
