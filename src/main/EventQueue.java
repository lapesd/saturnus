package main;

import java.util.Vector;

public class EventQueue extends OrderedSet {

    private Vector eventQueue;

    public EventQueue() {
        this.eventQueue = new Vector();
    }

    @Override
    void insert(Event event) {
        eventQueue.addElement(event);
    }

    @Override
    Event removeFirst() {
        if(eventQueue.size() == 0)  return null;
        Event aux = (Event)eventQueue.firstElement();
        eventQueue.removeElementAt(0);
        return aux;
    }

    @Override
    int queueSize() {
        return eventQueue.size();
    }

}
