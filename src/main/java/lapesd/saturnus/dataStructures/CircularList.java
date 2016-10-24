package lapesd.saturnus.dataStructures;

import java.util.ArrayList;

public class CircularList<T> extends ArrayList<T> {
    private static int lastAccess = -1;

    public T get(int index) {
        return super.get(index % size());
    }

    public T next() {
        lastAccess++;
        if (lastAccess == size()) {
            lastAccess = 0;
        }
        return get(lastAccess);
    }

    public void resetNextPointer() {
        lastAccess = -1;
    }
}
