package lapesd.saturnus.event;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;
import lapesd.saturnus.server.Client;
import lapesd.saturnus.simulator.AbstractSimulator;

public class Request extends Entity {

    private int subRequestsAmount, offset, outputTime;
    private AbstractSimulator model;
    private Client client;
    private SubRequest[] subRequests;

    public Request(Model model, Client client, int offset) {
        super(model, "Request", true);
        this.model = (AbstractSimulator)model;
        this.client = client;
        this.subRequestsAmount = this.model.getREQUESTSIZE()/ this.model.getSTRIPESIZE();
        this.offset = offset;
        this.subRequests = new SubRequest[this.subRequestsAmount];
        this.outputTime = 0;
        sendTraceNote("Write/read from offset " + getOffset()
                + " - Client " + client.getID());
    }

    public Client getClient() {
        return this.client;
    }

    public int getOffset() {
        return this.offset;
    }

    public int getOutputTime() {
        return this.outputTime;
    }

    public SubRequest[] getSubRequests() {
        return this.subRequests;
    }

    public void setOutputTime(int outputTime) {
        this.outputTime = outputTime;
    }

    /**
     * Generate all the sub requests from a request.
     * Obs: generating the sub requests with execution time as 1 time unit
     */
    public void generateSubRequests() {
        for (int i = 0; i < subRequestsAmount; i++) {
            subRequests[i] = new SubRequest(model, this, 1);
        }
    }

    /**
     * Synchronize all the sub requests. Set their 'initial time' as the time
     * passed, this way, the request will only execute at certain time.
     * @param timeToStartSubReq Time that the sub requests should start
     */
    public void sync(int timeToStartSubReq) {
        for (SubRequest subRequest : subRequests) {
            subRequest.setSendingTime(new TimeSpan(timeToStartSubReq));
        }
    }


}
