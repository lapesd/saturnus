package lapesd.saturnus.server;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.Queue;
import lapesd.saturnus.data.Block;
import lapesd.saturnus.dataStructures.CircularList;
import lapesd.saturnus.event.Request;
import lapesd.saturnus.event.SubRequest;
import lapesd.saturnus.simulator.AbstractSimulator;

public class Client extends Entity {

    private AbstractSimulator model;
    private int ID;

    public Client(Model model, int clientID) {
        super(model, "Client", true);
        this.model = (AbstractSimulator)model;
        this.ID = clientID;
    }

    public int getID() {
        return this.ID;
    }

    // Write just one block
    public void writeBlock(Block block, CircularList dataNodes) {
        block.generateRequests();
        Queue<Request> requests = block.getRequests();
        for (Request actualRequest : requests) {
            actualRequest.generateSubRequests();
            sendSubRequests(dataNodes, actualRequest);
        }
    }

    // Send sub requests of one request to the data nodes(round-robin)
    public void sendSubRequests(CircularList<DataNode> dataNodesList, Request request) {
        for (SubRequest subRequest : request.getSubRequests()) {
            dataNodesList.next().insertSubRequest(subRequest);
        }
    }
}
