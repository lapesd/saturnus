package lapesd.saturnus.model.server;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;
import lapesd.saturnus.model.dataStructures.SubReqComparator;
import lapesd.saturnus.model.event.SubRequest;
import lapesd.saturnus.control.AbstractSimulator;

import java.util.PriorityQueue;

public class DataNode extends Entity {

    private double nodeClock;
    private int ID;
    private AbstractSimulator model;
    private PriorityQueue<SubRequest> subRequestsQueue;

    public DataNode(Model model, int dataNodeID) {
        super(model, "DataNode", true);
        this.model = (AbstractSimulator)model;
        this.nodeClock = 0.0;
        this.ID = dataNodeID;
        this.subRequestsQueue = new PriorityQueue<>(new SubReqComparator());
    }

    public int getID() {
        return this.ID;
    }


    /**
     * Add a given sub request into the queue. Beyond that, check if
     * the sub-request needs to be scheduled at some specific time span
     * (usually because of a sync between requests).
     * @param subRequest Sub-request to insert
     */
    public void insertSubRequest(SubRequest subRequest) {
        if (subRequest.getSendingTime() == null) {
            subRequest.setSendingTime(new TimeSpan(nodeClock));
        }
        this.subRequestsQueue.add(subRequest);
    }

    /**
     * Remove the first element of the sub-request queue.
     * @return The removed element
     */
    private SubRequest removeFirstSubRequest() {
        return this.subRequestsQueue.poll();
    }


    /**
     * Get the given sub-request and schedule it. That means, execute the event.
     * Just to simplify, the 'execution time' of each event is considered as 1
     * time unit(incremented into the sub request).
     */
    public void executeOneSubRequest() {
        SubRequest toBeExecuted = removeFirstSubRequest();
        if (toBeExecuted != null) {
            double execTime = toBeExecuted.getExecutionTime();
            double sendingTime = toBeExecuted.getSendingTime().getTimeAsDouble();
            // Used to sync. among data nodes.
            // i.e. If the sending time is greater than the node clock, update it.
            this.nodeClock = (nodeClock < sendingTime) ? sendingTime : nodeClock;
            TimeSpan attendedTime = new TimeSpan(nodeClock);
            TimeSpan outputTime = new TimeSpan(nodeClock + execTime);
            toBeExecuted.setAttendedTime(attendedTime);
            toBeExecuted.setOutputTime(outputTime);

            skipTraceNote();    // Avoiding useless messages on trace
            toBeExecuted.schedule(toBeExecuted.getRequest(), this, toBeExecuted.getClient(),
                    attendedTime);
            this.nodeClock += execTime;
            this.model.saveSubRequest(toBeExecuted);

            // Send request ID and the sub-request output time.
            toBeExecuted.getClient().sendFinishedSignal(toBeExecuted.getRequest(), this.nodeClock);
        }
    }
}
