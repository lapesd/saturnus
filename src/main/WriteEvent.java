package main;

public class WriteEvent extends Event {

    public WriteEvent(double time, int ID) {
        this.time = time;
        this.ID = ID;
    }

    @Override
    public void execute(AbstractSimulator sim) {
        System.out.println("Event Finished.");
        System.out.println("Type: Write, ID: " + getID());
        System.out.println("Clock: " + sim.getMainClock() + "\n");
    }
}
