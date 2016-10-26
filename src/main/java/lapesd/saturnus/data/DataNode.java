package lapesd.saturnus.data;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;
import lapesd.saturnus.event.SubRequest;

import java.util.Vector;

public class DataNode extends Entity {

    private double nodeClock;
    private Vector<SubRequest> subRequestsQueue;

    public DataNode(Model model) {
        super(model, "DataNode", true);
        this.nodeClock = 0.0;
        this.subRequestsQueue = new Vector<SubRequest>();
    }

    /**
     * Just add a given sub request into the queue.
     * @param subRequest
     */
    public void insertSubRequest(SubRequest subRequest) {
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

    /**
     * Get all the sub requests of the queue and schedule them. That means,
     * execute all the events. Just to simplify, the 'execution time' of each
     * event is considered as 1 time unit.
     */
    public void execute() {
        SubRequest toBeExecuted;
        while ((toBeExecuted=removeFirstSubRequest()) != null) {
            toBeExecuted.schedule(toBeExecuted.getRequest(), this, new TimeSpan(nodeClock));
            this.nodeClock++;
        }
    }
}
