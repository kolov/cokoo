package net.kolov.hs.app.xmpp;

import com.google.appengine.api.xmpp.Message;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;
import net.kolov.hs.modules.hs.model.AppUser;
import net.kolov.hs.modules.hs.model.Levels;
import net.kolov.hs.modules.hs.server.UserRepository;
import net.kolov.hs.modules.hs.server.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import twitter4j.TwitterException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * AppUser: assen
 * Date: 4/30/11
 */
@Controller
public class Chat {
    @Autowired
    private XmppHelper xmppHelper;
    private HttpServletResponse response;


    @RequestMapping(value = "/_ah/xmpp/message/chat/", method = RequestMethod.POST)
    public void chat(HttpServletRequest request, HttpServletResponse response) throws IOException, TwitterException {
        XMPPService xmppService = XMPPServiceFactory.getXMPPService();

        Message m = xmppService.parseMessage(request);
        String body = m.getBody();
        final String respond = respond(body);
        response.getOutputStream().write(respond.getBytes());

    }

    private String respond(String body) {
        String[] parts = body.split(" ");
        if (parts.length == 0) {
            return help();
        }
        return respond(parts[0], parts);

    }

    private String respond(String cmd, String[] parts) {
        if (Levels.isLevelName(cmd)) {
            String[] nextParts = new String[parts.length - 1];
            for (int i = 1; i < parts.length; i++) {
                nextParts[i - 1] = parts[i];
            }
            return moveCategory(Levels.level(cmd), parts);
        } else {
            return help();
        }
    }

    private String moveCategory(int level, String[] parts) {
        String result = "";
        for (String part : parts) {
            if (part.startsWith("@")) {
                Users users = new UserRepository();
                final String twitterId = part.substring(1);
                AppUser user = users.findByTwitterId(twitterId);
                if (user != null) {
                    users.moveFriend(user.getTwitterId(), level, user);
                    result += "Moved " + user.getTwitterName();
                } else {
                    result = "Unknown: " + twitterId;
                }
            }
        }
        return result;
    }

    private String help() {
        return "star @user\nregular @user\ngnore @user\nstatus\nt - timeline";
    }


}
