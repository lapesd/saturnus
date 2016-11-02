package lapesd.saturnus.data;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.Queue;
import lapesd.saturnus.event.Request;
import lapesd.saturnus.server.Client;
import lapesd.saturnus.simulator.AbstractSimulator;

public class Block extends Entity {
    private int segment, blockID;
    private Client client;
    private Queue<Request> requests;
    private AbstractSimulator model;

    public Block(Model model, Client client, int segment, int blockID) {
        super(model, "Block", true);
        this.model = (AbstractSimulator)model;
        this.client = client;
        this.segment = segment;
        this.blockID = blockID;
        this.requests = new Queue<Request>(model, "Requests", false, false);
    }

    public int getBlockID() { return this.blockID; }

    public Queue<Request> getRequests() {
        return this.requests;
    }

    public void generateRequests(int firstRequestOffset) {
        int requestsPerBlock = model.getBLOCKSIZE()/model.getREQUESTSIZE();
        for (int i = 0; i < requestsPerBlock; i++) {
            int offset = firstRequestOffset + (i * model.getREQUESTSIZE());
            requests.insert(new Request(model, client, offset));
        }
    }
}
