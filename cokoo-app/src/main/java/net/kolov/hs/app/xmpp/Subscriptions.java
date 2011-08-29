package net.kolov.hs.app.xmpp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import twitter4j.TwitterException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * AppUser: assen
 * Date: 4/30/11
 */
@Controller
public class Subscriptions {

    private static final Logger LOGGER = LoggerFactory.getLogger(Subscriptions.class);

    @Autowired
    private XmppHelper xmppHelper;


    @RequestMapping(value = "/_ah/xmpp/subscription/subscribed/", method = RequestMethod.POST)
    public void subscribed(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException, TwitterException {
        String from = xmppHelper.getGoogleId(request);
        xmppHelper.changeUserXmpp(from, true);
    }

    @RequestMapping(value = "/_ah/xmpp/subscription/unsubscribed/", method = RequestMethod.POST)
    public void unsubscribed(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException, TwitterException {
        String from = xmppHelper.getGoogleId(request);
        xmppHelper.changeUserXmpp(from, false);
    }


}
