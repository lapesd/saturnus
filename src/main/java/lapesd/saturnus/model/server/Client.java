package lapesd.saturnus.model.server;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.Queue;
import lapesd.saturnus.model.data.Block;
import lapesd.saturnus.model.dataStructures.CircularList;
import lapesd.saturnus.model.event.Request;
import lapesd.saturnus.model.event.SubRequest;
import lapesd.saturnus.control.AbstractSimulator;

public class Client extends Entity {

    private AbstractSimulator model;
    private int ID;
    private Queue<Request> queueOfRequests;
    private CircularList<DataNode> dataNodesList;

    public Client(Model model, int clientID, CircularList dataServersChoice) {
        super(model, "Client", true);
        this.model = (AbstractSimulator)model;
        this.ID = clientID;
        this.dataNodesList = dataServersChoice;
        this.queueOfRequests = new Queue<>(model, "", false, false);
    }

    public int getID() {
        return this.ID;
    }

    public CircularList getDataNodesList() {
        return this.dataNodesList;
    }

    public Queue getQueueOfRequests() {
        return this.queueOfRequests;
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
        }
    }

    /**
     * Send sub-requests from a request to data nodes. Just a simplification.
     * Assumes that the request to be sent is the first of the client queue.
     * @param dataNodesList Sequence of nodes to send
     */
    public void sendRequestFromQueue(CircularList<DataNode> dataNodesList) {
        sendSubRequests(dataNodesList, queueOfRequests.first());
    }

    /**
     * Receives the signal, indicating that some sub-requests has ended its
     * execution.
     * @param request 'father' of the sub-request executed
     * @param subReqOutput Output time of the executed event
     */
    public void sendFinishedSignal(Request request, double subReqOutput) {
        boolean sendNewRequest = request.ReqExecutedSignal();
        if (sendNewRequest) {
            queueOfRequests.removeFirst();
            if (queueOfRequests.size() == 0) return;
            Request toSend = queueOfRequests.first();
            toSend.sync(subReqOutput);
            sendSubRequests(dataNodesList, toSend);
        }
    }

    /**
     * Creates the requests and sub-requests of one data block and store them
     * in the client internal queue.
     * @param block Block
     */
    public void generateBlockRequests(Block block) {
        block.generateRequests(block.getBlockID() * model.parameter("blockSize"));
        Queue<Request> requests = block.getRequests();
        for (Request actualRequest : requests) {
            actualRequest.generateSubRequests();
            queueOfRequests.insert(actualRequest);
        }
    }
}
