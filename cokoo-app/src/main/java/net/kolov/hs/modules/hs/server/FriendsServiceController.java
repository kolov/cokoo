package net.kolov.hs.modules.hs.server;

import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.XMPPServiceFactory;
import net.kolov.gwt.BaseRemoteService;
import net.kolov.hs.app.SessionData;
import net.kolov.hs.modules.hs.assembler.ClientUserAssembler;
import net.kolov.hs.modules.hs.client.ClientUser;
import net.kolov.hs.modules.hs.client.FriendsService;
import net.kolov.hs.modules.hs.client.SortedStatus;
import net.kolov.hs.modules.hs.model.AppUser;
import net.kolov.hs.modules.hs.model.TwitterUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/**
 * User: assen
 * Date: 5/14/11
 */
@Controller
@RequestMapping("/**/FriendsService.do")
public class FriendsServiceController extends BaseRemoteService implements FriendsService {

    @Autowired
    private FriendsServiceImpl service;
    @Autowired
    private Messenger messenger;


    @Override
    public ClientUser[] askFriends(Integer fromLevel, Integer toLevel) {
        HttpSession session = this.getThreadLocalRequest().getSession();
        return service.askFriends(fromLevel, toLevel, SessionData.getSessionData(session).getUser());
    }

    @Override
    public SortedStatus getHomeTimeline() {
        HttpSession session = this.getThreadLocalRequest().getSession();
        return service.getHomeTimeline(SessionData.getSessionData(session).getUser());
    }

    @Override
    public ClientUser getUser() {
        HttpSession session = this.getThreadLocalRequest().getSession();
        AppUser appUser = SessionData.getSessionData(session).getUser();

        return new ClientUserAssembler().from(appUser);
    }

    @Override
    public void moveFriend(int userTwitterId, Integer level) {
        HttpSession session = this.getThreadLocalRequest().getSession();
        AppUser user = SessionData.getSessionData(session).getUser();
        service.moveFriend(userTwitterId, level, user);
    }


    @Override
    public ClientUser syncFriends() {
        HttpSession session = this.getThreadLocalRequest().getSession();
        return service.syncFriends(SessionData.getSessionData(session).getUser());
    }

    @Override
    public void sendInvite() {
        HttpSession session = this.getThreadLocalRequest().getSession();
        JID jid = new JID(SessionData.getSessionData(session).getUser().getGoogleId());
        XMPPServiceFactory.getXMPPService().sendInvitation(jid);
    }

    @Override
    public void updateUserFromClient(ClientUser user) {
        Users users = new UserRepository();
        AppUser u = users.findByGoogleId(user.getGoogleName());
        u.setNotifyStarTweets(user.isNotifyStarTweets());
        users.save(u);
    }
}
