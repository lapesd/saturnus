package lapesd.saturnus.event;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.Queue;
import lapesd.saturnus.data.Block;
import lapesd.saturnus.data.DataNode;
import lapesd.saturnus.data.Segment;
import lapesd.saturnus.simulator.AbstractSimulator;

public class Task extends Entity{
    private AbstractSimulator model;
    private DataNode node;
    private Block block;
    private int requestsPerBlock, requestSize, stripeSize;
    private Queue<Request> requestQueue;

    public Task(Model model, DataNode node, Segment segment, int requestsPerBlock,
                int requestSize, int stripeSize) {
        super(model, "Task", true);
        this.model = (AbstractSimulator)model;
        this.node = node;
        this.block = new Block(model, segment);
        this.requestsPerBlock = requestsPerBlock;
        this.requestSize = requestSize;
        this.stripeSize = stripeSize;
        this.requestQueue = new Queue<Request>(model, null, false, false);
        initRequests();
    }

    private void initRequests() {
        for (int i = 0; i < this.requestsPerBlock; i++) {
            requestQueue.insert(new Request(model, this, node, requestSize, stripeSize));
        }
    }

    public void execute() {
        int queueSize = this.requestQueue.size();
        for (int i = 0; i < queueSize; i++) {
            removeFirstRequest().executeRequest();
        }
    }

    private Request removeFirstRequest() {
        return this.requestQueue.removeFirst();
    }

}
