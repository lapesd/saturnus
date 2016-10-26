package lapesd.saturnus.data;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;

public class Block extends Entity {
    private Segment segment;
    private int blockSize, ID;

    public Block(Model model, Segment segment, int blockSize, int ID) {
        super(model, "Block", true);
        this.segment = segment;
        this.blockSize = blockSize;
        this.ID = ID;
    }

    public int getID() {
        return this.ID;
    }

    public int getBlockSize() {
        return this.blockSize;
    }

    public Segment getSegment() {
        return this.segment;
    }
}
