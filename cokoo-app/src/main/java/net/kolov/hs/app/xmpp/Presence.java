package net.kolov.hs.app.xmpp;

import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.PresenceType;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;
import net.kolov.hs.modules.hs.model.AppUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import twitter4j.TwitterException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * AppUser: assen
 * Date: 4/30/11
 */
@Controller
public class Presence {
    @Autowired
    private XmppHelper xmppHelper;
    private static final Logger LOGGER = LoggerFactory.getLogger(TweetsChecker.class);


    /**
     * On available - set status message and set userXmppAvalability to true;
     *
     * @param request
     * @throws IOException
     * @throws TwitterException
     */
    @RequestMapping(value = "/_ah/xmpp/presence/probe/", method = RequestMethod.POST)
    public void probe(HttpServletRequest request) throws IOException, TwitterException {
        available(request);
    }

    @RequestMapping(value = "/_ah/xmpp/presence/available/", method = RequestMethod.POST)
    public void available(HttpServletRequest request) throws IOException, TwitterException {

        XMPPService xmppService = XMPPServiceFactory.getXMPPService();
        com.google.appengine.api.xmpp.Presence presence = xmppService.parsePresence(request);

        LOGGER.info("Called available " + presence.getFromJid());


        // Split the XMPP address (e.g., user@gmail.com)
        // from the resource (e.g., gmail.CD6EBC4A)
        String from = presence.getFromJid().getId().split("/")[0];


        AppUser user = xmppHelper.changeUserXmpp(from, true);
        String status;

        if (user != null) {
            final String twitterName = user.getTwitterName();
            if (twitterName == null) {
                status = "Please, authorize access to Twitter";
            } else {
                status = "Ready to resend star tweets for [@" + twitterName +"]";
            }
        } else {
            status = "Hey, do I know you?";
        }
        xmppService.sendPresence(new JID(from), PresenceType.AVAILABLE, presence.getPresenceShow(), status);


        xmppHelper.changeUserXmpp(from, true);

    }

    @RequestMapping(value = "/_ah/xmpp/presence/unavailable/", method = RequestMethod.POST)
    public void unavailable(HttpServletRequest request) throws IOException, TwitterException {
    }


}
