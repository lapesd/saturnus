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

    public void insertSubRequest(SubRequest subReq) {
        this.subRequestsQueue.add(subReq);
    }

    public void execute() {
        int queueSize = this.subRequestsQueue.size();
        for (int i = 0; i < queueSize; i++) {
            SubRequest toBeExecuted = removeFirstSubRequest();
            toBeExecuted.schedule(toBeExecuted.getRequest(), this, new TimeSpan(nodeClock));
            this.nodeClock++;
        }
    }

    private SubRequest removeFirstSubRequest() {
        return this.subRequestsQueue.remove(0);
    }

}
