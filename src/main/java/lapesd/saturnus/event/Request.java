package lapesd.saturnus.event;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;
import lapesd.saturnus.data.DataNode;
import lapesd.saturnus.simulator.AbstractSimulator;

public class Request extends Entity {

    private int stripeSize, requestSize, subRequestsAmount, segmentID;
    private AbstractSimulator model;
    private DataNode dataNode;

    public Request(Model model, DataNode dataNode, int requestSize,
                   int stripeSize, int segmentID) {
        super(model, "Request", true);
        this.model = (AbstractSimulator)model;
        this.requestSize = requestSize;
        this.stripeSize = stripeSize;
        this.dataNode = dataNode;
        this.segmentID = segmentID;
        this.subRequestsAmount = requestSize/stripeSize;
    }

    public void executeRequest() {
        SubRequest[] subRequestsQueue = new SubRequest[subRequestsAmount];
        for (int i = 0; i < subRequestsAmount; i++) {
            subRequestsQueue[i] = new SubRequest(this.model, this.stripeSize);
            subRequestsQueue[i].schedule(this, dataNode, new TimeSpan(dataNode.incrementNodeClock(1)));
        }
    }

    public int getSegmentID() {
        return this.segmentID;
    }
}
