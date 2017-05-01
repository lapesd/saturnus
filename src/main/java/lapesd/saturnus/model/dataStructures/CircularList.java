package lapesd.saturnus.model.dataStructures;

import java.util.ArrayList;

public class CircularList<T> extends ArrayList<T> {
    private static int lastAccess = -1;

    public T get(long index) {
        return super.get((int)index % size());
    }

    public T next() {
        this.lastAccess++;
        return get(this.lastAccess % size());
    }
}
