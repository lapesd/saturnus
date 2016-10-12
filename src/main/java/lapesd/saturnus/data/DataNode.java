package lapesd.saturnus.data;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.Queue;
import lapesd.saturnus.event.Task;

public class DataNode extends Entity {

    private double nodeClock;
    private Queue<Task> tasksQueue;

    public DataNode(Model model) {
        super(model, "DataNode", true);
        this.nodeClock = 0.0;
        this.tasksQueue = new Queue<Task>(model, "Task queue: " + this, true, true);
    }

    public double incrementNodeClock(double value) {
        return this.nodeClock += value;
    }

    public void insertTaskToQueue(Task newTask) {
        this.tasksQueue.insert(newTask);
    }

    public void execute() {
        int queueSize = this.tasksQueue.size();
        for (int i = 0; i < queueSize; i++) {
            removeFirstTask().execute();
        }
    }

    private Task removeFirstTask() {
        return this.tasksQueue.removeFirst();
    }
}
