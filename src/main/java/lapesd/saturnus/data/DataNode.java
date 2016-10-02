package lapesd.saturnus.data;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;

public class DataNode extends Entity {

    private double nodeClock;

    public DataNode(Model model) {
        super(model, "DataNode", true);
        this.nodeClock = 0.0;
    }

    public double getNodeClock() {
        return this.nodeClock;
    }

    public double incrementNodeClock(double value) {
        return this.nodeClock += value;
    }
}
