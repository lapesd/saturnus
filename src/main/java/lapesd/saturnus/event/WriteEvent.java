package lapesd.saturnus.event;

import desmoj.core.simulator.EventOf2Entities;
import desmoj.core.simulator.Model;
import lapesd.saturnus.data.DataNode;

public class WriteEvent extends EventOf2Entities<Task, DataNode> {

    public WriteEvent(Model model) {
        super(model, "Write event", true);
    }

    @Override
    public void eventRoutine(Task task, DataNode dataNode) {
        sendTraceNote(dataNode + " executed a write event. From " + task);
    }
}