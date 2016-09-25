package main;


import desmoj.core.simulator.EventOf2Entities;
import desmoj.core.simulator.Model;

public class ReadEvent extends EventOf2Entities<main.Task, main.DataNode>{

    public ReadEvent(Model model) {
        super(model, "Read event", true);
    }

    @Override
    public void eventRoutine(Task task, DataNode dataNode) {
        sendTraceNote(dataNode + " executed a read event. From " + task);
    }
}
