package lapesd.saturnus.event;

import desmoj.core.simulator.EventOf3Entities;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;
import lapesd.saturnus.server.Client;
import lapesd.saturnus.server.DataNode;
import lapesd.saturnus.simulator.CSVformat;

public class SubRequest extends EventOf3Entities<Request, DataNode, Client> {

    private Request request;
    private Client client;
    private int executionTime;
    private TimeSpan initialScheduleTime, outputTime;

    public SubRequest(Model model, Request request, int executionTime) {
        super(model, "Sub request.", true);
        this.request = request;
        this.executionTime = executionTime;
        this.client = request.getClient();
    }

    public Request getRequest() {
        return this.request;
    }

    public Client getClient() {
        return this.client;
    }

    public int getExecutionTime() {
        return this.executionTime;
    }

    public TimeSpan getInitialScheduleTime() {
        return this.initialScheduleTime;
    }

    public void setInitialScheduleTime(TimeSpan time) {
        this.initialScheduleTime = time;
    }

    /**
     * Routine called when the sub request is scheduled. Send a message into
     * the trace report with the sub request info.
     * @param request
     * @param dataNode
     */
    @Override
    public void eventRoutine(Request request, DataNode dataNode, Client client) {
        // Execute the write()/read()
        sendTraceNote("Sub request executed - " + request
                + ", Client " + client.getID()
                + " , " + dataNode);

        CSVformat.writeLine(request.getOffset() + ", " + dataNode.getID()
                            + ", " + client.getID()
                            + ", " + getInitialScheduleTime());
    }
}
