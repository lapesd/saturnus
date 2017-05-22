package lapesd.saturnus.model.event;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;
import lapesd.saturnus.control.AbstractSimulator;
import lapesd.saturnus.model.server.Client;
import lapesd.saturnus.model.utils.MathFunctions;

public class Request extends Entity {
    private int requestsExecuted;
    private long offset;
    private double outputTime;
    private double subRequestsAmount;
    private AbstractSimulator model;
    private Client client;
    private SubRequest[] subRequests;

    public Request(Model model, Client client, long offset, long requestSize) {
        super(model, "Request", true);
        this.model = (AbstractSimulator)model;
        this.client = client;
        this.subRequestsAmount = ((double)requestSize /
                                       this.model.parameter("stripeSize"));
        this.offset = offset;
        this.subRequests = new SubRequest[(int)Math.ceil(this.subRequestsAmount)];
        this.outputTime = 0;
    }

    public Client getClient() {
        return this.client;
    }

    public long getOffset() {
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
        long stripeSize = this.model.parameter("stripeSize");
        for (int i = 0; i < subRequestsAmount; i++) {
            subRequests[i] = new SubRequest(model, this, stripeSize);
        }

        double decimal = MathFunctions.getDecimalPart(subRequestsAmount);
        if (decimal != 0) {
            long partialStripeSize = (long)Math.ceil(decimal * stripeSize);
            subRequests[(int)subRequestsAmount] = new SubRequest(model, this, partialStripeSize);
        }

        this.requestsExecuted = 0;
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

    /**
     * Receives the signal of 'end of execution', from one sub-request.
     * @return true if all the sub-requests have been executed
     */
    public boolean ReqExecutedSignal() {
        requestsExecuted += 1;
        return (requestsExecuted == Math.ceil(subRequestsAmount));
    }
}
