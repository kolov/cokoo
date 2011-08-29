package net.kolov.hs.modules.hs.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import net.kolov.hs.app.SessionData;
import net.kolov.hs.modules.hs.assembler.ClientUserAssembler;
import net.kolov.hs.modules.hs.assembler.TwitterUserAssembler;
import net.kolov.hs.modules.hs.client.ClientStatus;
import net.kolov.hs.modules.hs.client.ClientUser;
import net.kolov.hs.modules.hs.client.SortedStatus;
import net.kolov.hs.modules.hs.model.AppUser;
import net.kolov.hs.modules.hs.model.TwitterUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import twitter4j.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AppUser: assen
 * Date: 4/16/11
 */
@Component
public class FriendsServiceImpl extends RemoteServiceServlet {


    @Autowired
    private TwitterClient twitterClient;

    private net.kolov.twiscriminator.sc.ScalaAssembler clientUserAssembler = new net.kolov.twiscriminator.sc.ScalaAssembler();
    private TwitterUserAssembler twitterUserAssembler = new TwitterUserAssembler();
    private static final int FRIENDS_BLOCK = 30;


    public ClientUser[] askFriends(Integer fromLevel, Integer toLevel) {
        AppUser user = getSessionUser();
        return askFriends(fromLevel, toLevel, user);
    }

    ClientUser syncFriends(AppUser user) {
        Twitter twitter = twitterClient.createTwitter(user);

        Users users = new UserRepository();
        List<Integer> existingFriendIds = getExistingFriendIds(user);


        ArrayList<TwitterUser> newTwitterFriends = new ArrayList<TwitterUser>();
        List<ClientUser> allFriends = new ArrayList<ClientUser>();


        try {
            IDs twitterFriends = twitter.getFriendsIDs();
            int[] ids = twitterFriends.getIDs();
            List<Integer> idsToAsk = new ArrayList<Integer>();

            for (int pos = 0; pos < ids.length; pos++) {
                // if id present in knownFriends - do nothing
                int id = ids[pos];
                if (!existingFriendIds.contains(id)) {
                    idsToAsk.add(id);
                }
                int size = idsToAsk.size();
                if (size == FRIENDS_BLOCK || size > 0 && pos == ids.length - 1) {
                    ResponseList<User> newFriends = getTwitterUsers(twitter, idsToAsk);
                    for (User usr : newFriends) {
                        TwitterUser twitterUser = twitterUserAssembler.from(usr);
                        newTwitterFriends.add(twitterUser);
                    }
                    idsToAsk.clear();
                }

            }

            users.storeFriends(user, newTwitterFriends);
        } catch (TwitterException e) {
            throw new RuntimeException(e);
        }


        return new ClientUserAssembler().from(user);

    }

    private List<Integer> getExistingFriendIds(AppUser user) {

        Users users = new UserRepository();
        List<ClientUser> knownFriends = users.getFriends(user, null, null);
        List<Integer> existingFriendIds = new ArrayList<Integer>();
        for (ClientUser knownFriend : knownFriends) {
            existingFriendIds.add(knownFriend.getTwitterId());
        }
        return existingFriendIds;
    }

    private ResponseList<User> getTwitterUsers(Twitter twitter, List<Integer> idsToAsk) throws TwitterException {
        int[] block = new int[idsToAsk.size()];
        int i = 0;
        for (Integer integer : idsToAsk) {
            block[i++] = integer.intValue();
        }
        return twitter.lookupUsers(block);
    }

    public ClientUser[] askFriends(Integer fromLevel, Integer toLevel, AppUser user) {

        Users users = new UserRepository();
        List<ClientUser> clientUsers = users.getFriends(user, fromLevel, toLevel);

        ClientUser[] result = new ClientUser[clientUsers.size()];
        return clientUsers.toArray(result);
    }

    private AppUser getSessionUser() {
        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession session = request.getSession();
        SessionData sessionData = SessionData.getSessionData(session);
        return sessionData.getUser();
    }


    public SortedStatus getHomeTimeline() {
        AppUser user = getSessionUser();
        return getHomeTimeline(user);
    }


    public SortedStatus getHomeTimeline(AppUser user) {
        Twitter twitter = twitterClient.createTwitter(user);
        List<ClientUser> friends = new UserRepository().getFriends(user, null, null);

        Map<Integer, ClientUser> usersById = new HashMap<Integer, ClientUser>();
        for (ClientUser friend : friends) {
            usersById.put(friend.getTwitterId(), friend);
        }


        SortedStatus result = new SortedStatus();

        try {
            ResponseList<Status> tweets = twitter.getHomeTimeline();
            for (Status tweet : tweets) {
                ClientStatus clientStatus = new ClientStatus(tweet.getText());

                User tweetUser = tweet.getUser();
                ClientUser cu = usersById.get(tweet.getUser().getId());
                if (cu != null) {
                    clientStatus.setUser(cu);
                    result.add(cu.getLevel(), clientStatus);
                }
            }
        } catch (TwitterException e) {
            throw new RuntimeException(e);
        }


        return result;
    }

    public void moveFriend(int id, Integer level, AppUser user) {
        Users users = new UserRepository();
        users.moveFriend(id, level, user);
    }
}