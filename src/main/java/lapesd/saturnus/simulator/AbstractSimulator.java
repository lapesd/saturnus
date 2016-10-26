package lapesd.saturnus.simulator;

import desmoj.core.simulator.Model;
import desmoj.core.simulator.Queue;
import lapesd.saturnus.data.Block;
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
    private static final int TASKNUMBER = 3;
    private static final int SEGMENTSNUMBER = 2;
    private static final int STRIPECOUNT = 2;
    private static final int STRIPESIZE = 512;
    private static final int NODESAMOUNT = 10;
    private static final int BLOCKSIZE = 2048;
    private static final int REQUESTSIZE = 512;

    protected Queue<DataNode> dataNodesQueue;
    protected Queue<Segment> segments;

    public AbstractSimulator() {
        super(null, "AbstractSimulator", true, false);
    }

    /**
     * Initialize all the queues, nodes and distributions used through the
     * simulator. All the initial settings are made here.
     */
    @Override
    public void init() {
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
     * Creates the initial schedules to the simulator and send the signal
     * to schedule them.
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

    /**
     * 'Walks' through the data nodes queue and schedule all actions
     * scheduled at each node(sub request, request, etc.)
     */
    private void executeAllNodes() {
        for (int i = 0; i < NODESAMOUNT; i++) {
            this.dataNodesQueue.get(i).execute();
        }
    }

    /**
     * Schedule the nodes according the parameters 'FILETYPE' and
     * 'ACCESPATTERN'. To all 4 combinations, the simulator needs to perform
     * a different action.
     */
    private void scheduleTasks() {
        int blockID = 0;
        if (FILETYPE == "FPP" && ACCESSPATTERN == "SEQUENTIAL") {
            int randomNode = MathFunctions.randomInt(NODESAMOUNT);
            for (int i = 0; i < segments.size(); i++) {
                Segment actualSeg = segments.get(i);
                for (int j = 0; j < TASKNUMBER; j++) {
                    Block block = new Block(this, actualSeg, BLOCKSIZE, blockID++);
                    Task newTask = new Task(this, dataNodesQueue.get(randomNode),
                            block, REQUESTSIZE, STRIPESIZE);
                    newTask.schedule();
                }
            }
        } else if(FILETYPE == "SHARED" && ACCESSPATTERN == "SEQUENTIAL") {
            CircularList<DataNode> sharedNodes = new CircularList<DataNode>();
            int[] nodesIndex = MathFunctions.randomInt(NODESAMOUNT, STRIPECOUNT);
            for (int i = 0; i < STRIPECOUNT; i++) {
                sharedNodes.add(dataNodesQueue.get(nodesIndex[i]));
            }
            for (int i = 0; i < segments.size(); i++) {
                Segment actualSeg = segments.get(i);
                sharedNodes.resetNextPointer();
                for (int j = 0; j < TASKNUMBER; j++) {
                    DataNode actualNode = sharedNodes.next();
                    Block block = new Block(this, actualSeg, BLOCKSIZE, blockID++);
                    Task newTask = new Task(this, actualNode,
                            block, REQUESTSIZE, STRIPESIZE);
                    newTask.schedule();
                }
            }

        }
    }
}
