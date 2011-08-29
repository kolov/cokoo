package net.kolov.hs.modules.hs.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * AppUser: assen
 * Date: 4/16/11
 */
public interface FriendsServiceAsync {


    void getHomeTimeline(AsyncCallback<SortedStatus> async);

    void getUser(AsyncCallback<ClientUser> async);

    void askFriends(Integer fromLevel, Integer toLevel, AsyncCallback<ClientUser[]> async);

    void moveFriend(int id, Integer level, AsyncCallback<Void> async);

    void syncFriends(AsyncCallback<ClientUser> async);

    void sendInvite(AsyncCallback<Void> async);

    void updateUserFromClient(ClientUser cu, AsyncCallback<Void> async);
}
