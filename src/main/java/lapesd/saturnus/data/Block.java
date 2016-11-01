package lapesd.saturnus.data;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.Queue;
import lapesd.saturnus.event.Request;
import lapesd.saturnus.server.Client;
import lapesd.saturnus.simulator.AbstractSimulator;

public class Block extends Entity {
    private int segment;
    private Client client;
    private Queue<Request> requests;
    private AbstractSimulator model;

    public Block(Model model, Client client, int segment) {
        super(model, "Block", true);
        this.model = (AbstractSimulator)model;
        this.client = client;
        this.segment = segment;
        this.requests = new Queue<Request>(model, "Requests", false, false);
    }

    public int getSegment() {
        return this.segment;
    }

    public Queue<Request> getRequests() {
        return this.requests;
    }

    public void generateRequests() {
        int requestsPerBlock = model.getBLOCKSIZE()/model.getREQUESTSIZE();
        for (int i = 0; i < requestsPerBlock; i++) {
            requests.insert(new Request(model, client));
        }
    }
}
