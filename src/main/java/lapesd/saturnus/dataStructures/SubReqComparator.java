package lapesd.saturnus.dataStructures;

import lapesd.saturnus.event.SubRequest;
import java.util.Comparator;

public class SubReqComparator implements Comparator<SubRequest> {

    public int compare(SubRequest o, SubRequest t1) {
        double oTime = o.getSendingTime().getTimeAsDouble();
        double t1Time = t1.getSendingTime().getTimeAsDouble();
        if (oTime < t1Time)
            return -1;
        else if (oTime > t1Time)
            return 1;
        return 0;
    }
}
