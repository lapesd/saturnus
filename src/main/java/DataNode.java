package main;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;

public class DataNode extends Entity {

    private double nodeClock;
    private int tasksNumber;

    public DataNode(Model model) {
        super(model, "DataNode", true);
        this.nodeClock = 0.0;
        this.tasksNumber = 0;
    }

    public void setNodeClock(double time) {
        this.nodeClock = time;
    }

    public void setTasksNumber(int number) {
        this.tasksNumber = number;
    }

    public double getNodeClock() {
        return this.nodeClock;
    }

    public int getTasksNumber() {
        return this.tasksNumber;
    }
}
