package main;

public abstract class Event {

    protected double time;
    protected int ID;

    public int getID() {
        return this.ID;
    }
    abstract void execute(AbstractDataNode node);
}
