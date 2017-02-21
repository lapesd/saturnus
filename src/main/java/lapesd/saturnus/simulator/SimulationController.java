package lapesd.saturnus.simulator;

import com.opencsv.CSVWriter;
import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.TimeInstant;
import org.apache.commons.collections.map.HashedMap;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class SimulationController {

    public static void initSimulation(Map<String, String> parameters) throws IOException{
        // Pre-processing the parameters
        Map<String, Integer> numericParams = new HashedMap();
        numericParams.put("numberTasks", Integer.parseInt(parameters.get("numberTasks")));
        numericParams.put("numberSegments", Integer.parseInt(parameters.get("numberSegments")));
        numericParams.put("numberDataNodes", Integer.parseInt(parameters.get("numberDataNodes")));
        numericParams.put("blockSize", Integer.parseInt(parameters.get("blockSize")));
        numericParams.put("requestSize", Integer.parseInt(parameters.get("requestSize")));
        numericParams.put("stripeSize", Integer.parseInt(parameters.get("stripeSize")));
        numericParams.put("stripeCount", Integer.parseInt(parameters.get("stripeCount")));

        AbstractSimulator model = new AbstractSimulator(numericParams, parameters.get("fileType"), parameters.get("accessPattern"));
        Experiment experiment = new Experiment("Simulation");
        experiment.setSilent(true);

        // Initializes the CSV output buffer
        String head = "request_offset datanode client_id sending_time attended_time output_time";
        CSVWriter trace_csv = new CSVWriter(new FileWriter("experiment_trace.csv"));
        trace_csv.writeNext(head.split(" "));
        model.traceCSV(trace_csv);

        // Executes the model
        model.connectToExperiment(experiment);
        //output.appendText("--------------------------------------------------\n");
        //output.appendText("Simulation starts at simulation time: 0.000000\n");
        //output.appendText("Please wait...\n");
        experiment.traceOn(new TimeInstant(0));
        experiment.start();
        experiment.report();
        experiment.finish();
        trace_csv.close();

        //output.appendText("Simulation stopped.\nLast request was attended at simulation time: " +
        //        experiment.getSimClock().getTime().getTimeAsDouble() + "\n");

    }

}
