package lapesd.saturnus.simulator;

import com.opencsv.CSVWriter;
import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.TimeInstant;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class SimulationController {

    public static void initSimulation(Map parameters, String fType, String aPattern) throws IOException{
        AbstractSimulator model = new AbstractSimulator(parameters, fType, aPattern);
        Experiment experiment = new Experiment("Simulation");

        // Initializes the CSV output buffer
        String head = "request_offset datanode client_id sending_time attended_time output_time";
        CSVWriter trace_csv = new CSVWriter(new FileWriter("experiment_trace.csv"));
        trace_csv.writeNext(head.split(" "));
        model.traceCSV(trace_csv);

        // Executes the model
        model.connectToExperiment(experiment);
        experiment.traceOn(new TimeInstant(0));
        experiment.start();
        experiment.report();
        experiment.finish();
        trace_csv.close();

    }

}
