package lapesd.saturnus.data;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.Queue;
import lapesd.saturnus.event.Request;
import lapesd.saturnus.server.Client;
import lapesd.saturnus.simulator.AbstractSimulator;

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
        this.requestsPerBlock = this.model.getBLOCKSIZE()/this.model.getREQUESTSIZE();
    }

    public int getBlockID() { return this.blockID; }

    public Queue<Request> getRequests() {
        return this.requests;
    }

    public void generateRequests(int firstRequestOffset) {
        for (int i = 0; i < requestsPerBlock; i++) {
            int offset = firstRequestOffset + (i * model.getREQUESTSIZE());
            requests.insert(new Request(model, client, offset));
        }
    }
}
