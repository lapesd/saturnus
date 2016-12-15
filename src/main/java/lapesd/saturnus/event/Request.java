package lapesd.saturnus.event;

import desmoj.core.dist.ContDistNormal;
import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;
import lapesd.saturnus.server.Client;
import lapesd.saturnus.simulator.AbstractSimulator;

public class Request extends Entity {

    private int subRequestsAmount, offset;
    private double outputTime;
    private AbstractSimulator model;
    private Client client;
    private SubRequest[] subRequests;
    private ContDistNormal subReqExecTime;

    public Request(Model model, Client client, int offset) {
        super(model, "Request", true);
        this.model = (AbstractSimulator)model;
        this.client = client;
        this.subRequestsAmount = this.model.getREQUESTSIZE()/ this.model.getSTRIPESIZE();
        this.offset = offset;
        this.subRequests = new SubRequest[this.subRequestsAmount];
        this.outputTime = 0;
        // Note: The smallest stripe size actually considered is 64.
        // If a greater size is used, the execution time is higher.
        this.subReqExecTime = new ContDistNormal(model, "Execution time", (int)(this.model.getSTRIPESIZE()/32),
                0.001, false, false);
        this.subReqExecTime.setSeed(System.currentTimeMillis());
        sendTraceNote("Write/read from offset " + getOffset()
                + " - Client " + client.getID());
    }

    public Client getClient() {
        return this.client;
    }

    public int getOffset() {
        return this.offset;
    }

    public double getOutputTime() {
        return this.outputTime;
    }

    public SubRequest[] getSubRequests() {
        return this.subRequests;
    }

    public void setOutputTime(double outputTime) {
        this.outputTime = outputTime;
    }

    /**
     * Generate all the sub requests from a request.
     * Obs: generating the sub requests with execution time as 1 time unit
     */
    public void generateSubRequests() {
        for (int i = 0; i < subRequestsAmount; i++)
            subRequests[i] = new SubRequest(model, this, subReqExecTime.sample());
    }

    /**
     * Synchronize all the sub requests. Set their 'initial time' as the time
     * passed, this way, the request will only execute at certain time.
     * @param timeToStartSubReq Time that the sub requests should start
     */
    public void sync(double timeToStartSubReq) {
        for (SubRequest subRequest : subRequests) {
            subRequest.setSendingTime(new TimeSpan(timeToStartSubReq));
        }
    }


}
