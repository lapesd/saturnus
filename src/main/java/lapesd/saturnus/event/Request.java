package lapesd.saturnus.event;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;
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

    public Task getTask() {
        return this.task;
    }

    /**
     * Creates sub requests and insert the into the request data node.
     */
    public void scheduleRequest() {
        for (int i = 0; i < subRequestsAmount; i++) {
            SubRequest newSubRequests = new SubRequest(model, this, stripeSize);
            dataNode.insertSubRequest(newSubRequests);
        }
    }

}
