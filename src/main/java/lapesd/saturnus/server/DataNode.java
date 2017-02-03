package lapesd.saturnus.server;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;
import lapesd.saturnus.dataStructures.SubReqComparator;
import lapesd.saturnus.event.SubRequest;

import java.util.PriorityQueue;

public class DataNode extends Entity {

    private double nodeClock;
    private int ID;
    private PriorityQueue<SubRequest> subRequestsQueue;

    public DataNode(Model model, int dataNodeID) {
        super(model, "DataNode", true);
        this.nodeClock = 0.0;
        this.ID = dataNodeID;
        this.subRequestsQueue = new PriorityQueue<>(new SubReqComparator());
    }

    public int getID() {
        return this.ID;
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
        this.nodeClock = 0.0;
        SubRequest toBeExecuted;
        while ((toBeExecuted=removeFirstSubRequest()) != null) {
            double timeSent = toBeExecuted.getSendingTime().getTimeAsDouble();
            if (timeSent > nodeClock)  this.nodeClock = timeSent;
            TimeSpan attendedTime = new TimeSpan(nodeClock);
            TimeSpan outputTime = new TimeSpan(nodeClock + toBeExecuted.getExecutionTime());
            toBeExecuted.setAttendedTime(attendedTime);
            toBeExecuted.setOutputTime(outputTime);
            skipTraceNote();    // Avoiding useless messages on trace
            toBeExecuted.schedule(toBeExecuted.getRequest(), this, toBeExecuted.getClient(),
                    attendedTime);
            this.nodeClock += toBeExecuted.getExecutionTime();
        }
    }

    /**
     * Add a given sub request into the queue. Beyond that, check if
     * the sub request needs to be scheduled at some specific time span
     * (usually because of a sync between requests).
     * @param subRequest Sub request to insert
     */
    public double insertSubRequest(SubRequest subRequest) {
        nodeClock += subRequest.getExecutionTime();
        this.subRequestsQueue.add(subRequest);
        return nodeClock;
    }

    /**
     * Remove the first element of the sub request queue.
     * @return The removed element
     */
    private SubRequest removeFirstSubRequest() {
        return this.subRequestsQueue.poll();
    }

}
