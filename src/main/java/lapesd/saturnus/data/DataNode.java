package lapesd.saturnus.data;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.Queue;
import lapesd.saturnus.event.Request;

public class DataNode extends Entity {

    private double nodeClock;
    private Queue<Request> requestQueue;

    public DataNode(Model model) {
        super(model, "DataNode", true);
        this.nodeClock = 0.0;
        this.requestQueue = new Queue<Request>(model, "Request queue: " + this, true, true);
    }

    public double getNodeClock() {
        return this.nodeClock;
    }

    public double incrementNodeClock(double value) {
        return this.nodeClock += value;
    }

    public void insertRequestToQueue(Request newRequest) {
        this.requestQueue.insert(newRequest);
    }

    public void execute() {
        int queueSize = this.requestQueue.size();
        for (int i = 0; i < queueSize; i++) {
            removeFirstRequest().executeRequest();
        }
    }

    private Request removeFirstRequest() {
        return this.requestQueue.removeFirst();
    }
}
