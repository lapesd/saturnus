package lapesd.saturnus.model.dataStructures;

import lapesd.saturnus.model.event.SubRequest;

import java.util.Comparator;

public class AttTimeComparator implements Comparator<SubRequest> {
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
