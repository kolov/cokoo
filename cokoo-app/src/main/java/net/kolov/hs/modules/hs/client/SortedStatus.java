package net.kolov.hs.modules.hs.client;

import java.util.ArrayList;

/**
 * User: assen
 * Date: 5/14/11
 */
public class SortedStatus extends ArrayList<CategorizedStatus> {

    public SortedStatus() {
        add(new CategorizedStatus("Star"));
        add(new CategorizedStatus("Regular"));
    }

    public void add(int level, ClientStatus clientStatus) {
        CategorizedStatus cs;
        switch (level) {
            case 1:
                cs = get(0);
                break;
            case 0:
                cs = get(1);
                break;
            default:
                return;
        }

        cs.getStates().add(clientStatus);
    }

    public int recordsSize() {
        int result = 0;
        for (CategorizedStatus categorizedStatus : this) {
            int size = categorizedStatus.getStates().size();
            if (size != 0) {
                result += 1 + size;
            }
        }
        return result;
    }

    public int categoriesSize() {
        int result = 0;
        for (CategorizedStatus categorizedStatus : this) {
            int size = categorizedStatus.getStates().size();
            if (size != 0) {
                result++;
            }
        }
        return result;
    }
}
