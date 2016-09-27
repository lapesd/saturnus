package lapesd.saturnus.simulator;

import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeInstant;

public class SimulationRunner {

    public static void main(String[] args) {
        Model model = new AbstractSimulator();
        Experiment exp = new Experiment("Experiment");

        // Connect both model and experiment.
        model.connectToExperiment(exp);

        // Experiment parameters.
        exp.setShowProgressBar(true);
        exp.setShowProgressBarAutoclose(true);

        exp.traceOn(new TimeInstant(0));

        exp.start();

        exp.report();
        exp.finish();

    }
}
