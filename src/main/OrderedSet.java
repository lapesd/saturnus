package main;

public abstract class OrderedSet {

    abstract void insert(Event event);
    abstract Event removeFirst();
    abstract int queueSize();
}
