package lapesd.saturnus.data;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;

public class Block extends Entity {
    private Segment segment;

    public Block(Model model, Segment segment) {
        super(model, "Block", true);
        this.segment = segment;
    }3
}
