package lapesd.saturnus.control;

import desmoj.core.simulator.Model;
import desmoj.core.simulator.Queue;
import lapesd.saturnus.model.data.Block;
import lapesd.saturnus.model.dataStructures.CircularList;
import lapesd.saturnus.model.event.SubRequest;
import lapesd.saturnus.model.utils.MathFunctions;
import lapesd.saturnus.model.utils.CSVWriter;
import lapesd.saturnus.model.server.Client;
import lapesd.saturnus.model.server.DataNode;
import java.util.*;

/**
 * The "base" of the simulator. Has all methods to handle with the events,
 * queue of tasks, data nodes, etc.
 */
public class AbstractSimulator extends Model {

    private Map<String, Integer> parameters;
    private String fileType, accessPattern;
    private CircularList<DataNode> allDataNodes;
    private Queue<Client> clients;
    private ArrayList<SubRequest> executedSubRequests;
    private CSVWriter reportWriter;

    public AbstractSimulator(Map parameters, String fileType, String accessPattern) {
        super(null, "Abstract simulator", true, false);
        this.parameters = parameters;
        this.fileType = fileType;
        this.accessPattern = accessPattern;
        this.executedSubRequests = new ArrayList<>();
    }


    /**
     * Initialize all the queues, nodes and distributions used through the
     * simulator. All the initial settings are made here.
     */
    @Override
    public void init() {
        this.allDataNodes = new CircularList<>();
        this.clients = new Queue<>(this, "Clients", true, true);

        // Initialize all the data nodes
        for (int i = 0; i < parameter("numberDataNodes"); i++) {
            allDataNodes.add(new DataNode(this, i));
        }

        // Initialize the Clients
        CircularList<DataNode> dataNodesChoice;
        for (int i = 0; i < parameter("numberTasks"); i++) {
            dataNodesChoice = randomDataNodes(parameter("numberDataNodes"),
                    parameter("stripeCount"));
            clients.insert(new Client(this, i, dataNodesChoice));
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


    public int parameter(String desiredParam) {
        return parameters.get(desiredParam);
    }

    public void setReportWriter(CSVWriter writer) {
        this.reportWriter = writer;
    }

    public CSVWriter getReportWriter() {
        return this.reportWriter;
    }

    public ArrayList<SubRequest> getExecutedSubRequests() {
        return this.executedSubRequests;
    }

    /**
     * Save a sub-request executed on the queue. Can be used to return information
     * about them.
     * @param subReq To be added
     */
    public void setSubRequestExecuted(SubRequest subReq) {
        this.executedSubRequests.add(subReq);
    }

    /**
     * 'Walks' through the data nodes queue and schedule all actions
     * scheduled at each node(sub request, request, etc.)
     */
    private void executeAllNodes() {
        // Initial requests being sent
        for (Client auxiliar : clients)
            auxiliar.sendRequestFromQueue(auxiliar.getDataNodesList());

        // Execute all the requests
        while (!allRequestsExecuted()) {
            for (DataNode auxiliar : allDataNodes) {
                auxiliar.executeOneSubRequest();
            }
        }
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
     * Checks if there's some requests executing or to be executed on the system
     * @return If all the requests have been executed
     */
    private boolean allRequestsExecuted() {
        for (Client auxiliar : clients) {
            if (auxiliar.getQueueOfRequests().size() > 0)
                return false;
        }
        return true;
    }

    /**
     * Schedule the nodes according the parameters 'fileType' and
     * 'accessPattern'. To all 4 combinations, the simulator performs
     * a different action.
     */
    private void scheduleTasks() {
        if (accessPattern.equals("Sequential"))
            sequentialTasks();
        else if(accessPattern.equals("Random"))
            randomTasks();
    }

    private void randomTasks() {
        if (fileType.equals("File per Process")) {
            int blockID = 0;
            // One file for each client - Random access
            for (Client actualClient : this.clients) {
                Vector<Block> blocks = new Vector();

                for (int i = 0; i < parameter("numberSegments"); i++) {
                    Block createdBlock = new Block(this, actualClient, blockID);
                    blocks.add(createdBlock);
                    blockID++;
                }

                Collections.shuffle(blocks);

                for (Block actualBlock : blocks)
                    actualClient.generateBlockRequests(actualBlock);
            }
        } else if (fileType.equals("Shared")) {
            // One single file, with all the clients working on it
            int blockID = 0;
            int clientsSize = this.clients.size();
            int numberSegments = parameter("numberSegments");

            // Generates all the blocks
            Block[][] blocks = new Block[clientsSize][numberSegments];
            for (int i = 0; i < clientsSize; i++) {
                for (int j = 0; j < numberSegments; j++) {
                    Block block = new Block(this, this.clients.get(i), blockID);
                    blocks[i][j] = block;
                    blockID++;
                }

                Collections.shuffle(Arrays.asList(blocks[i]));

                for (int j = 0; j < numberSegments; j++)
                    this.clients.get(i).generateBlockRequests(blocks[i][j]);
            }

        }
    }

    private void sequentialTasks() {
        if (fileType.equals("File per Process")) {
            // One file for each client - Sequential access
            int blockID = 0;
            for (Client actualClient : this.clients) {
                for (int j = 0; j < parameter("numberSegments"); j++) {
                    Block block = new Block(this, actualClient, blockID);
                    actualClient.generateBlockRequests(block);
                    blockID++;
                }
            }
        } else if (fileType.equals("Shared")) {
            // One single file, with all the clients working on it
            int blockID = 0;
            for (int i = 0; i < parameter("numberSegments"); i++) {
                for (Client actualClient : this.clients) {
                    Block block = new Block(this, actualClient, blockID);
                    actualClient.generateBlockRequests(block);
                    blockID++;
                }
            }
        }
    }

}
