package main;

import desmoj.core.dist.ContDistExponential;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.Queue;

/**
 * The "base" of the simulator. Has all methods to handle with the events,
 * queue of tasks, data nodes, etc.
 */
public class AbstractSimulator extends Model {

    private static final int NODESAMOUNT = 4;
    private static final int TASKNUMBER = 6;
    private static final int REQUESTSIZE = 3;

    private ContDistExponential exponentialWriteTime;
    private ContDistExponential exponentialReadTime;
    protected Queue<DataNode> dataNodesQueue;
    protected Queue<Task> tasksQueue;


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

    /**
     * Creates the initial schedules to the simulator. Besides that, send the requests
     * to the nodes with the smaller workload.
     */
    @Override
    public void doInitialSchedules() {
        Request request = new Request(REQUESTSIZE, this);
        for (int i = 0; i < TASKNUMBER; i++) {
            DataNode bestNode = workloadBalance();
            request.schedule(tasksQueue.removeFirst(), bestNode);
            bestNode.setTasksNumber(bestNode.getTasksNumber() + 1);
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

    /**
     * Find the best node to process a task/request into the data nodes queue.
     * @return The best data note to execute a task.
     */
    private DataNode workloadBalance() {
        DataNode aux = dataNodesQueue.get(0);
        for (int i = 1; i < NODESAMOUNT; i++) {
            if (dataNodesQueue.get(i).getTasksNumber() < aux.getTasksNumber()) {
                aux = dataNodesQueue.get(i);
            }
        }
        return aux;
    }

}
