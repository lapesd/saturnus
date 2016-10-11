package lapesd.saturnus.event;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;
import lapesd.saturnus.data.DataNode;
import lapesd.saturnus.simulator.AbstractSimulator;

public class Request extends Entity {

    private int stripeSize, requestSize, subRequestsAmount;
    private AbstractSimulator model;
    private Task task;
    private DataNode dataNode;

    public Request(Model model, Task task, DataNode dataNode, int requestSize,
                   int stripeSize) {
        super(model, "Request", true);
        this.model = (AbstractSimulator)model;
        this.task = task;
        this.dataNode = dataNode;
        this.requestSize = requestSize;
        this.stripeSize = stripeSize;
        this.subRequestsAmount = requestSize/stripeSize;
    }

    public void executeRequest() {
        for (int i = 0; i < subRequestsAmount; i++) {
            SubRequest newSubRequests = new SubRequest(this.model, this.stripeSize);
            newSubRequests.schedule(this, dataNode, new TimeSpan(dataNode.incrementNodeClock(1)));
        }
    }

    public Task getTask() {
        return this.task;
    }
}
