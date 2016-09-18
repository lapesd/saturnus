package main;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;

public class DataNode extends Entity {

    private boolean busy;

    public DataNode(Model model) {
        super(model, "DataNode", true);
        this.busy = false;
    }

    public boolean isBusy() {
        return this.busy;
    }

    public void setBusy(boolean state) {
        this.busy = state;
    }

}
