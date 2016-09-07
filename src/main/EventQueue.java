package main;

import java.util.Vector;

/**
 * Implementation of a queue of events, using java.util.Vector, for a
 * dynamically allocated vector.
 */
public class EventQueue extends OrderedSet {

    private Vector eventQueue;

    public EventQueue() {
        this.eventQueue = new Vector();
    }

    /**
     * Insert an event in the end of the queue.
     * @param event - Event to be added to the queue.
     */
    @Override
    void insert(Event event) {
        eventQueue.addElement(event);
    }

    /**
     * Remove the first element from the queue.
     * @return The first element to be executed, on the queue.
     */
    @Override
    Event removeFirst() {
        if(eventQueue.size() == 0)  return null;
        Event aux = (Event)eventQueue.firstElement();
        eventQueue.removeElementAt(0);
        return aux;
    }

    /**
     * Gets the number of elements into the queue.
     * @return Size of the queue.
     */
    @Override
    int queueSize() {
        return eventQueue.size();
    }

}
