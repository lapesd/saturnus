package lapesd.saturnus.dataStructures;

import lapesd.saturnus.event.SubRequest;

import java.util.Comparator;

public class ComparatorAttTime implements Comparator<SubRequest> {
    @Override
    public int compare(SubRequest o, SubRequest t1) {
        double oTime = o.getAttendedTime().getTimeAsDouble();
        double t1Time = t1.getAttendedTime().getTimeAsDouble();
        if (oTime < t1Time)
            return -1;
        else if (oTime > t1Time)
            return 1;
        return 0;
    }
}
