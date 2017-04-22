package lapesd.saturnus.model.data;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.Queue;
import lapesd.saturnus.model.event.Request;
import lapesd.saturnus.model.server.Client;
import lapesd.saturnus.control.AbstractSimulator;

public class Block extends Entity {
    private int blockID, requestsPerBlock;
    private Client client;
    private Queue<Request> requests;
    private AbstractSimulator model;


    public Block(Model model, Client client, int blockID) {
        super(model, "Block", true);
        this.model = (AbstractSimulator)model;
        this.client = client;
        this.blockID = blockID;
        this.requests = new Queue<>(model, "Requests", false, false);
        this.requestsPerBlock = this.model.parameter("blockSize")/this.model.parameter("requestSize");
    }

    public int getBlockID() { return this.blockID; }

    public Queue<Request> getRequests() {
        return this.requests;
    }

    public void generateRequests(int firstRequestOffset) {
        for (int i = 0; i < requestsPerBlock; i++) {
            int offset = firstRequestOffset + (i * model.parameter("requestSize"));
            requests.insert(new Request(model, client, offset));
        }
    }
}
