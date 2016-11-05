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

    /**
     * Receives a block to 'write' into the data servers. Beyond that,
     * synchronize the schedule time between requests of the same block.
     * @param block Block to be scheduled into data servers
     * @param dataNodes The data nodes or data servers
     */
    public void writeBlock(Block block, CircularList dataNodes) {
        block.generateRequests(block.getBlockID() * model.getBLOCKSIZE());
        Queue<Request> requests = block.getRequests();
        int nextRequestStart = 0;
        for (Request actualRequest : requests) {
            actualRequest.generateSubRequests();
            actualRequest.sync(nextRequestStart);
            sendSubRequests(dataNodes, actualRequest);
            nextRequestStart = actualRequest.getOutputTime();
        }
    }

    /**
     * Receives a request, where gets the sub requests and insert them into
     * the given data nodes(round-robin).
     * @param dataNodesList Data nodes that will process the request
     * @param request Request to be processed
     */
    public void sendSubRequests(CircularList<DataNode> dataNodesList, Request request) {
        for (SubRequest subRequest : request.getSubRequests()) {
            DataNode nodeToSchedule = dataNodesList.next();
            nodeToSchedule.insertSubRequest(subRequest);
            if (nodeToSchedule.getNodeClock() > request.getOutputTime()) {
                request.setOutputTime((int)nodeToSchedule.getNodeClock());
            }
        }
    }
}
