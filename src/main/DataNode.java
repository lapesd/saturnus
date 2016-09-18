package main;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;

public class DataNode extends Entity {

    private int nodeID;
    private boolean busy;

    public DataNode(int nodeID, Model model) {
        super(model, "DataNode", true);
        this.nodeID = nodeID;
        this.busy = false;
    }

    public int getNodeID() {
        return this.nodeID;
    }

    public boolean isBusy() {
        return this.busy;
    }

    public void setBusy(boolean state) {
        this.busy = state;
    }

}
