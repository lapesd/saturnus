package lapesd.saturnus.control;

import desmoj.core.simulator.Experiment;
import lapesd.saturnus.model.event.SubRequest;
import lapesd.saturnus.model.utils.CSVWriter;
import org.apache.commons.collections.map.HashedMap;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class SimulationController {

    public ArrayList<SubRequest> initSimulation(Map<String, String> parameters) throws IOException{
        // Pre-processing the parameters
        Map<String, Long> numericParams = new HashedMap();
        numericParams.put("numberTasks", Long.parseLong(parameters.get("numberTasks")));
        numericParams.put("numberSegments", Long.parseLong(parameters.get("numberSegments")));
        numericParams.put("numberDataNodes", Long.parseLong(parameters.get("numberDataNodes")));
        numericParams.put("blockSize", Long.parseLong(parameters.get("blockSize")));
        numericParams.put("requestSize", Long.parseLong(parameters.get("requestSize")));
        numericParams.put("stripeSize", Long.parseLong(parameters.get("stripeSize")));
        numericParams.put("stripeCount", Long.parseLong(parameters.get("stripeCount")));

        // Creating the model
        AbstractSimulator model = new AbstractSimulator(
                numericParams,
                parameters.get("fileType"),
                parameters.get("accessPattern")
        );

        Experiment experiment = new Experiment("Simulation");
        experiment.setSilent(true);

        // Initializes the CSV output buffer
        String head = "request_offset datanode client_id sending_time attended_time output_time";
        CSVWriter trace = new CSVWriter(
                new FileWriter("experiment_trace.csv"),
                head
        );
        model.setReportWriter(trace);

        // Executes the model
        model.connectToExperiment(experiment);
        experiment.start();

        // End of simulation
        experiment.finish();
        model.getReportWriter().close();

        return model.getExecutedSubRequests();
    }

}
