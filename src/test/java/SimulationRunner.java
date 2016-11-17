import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeInstant;
import desmoj.extensions.xml.report.XMLTraceOutput;
import lapesd.saturnus.simulator.AbstractSimulator;
import lapesd.saturnus.simulator.CSVformat;

public class SimulationRunner {

    public static void main(String[] args) {
        Model model = new AbstractSimulator();
        Experiment exp = new Experiment("Simulation");
        XMLTraceOutput xmlTrace = new XMLTraceOutput();

        // Initialize the CSV output buffer.
        String head = "request_offset, datanode, client_id, sending_time, attended_time, output_time";
        CSVformat.openFileToWrite("trace.csv", head);

        // Connect both model and experiment.
        model.connectToExperiment(exp);

        // Write the trace into a XML file
        xmlTrace.open(null, "Simulation");
        exp.addTraceReceiver(xmlTrace);

        exp.traceOn(new TimeInstant(0));
        exp.start();

        exp.report();

        xmlTrace.close();
        CSVformat.closeFile();

        exp.finish();

    }
}
