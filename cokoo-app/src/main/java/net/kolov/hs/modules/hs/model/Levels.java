package net.kolov.hs.modules.hs.model;


import java.util.ArrayList;
import java.util.List;

/**
 * User: assen
 * Date: 5/17/11
 */
public class Levels {
    public static final int DEFAULT = 0;
    public static final int INITIAL = DEFAULT;
    public static final int REGULAR = 0;
    public static final int LIVE = 1;
    public static final int IGNORE = -1;
    public static final int COUNT = 3;
    public static final int MAX = LIVE;
    public static final int MIN = IGNORE;
    private static List<String> labels = new ArrayList();

    static {
        labels.add("Ignore");
        labels.add("Regular");
        labels.add("Star");
    }


    public static String getLabel(int i) {
        return labels.get(i - MIN);
    }

    public static boolean isLevelName(String cmd) {
        return labels.contains(cmd);
    }


    public static int level(String cmd) {
        int i = MIN;
        for (String label : labels) {
            if (label.equals(cmd)) {
                return i;
            }
            i++;
        }
        return 0;
    }
}
