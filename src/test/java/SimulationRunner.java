import com.opencsv.CSVWriter;
import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.TimeInstant;
import lapesd.saturnus.simulator.AbstractSimulator;

import java.io.FileWriter;
import java.io.IOException;

public class SimulationRunner {

    public static void main(String[] args) throws IOException{
        AbstractSimulator model = new AbstractSimulator();
        Experiment experiment = new Experiment("Simulation");

        // Initializes the CSV output buffer
        String head = "request_offset datanode client_id sending_time attended_time output_time";
        CSVWriter trace_csv = new CSVWriter(new FileWriter("experiment_trace.csv"));
        trace_csv.writeNext(head.split(" "));
        model.setTraceCSV(trace_csv);

        // Executes the model
        model.connectToExperiment(experiment);
        experiment.traceOn(new TimeInstant(0));
        experiment.start();
        experiment.report();
        experiment.finish();
        trace_csv.close();

    }
}
