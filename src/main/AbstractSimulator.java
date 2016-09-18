package main;

import desmoj.core.dist.ContDistExponential;
import desmoj.core.simulator.*;
import java.util.concurrent.TimeUnit;

/**
 * The "base" of the simulator. Has all methods to handle with the events,
 * queue of events, data nodes, etc.
 */
public class AbstractSimulator extends Model {

    private static final int NODESAMOUNT = 4;
    private static final int TASKNUMBER = 1;

    // Queues
    protected Queue<DataNode> dataNodesQueue;
    private ContDistExponential exponentialDist;

    public AbstractSimulator() {
        super(null, "AbstractSimulator", true, false);
    }


    @Override
    public void init() {
        this.exponentialDist = new ContDistExponential(this, "Event time", 6.0, true, false);
        this.exponentialDist.setNonNegative(true);
        this.dataNodesQueue = new Queue<DataNode>(this, "Data nodes.", true, true);

        // Initialize the data nodes.
        for (int i = 0; i < NODESAMOUNT; i++) {
            dataNodesQueue.insert(new DataNode(i, this));
        }
    }

    @Override
    public void doInitialSchedules() {
        WriteEvent write = new WriteEvent(this);
        write.schedule(dataNodesQueue.get(0), new TimeInstant(0));
    }

    @Override
    public String description() {
        return "Saturnus is an event based discrete simulator for parallel " +
                "file systems.";
    }

    public int getNodesAmount() {
        return this.NODESAMOUNT;
    }

    public double getExponentialDist() {
        return this.exponentialDist.sample();
    }


    public static void main(String[] args) {
        Model model = new AbstractSimulator();
        Experiment exp = new Experiment("Experiment");
        // Connect both model and experiment.
        model.connectToExperiment(exp);

        // Experiment parameters.
        exp.setShowProgressBar(true);
        exp.setShowProgressBarAutoclose(true);

        exp.traceOn(new TimeInstant(0));
        exp.stop(new TimeInstant(2, TimeUnit.MINUTES));

        exp.start();
        exp.report();
        exp.finish();

    }
}
