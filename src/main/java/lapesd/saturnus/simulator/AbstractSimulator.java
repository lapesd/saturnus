package lapesd.saturnus.simulator;

import com.opencsv.CSVWriter;
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
    private final String FILETYPE = "FPP";
    private final String ACCESSPATTERN = "SEQUENTIAL";
    private final int TASKNUMBER = 3;
    private final int SEGMENTSNUMBER = 2;
    private final int NODESAMOUNT = 3;
    private final int STRIPECOUNT = 3;
    private final int BLOCKSIZE = 2048;
    private final int REQUESTSIZE = 1024;
    private final int STRIPESIZE = 512;

    private CircularList<DataNode> allDataNodes;
    private CircularList<DataNode> dataNodes;
    private Queue<Client> clients;
    private CSVWriter traceCSV;

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

    public void setTraceCSV(CSVWriter writer) {
        this.traceCSV = writer;
    }

    public void writeTraceLine(String[] data) {
        this.traceCSV.writeNext(data);
    }

    /**
     * Initialize all the queues, nodes and distributions used through the
     * simulator. All the initial settings are made here.
     */
    @Override
    public void init() {
        this.allDataNodes = new CircularList<>();
        this.clients = new Queue<>(this, "Clients", true, true);

        // Initialize the Clients.
        for (int i = 0; i < TASKNUMBER; i++) {
            clients.insert(new Client(this, i));
        }
        // Initialize the data nodes.
        // Not used in fact. Just to formalization.
        for (int i = 0; i < NODESAMOUNT; i++) {
            allDataNodes.add(new DataNode(this, i));
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
        for (DataNode auxiliar : dataNodes)
            auxiliar.execute();
    }

    /**
     * Generates a circular list with "stripeCount" data nodes, without
     * repetition.
     * @param nodesAmount Total nodes amount
     * @param stripeCount Number of nodes to be selected
     * @return A circular list with 'stripeCount' data nodes
     */
    private CircularList randomDataNodes(int nodesAmount, int stripeCount) {
        // Generate a circular list with 'STRIPECOUNT' data nodes
        CircularList<DataNode> dataNodes = new CircularList<>();
        int[] nodesIndex = MathFunctions.randomInt(nodesAmount, stripeCount);
        for (int index : nodesIndex)
            dataNodes.add(allDataNodes.get(index));
        return dataNodes;
    }

    /**
     * Schedule the nodes according the parameters 'FILETYPE' and
     * 'ACCESPATTERN'. To all 4 combinations, the simulator needs to perform
     * a different action.
     */
    private void scheduleTasks() {
        int blockID = 0;    // Used to calculate the requests offset
        if (FILETYPE == "FPP" && ACCESSPATTERN == "SEQUENTIAL") {
            // Creates "TasksNumber" files and schedule them
            for (Client actualClient : clients) {
                // Select the random data nodes for each file
                this.dataNodes = randomDataNodes(NODESAMOUNT, STRIPECOUNT);
                for (int j = 0; j < SEGMENTSNUMBER; j++) {
                    Block block = new Block(this, actualClient, blockID);
                    actualClient.writeBlock(block, this.dataNodes);
                    blockID++;
                }
            }
        } else if(FILETYPE == "SHARED" && ACCESSPATTERN == "SEQUENTIAL") {
            // One single file, with all the clients working on it
            // Select the random data nodes to the file
            this.dataNodes = randomDataNodes(NODESAMOUNT, STRIPECOUNT);
            for (int i = 0; i < SEGMENTSNUMBER; i++) {
                for (Client actualClient : clients) {
                    Block block = new Block(this, actualClient, blockID);
                    actualClient.writeBlock(block, this.dataNodes);
                    blockID++;
                }
            }
        }
    }

}
