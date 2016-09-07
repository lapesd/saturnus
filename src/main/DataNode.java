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

    /**
     * Insert an event to the node queue of events.
     * @param event - Event to be added.
     */
    @Override
    public void insertEvent(Event event) {
        this.queue.addElement(event);
    }

    /**
     * Remove the first event of the node queue.
     * @return The first evento on the queue.
     */
    @Override
    public Event removeFirstEvent() {
        return (Event)this.queue.remove(0);
    }

    /**
     * Executes all the events of a node.
     */
    @Override
    public void executeAll() {
        while(getQueueSize() != 0) {
            Event newEvent = removeFirstEvent();
            newEvent.execute(this);
        }
    }

    /**
     * Default getter for int NodeID.
     * @return The ID of the node.
     */
    @Override
    public int getID() {
        return this.NodeID;
    }

    /**
     * Default getter for double clock.
     * @return The clock of the node.
     */
    @Override
    public double getClock() {
        return this.clock;
    }

    /**
     * Increase the node clock with the value, passed by parameter.
     * @param value - Value to increase.
     */
    @Override
    public void increaseNodeClock(double value) {
        this.clock += value;
        System.out.println("Clock: " + getClock() + "\n");
    }

    /**
     * Gets the size of the events queue.
     * @return An int with the queue size.
     */
    @Override
    public int getQueueSize() {
        return this.queue.size();
    }

    /**
     * Reset the node clock.
     */
    @Override
    public void clearClock() {
        this.clock = 0.0;
    }

    /**
     * Reset the node event queue.
     */
    @Override
    public void clearQueue() {
        this.queue = new Vector();
    }

}
