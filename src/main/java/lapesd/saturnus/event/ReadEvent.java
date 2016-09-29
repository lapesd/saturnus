package lapesd.saturnus.event;

import desmoj.core.simulator.EventOf2Entities;
import desmoj.core.simulator.Model;
import lapesd.saturnus.data.DataNode;

public class ReadEvent extends EventOf2Entities<Request, DataNode>{

    public ReadEvent(Model model) {
        super(model, "Read event", true);
    }

    @Override
    public void eventRoutine(Request request, DataNode dataNode) {
        sendTraceNote(dataNode + " executed a read event. From " + request);
    }
}
