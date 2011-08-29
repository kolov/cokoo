package net.kolov.hs.modules.hs.assembler;

import net.kolov.hs.modules.hs.model.TwitterUser;
import twitter4j.User;

/**
 * User: assen
 * Date: 5/17/11
 */
public class TwitterUserAssembler {

    public TwitterUser from(User usr) {
        TwitterUser tu = new TwitterUser();
        tu.setPicUrl(usr.getProfileImageURL().toString());
        tu.setScreenName(usr.getScreenName());
        tu.setTwitterId(usr.getId());
        return tu;
    }


}
