package main;

public interface AbstractDataNode {

    void insertEvent(Event event);
    Event removeFirstEvent();
    void executeAll();
    int getID();
    double getClock();
    void increaseNodeClock(double value);
    int getQueueSize();
    void clearClock();
    void clearQueue();
}
