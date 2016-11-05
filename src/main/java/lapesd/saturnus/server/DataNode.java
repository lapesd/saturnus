package lapesd.saturnus.server;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;
import lapesd.saturnus.event.SubRequest;

import java.util.Vector;

public class DataNode extends Entity {

    private double nodeClock;
    private int ID;
    private Vector<SubRequest> subRequestsQueue;

    public DataNode(Model model, int dataNodeID) {
        super(model, "DataNode", true);
        this.nodeClock = 0.0;
        this.ID = dataNodeID;
        this.subRequestsQueue = new Vector<SubRequest>();
    }

    public double getNodeClock() {
        return this.nodeClock;
    }

    /**
     * Get all the sub requests of the queue and schedule them. That means,
     * execute all the events. Just to simplify, the 'execution time' of each
     * event is considered as 1 time unit(incremented into the sub request).
     */
    public void execute() {
        SubRequest toBeExecuted;
        while ((toBeExecuted=removeFirstSubRequest()) != null) {
            toBeExecuted.schedule(toBeExecuted.getRequest(), this, toBeExecuted.getScheduleTime());
        }
    }

    /**
     * Add a given sub request into the queue. Beyond that, check if
     * the sub request needs to be scheduled at some specific time span
     * (usually because of a sync between requests).
     * @param subRequest Sub request to insert
     */
    public void insertSubRequest(SubRequest subRequest) {
        TimeSpan scheduleTime = subRequest.getScheduleTime();
        if (scheduleTime == null || scheduleTime.getTimeAsDouble() < this.nodeClock)
            subRequest.setScheduleTime(new TimeSpan(this.nodeClock));
        else
            this.nodeClock = scheduleTime.getTimeAsDouble();
        incrementNodeClock(subRequest.getExecutionTime());
        this.subRequestsQueue.add(subRequest);
    }

    /**
     * Remove the first element of the sub request queue.
     * @return The removed element
     */
    private SubRequest removeFirstSubRequest() {
        if (subRequestsQueue.size() == 0) return null;
        return this.subRequestsQueue.remove(0);
    }

    private void incrementNodeClock(double time) {
        this.nodeClock += time;
    }
}
