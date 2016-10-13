package lapesd.saturnus.simulator;

import desmoj.core.simulator.Model;
import desmoj.core.simulator.Queue;
import lapesd.saturnus.data.DataNode;
import lapesd.saturnus.data.Segment;
import lapesd.saturnus.dataStructures.CircularList;
import lapesd.saturnus.event.Task;
import lapesd.saturnus.math.MathFunctions;

/**
 * The "base" of the simulator. Has all methods to handle with the events,
 * queue of tasks, data nodes, etc.
 */
public class AbstractSimulator extends Model {

    private static final String FILETYPE = "SHARED";
    private static final String ACCESSPATTERN = "SEQUENTIAL";
    private static final int TASKNUMBER = 4;
    private static final int SEGMENTSNUMBER = 2;
    private static final int STRIPECOUNT = 3;
    private static final int STRIPESIZE = 64;
    private static final int NODESAMOUNT = 4;
    private static final int BLOCKSIZE = 2048;
    private static final int REQUESTSIZE = 1024;

    private int requestsPerBlock;
    protected Queue<DataNode> dataNodesQueue;
    protected Queue<Segment> segments;

    public AbstractSimulator() {
        super(null, "AbstractSimulator", true, false);
    }

    /**
     * Initialize all the queues and distributions used through the simulator.
     */
    @Override
    public void init() {
        this.requestsPerBlock = MathFunctions.ceilInt((double)BLOCKSIZE/(double)REQUESTSIZE);
        this.dataNodesQueue = new Queue<DataNode>(this, "Data nodes", true, true);
        this.segments = new Queue<Segment>(this, "Segments", true, true);

        // Initialize the segments.
        for (int i = 0; i < SEGMENTSNUMBER; i++) {
            segments.insert(new Segment(this));
        }
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
        scheduleTasks();
        executeAllNodes();
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


    private void executeAllNodes() {
        for (int i = 0; i < NODESAMOUNT; i++) {
            this.dataNodesQueue.get(i).execute();
        }
    }

    private void scheduleTasks() {
        if (FILETYPE == "FPP" && ACCESSPATTERN == "SEQUENTIAL") {
            int randomNode = MathFunctions.randomInt(NODESAMOUNT);
            for (int i = 0; i < segments.size(); i++) {
                for (int j = 0; j < TASKNUMBER; j++) {
                    Task newTask = new Task(this, dataNodesQueue.get(randomNode),
                            segments.get(i), requestsPerBlock, REQUESTSIZE, STRIPESIZE);
                    dataNodesQueue.get(randomNode).insertTaskToQueue(newTask);
                }
            }
        } else if(FILETYPE == "SHARED" && ACCESSPATTERN == "SEQUENTIAL") {
            CircularList<DataNode> sharedNodes = new CircularList<DataNode>();
            int[] nodesIndex = MathFunctions.randomInt(NODESAMOUNT, STRIPECOUNT);
            for (int i = 0; i < STRIPECOUNT; i++) {
                sharedNodes.add(dataNodesQueue.get(nodesIndex[i]));
            }
            for (int i = 0; i < segments.size(); i++) {
                for (int j = 0; j < TASKNUMBER; j++) {
                    DataNode actualNode = sharedNodes.next();
                    Task newTask = new Task(this, actualNode,
                            segments.get(i), requestsPerBlock, REQUESTSIZE, STRIPESIZE);
                    actualNode.insertTaskToQueue(newTask);
                }
            }

        }
    }
}
