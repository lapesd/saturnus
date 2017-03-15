package lapesd.saturnus.simulator;

import com.opencsv.CSVWriter;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.Queue;
import lapesd.saturnus.data.Block;
import lapesd.saturnus.dataStructures.CircularList;
import lapesd.saturnus.event.SubRequest;
import lapesd.saturnus.math.MathFunctions;
import lapesd.saturnus.server.Client;
import lapesd.saturnus.server.DataNode;

import java.util.ArrayList;
import java.util.Map;

/**
 * The "base" of the simulator. Has all methods to handle with the events,
 * queue of tasks, data nodes, etc.
 */
public class AbstractSimulator extends Model {

    private Map<String, Integer> parameters;
    private String fileType, accessPattern;
    private CircularList<DataNode> allDataNodes;
    private CircularList<DataNode> dataNodes;       // Can be declared inside the scheduleTasks()
    private Queue<Client> clients;
    private ArrayList<SubRequest> allSubRequests;
    private CSVWriter traceCSV;

    public AbstractSimulator(Map parameters, String fileType, String accessPattern) {
        super(null, "Abstract simulator", true, false);
        this.parameters = parameters;
        this.fileType = fileType;
        this.accessPattern = accessPattern;
        this.allSubRequests = new ArrayList<>();
    }

    public int parameter(String desiredParam) {
        return parameters.get(desiredParam);
    }

    public void traceCSV(CSVWriter writer) {
        this.traceCSV = writer;
    }

    public void writeTraceLine(String[] data) {
        this.traceCSV.writeNext(data);
    }

    public ArrayList<SubRequest> getAllSubRequests() {
        return this.allSubRequests;
    }

    /**
     * Save a sub-request executed on the queue. Can be used to return informations
     * about them.
     * @param subReq To be added
     */
    public void saveSubRequest(SubRequest subReq) {
        allSubRequests.add(subReq);
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
        for (int i = 0; i < parameter("numberTasks"); i++) {
            clients.insert(new Client(this, i));
        }
        // Initialize all the data nodes, even the unused.
        for (int i = 0; i < parameter("numberDataNodes"); i++) {
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
     * Schedule the nodes according the parameters 'FILETYPE' and
     * 'ACCESPATTERN'. To all 4 combinations, the simulator needs to perform
     * a different action.
     */
    private void scheduleTasks() {
        int blockID = 0;    // Used to calculate the requests offset
        if (fileType.equals("File per Process") && accessPattern.equals("Sequential")) {
            // Creates "TasksNumber" files and schedule them
            for (Client actualClient : clients) {
                // Select the random data nodes for each file
                dataNodes = randomDataNodes(parameter("numberDataNodes"),
                        parameter("stripeCount"));
                for (int j = 0; j < parameter("numberSegments"); j++) {
                    Block block = new Block(this, actualClient, blockID);
                    actualClient.generateBlockRequests(block, dataNodes);
                    blockID++;
                }
            }
        } else if(fileType.equals("Shared") && accessPattern.equals("Sequential")) {
            // One single file, with all the clients working on it
            // Select the random data nodes to the file
            dataNodes = randomDataNodes(parameter("numberDataNodes"),
                    parameter("stripeCount"));
            for (int i = 0; i < parameter("numberSegments"); i++) {
                for (Client actualClient : clients) {
                    Block block = new Block(this, actualClient, blockID);
                    actualClient.generateBlockRequests(block, dataNodes);
                    blockID++;
                }
            }
        }
    }

}
