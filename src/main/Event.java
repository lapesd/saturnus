package main;

public class Event {

    private double time;
    private int ID;

    public Event(double time, int ID) {
        this.time = time;
        this.ID = ID;
    }

    public double getTime() {
        return this.time;
    }

    public int getID() {
        return this.ID;
    }

    public void execute(AbstractSimulator sim) {
        System.out.println("Clock: " + sim.getTime());
        System.out.println("Event Finished. ID: " + getID());
    }
}
