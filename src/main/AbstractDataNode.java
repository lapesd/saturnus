package main;

public interface AbstractDataNode {

    void insertEvent(Event event);
    Event removeFirstEvent();
    void executeAll();
    void clearQueue();
    int getID();
    void increaseNodeClock(double value);
    double getClock();
    int getQueueSize();
}
