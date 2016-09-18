package main;

import desmoj.core.dist.ContDistExponential;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.Queue;
import desmoj.core.simulator.TimeInstant;


/**
 * The "base" of the simulator. Has all methods to handle with the events,
 * queue of events, data nodes, etc.
 */
public class AbstractSimulator extends Model {

    private static final int NODESAMOUNT = 4;
    private static final int TASKNUMBER = 2;
    private static final int REQUESTSIZE = 10;

    private ContDistExponential exponentialWriteTime;
    private ContDistExponential exponentialReadTime;
    protected Queue<DataNode> dataNodesQueue;
    protected Queue<Task> tasksQueue;


    public AbstractSimulator() {
        super(null, "AbstractSimulator", true, false);
    }


    @Override
    public void init() {
        this.exponentialWriteTime = new ContDistExponential(this, "Write time", 6.0, true, false);
        this.exponentialWriteTime.setNonNegative(true);
        this.exponentialReadTime = new ContDistExponential(this, "Write time", 2.0, true, false);
        this.exponentialReadTime.setNonNegative(true);
        this.dataNodesQueue = new Queue<DataNode>(this, "Data nodes", true, true);
        this.tasksQueue = new Queue<Task>(this, "Tasks queue", true, true);

        // Initialize the data nodes.
        for (int i = 0; i < NODESAMOUNT; i++) {
            dataNodesQueue.insert(new DataNode(this));
        }

        // Initialize the tasks.
        for (int i = 0; i < TASKNUMBER; i++) {
            tasksQueue.insert(new Task(this));
        }
    }

    @Override
    public void doInitialSchedules() {
        Request request = new Request(REQUESTSIZE, this);
        request.schedule(tasksQueue.removeFirst(), dataNodesQueue.get(0), new TimeInstant(0));
    }

    @Override
    public String description() {
        return "Saturnus is an event based discrete simulator for parallel " +
                "file systems.";
    }

    public int getNodesAmount() {
        return this.NODESAMOUNT;
    }

    public double getWriteTime() {
        return this.exponentialWriteTime.sample();
    }

    public double getReadTime() {
        return this.exponentialReadTime.sample();
    }


}
