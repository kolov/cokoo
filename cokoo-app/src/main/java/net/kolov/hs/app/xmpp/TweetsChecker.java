package net.kolov.hs.app.xmpp;

import net.kolov.hs.modules.hs.client.ClientUser;
import net.kolov.hs.modules.hs.model.AppUser;
import net.kolov.hs.modules.hs.model.Levels;
import net.kolov.hs.modules.hs.server.Messenger;
import net.kolov.hs.modules.hs.server.TwitterClient;
import net.kolov.hs.modules.hs.server.UserRepository;
import net.kolov.hs.modules.hs.server.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import twitter4j.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * AppUser: assen
 * Date: 4/30/11
 */
@Controller
public class TweetsChecker {
    @Autowired
    private TwitterClient twitterClient;
    @Autowired
    private Messenger messenger;


    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(TweetsChecker.class.getName());

    @RequestMapping("/checktweets.do")
    public void checkTweets(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException, TwitterException {
        Users users = new UserRepository();
        List<AppUser> allUsers = users.findAll();
        for (AppUser user : allUsers) {
            LOGGER.info("Checking tweets for user " + user.getGoogleId());
            if (!user.getNotifyStarTweets()) {
                LOGGER.info("User " + user.getGoogleId() + " doesn't want to be notified");
                continue;
            }

            Twitter twitter = twitterClient.createTwitter(user);
            ResponseList<Status> tweets;
            if (user.getLastHomelineId() == -1) {
                tweets = twitter.getHomeTimeline();
            } else {
                tweets = twitter.getHomeTimeline(new Paging(user.getLastHomelineId()));
            }
            LOGGER.info("Retreived " + tweets.size() + " tweets. ");
            if (!tweets.isEmpty()) {
                long id1 = tweets.get(tweets.size() - 1).getId();
                long id2 = tweets.get(0).getId();
                long id = Math.max(id1, id2);
                user.setLastHomelineId(id);
                LOGGER.info("Set setLastHomelineId " + id);
                users.save(user);
                List<ClientUser> realTimeFriends = users.getFriends(user, Levels.LIVE, null);
                List<Integer> realTimeIds = new ArrayList<Integer>();
                for (ClientUser realTimeFriend : realTimeFriends) {
                    realTimeIds.add(realTimeFriend.getTwitterId());
                }
                for (Status tweet : tweets) {
                    if (realTimeIds.contains(tweet.getUser().getId())) {
                        messenger.sendMessage(user, tweet.getUser().getName() + ":" + tweet.getText());
                    }
                }

            }

        }
    }


}
