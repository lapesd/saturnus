package lapesd.saturnus.dataStructures;

import java.util.ArrayList;

public class CircularList<T> extends ArrayList<T> {
    private static int lastAccess = -1;

    public T get(int index) {
        return super.get(index % size());
    }

    public T next() {
        lastAccess++;
        return get(lastAccess % size());
    }

    public void resetNextPointer() {
        lastAccess = -1;
    }
}
