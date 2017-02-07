package lapesd.saturnus.simulator;

import com.opencsv.CSVWriter;
import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.TimeInstant;
import javafx.scene.control.TextArea;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class SimulationController {

    public static void initSimulation(TextArea output, Map parameters, String fType, String aPattern) throws IOException{
        AbstractSimulator model = new AbstractSimulator(parameters, fType, aPattern);
        Experiment experiment = new Experiment("Simulation");
        experiment.setSilent(true);

        // Initializes the CSV output buffer
        String head = "request_offset datanode client_id sending_time attended_time output_time";
        CSVWriter trace_csv = new CSVWriter(new FileWriter("experiment_trace.csv"));
        trace_csv.writeNext(head.split(" "));
        model.traceCSV(trace_csv);

        // Executes the model
        model.connectToExperiment(experiment);
        output.appendText("--------------------------------------------------\n");
        output.appendText("Simulation starts at simulation time: 0.000000\n");
        output.appendText("Please wait...\n");
        experiment.traceOn(new TimeInstant(0));
        experiment.start();
        experiment.report();
        experiment.finish();
        trace_csv.close();
        output.appendText("Simulation stopped.\nLast request was attended at simulation time: " +
                experiment.getSimClock().getTime().getTimeAsDouble() + "\n");

    }

}
