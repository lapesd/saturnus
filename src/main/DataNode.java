package main;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;

public class DataNode extends Entity {

    private double nodeClock;

    public DataNode(Model model) {
        super(model, "DataNode", true);
        this.nodeClock = 0.0;
    }

    public void setNodeClock(double time) {
        this.nodeClock = time;
    }

    public double getNodeClock() {
        return this.nodeClock;
    }

}
