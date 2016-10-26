package lapesd.saturnus.event;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.Queue;
import lapesd.saturnus.data.Block;
import lapesd.saturnus.data.DataNode;
import lapesd.saturnus.simulator.AbstractSimulator;

public class Task extends Entity{
    private AbstractSimulator model;
    private DataNode node;
    private Block block;
    private int requestSize, stripeSize;
    private Queue<Request> requestQueue;

    public Task(Model model, DataNode node, Block block, int requestSize, int stripeSize) {
        super(model, "Task", true);
        this.model = (AbstractSimulator)model;
        this.node = node;
        this.block = block;
        this.requestSize = requestSize;
        this.stripeSize = stripeSize;
        this.requestQueue = new Queue<Request>(model, null, false, false);
        initRequests(block.getBlockSize() / requestSize);
        sendTraceNote("Read/Write at " + this.block + ", "
                + this.block.getSegment());
    }

    /**
     * Add 'n' requests into the request queue, where the 'n' is the
     * number of requests per block. Otherwise, calculate the offset
     * of each request.
     * @param requestsPerBlock
     */
    private void initRequests(int requestsPerBlock) {
        int requestOffset = block.getID() * block.getBlockSize();
        for (int i = 0; i < requestsPerBlock; i++) {
            requestQueue.insert(new Request(model, this, node, requestSize, stripeSize, requestOffset));
            requestOffset += this.requestSize;
        }
    }

    /**
     * 'Walks' through the request queue and schedule it. That means,
     * generate the sub requests and execute them.
     */
    public void execute() {
        int queueSize = this.requestQueue.size();
        for (int i = 0; i < queueSize; i++) {
            removeFirstRequest().scheduleRequest();
        }
    }

    /**
     * Remove the first element of request queue.
     * @return The removed element
     */
    private Request removeFirstRequest() {
        return this.requestQueue.removeFirst();
    }

}
