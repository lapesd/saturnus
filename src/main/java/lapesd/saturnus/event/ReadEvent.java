package lapesd.saturnus.event;

import desmoj.core.simulator.EventOf2Entities;
import desmoj.core.simulator.Model;
import lapesd.saturnus.data.DataNode;

public class ReadEvent extends EventOf2Entities<Task, DataNode>{

    public ReadEvent(Model model) {
        super(model, "Read event", true);
    }

    @Override
    public void eventRoutine(Task task, DataNode dataNode) {
        sendTraceNote(dataNode + " executed a read event. From " + task);
    }
}
