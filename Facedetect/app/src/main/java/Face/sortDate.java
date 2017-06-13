package Face;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by hyc on 2016/5/12.
 */
public class sortDate implements Comparator {
    public int compare(Object obj1, Object obj2) {
        Date begin = (Date) obj1;
        Date end = (Date) obj2;
        if (begin.after(end)) {
            return 1;
        } else {
            return -1;
        }
    }
}
