package main;

import java.util.Vector;

public class DataNode implements AbstractDataNode {

    private final int NodeID;
    private double clock;
    private Vector queue;

    public DataNode(int NodeID) {
        this.NodeID = NodeID;
        this.queue = new Vector();
        this.clock = 0;
        System.out.println("Data Node up. ID: " + this.NodeID);
    }

    @Override
    public void insertEvent(Event event) {
        this.queue.addElement(event);
    }

    @Override
    public Event removeFirstEvent() {
        return (Event)this.queue.remove(0);
    }

    @Override
    public void executeAll() {
        while(getQueueSize() != 0) {
            Event newEvent = removeFirstEvent();
            newEvent.execute(this);
        }
    }

    @Override
    public void clearQueue() {
        this.queue = new Vector();
    }

    @Override
    public int getID() {
        return this.NodeID;
    }

    @Override
    public void increaseNodeClock(double value) {
        this.clock += value;
        System.out.println("Clock: " + getClock() + "\n");
    }

    @Override
    public double getClock() {
        return this.clock;
    }

    @Override
    public int getQueueSize() {
        return this.queue.size();
    }
}
