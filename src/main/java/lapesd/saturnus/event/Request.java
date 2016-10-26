package lapesd.saturnus.event;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;
import lapesd.saturnus.data.DataNode;
import lapesd.saturnus.simulator.AbstractSimulator;

public class Request extends Entity {

    private int stripeSize, requestSize, subRequestsAmount, offset;
    private AbstractSimulator model;
    private Task task;
    private DataNode dataNode;

    public Request(Model model, Task task, DataNode dataNode, int requestSize,
                   int stripeSize, int offset) {
        super(model, "Request", true);
        this.model = (AbstractSimulator)model;
        this.task = task;
        this.dataNode = dataNode;
        this.stripeSize = stripeSize;
        this.requestSize = requestSize;
        this.subRequestsAmount = requestSize/stripeSize;
        this.offset = offset;
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
        sendTraceNote(task + " writing/reading from offset " + this.offset
                        + " - " + this);
    }

}
