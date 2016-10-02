package lapesd.saturnus.simulator;

import desmoj.core.dist.ContDistExponential;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.Queue;
import lapesd.saturnus.data.DataNode;
import lapesd.saturnus.event.Request;

/**
 * The "base" of the simulator. Has all methods to handle with the events,
 * queue of tasks, data nodes, etc.
 */
public class AbstractSimulator extends Model {

    private static final int TASKNUMBER = 6;
    private static final int SEGMENTSNUMBER = 2;
    private static final int BLOCKSIZE = 2048;
    private static final int STRIPESIZE = 64;
    private static final int NODESAMOUNT = 4;
    private static final int REQUESTSIZE = 1024;

    private ContDistExponential exponentialWriteTime;
    private ContDistExponential exponentialReadTime;
    private int requestsPerBlock;
    protected Queue<DataNode> dataNodesQueue;

    public AbstractSimulator() {
        super(null, "AbstractSimulator", true, false);
    }

    /**
     * Initialize all the queues and distributions used through the simulator.
     */
    @Override
    public void init() { 
        this.exponentialWriteTime = new ContDistExponential(this, "Write time", 6.0, true, false);
        this.exponentialWriteTime.setNonNegative(true);
        this.exponentialReadTime = new ContDistExponential(this, "Read time", 2.0, true, false);
        this.exponentialReadTime.setNonNegative(true);
        this.requestsPerBlock = BLOCKSIZE/REQUESTSIZE;
        this.dataNodesQueue = new Queue<DataNode>(this, "Data nodes", true, true);

        // Initialize the data nodes.
        for (int i = 0; i < NODESAMOUNT; i++) {
            dataNodesQueue.insert(new DataNode(this));
        }
    }

    /**
     * Creates the initial schedules to the simulator. Besides that, send the requests
     * to the nodes with the smaller workload.
     */
    @Override
    public void doInitialSchedules() {
        for (int i = 0; i < this.requestsPerBlock; i++) {
            Request request = new Request(this, dataNodesQueue.first(), REQUESTSIZE, STRIPESIZE, 1);
            request.generateSubRequests();
            request.executeRequest();
        }
    }

    /**
     * Provides a short description of the simulator.
     * @return A description of the simulator.
     */
    @Override
    public String description() {
        return "Saturnus is an event based discrete simulator for parallel " +
                "file systems.";
    }


    public double getWriteTime() {
        return this.exponentialWriteTime.sample();
    }

    public double getReadTime() {
        return this.exponentialReadTime.sample();
    }

}
