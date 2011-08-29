package net.kolov.hs.modules.hs.assembler;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import net.kolov.hs.modules.hs.client.ClientUser;
import net.kolov.hs.modules.hs.model.AppUser;
import net.kolov.hs.modules.hs.model.Levels;
import net.kolov.hs.modules.hs.model.Relation;
import net.kolov.hs.modules.hs.model.TwitterUser;
import twitter4j.User;

/**
 * User: assen
 * Date: 5/17/11
 */
public class ClientUserAssembler {

    public ClientUser from(TwitterUser tu, Relation rel) {
        return from(tu, rel.getLevel());
    }

    public ClientUser from(TwitterUser tu, int level) {
        ClientUser cu = new ClientUser();
        cu.setTwitterName(tu.getScreenName());
        cu.setPicurl(tu.getPicUrl());
        cu.setLevel(level);
        cu.setTwitterId(tu.getTwitterId());
        return cu;
    }

    public ClientUser from(TwitterUser tu) {
        return from(tu, Levels.INITIAL);
    }


    public ClientUser from(User tweetUser) {
        ClientUser cu = new ClientUser();
        cu.setTwitterName(tweetUser.getScreenName());
        cu.setTwitterId(tweetUser.getId());
        cu.setPicurl(tweetUser.getProfileImageURL().toString());

        return cu;

    }

    public ClientUser from(AppUser appUser, String url) {
        ClientUser clientUser = new ClientUser();
        clientUser.setGoogleName(appUser.getGoogleId());
        clientUser.setTwitterName(appUser.getTwitterName());
        clientUser.setTwitterId(appUser.getTwitterId());
        clientUser.setXmppContact(appUser.isXmppContact());
        clientUser.setGoogleLoginUrl(url);
        clientUser.setNotifyStarTweets(PrimitiveUtils.getValue(appUser.getNotifyStarTweets()));
        return clientUser;
    }

    public ClientUser from(AppUser appUser) {
        UserService userService = UserServiceFactory.getUserService();
        return from(appUser, userService.createLoginURL("/hello.do"));
    }

}
