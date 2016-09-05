package main;

public class AbstractSimulator {

    private double time;
    protected OrderedSet events;

    public double getTime() {
        return this.time;
    }

    public void insertEvent(Event newEvent) {
        events.insert(newEvent);
    }

    public void executeEvents() {
        Event newEvent;
        while ((newEvent = (Event)events.removeFirst()) != null) {
            this.time += newEvent.getTime();
            newEvent.execute(this);
        }
    }
}
