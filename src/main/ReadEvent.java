package main;

public class ReadEvent extends Event {

    public ReadEvent(double time, int ID) {
        this.time = time;
        this.ID = ID;
    }

    @Override
    void execute(AbstractDataNode node) {
        System.out.println("Event Finished.");
        System.out.println("Type: Read, ID: " + getID() + " , Executed on node: " + node.getID());
        node.increaseNodeClock(this.time);
    }
}
