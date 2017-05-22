package lapesd.saturnus.model.data;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.Queue;
import lapesd.saturnus.model.event.Request;
import lapesd.saturnus.model.server.Client;
import lapesd.saturnus.control.AbstractSimulator;
import lapesd.saturnus.model.utils.MathFunctions;

public class Block extends Entity {
    private int blockID;
    private double requestsPerBlock;
    private Client client;
    private Queue<Request> requests;
    private AbstractSimulator model;


    public Block(Model model, Client client, int blockID) {
        super(model, "Block", true);
        this.model = (AbstractSimulator)model;
        this.client = client;
        this.blockID = blockID;
        this.requests = new Queue<>(model, "Requests", false, false);
        this.requestsPerBlock = ((double)this.model.parameter("blockSize") /
                                 (double)this.model.parameter("requestSize"));
    }

    public int getBlockID() { return this.blockID; }

    public Queue<Request> getRequests() {
        return this.requests;
    }

    public void generateRequests(long firstRequestOffset) {
        long requestSize = model.parameter("requestSize");
        long offset = 0;
        for (int i = 0 ; i < (int)requestsPerBlock; i++) {
            offset = firstRequestOffset + (i * requestSize);
            requests.insert(new Request(model, client, offset, requestSize));
        }

        double decimal = MathFunctions.getDecimalPart(requestsPerBlock);
        if (decimal != 0) {
            long partialRequestSize = (long)Math.ceil(decimal * requestSize);
            long partialOffset = offset + requestSize;
            requests.insert(new Request(model, client, partialOffset, partialRequestSize));
        }

    }
}
