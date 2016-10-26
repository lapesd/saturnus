import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeInstant;
import desmoj.extensions.xml.report.XMLTraceOutput;
import lapesd.saturnus.simulator.AbstractSimulator;

public class SimulationRunner {

    public static void main(String[] args) {
        Model model = new AbstractSimulator();
        Experiment exp = new Experiment("Simulation");
        XMLTraceOutput xmlTrace = new XMLTraceOutput();

        // Connect both model and experiment.
        model.connectToExperiment(exp);

        // Write the trace into a XML file
        xmlTrace.open(null, "Simulation");
        exp.addTraceReceiver(xmlTrace);

        exp.traceOn(new TimeInstant(0));
        exp.start();

        exp.report();
        xmlTrace.close();
        exp.finish();

    }
}
