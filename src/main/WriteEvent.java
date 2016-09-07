package main;

public class WriteEvent extends Event {

    public WriteEvent(double time, int ID) {
        this.time = time;
        this.ID = ID;
    }

    @Override
    public void execute(AbstractDataNode node) {
        System.out.println("Event Finished.");
        System.out.println("Type: Write, ID: " + getID() + ", Executed on node: " + node.getID());
        node.increaseNodeClock(this.time);
    }
}
