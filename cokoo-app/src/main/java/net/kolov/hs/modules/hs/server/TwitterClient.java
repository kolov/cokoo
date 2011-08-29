package net.kolov.hs.modules.hs.server;

import net.kolov.hs.modules.hs.model.AppUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;

/**
 * User: assen
 * Date: 6/6/11
 */
@Component
public class TwitterClient {
    @Value("${oauth.key}")
    private String authKey;

    @Value("${oauth.secret}")
    private String authSecret;

    public Twitter createTwitter(AppUser user) {
        AccessToken accessToken = new AccessToken(user.getToken(), user.getTokenKey());
        return new TwitterFactory().getOAuthAuthorizedInstance(authKey, authSecret, accessToken);
    }
}
