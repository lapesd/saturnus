package lapesd.saturnus.event;

import desmoj.core.simulator.EventOf2Entities;
import desmoj.core.simulator.Model;
import lapesd.saturnus.data.DataNode;
import lapesd.saturnus.simulator.AbstractSimulator;

public class SubRequest extends EventOf2Entities<Request, DataNode> {

    private AbstractSimulator model;
    private Request request;
    private int stripeSize;

    public SubRequest(Model model, Request request, int stripeSize) {
        super(model, "Sub request.", true);
        this.model = (AbstractSimulator)model;
        this.stripeSize = stripeSize;
        this.request = request;
    }

    @Override
    public void eventRoutine(Request request, DataNode dataNode) {
        // Execute the write()/read()
        sendTraceNote("Sub request executed. From " + this.request
                        + ", " + this.request.getTask());
    }

    public Request getRequest() {
        return this.request;
    }
}
