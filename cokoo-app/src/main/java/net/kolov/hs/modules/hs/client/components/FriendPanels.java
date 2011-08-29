package net.kolov.hs.modules.hs.client.components;

import java.util.HashMap;
import java.util.Map;

/**
 * User: assen
 * Date: 6/14/11
 */
public class FriendPanels {
    private Map<Integer, FriendsScrollPanel> panels = new HashMap<Integer, FriendsScrollPanel>();

    public void add(Integer level, FriendsScrollPanel panel) {
        panels.put(level, panel);
    }

    public void highlight(int twitterId) {
        for (FriendsScrollPanel friendsScrollPanel : panels.values()) {
            int ix = friendsScrollPanel.indexOf(twitterId);
            if (ix != -1) {
                friendsScrollPanel.highlightIndex(ix);
                break;
            }
        }
    }
}
