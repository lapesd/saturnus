package lapesd.saturnus.event;

import desmoj.core.simulator.EventOf2Entities;
import desmoj.core.simulator.Model;
import lapesd.saturnus.server.DataNode;

public class SubRequest extends EventOf2Entities<Request, DataNode> {

    private Request request;

    public SubRequest(Model model, Request request) {
        super(model, "Sub request.", true);
        this.request = request;
    }

    public Request getRequest() {
        return this.request;
    }

    /**
     * Routine called when the sub request is scheduled. Send a message into
     * the trace report with the sub request info.
     * @param request
     * @param dataNode
     */
    @Override
    public void eventRoutine(Request request, DataNode dataNode) {
        // Execute the write()/read()
        sendTraceNote("Sub request executed - " + getRequest()
                + ", Client " + request.getClient().getID()
                + " , " + dataNode);
    }
}
