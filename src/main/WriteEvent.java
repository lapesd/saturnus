package main;

import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;

public class WriteEvent extends Event<DataNode> {

    private AbstractSimulator model;

    public WriteEvent(Model model) {
        super(model, "Write event", true);
        this.model = (AbstractSimulator)model;
    }

    @Override
    public void eventRoutine(DataNode dataNode) {
        schedule(dataNode, new TimeSpan(model.getExponentialDist()));
    }
}
