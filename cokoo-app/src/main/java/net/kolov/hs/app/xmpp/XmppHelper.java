package net.kolov.hs.app.xmpp;

import com.google.appengine.api.xmpp.Subscription;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;
import net.kolov.hs.modules.hs.model.AppUser;
import net.kolov.hs.modules.hs.server.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * User: assen
 * Date: 6/6/11
 */

@Component
public class XmppHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(XmppHelper.class);


    public String getGoogleId(HttpServletRequest request) throws IOException {
        // In the handler for _ah/xmpp/presence/subscribe
        XMPPService xmppService = XMPPServiceFactory.getXMPPService();
        Subscription sub = xmppService.parseSubscription(request);

        // Split the bare XMPP address (e.g., user@gmail.com)
        // from the resource (e.g., gmail.CD6EBC4A)
        return sub.getFromJid().getId().split("/")[0];
    }

    public AppUser changeUserXmpp(String from, boolean val) {
        UserRepository userRepository = new UserRepository();
        AppUser user = userRepository.findByGoogleId(from);
        if (user != null) {
            if (!Boolean.valueOf(val).equals(user.isXmppContact())) {
                user.setXmppContact(val);
                userRepository.save(user);
            }
            return user;
        } else {
            LOGGER.error("No user {}", from);
            return null;
        }
    }


}
