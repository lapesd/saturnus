package lapesd.saturnus.model.event;

import desmoj.core.dist.ContDistNormal;
import desmoj.core.simulator.EventOf3Entities;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;
import lapesd.saturnus.control.AbstractSimulator;
import lapesd.saturnus.model.server.Client;
import lapesd.saturnus.model.server.DataNode;

public class SubRequest extends EventOf3Entities<Request, DataNode, Client> {

    private Request request;
    private Client client;
    private ContDistNormal executionTime;
    private AbstractSimulator model;
    private TimeSpan sendingTime, attendedTime, outputTime;
    private String actionDescription;
    private DataNode dataNodeExecuted;

    public SubRequest(Model model, Request request, long stripeSize) {
        super(model, "Sub-request.", true);
        this.model = (AbstractSimulator)model;
        this.request = request;
        generateTimeDistribution(stripeSize);
        this.client = request.getClient();
    }

    public Request getRequest() {
        return this.request;
    }

    public Client getClient() {
        return this.client;
    }

    public double getExecutionTime() {
        return this.executionTime.sample();
    }

    public TimeSpan getSendingTime() {
        return this.sendingTime;
    }

    public TimeSpan getOutputTime() {
        return this.outputTime;
    }

    public TimeSpan getAttendedTime() {
        return this.attendedTime;
    }

    public void setSendingTime(TimeSpan sendingTime) {
        this.sendingTime = sendingTime;
    }

    public void setOutputTime(TimeSpan outputTime) {
        this.outputTime = outputTime;
    }

    public void setAttendedTime(TimeSpan attendedTime) {
        this.attendedTime = attendedTime;
    }

    public String getActionDescription() {
        return this.actionDescription;
    }

    public DataNode getDataNodeExecuted() {
        return this.dataNodeExecuted;
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
        model.getReportWriter().writeNext((request.getOffset() + "," +
                dataNode.getID() + "," +
                client.getID()   + "," +
                getSendingTime().getTimeAsDouble() + "," +
                getAttendedTime().getTimeAsDouble()+ "," +
                getOutputTime().getTimeAsDouble()
                ).split(",")
        );

        this.actionDescription = "Default - Sub-request executed.";
        this.dataNodeExecuted = dataNode;
    }

    /**
     * Creates the distribution to calculate the execution time for each
     * sub-request. Using a normal distribution, with standard deviation equals
     * to 0.01%. The mean is calculated based on the Stripe Size.
     */
    private void generateTimeDistribution(long mean) {
        this.executionTime = new ContDistNormal(model, "Execution time", mean, 0.001, false, false);
        this.executionTime.setSeed(System.currentTimeMillis());
    }
}
