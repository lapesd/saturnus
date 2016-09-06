package main;

public class ReadEvent extends Event {

    public ReadEvent(double time, int ID) {
        this.time = time;
        this.ID = ID;
    }

    @Override
    void execute(AbstractSimulator sim) {
        System.out.println("Event Finished.");
        System.out.println("Type: Read, ID: " + getID());
        System.out.println("Clock: " + sim.getMainClock() + "\n");
    }
}
