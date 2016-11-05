package lapesd.saturnus.simulator;

import desmoj.core.simulator.Model;
import desmoj.core.simulator.Queue;
import lapesd.saturnus.data.Block;
import lapesd.saturnus.dataStructures.CircularList;
import lapesd.saturnus.math.MathFunctions;
import lapesd.saturnus.server.Client;
import lapesd.saturnus.server.DataNode;

/**
 * The "base" of the simulator. Has all methods to handle with the events,
 * queue of tasks, data nodes, etc.
 */
public class AbstractSimulator extends Model {

    // Parameters
    private final String FILETYPE = "SHARED";
    private final String ACCESSPATTERN = "SEQUENTIAL";
    private final int TASKNUMBER = 2;
    private final int SEGMENTSNUMBER = 2;
    private final int STRIPECOUNT = 2;
    private final int STRIPESIZE = 1000;
    private final int NODESAMOUNT = 3;
    private final int BLOCKSIZE = 6000;
    private final int REQUESTSIZE = 3000;

    private CircularList<DataNode> allDataNodes;
    private CircularList<DataNode> dataNodes;
    private Queue<Client> clients;

    public AbstractSimulator() {
        super(null, "Abstract simulator", true, false);
    }

    public int getSTRIPESIZE() {
        return STRIPESIZE;
    }

    public int getBLOCKSIZE() {
        return BLOCKSIZE;
    }

    public int getREQUESTSIZE() {
        return REQUESTSIZE;
    }

    /**
     * Initialize all the queues, nodes and distributions used through the
     * simulator. All the initial settings are made here.
     */
    @Override
    public void init() {
        this.allDataNodes = new CircularList<DataNode>();
        this.clients = new Queue<Client>(this, "Clients", true, true);

        // Initialize the Clients.
        for (int i = 0; i < TASKNUMBER; i++) {
            clients.insert(new Client(this, i));
        }
        // Initialize the data nodes.
        for (int i = 0; i < NODESAMOUNT; i++) {
            allDataNodes.add(new DataNode(this, i));
        }

        randomDataNodes(NODESAMOUNT, STRIPECOUNT);
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
            this.dataNodes.get(i).execute();
        }
    }

    private void randomDataNodes(int nodesAmount, int stripeCount) {
        // Generate a circular list with 'STRIPECOUNT' data nodes
        this.dataNodes = new CircularList<DataNode>();
        int[] nodesIndex = MathFunctions.randomInt(nodesAmount, stripeCount);
        for (int index : nodesIndex) {
            this.dataNodes.add(allDataNodes.get(index));
        }
    }

    /**
     * Schedule the nodes according the parameters 'FILETYPE' and
     * 'ACCESPATTERN'. To all 4 combinations, the simulator needs to perform
     * a different action.
     */
    private void scheduleTasks() {
        if (FILETYPE == "FPP" && ACCESSPATTERN == "SEQUENTIAL") {
            int blockID = 0;
            Client actualClient = clients.get(MathFunctions.randomInt(TASKNUMBER));
            // Mantém sincronia entre os segmentos.
            for (int i = 0; i < SEGMENTSNUMBER; i++) {
                for (int j = 0; j < TASKNUMBER; j++) {
                    Block block = new Block(this, actualClient, i, blockID);
                    actualClient.writeBlock(block, this.dataNodes);
                    blockID++;
                }
            }
        } else if(FILETYPE == "SHARED" && ACCESSPATTERN == "SEQUENTIAL") {
            int blockID = 0;
            // Mantém sincronia entre os segmentos.
            for (int i = 0; i < SEGMENTSNUMBER; i++) {
                for (Client actualClient : clients) {
                    Block block = new Block(this, actualClient, i, blockID);
                    actualClient.writeBlock(block, this.dataNodes);
                    blockID++;
                }
            }
        }

    }
}
