package lapesd.saturnus.dataStructures;

import java.util.ArrayList;

public class CircularList<T> extends ArrayList<T> {
    private static int lastAccess = -1;

    public T get(int index) {
        return super.get(index % size());
    }

    public T next() {
        this.lastAccess++;
        return get(this.lastAccess % size());
    }

    public void resetNextPointer() {
        this.lastAccess = -1;
    }
}
