package net.kolov.hs.app;

import net.kolov.hs.modules.hs.model.AppUser;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.io.Serializable;

/**
 * User: assen
 * Date: 5/11/11
 */
public class SessionData implements Serializable {

    private static org.slf4j.Logger LOG = LoggerFactory.getLogger(SessionData.class);

    private static final String SESSION_DATA = "sessionData";

    private AppUser user;

    private String token;
    private String key;

    public static SessionData getSessionData(HttpSession session) {
        SessionData sessionData = (SessionData) session.getAttribute(SESSION_DATA);
        if (sessionData == null) {
            throw new RuntimeException("Unexpected: No session data in session " + session.getId());
        }
        return sessionData;
    }

    public SessionData(String token, String key, AppUser user) {
        this.token = token;

        this.key = key;
        this.user = user;
    }

    public void save(HttpSession session) {
        session.setAttribute(SESSION_DATA, this);
        LOG.info("Stored session data in session " + session.getId());
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }


}
