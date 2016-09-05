package main;

public class Simulator extends AbstractSimulator {

    public static void main(String[] args) {
        new Simulator().init();
    }

    public void init() {
        events = new EventQueue();
        insertEvent(new Event(2.0, 12345));
        insertEvent(new Event(1.5, 00001));
        insertEvent(new Event(6.7, 25555));
        executeEvents();
    }
}