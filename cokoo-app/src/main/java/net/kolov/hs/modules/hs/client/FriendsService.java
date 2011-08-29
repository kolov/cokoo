package net.kolov.hs.modules.hs.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * AppUser: assen
 * Date: 4/16/11
 */
@RemoteServiceRelativePath("FriendsService.do")
public interface FriendsService extends RemoteService {

    ClientUser[] askFriends(Integer fromLevel, Integer toLevel);

    SortedStatus getHomeTimeline();

    ClientUser getUser();

    void moveFriend(int id, Integer level);

    ClientUser syncFriends();

    void sendInvite();

    void updateUserFromClient(ClientUser user);

    /**
     * Utility/Convenience class.
     * Use FriendsService.App.getInstance() to access static instance of LoginServiceAsync
     */
    public static class App {
        private static final FriendsServiceAsync ourInstance = (FriendsServiceAsync) GWT.create(FriendsService.class);

        public static FriendsServiceAsync getInstance() {
            return ourInstance;
        }
    }
}
