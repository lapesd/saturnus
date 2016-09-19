package main;

import desmoj.core.simulator.EventOf2Entities;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeInstant;


public class Request extends EventOf2Entities<Task, DataNode> {

    private int requestSize;
    private AbstractSimulator model;

    public Request(int requestSize, Model model) {
        super(model, "New request", true);
        this.model = (AbstractSimulator)model;
        this.requestSize = requestSize;
    }

    @Override
    public void eventRoutine(Task task, DataNode dataNode) {
        for (int i = 0; i < this.requestSize; i++) {
            dataNode.setNodeClock(dataNode.getNodeClock() + model.getWriteTime());
            // Using an explicit "Write Event" for testing.
            WriteEvent write = new WriteEvent(this.model);
            write.schedule(task, dataNode, new TimeInstant(dataNode.getNodeClock()));
        }
        sendTraceNote("All requests from " + task + " scheduled.");
    }

}
