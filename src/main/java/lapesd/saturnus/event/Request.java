package lapesd.saturnus.event;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;
import lapesd.saturnus.server.Client;
import lapesd.saturnus.simulator.AbstractSimulator;

public class Request extends Entity {

    private int subRequestsAmount, offset;
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
        sendTraceNote("Write/read from offset " + offset
                + " - Client " + client.getID());
    }

    public Client getClient() {
        return this.client;
    }

    public int getOffset() {
        return this.offset;
    }

    public SubRequest[] getSubRequests() {
        return this.subRequests;
    }

    public void generateSubRequests() {
        for (int i = 0; i < subRequestsAmount; i++) {
            subRequests[i] = new SubRequest(model, this);
        }
    }

}
