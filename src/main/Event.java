package main;

public abstract class Event {

    protected double time;
    protected int ID;

    public double getTime() {
        return this.time;
    }

    public int getID() {
        return this.ID;
    }
    abstract void execute(AbstractSimulator sim);
}
